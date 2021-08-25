(def constant constantly)
(defn variable [name] (fn [args] (args name)))

(defn get-functional-exp [op] (fn [& args] (fn [arg] (apply op (mapv #(% arg) args)))))
(def add (get-functional-exp +))
(def subtract (get-functional-exp -))
(def multiply (get-functional-exp *))
(defn div [& args] (if (== (count args) 1) (/ 1.0 (first args))
                                           (apply * (first args) (mapv #(/ 1.0 %1) (rest args)))))
(def divide (get-functional-exp div))
(def negate subtract)
(def sum add)
(def avg (get-functional-exp (fn [& args] (/ (apply + args) (count args)))))

(defn get-field [this symbol] (cond (contains? this symbol) (this symbol)
                                    (contains? this :proto) (get-field (this :proto) symbol)))

(defn _evaluate [obj] (get-field obj :evaluate))
(defn _toString [obj] (get-field obj :toString))
(defn _toInfix [obj] (get-field obj :toInfix))
(defn _diff [obj] (get-field obj :diff))

(defn evaluate [expr vars] ((_evaluate expr) expr vars))
(defn toString [expr] ((_toString expr) expr))
(defn diff [expr name] ((_diff expr) expr name))
(defn toStringInfix [expr] ((_toInfix expr) expr))
(defn object-operation [op symbol diff-impl] {
                                              :evaluate
                                              (fn [this vars] (apply op (mapv #(evaluate % vars) (this :args))))
                                              :toString
                                              (fn [this] (str "(" symbol " " (clojure.string/join " " (mapv toString (this :args))) ")"))
                                              :toInfix
                                              (fn [this] (str "(" (toStringInfix (first (this :args))) " " symbol " " (toStringInfix (nth (this :args) 1)) ")"))
                                              :diff
                                              (fn [this name] (diff-impl (this :args) (mapv #(diff % name) (this :args))))
                                              })

(defn getOp [op symbol diff]
  (fn [& args] {:proto (object-operation op symbol diff), :args args}))

(def Constant #())
(def ConstantProto {:evaluate (fn [this _] (this :value))
                    :toString (fn [this] (format "%.1f" (double (this :value))))
                    :toInfix  (fn [this] (format "%.1f" (double (this :value))))
                    :diff     (fn [_ _] (Constant 0))})

(defn Constant [value] {:proto ConstantProto :value value})

(def VariableProto {:evaluate (fn [this vars] (vars (clojure.string/lower-case (subs (this :name) 0 1))))
                    :toString (fn [this] (this :name))
                    :toInfix  (fn [this] (this :name))
                    :diff     (fn [this name] (if (= (this :name) name) (Constant 1.0) (Constant 0.0)))})

(defn Variable [name] {:proto VariableProto :name name})
;(println (subs "x" 0 1))
;(println (evaluate (Variable "x") {"x" 123}))

(def Add (getOp + '+ #(apply Add %2)))
(def Negate)
(def negate-proto {:evaluate
                   (fn [this vars] (apply - (mapv #(evaluate % vars) (this :args))))
                   :toString
                   (fn [this] (str "(" 'negate " " (clojure.string/join " " (mapv toString (this :args))) ")"))
                   :toInfix
                   (fn [this] (str 'negate "(" (toStringInfix (first (this :args))) ")"))
                   :diff
                   (fn [this name] (#(apply Negate %2) (this :args) (mapv #(diff % name) (this :args))))
                   })

(def Negate (fn [& args] {:proto negate-proto, :args args}))
(def Subtract (getOp - '- #(apply Subtract %2)))
(def Divide #())
(def Multiply #())

(defn d-m-diff [op ind-args ind-d-args] (apply op (mapv (fn [[x y]]
                                                          (apply Multiply y (mapv second (filter (fn [[num _]] (not= num x)) ind-args)))
                                                          ) ind-d-args)))

(defn mult-diff [args d-args]
  (d-m-diff Add (map-indexed vector args) (map-indexed vector d-args)))

(defn divide-diff [args d-args] (if (== (count args) 1)
                                  (Negate (Divide (first d-args) (Multiply (first args) (first args))))
                                  (Divide (d-m-diff Subtract (map-indexed vector args) (map-indexed vector d-args))
                                          (Multiply (apply Multiply (rest args)) (apply Multiply (rest args))))))

(def Multiply (getOp * '* mult-diff))
(def Divide (getOp div '/ divide-diff))
(def Sum (getOp + 'sum #(apply Add %2)))
(defn avg-ev [& args] (/ (apply + args) (count args)))
(def Avg (getOp avg-ev 'avg #(apply Avg %2)))
(println (integer? 0.0))
(defn evalfpow [a b] (if (and (< a 0) (odd? (Math/round b))) (- (Math/pow (- a) b)) (Math/pow a b)))
(def IPow (getOp evalfpow '** #()))
(def ILog (getOp #(/ (Math/log (Math/abs (double %2))) (Math/log (Math/abs (double %1)))) '"//" #()))
;(println (Math/pow -15.23132 0.9999999))
(def op {'+      add
         '-      subtract
         '*      multiply
         '/      divide
         'negate negate
         'avg    avg
         'sum    sum
         'cons   constant
         'var    variable})

(def vars {'x "x"
           'y "y"
           'z "z"})

(def objectOp {'+      Add
               '-      Subtract
               '/      Divide
               '*      Multiply
               'negate Negate
               'cons   Constant
               'var    Variable
               'sum    Sum
               'avg    Avg})

(defn parse-list-op [op exp] (cond
                               (number? exp) ((op 'cons) exp)
                               (contains? vars exp) ((op 'var) (vars exp))
                               (seq? exp) (apply (op (first exp)) (mapv (partial parse-list-op op) (rest exp)))))

(defn parseFunction [x] (parse-list-op op (read-string x)))
(defn parseObject [x] (parse-list-op objectOp (read-string x)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(load-file "parser.clj")

; (defn -return [value tail] {:value value :tail tail})
; (def -valid? boolean)
; (def -value :value)
; (def -tail :tail)
;
; (defn -show [result]
;   (if (-valid? result) (str "-> " (pr-str (-value result)) " | " (pr-str (apply str (-tail result))))
;                        "!"))
; (defn tabulate [parser inputs]
;   (run! (fn [input] (printf "    %-10s %s\n" (pr-str input) (-show (parser input)))) inputs))
;
; (defn _empty [value] (partial -return value))
;
; (defn _char [p]
;   (fn [[c & cs]]
;     (if (and c (p c))
;       (-return c cs))))
;
; (defn _map [f result]
;   (if (-valid? result)
;     (-return (f (-value result)) (-tail result))))
;
; (defn _combine [f a b]
;   (fn [input]
;     (let [ar ((force a) input)]
;       (if (-valid? ar)
;         (_map (partial f (-value ar))
;               ((force b) (-tail ar)))))))
;
; (defn _either [a b]
;   (fn [input]
;     (let [ar ((force a) input)]
;       (if (-valid? ar)
;         ar
;         ((force b) input)))))
;
; (defn _parser [p]
;   (let [pp (_combine (fn [v _] v) p (_char #{\u0000}))]
;     (fn [input] (-value (pp (str input \u0000))))))
;
; (defn +char [chars]
;   (_char (set chars)))
;
; (defn +char-not [chars]
;   (_char (comp not (set chars))))
;
; (defn +map [f parser]
;   (comp (partial _map f) parser))
;
; (def +parser _parser)
;
; (def +ignore
;   (partial +map (constantly 'ignore)))
;
; (defn iconj [coll value]
;   (if (= value 'ignore)
;     coll
;     (conj coll value)))
;
; (defn +seq [& ps]
;   (reduce (partial _combine iconj) (_empty []) ps))
;
; (defn +seqf [f & ps]
;   (+map (partial apply f) (apply +seq ps)))
;
; (defn +seqn [n & ps]
;   (apply +seqf #(nth %& n) ps))
;
; (defn +or [p & ps]
;   (reduce _either p ps))
;
; (defn +opt [p]
;   (+or p (_empty nil)))
;
; (defn +star [p]
;   (letfn [(rec [] (+or (+seqf cons p (delay (rec))) (_empty ())))]
;     (rec)))
;
; (defn +plus [p]
;   (+seqf cons p (+star p)))
;
; (defn +str [p] (+map (partial apply str) p))

;;;;;;;;;;;;;;;;;;;;;;;;

(defn +string [string] (let [spstring (clojure.string/split string #"")] (apply +seq (mapv +char spstring))))

(def *all-chars (mapv char (range 0 128)))
(def *space (+char (apply str (filter #(Character/isWhitespace %) *all-chars))))
(def *ws (+ignore (+star *space)))
(def *digit (+char "0123456789"))
(def *number (+map read-string (+str (+seqf cons (+str (+seqf cons (+opt (+char "-")) (+plus *digit)))
                                            (+opt (+seq (+char ".") (+str (+plus *digit))))))))
(def *var (+str (+plus (+char "xyzXYZ"))))
(declare *plus-minus)
(def *brackets (+seqn 1 (+char "(") (delay *plus-minus) (+char ")")))
(declare *factor)
(def *negate (+seqn 0 (+ignore (+string "negate"))
                    (+or (+map Variable *var) (+map Constant *number) (delay *factor))))

(defn right-reduce [f [fi & expr]] (if (not= expr nil)
                                     (f fi (vector (first (first expr)) (right-reduce f (cons (second (first expr)) (rest expr)))))
                                     fi))

(defn get-map [f symb f1 f2] (fn [expr] (letfn [(f-for-r [expr arg] (if (= (first arg) symb)
                                                                      (f1 expr (second arg))
                                                                      (f2 expr (second arg))))]
                                          (f f-for-r expr))))

(def map-plus-minus (get-map reduce \+ Add Subtract))
(def map-log-pow (get-map right-reduce "**" IPow ILog))
(def map-mul-div (get-map reduce \* Multiply Divide))

(def get-lvl )

(def *factor (+seqn 0 *ws (+or (+map Constant *number) (+map Variable *var) (+map Negate *negate) *brackets) *ws))
(def *log-pow (+map map-log-pow (+seqf cons *factor
                                       (+star (+seq *ws (+or (+str (+string "**"))
                                                             (+str (+string "//"))) *factor)))))
(def *mul-div (+map map-mul-div (+seqf cons *log-pow (+star (+seq *ws (+char "*/") *log-pow)))))
(def *plus-minus (+map map-plus-minus (+seqf cons *mul-div (+star (+seq *ws (+char "+-") *mul-div)))))
(defn parseObjectInfix [expr] (:value (*plus-minus expr)))
