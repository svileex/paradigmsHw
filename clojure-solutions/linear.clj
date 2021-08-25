(defn math-vect? [& vs] (every? (every-pred vector? (partial every? number?)) vs))

(defn same-length? [& vs] (apply == (mapv count vs)))
(defn matrix? [m] (and (vector? m) (apply math-vect? m) (apply same-length? m)))
(defn get-m-size [m]
  {:pre [(matrix? m)]}
  [(count m) (count (first m))])
(defn same-length-m? [& v] (apply = (mapv get-m-size v)))

(defn get-factory [pred] (fn [op] (fn [& v]
                                    {:pre [(pred v)]}
                                    (apply mapv op v))))

(def get-op (get-factory #(and (apply math-vect? %) (apply same-length? %))))
(def v+ (get-op +))
(def v- (get-op -))
(def v* (get-op *))
(def vd (get-op /))

(defn v*s [vector & num]
  {:pre [(math-vect? vector) (every? number? num)]}
  (let [s (apply * num)] (mapv #(* s %) vector)))

(defn scalar [& vector]
  {:pre [(apply math-vect? vector) (apply same-length? vector)]}
  (apply + (apply v* vector)))

(defn det2x2 [vector1 vector2 x y] (- (* (nth vector1 x) (nth vector2 y))
                                      (* (nth vector1 y) (nth vector2 x))))

(defn vect [& v] (reduce (fn [v1 v2]
                           {:pre [(math-vect? v1 v2) (== (count v1) (count v2) 3)]}
                           (vector
                             (det2x2 v1 v2 1 2)
                             (det2x2 v1 v2 2 0)
                             (det2x2 v1 v2 0 1))) v))


(def get-matrix-op (get-factory #(and (every? matrix? %) (apply same-length-m? %))))

(def m+ (get-matrix-op v+))
(def m- (get-matrix-op v-))
(def m* (get-matrix-op v*))
(def md (get-matrix-op vd))

(defn m*s [matrix & num]
  {:pre [(matrix? matrix) (every? number? num)]}
  (mapv (fn [x] (v*s x (apply * num))) matrix))

(defn transpose [matrix]
  {:pre [(matrix? matrix)]}
  (apply mapv vector matrix))

(defn m*v [matrix vector] (mapv (partial scalar vector) matrix))

(defn m*m [& matrix] (reduce (fn [m1 m2]
                               {:pre [(matrix? m1) (matrix? m2) (== (count (first m1)) (count m2))]}
                               (transpose (mapv (fn [x] (m*v m1 x)) (transpose m2)))) matrix))

(defn simplex? [s] (or
                     (empty? s)
                     (math-vect? s)
                     (and
                       (same-length? s (first s))
                       (simplex? (first s))
                       (simplex? (rest s)))))

(defn get-s-op [op] (fn x [& s]
                      {:pre [(apply == (mapv count s)) (every? simplex? s)]}
                      (if (apply math-vect? s) (apply op s) (apply mapv x s))))

(def x+ (get-s-op v+))
(def x- (get-s-op v-))
(def x* (get-s-op v*))
(def xd (get-s-op vd))