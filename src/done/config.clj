(ns done.config
    (:import [com.typesafe.config ConfigFactory]))

(def config (ConfigFactory/load))

(defn get-string [name] (.getString config name))
