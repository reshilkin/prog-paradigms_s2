"use strict";

const VARIABLES = new Map([["x", 0], ["y", 1], ["z", 2]]);

function Abstract(...operands) {
    this.operands = operands;
}

Abstract.prototype = {
    evaluate: function (...vars) {
        return this.f(...this.operands.map(operand => operand.evaluate(...vars)));
    },
    toString: function () {
        return (this.operands.map(e => e.toString()).join(" ") + " " + this.identifier);
    },
    diff: function (varName) {
        return this.differentiate(varName, ...this.operands.map(e => [e, e.diff(varName)]));
    },
    prefix: function () {
        return "(" + this.identifier + " " + this.operands.map(e => e.prefix()).join(" ") + ")";
    },
    postfix: function () {
        return "(" + this.operands.map(e => e.postfix()).join(" ") + " " + this.identifier + ")";
    }
};

function createOperationConstructor(f, identifier, differentiate) {
    const Operation = function (...operands) {
        Abstract.call(this, ...operands);
    };
    Operation.prototype = Object.create(Abstract.prototype);
    Operation.prototype.f = f;
    Operation.prototype.identifier = identifier;
    Operation.prototype.differentiate = differentiate;
    if (f.length !== 0) {
        Object.defineProperty(Operation, "argsNum", {value: f.length});
    }
    Operation.constructor = Operation;
    return Operation.constructor;
}

function Const(value) {
    this.value = value;
}

Const.prototype = {
    evaluate: function () {
        return this.value;
    },
    toString: function () {
        return this.value.toString();
    },
    diff: function () {
        return ZERO;
    },
    prefix: function () {
        return this.value.toString();
    },
    postfix: function () {
        return this.value.toString();
    }
};


const ZERO = new Const(0);
const ONE = new Const(1);
const TWO = new Const(2);

function Variable(name) {
    this.name = name;
}

Variable.prototype = {
    evaluate: function (...vars) {
        return vars[VARIABLES.get(this.name)];
    },
    toString: function () {
        return this.name;
    },
    diff: function (varName) {
        if (varName === this.name) {
            return ONE;
        } else {
            return ZERO;
        }
    },
    prefix: function () {
        return this.name;
    },
    postfix: function () {
        return this.name;
    }
};

const Add = createOperationConstructor(
    (a, b) => a + b,
    "+",
    (varName, a, b) => new Add(a[1], b[1])
);

const Subtract = createOperationConstructor(
    (a, b) => a - b,
    "-",
    (varName, a, b) => new Subtract(a[1], b[1])
);

const Multiply = createOperationConstructor(
    (a, b) => a * b,
    "*",
    (varName, a, b) => new Add(
        new Multiply(a[1], b[0]),
        new Multiply(a[0], b[1])
    )
);

const Divide = createOperationConstructor(
    (a, b) => a / b,
    "/",
    (varName, a, b) => new Divide(
        new Subtract(
            new Multiply(a[1], b[0]),
            new Multiply(a[0], b[1])
        ),
        new Multiply(b[0], b[0])
    )
);

const Negate = createOperationConstructor(
    a => -a,
    "negate",
    (varName, a) => new Negate(a[1])
);

const Gauss = createOperationConstructor(
    (a, b, c, x) => a * Math.pow(Math.E, -(x - b) * (x - b) / (2 * c * c)),
    "gauss",
    (varName, a, b, c, x) => {
        let subxb = new Subtract(x[0], b[0]);
        return new Add(
            new Gauss(a[1], b[0], c[0], x[0]),
            new Multiply(
                new Gauss(a[0], b[0], c[0], x[0]),
                new Divide(
                    new Negate(new Multiply(subxb, subxb)),
                    new Multiply(TWO, new Multiply(c[0], c[0]))).diff(varName)
            )
        )
    }
);

const sumexp = (...operands) => operands.reduce((res, cur) => res + Math.pow(Math.E, cur), 0);

const Sumexp = createOperationConstructor(
    sumexp,
    "sumexp",
    (varName, ...operands) => operands.reduce((res, cur) =>
        new Add(res, new Multiply(cur[1], new Sumexp(cur[0]))), ZERO)
);

const Softmax = createOperationConstructor(
    (...operands) => sumexp(operands[0]) / sumexp(...operands),
    "softmax",
    (varName, ...operands) => new Divide(
        new Sumexp(operands[0][0]),
        new Sumexp(...operands.map(element => element[0]))
    ).diff(varName)
);

