(defn tensor? [dim t]
  (cond
    (== dim 0) (number? t)
    (== dim 1) (and (vector? t) (every? number? t))
    :else (and (vector? t) (every? #(tensor? (dec dim) %) t) (apply = (mapv count t)))))

(defn eq-tensor? [dim & ts] (tensor? (inc dim) (vec ts)))

(defn transpose
  [m]
  {:pre  [(tensor? 2 m)]}
  (apply mapv vector m))

(defn funcToVectors [f]
  (fn [& vs]
    {:pre  [(apply eq-tensor? 1 vs)]}
    (apply mapv f vs)))

(def v+ (funcToVectors +))

(def v- (funcToVectors -))

(def v* (funcToVectors *))

(def vd (funcToVectors /))

(defn scalar
  [& vs]
  {:pre  [(apply eq-tensor? 1 vs)]}
  (apply + (apply mapv * vs)))

(defn v*s
  [v & s]
  {:pre  [(tensor? 1 v)]}
  (mapv #(* % (apply * s)) v))

(defn funcToMatrix [f]
  (fn [& ms]
    {:pre  [(apply eq-tensor? 2 ms)]}
    (apply mapv f ms)))

(def m+ (funcToMatrix v+))

(def m- (funcToMatrix v-))

(def m* (funcToMatrix v*))

(def md (funcToMatrix vd))

(defn m*s
  [m & s]
  {:pre  [(and (tensor? 2 m) (every? number? s))]}
  (mapv #(v*s % (apply * s)) m))

(defn m*v
  [m v]
  {:pre  [(and (tensor? 2 m) (tensor? 1 v) (== (count (first m)) (count v)))]}
  (mapv #(scalar % v) m))

(defn m*tr
  [m1 m2]
  {:pre  [(and (tensor? 2 m1) (tensor? 2 m2) (== (count (first m1)) (count m2)))]}
  (mapv #(m*v (transpose m2) %) m1))

(defn m*m
  [& ms]
  (reduce m*tr ms))

(defn vect
  [& vs]
  {:pre  [(and (apply eq-tensor? 1 vs) (== 3 (count (first vs))))]}
  (reduce #(m*v [[0 (- (nth %1 2)) (nth %1 1)] [(nth %1 2) 0 (- (nth %1 0))] [(- (nth %1 1)) (nth %1 0) 0]] %2) vs))

(defn getDim [t]
  {:pre  [(or (number? t) (vector? t))]}
  (if (number? t) 0 (+ 1 (getDim (first t)))))

(defn getForm [t]
  {:pre [(tensor? (getDim t) t)]}
  (letfn
    [(rec [z]
       (cond
         (number? z) (list)
         (and (vector? z) (number? (first z))) (list (count z))
         :else (conj (rec (first z)) (count z))))]
    (rec t)))

(defn suffix? [a b]
  (letfn
    [(rec [x y]
       (cond (empty? x) true
             (empty? y) false
             (not (== (peek x) (peek y))) false
             :else (rec (pop x) (pop y))))]
    (rec (vec a) (vec b))))

(defn broadcast [t form]
  {:pre [(and (suffix? (getForm t) form) (list? form))]}
  (if (= (getForm t) form) t (vec (repeat (peek form) (broadcast t (pop form))))))

(defn broadcastAll [& ts]
  (let [mostCommonForm (fn [& ts] (reduce #(if (suffix? %1 %2) %2 %1) (list) (mapv #(getForm %) ts)))
        form (apply mostCommonForm ts)]
    (mapv #(broadcast % form) ts)))

(defn funcToTensor [f]
  (fn [& ts]
    (let
      [t (apply broadcastAll ts)]
      (letfn [(rec [& ts] (if (number? (first ts))
                            (apply f ts)
                            (apply mapv rec ts)))]
        (apply rec t)))))

(def hb+ (funcToTensor +))
(def hb- (funcToTensor -))
(def hb* (funcToTensor *))
(def hbd (funcToTensor /))
