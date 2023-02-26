(def constant constantly)

(defn variable [name] (fn [vars] (vars name)))

(defn func [f] (fn [& operands] (fn [vars] (apply f (map #(% vars) operands)))))

(def add (func +))

(def subtract (func -))

(def multiply (func *))

(def myDiv (fn
             ([a] (/ 1 (double a)))
             ([a & ops] (reduce #(/ (double %1) (double %2)) a ops))))

(def divide (func myDiv))

(def negate (func -))

(defn sumexp-function [& ops] (let [args (map #(Math/exp %) ops)] (apply + args)))

(def sumexp (func sumexp-function))

(defn softmax-function [& ops] (let [args (map #(Math/exp %) ops)] (/ (first args) (double (apply + args)))))

(def softmax (func softmax-function))

(def ops {'+       add
          '-       subtract
          '*       multiply
          '/       divide
          'negate  negate
          'sumexp  sumexp
          'softmax softmax})

(defn parseFunction [s]
  (letfn [(rec [l] (cond
                     (symbol? l) (variable (name l))
                     (number? l) (constant l)
                     :else (apply (ops (peek l)) (map rec (pop l)))))]
    (rec (read-string s))))





(defn proto-get
  ([obj key] (proto-get obj key nil))
  ([obj key default] (cond
                       (contains? obj key) (obj key)
                       (contains? obj :prototype) (proto-get (obj :prototype) key)
                       :else default)))

(defn proto-call [this f & args] (apply (proto-get this f) this args))

(defn field ([key] (fn ([this] (proto-get this key)) ([this default] (proto-get this default)))))

(defn method [key] (fn ([this & args] (apply proto-call this key args))))

(defn constructor [ctor prototype] (fn [& args] (apply ctor {:prototype prototype} args)))

(def _f (field :f))
(def _diffRule (field :diffRule))
(def _init (field :init))
(def _operands (field :operands))
(def _value (field :value))
(def _name (field :name))
(def evaluate (method :evaluate))
(def diff (method :diff))
(def toString (method :toString))
(def toStringInfix (method :toStringInfix))

(def BinOpP {
             :toStringInfix (fn
                              [this]
                              (if (= (count (_operands this)) 1)
                                (str (_init this) "(" (toStringInfix (nth (_operands this) 0)) ")")
                                (str
                                  "("
                                  (clojure.string/join (str " " (_init this) " ") (map toStringInfix (_operands this)))
                                  ")")))
             :evaluate      (fn [this args] (apply (_f this) (map #(evaluate % args) (_operands this))))
             :toString      (fn
                              [this]
                              (str "(" (_init this) " " (clojure.string/join " " (map toString (_operands this))) ")"))
             :diff          (fn [this varName]
                              (apply (_diffRule this) varName (mapv #(vector % (diff % varName)) (_operands this))))})

(defn createOperation
  [init f diffRule]
  (constructor
    (fn [this & args] (assoc this :operands (vec args)))
    {:prototype BinOpP :init init :f f :diffRule diffRule}))

(declare Constant)
(declare ZERO)

(def ConstP
  {:toStringInfix (fn [this] (str (_value this)))
   :evaluate      (fn [this args] (_value this))
   :toString      (fn [this] (str (_value this)))
   :diff          (fn [this varName] ZERO)})

(def Constant (constructor (fn [this value] (assoc this :value value)) ConstP))

(def ZERO (Constant 0))
(def ONE (Constant 1))

(def VariableP
  {:toStringInfix (fn [this] (_name this))
   :evaluate      (fn [this args] (args ((comp clojure.string/lower-case str #(get % 0)) (_name this))))
   :toString      (fn [this] (_name this))
   :diff          (fn [this varName] (if (= (_name this) varName) ONE ZERO))})

(def Variable (constructor (fn [this name] (assoc this :name name)) VariableP))

(def Negate (createOperation "negate" - (fn [varName & args] (Negate (nth (first args) 1)))))

(def Add (createOperation "+" + (fn [varName & args] (apply Add (map #(peek %) args)))))

(def Subtract (createOperation "-" - (fn [varName & args] (apply Subtract (map #(peek %) args)))))

(defn pasteDiffs
  [res operands diffs]
  (if (empty? diffs)
    res
    (pasteDiffs (conj res (assoc operands (dec (count diffs)) (peek diffs))) operands (pop diffs))))

(def Multiply (createOperation "*"
                               *
                               (fn [varName & args]
                                 (let [operands (mapv #(nth % 0) args) diffs (mapv peek args) res []]
                                   (apply Add (map #(apply Multiply %) (pasteDiffs res operands diffs)))))))

(def Divide (createOperation "/"
                             myDiv
                             (fn [varName & args]
                               (let [operands (mapv #(nth % 0) args)
                                     first (if (= (count operands) 1) ONE (first operands))
                                     second (if (= (count operands) 1) (peek operands) (apply Multiply (drop 1 operands)))]
                                 (Divide
                                   (Subtract
                                     (Multiply (diff first varName) second) (Multiply (diff second varName) first))
                                   (Multiply second second))))))

(def Sumexp (createOperation "sumexp"
                             sumexp-function
                             (fn [varName & args]
                               (apply Add (map #(Multiply (peek %) (Sumexp (nth % 0))) args)))))

(def Softmax (createOperation "softmax"
                              softmax-function
                              (fn [varName & args]
                                (let [operands (mapv #(nth % 0) args)]
                                  (diff (Divide (Sumexp (nth operands 0)) (apply Sumexp operands)) varName)))))


(defn apply-bit-op [f] (fn [& doubles] (Double/longBitsToDouble (apply f (map #(Double/doubleToLongBits %) doubles)))))

(def BitAnd (createOperation "&"
                             (apply-bit-op bit-and)
                             (fn [varName & args] nil)))

(def BitOr (createOperation "|"
                            (apply-bit-op bit-or)
                            (fn [varName & args] nil)))

(def BitXor (createOperation "^"
                             (apply-bit-op bit-xor)
                             (fn [varName & args] nil)))

(def BitImpl (createOperation "=>"
                              (fn [a b]
                                (Double/longBitsToDouble
                                  (bit-or
                                    (bit-not (Double/doubleToLongBits a))
                                    (Double/doubleToLongBits b))))
                              (fn [varName & args] nil)))

(def BitIff (createOperation "<=>"
                             (apply-bit-op (comp bit-not bit-xor))
                             (fn [varName & args] nil)))

(def Ops {'+           Add
          '-           Subtract
          '*           Multiply
          '/           Divide
          'negate      Negate
          'sumexp      Sumexp
          'softmax     Softmax
          '&           BitAnd
          '|           BitOr
          (symbol "^") BitXor
          '=>          BitImpl
          '<=>         BitIff})

(defn parseObject [s]
  (letfn [(rec [expr] (cond
                        (symbol? expr) (Variable (name expr))
                        (number? expr) (Constant expr)
                        :else (apply (Ops (peek expr)) (map rec (pop expr)))))]
    (rec (read-string s))))


(load-file "parser.clj")

(def all-chars (mapv char (range 32 128)))

(def *digit (+char (apply str (filter #(Character/isDigit %) all-chars))))

(def *number
  (+map (comp Constant read-string)
        (+seqf (partial apply str) (+opt (+char "-")) (+str (+plus *digit)) (+opt (+char ".")) (+str (+star *digit)))))

(def *space (+char (apply str (filter #(Character/isWhitespace %) all-chars))))

(def *ws (+ignore (+star *space)))

(def *letter (+char (apply str (filter #(Character/isLetter %) all-chars))))

(def *identifier (+map Variable (+str (+seqf cons *letter (+star (+or *letter *digit))))))

(defn *string [s] (+map (partial apply str) (apply +seq (map #(+char (str %)) s))))

(def op-right #{"=>"})

(defn build-expression
  [beg & tail]
  (if (empty? tail)
    beg
    (let [init (nth (first tail) 0) op (Ops (symbol init)) cur (nth (first tail) 1)]
      (if (contains? op-right init)
        (op beg (apply build-expression cur (rest tail)))
        (apply build-expression (op beg cur) (rest tail))))))

(def op-priority [["/" "*"] ["+" "-"] ["&"] ["|"] ["^"] ["=>"] ["<=>"]])

(declare *factor)
(def *unminus (+map Negate (+seqn 1 (*string "negate") *ws (delay *factor))))

(declare parser)
(def *factor
  (+or *unminus
       *number
       *identifier
       (+seqn 1 (+char "(") (delay parser) *ws (+char ")"))))

(def parser (letfn
              [(rec [prev i]
                 (if (= i (count op-priority))
                   prev
                   (rec
                     (+seqf (partial apply build-expression)
                            *ws
                            prev
                            (+star (+seq *ws (apply +or (map #(*string %) (nth op-priority i))) *ws prev)))
                     (inc i))))]
              (rec *factor 0)))

(defn parseObjectInfix [str] (:value (parser str)))