const OPERATIONS = new Map([
    ["negate", Negate],
    ["+", Add],
    ["-", Subtract],
    ["*", Multiply],
    ["/", Divide],
    ["gauss", Gauss],
    ["sumexp", Sumexp],
    ["softmax", Softmax]
]);

const parse = function (str) {
    let st = [];
    for (const i of str.split(" ").filter(el => el)) {
        if (VARIABLES.has(i)) {
            st.push(new Variable(i));
        } else if (OPERATIONS.has(i)) {
            st.push(new (OPERATIONS.get(i))(...st.splice(-OPERATIONS.get(i).argsNum)));
        } else {
            st.push(new Const(parseFloat(i)));
        }
    }
    return st[0];
};

function ParserError(message) {
    this.message = message;
}

ParserError.prototype = Object.create(Error.prototype);
ParserError.prototype.name = "ParserError";
ParserError.prototype.constructor = ParserError;

function Parser(str) {
    this.str = str;
    this.str_ptr = 0;
    this.separators = new Set(["(", " ", ")"]);
    this.buf = "";
}

Parser.PREFIX_MODE = 0;
Parser.POSTFIX_MODE = 1;
Parser.prototype = {
    tryToken: function (expected) {
        if (this.buf === expected) {
            this.takeToken();
            return true;
        }
        return false;
    },
    expectToken: function (expected) {
        if (!this.tryToken(expected)) {
            throw new ParserError("Expected '" + expected + "' on position " + (1 + this.str_ptr));
        }
    },
    readBuf: function () {
        this.skipWhitespaces();
        let last = this.str_ptr;
        while (last < this.str.length && !this.separators.has(this.str[last])) {
            last++;
        }
        last = Math.max(last, this.str_ptr + 1);
        this.buf = this.str.substring(this.str_ptr, last);
    },
    skipWhitespaces: function () {
        while (this.str[this.str_ptr] === " ") {
            this.str_ptr++;
        }
    },
    takeToken: function () {
        let res = this.buf;
        this.str_ptr += this.buf.length;
        this.readBuf();
        return res;
    },
    takeOperands: function (mode) {
        let res = [];
        while (this.buf.length > 0 && this.buf !== ")" && !OPERATIONS.has(this.buf)) {
            res.push(this.takeOperand(mode));
        }
        return res;
    },
    takeOperation: function () {
        if (this.buf === ")") {
            throw new ParserError("Operation identifier expected on position " + (this.str_ptr + 1));
        }
        let res = OPERATIONS.get(this.buf);
        if (res === undefined) {
            throw new ParserError("Unknown operation '" + this.buf + "' on position " + (1 + this.str_ptr));
        }
        this.takeToken();
        return res;
    },
    ensureNotEmpty: function () {
        if (this.buf.length === 0) {
            throw new ParserError("Token expected on position " + (1 + this.str_ptr));
        }
    },
    takeOperand: function (mode) {
        this.readBuf();
        this.ensureNotEmpty();
        if (this.tryToken("(")) {
            let operands;
            let operation;
            if (mode === Parser.PREFIX_MODE) {
                operation = this.takeOperation();
                operands = this.takeOperands(mode);
            } else if (mode === Parser.POSTFIX_MODE) {
                operands = this.takeOperands(mode);
                operation = this.takeOperation();
            }
            if (operation.hasOwnProperty("argsNum")
                && operation.argsNum !== operands.length) {
                throw new ParserError("Wrong num of arguments for operation '"
                    + operation.prototype.identifier
                    + "' expected: " + operation.argsNum + " got: " + operands.length
                    + ", last operand ends on position "
                    + (1 + this.str_ptr));
            }
            this.expectToken(")");
            return new operation(...operands);
        }
        if (VARIABLES.has(this.buf)) {
            return new Variable(this.takeToken());
        }
        if (!isNaN(+this.buf)) {
            return new Const(parseFloat(this.takeToken()));
        }
        throw new ParserError("Unknown operand '" + this.buf + "' on position " + (1 + this.str_ptr));
    },
    parse: function (mode) {
        let res = this.takeOperand(mode);
        this.skipWhitespaces();
        if (this.str_ptr !== this.str.length) {
            throw new ParserError("No characters expected after position " + this.str_ptr);
        }
        return res;
    }
};

// :NOTE: Дубль
const parsePrefix = function (str) {
    let parser = new Parser(str);
    return parser.parse(Parser.PREFIX_MODE);
};

const parsePostfix = function (str) {
    let parser = new Parser(str);
    return parser.parse(Parser.POSTFIX_MODE);
};
