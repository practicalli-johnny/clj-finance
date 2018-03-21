;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; clj-finance.core
;;
;; Demonstrating the use of Clojure for financial applications
;;


(ns clj-finance.core)

;; Generate a sequence of numbers
(range 10)
;; => (0 1 2 3 4 5 6 7 8 9)

;; take the first 10 values from an infinate number of Integers
;; range is lazy, so when wrapped by a function, it will operate only within the context of that function.
;; so range will only generate the numbers needed to satisfy the take function.
(take 10 (range))
;; => (0 1 2 3 4 5 6 7 8 9)


;; Filter a collection of values to return only what matches the condition
(filter odd? (range 24))
;; => (1 3 5 7 9 11 13 15 17 19 21 23)


;; Filter the range of numbers and return only those equal or greater than 12
;; An anonymous function is used to define the condition of the filter
(filter (fn [number] (>= number 12)) (range 24))
;; => (12 13 14 15 16 17 18 19 20 21 22 23)


;; Generate a random decimal number between 0 and 42
;; Use the function rand-int for a random integer
(rand 42)
;; => 9.693938439022787


;; We can generate more values using the repeatedly function
;; This is conceptually similar to range, however, we can
;; specify our own function for generating a value.

(take 10 (repeatedly (fn [] (rand 42))))
;; => (33.42616173788164 20.111298103371816 16.952595513539226 35.11218966610366 39.71553453871661 
;;     15.383728303138005 36.2597160713536 40.40755842330494 23.51796699046629 41.23334101070866)



;; Lets put these functions together
;; Take the first 24 values
;; That are 12 or greater in value
;; from randomly generated numbers between 0 and 42 (exclusively)

(take 24 (filter (fn [number] (>= number 12))
          (repeatedly (fn [] (rand 42)))))


;; Using the threading macros

;; Generated a lazy sequence of random decimal numbers
;; Filter those numbers son only thouse greater than 12 are ruturned
;; take the first 24

(->> (repeatedly #(rand 42))
     (filter #(>= % 12))
     (take 24))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Part 2 - writing a named function for generating numbers

(defn generate-prices [lower-bound upper-bound]
  (filter
   (fn [number] (>= number lower-bound))
   (repeatedly (fn [] (rand upper-bound)))))

(take 10
      (generate-prices 24 42))
;; => (25.860035391116053 41.38545427006588 33.28994474087834 27.767565843611763 40.36057621882923 39.813902069133896 37.943218630223846 25.36894807127188 30.999469945668903 40.75929024966659)

;; Using let to capture the generated prices in a local name (a bit superfluous)

(let [pricelist (take 10 (generate-prices 24 42))]
  (count pricelist))
;; => 10


;; Give our values more context by using a map (key value pairs)

;; Create an infinite list of prices within lower and upper bounds
(def pricelist
  (generate-prices 24 42))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Creating a map of prices and other related data

;; generate a map of price key and value from the price list
(take 10 (map (fn [x] {:price x}) pricelist))



(take 10 (map (fn [times prices]
                [times prices])
              (map (fn [time] {:time time}) (iterate inc 0))
              (map (fn [price] {:price price}) pricelist)))
;; => ([{:time 0} {:price 28.232748438745983}]
;;     [{:time 1} {:price 33.169702466700485}]
;;     [{:time 2} {:price 29.324335235434006}]
;;     [{:time 3} {:price 33.98095722884446}]
;;     [{:time 4} {:price 37.16691794371352}]
;;     [{:time 5} {:price 29.380244416225363}]
;;     [{:time 6} {:price 34.9344987988658}]
;;     [{:time 7} {:price 27.989664999481572}]
;;     [{:time 8} {:price 29.327083411249394}]
;;     [{:time 9} {:price 31.398797232263323}])






;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; str verses format - performance

;; The format function is a wrapper around java.lang.StringBuilder
;; making format a slower function to execute.
;; Format is also Java host specific, so the code cannot be used on platforms other than the JVM.

;; TODO: Show test results with criterium

#_(defn db-package [package-name]
  (str "{ call " package-name ".SP_DF_AppINSTID(?, ?, ?) }"))

#_(db-package "version30")


