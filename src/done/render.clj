(ns done.render
  (:require  [clojure.data.json :as json]))


(defn error
  [status val-error]
  (let [messages (val-error 0)]
    (hash-map :status status :body (json/write-str (flatten (map #(%1 1) messages))))))