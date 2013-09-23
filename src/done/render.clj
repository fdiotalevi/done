(ns done.render
  (:require  [clojure.data.json :as json]))


(defn error
  "val-error is an hashmap. Keys are ininfluent, values are the error messages"
  [status val-error]
  (let [messages val-error]
    (hash-map :status status :body (json/write-str (flatten (map #(%1 1) messages))))))

(defn dones
  "render a list of dones"
  [dones]
  (json/write-str dones))
