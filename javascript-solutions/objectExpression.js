"use strict"

const {Variable, Const, Negate, Add, Subtract, Multiply, Divide, Cube, Cbrt, Sumsq, Length} = (function () {
    function GetAbstractOp(implEvaluate, diff, getOp) {
        function Operation(...args) {
            this.opArguments = args;
        }

        Operation.prototype.arity = implEvaluate.length;
        SetFields.call(Operation, function (...args) {
                return implEvaluate(...this.opArguments.map(arg => arg.evaluate(...args)));
            }, function () {
                return `${this.opArguments.map(arg => arg.toString()).join(" ")} ${getOp}`;
            }, diff, function () {
                return `(${this.opArguments.map(arg => arg.postfix()).join(" ")} ${getOp})`;
            },
            function () {
                return `(${getOp} ${this.opArguments.map(arg => arg.prefix()).join(" ")})`;
            }
        )

        return Operation;
    }

    function SetFields(evaluate, toString, diff, postfix, prefix) {
        this.prototype.evaluate = evaluate;
        this.prototype.toString = toString;
        this.prototype.diff = diff;
        this.prototype.postfix = postfix;
        this.prototype.prefix = prefix;
    }

    SetFields.call(Variable, function (...args) {
        return args[this.index];
    }, function () {
        return this.arg;
    }, function (name) {
        if (name === this.arg) {
            return new Const(1);
        }
        return new Const(0);
    }, function () {
        return this.arg;
    }, function () {
        return this.arg;
    });

    const vars = {"x": 0, "y": 1, "z": 2};

    function Variable(arg) {
        this.arg = arg;
        this.index = vars[arg];
    }

    function Const(arg) {
        this.arg = arg;
    }

    SetFields.call(Const, function () {
        return this.arg;
    }, function () {
        return this.arg.toString();
    }, function () {
        return new Const(0);
    }, function () {
        return this.arg.toString();
    }, function () {
        return this.arg.toString();
    });

    const Sumsq = GetAbstractOp((...args) => {
        if (args.length !== 0) {
            return args.map((x) => x * x).reduce((x, y) => x + y);
        }
        return 0;
    }, function (name) {
        let answ = new Const(0);

        for (let i of this.opArguments) {
            answ = new Add(
                answ,
                new Multiply(i,
                    new Multiply(
                        new Const(2),
                        i.diff(name)
                    )
                )
            );
        }

        return answ;
    }, "sumsq");

    const Length = GetAbstractOp(function (...args) {
        if (args.length !== 0) {
            return Math.sqrt(args.map((x) => x * x).reduce((x, y) => x + y));
        }
        return 0;
    }, function (name) {
        if (this.opArguments.length === 0) {
            return new Const(0);
        }
        return new Divide(
            Sumsq.prototype.diff.call(this, name),
            new Multiply(new Const(2),
                new Length(...this.opArguments)
            )
        );
    }, "length");

    const Cube = GetAbstractOp((x) => x * x * x, function (name) {
        return new Multiply(
            new Multiply(this.opArguments[0],
                this.opArguments[0]
            ),
            new Multiply(new Const(3),
                this.opArguments[0].diff(name)
            )
        );
    }, "cube");

    const Cbrt = GetAbstractOp(Math.cbrt, function (name) {
        return new Divide(
            this.opArguments[0].diff(name),
            new Multiply(
                new Const(3),
                new Multiply(
                    new Cbrt(this.opArguments[0]),
                    new Cbrt(this.opArguments[0])
                )
            )
        )
    }, "cbrt");

    const Add = GetAbstractOp((x, y) => x + y, function (name) {
        return new Add(
            this.opArguments[0].diff(name),
            this.opArguments[1].diff(name)
        );
    }, "+");

    const Subtract = GetAbstractOp((x, y) => x - y, function (name) {
        return new Subtract(
            this.opArguments[0].diff(name),
            this.opArguments[1].diff(name)
        );
    }, "-");

    const Divide = GetAbstractOp((x, y) => x / y, function (name) {
        return new Divide(
            new Subtract(
                new Multiply(
                    this.opArguments[0].diff(name),
                    this.opArguments[1]),
                new Multiply(
                    this.opArguments[0],
                    this.opArguments[1].diff(name)
                )
            ),
            new Multiply(this.opArguments[1], this.opArguments[1])
        );
    }, "/");

    const Multiply = GetAbstractOp((x, y) => x * y, function (name) {
        return new Add(
            new Multiply(
                this.opArguments[0],
                this.opArguments[1].diff(name)
            ),
            new Multiply(
                this.opArguments[0].diff(name),
                this.opArguments[1]
            )
        );
    }, "*");

    const Negate = GetAbstractOp((x) => -x, function (name) {
        return new Negate(this.opArguments[0].diff(name));
    }, "negate");

    return {Variable, Const, Negate, Add, Subtract, Multiply, Divide, Cube, Cbrt, Sumsq, Length};
})()

const Classes = {
    "-": Subtract,
    "+": Add,
    "*": Multiply,
    "/": Divide,
    "negate": Negate,
    "cube": Cube,
    "cbrt": Cbrt,
    "sumsq": Sumsq,
    "length": Length
}

const parse = expression => {
    let parsedExpression = [];

    for (let term of expression.split(" ").filter(e => e.length !== 0)) {
        if (term in Classes) {
            parsedExpression.push(new Classes[term](...parsedExpression.splice(-(Classes[term].prototype).arity)));
        } else if (term === "x" || term === "y" || term === "z") {
            parsedExpression.push(new Variable(term));
        } else {
            parsedExpression.push(new Const(parseFloat(term)));
        }
    }

    return parsedExpression.pop();
}

