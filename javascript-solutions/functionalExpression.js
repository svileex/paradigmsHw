"use strict"

const cnst = arg => () => arg;

const variable = name => {
    switch (name) {
        case "x":
            return (...args) => args[0]
        case "y":
            return (...args) => args[1]
        case "z":
            return (...args) => args[2]
    }
}

const operation = op => (...args) => (...vars) => op(...args.map(arg => arg(...vars)));

const add      = operation((a, b) => a + b);
const subtract = operation((a, b) => a - b);
const multiply = operation((a, b) => a * b);
const divide   = operation((a, b) => a / b);
const negate = operation(a => -a);

const one = cnst(1);
const two = cnst(2);

const min5 = operation(Math.min)
const max3 = operation(Math.max)

const func = {
    "-": subtract,
    "+": add,
    "*": multiply,
    "/": divide,
    "negate" : negate,
    "max3" : max3,
    "min5" : min5
}

const arity = {
    "-": 2,
    "+": 2,
    "*": 2,
    "/": 2,
    "negate" : 1,
    "max3" : 3,
    "min5" : 5
}

const consts = {
    "one" : one,
    "two" : two
}

const parse = expression => {
    let parsedExpression = [];

    for (let term of expression.split(" ").filter(e => e.length !== 0)) {
        if (term in func) {
            parsedExpression.push(func[term](...parsedExpression.splice(-arity[term])));
        } else if (term === "x" || term === "y" || term === "z") {
            parsedExpression.push(variable(term));
        } else if (term in consts) {
            parsedExpression.push(consts[term]);
        } else {
            parsedExpression.push(cnst(parseFloat(term)));
        }
    }

    return parsedExpression.pop();
}

const test = () => {
    let expression = add(
        subtract(multiply(variable("x"), variable("x")), multiply(cnst(2), variable("x"))), cnst(1))
    for (let i = 0; i <= 10; i++) {
        console.log(expression(i));
    }
}
