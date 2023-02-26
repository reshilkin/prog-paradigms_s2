"use strict";
const VARIABLES = new Map([["x", 0], ["y", 1], ["z", 2]]);

const func = function (f) {
    let res = (...operands) => (...vars) => f(...operands.map(operand => operand(...vars)));
    res.argsNum = f.length;
    return res;
}
const cnst = a => () => a;
const variable = a => (...args) => args[VARIABLES.get(a)];
const add = func((x1, x2) => x1 + x2);
const subtract = func((x1, x2) => x1 - x2);
const multiply = func((x1, x2) => x1 * x2);
const divide = func((x1, x2) => x1 / x2);
const negate = func(x1 => -x1);
const pi = cnst(Math.PI);
const e = cnst(Math.E);
const avg3 = func((x1, x2, x3) => average(x1, x2, x3));
const average = (...input) => input.reduce((a, b) => a + b, 0) / input.length;
const med5 = func((x1, x2, x3, x4, x5) => median(x1, x2, x3, x4, x5));
const median = (...input) => input.sort((a, b) => a - b)[(input.length - input.length % 2) / 2];

const CONSTS = new Map([["pi", pi], ["e", e]]);
const OPERATIONS = new Map([
    ["negate", negate],
    ["+", add],
    ["-", subtract],
    ["*", multiply],
    ["/", divide],
    ["avg3", avg3],
    ["med5", med5]
]);

const parse = function (a) {
    let st = [];
    for (const i of a.split(" ").filter(el => el)) {
        if (VARIABLES.has(i)) {
            st.push(variable(i));
        } else if (CONSTS.has(i)) {
            st.push(CONSTS.get(i));
        } else if (OPERATIONS.has(i)) {
            st.push(OPERATIONS.get(i)(...st.splice(-OPERATIONS.get(i).argsNum)));
        } else {
            st.push(cnst(parseFloat(i)));
        }
    }
    return st[0];
}