class ParserError extends Error {
    constructor(message, position) {
        super(message + " in position: " + position);
        this.position = position;
        this.message = message + " in position: " + this.position;
    }
}

function ErrorFabric(exception) {
    class error extends ParserError {
        constructor(position) {
            super(exception, position);
        }
    }

    return error;
}

const InvalidNumberError = ErrorFabric("error in parse const");
const MissingCloseBracket = ErrorFabric("missing close bracket");
const MissingOpenBracket = ErrorFabric("missing open bracket");
const IncorrectTerm = ErrorFabric("incorrect term");
const MissingOperation = ErrorFabric("Missing operation or unknown operation");
const ConstOperation = ErrorFabric("expect: operation found: const");
const VariableOperation = ErrorFabric("expect: operation found: variable");

class InvalidArgsLength extends ParserError {
    constructor(position, argsLength, now) {
        super(`invalid args length for operation expect:  ${argsLength} found: ${now}`, position);
    }
}

class StringSource {
    constructor(x) {
        this.ourExpr = x;
        this.ch = this.ourExpr.charAt(0);
        this.pointer = 0;
    }

    test(a) {
        if (this.ch === a) {
            this.nextChar();
            this.skipWhitespace();
            return true;
        }
        return false;
    }

    getSymb() {
        return this.ch;
    }

    nextChar() {
        if (this.pointer + 1 < this.ourExpr.length) {
            this.ch = this.ourExpr.charAt(this.pointer + 1);
            this.pointer++;
        } else {
            this.ch = '\0';
        }
    }

    skipWhitespace() {
        while (this.getSymb() === ' ' && this.pointer < this.ourExpr.length) {
            this.nextChar();
        }
    }

    getTerm() {
        let res = this.ch;
        let temp = this.pointer + 1;
        while (temp <= this.ourExpr.length - 1 && this.ourExpr.charAt(temp) !== ' ' && this.ourExpr.charAt(temp) !== ')' && this.ourExpr.charAt(temp) !== '(') {
            res = res.concat(this.ourExpr.charAt(temp));
            temp++;
        }
        return res;
    }

    skipTerm() {
        while (this.ch !== '\0' && this.ch !== ' ' && this.ch !== '(' && this.ch !== ')') {
            this.nextChar();
        }
    }
}

const parsePostfix = expression => {
    return parseWithFlag(expression, true);
}

const parsePrefix = expression => {
    return parseWithFlag(expression, false);
}

const parseWithFlag = (expression, isPostfix) => {
    let source = new StringSource(expression);
    let parseExpr = parsePostOperation(source, isPostfix);
    source.skipWhitespace();
    if (source.getSymb() !== '\0') {
        throw new MissingOpenBracket(source.pointer + 1);
    }
    return parseExpr;
}

function getOp(expression, isPostfix) {
    if (!isPostfix) {
        let op = expression.getTerm();
        if (op in Classes) {
            expression.skipTerm();
            return op;
        }
        if (op === 'x' || op === 'y' || op === 'z') {
            throw new VariableOperation(expression.pointer + 1);
        } else if (!isNaN(op)) {
            throw new ConstOperation(expression.pointer + 1);
        } else {
            throw new MissingOperation(expression.pointer + 1);
        }
    }
    return NaN;
}

const parsePostOperation = (expression, isPostfix) => {
    expression.skipWhitespace();
    if (expression.test('(')) {
        let args = [];
        let op = getOp(expression, isPostfix);

        while (true) {
            expression.skipWhitespace();
            if (expression.getTerm() in Classes) {
                op = getOp(expression, !isPostfix);
                break;
            } else if (expression.getSymb() === ')' && !isPostfix) {
                break;
            } else {
                args.push(parseValue(expression, isPostfix));
            }
        }

        expression.skipWhitespace();
        if (expression.test(')')) {
            if ((op === "sumsq" || op === "length") || (op in Classes && args.length === Classes[op].prototype.arity)) {
                return new Classes[op](...args);
            } else if (op instanceof Const) {
                throw new ConstOperation(expression.pointer + 1);
            } else if (op instanceof Variable) {
                throw new VariableOperation(expression.pointer + 1);
            } else {
                throw new InvalidArgsLength(expression.pointer + 1, Classes[op].prototype.arity, args.length);
            }
        } else {
            throw new MissingCloseBracket(expression.pointer + 1);
        }
    } else {
        return parseValue(expression, isPostfix);
    }
}

const parseValue = (expression, isPostfix) => {
    expression.skipWhitespace();

    const currentSymb = expression.getSymb();
    if (currentSymb === '(') {
        let a;
        a = parsePostOperation(expression, isPostfix);
        expression.skipWhitespace();

        return a;
    } else if (currentSymb === 'x' || currentSymb === 'y' || currentSymb === 'z') {
        expression.nextChar();
        expression.skipWhitespace();

        return new Variable(currentSymb);
    } else if (currentSymb === '-' || ('0' <= currentSymb && currentSymb <= '9')) {
        const temp = expression.getTerm();
        if (!isNaN(+temp)) {
            expression.skipTerm();
            return new Const(+temp);
        } else {
            throw new InvalidNumberError(expression.pointer + 1);
        }
    } else if (currentSymb === ')') {
        throw new MissingOperation(expression.pointer + 1);
    } else {
        throw new IncorrectTerm(expression.pointer + 1);
    }
}

