(ns norvig-spell-corrector.core)

;; more or less a literal translation of the Python

(defn words [string]
  (re-seq #"\w+" (clojure.string/lower-case string)))


(def word-freqs
  (-> "resources/big.txt"
      slurp
      words
      frequencies))

(def total (reduce + 0 (vals word-freqs)))

(defn P
  [[word cnt]]
  (/ cnt total))


;; pretty sure this is what needs optimized...
(defn edits1 [word]
  (let [letters    "abcdefghijklmniopqrstuvwxyz"
        splits     (for [i (range 0 (inc (count word)))]
                     [(subs word 0 i) (subs word i)])
        deletes    (map (fn [[l r]] (when r (apply str l (rest r))))
                        splits)
        transposes (keep (fn [[l r]]
                           (when (> (count r) 1)
                             (apply str l (second r) (first r) (nthrest r 2))))
                         splits)
        replaces   (flatten
                    (for [c letters]
                      (map (fn [[l r]] (when r (apply str l c (rest r))))
                           splits)))
        inserts    (flatten
                    (for [c letters]
                      (map (fn [[l r]] (apply str l c r)) splits)))]
    (into #{} (concat deletes transposes replaces inserts))))


(defn edits2 [word]
  (->> word
       edits1
       (map edits1)
       (apply clojure.set/union)))


(defn in-word-freqs? [word]
  (contains? word-freqs word))

(defn known [words]
  (let [ret (->> words
                 (filter in-word-freqs?)
                 (into #{}))]
    (if (empty? ret)
      nil
      ret)))

(defn candidates [word]
  (or
   (known [word])
   (known (edits1 word))
   (known (edits2 word))
   [word]))

(defn correction [word]
  (->> word
       candidates
       (select-keys word-freqs)
       (apply max-key val)
       key))
