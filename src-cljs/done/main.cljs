(ns done.hello
  (:require [jayq.core :as jq]))

(defn show-login [] (log "log in"))

(jq/ajax "/api/sessions/me" {:success show-login :error show-login} )

;(log (str "a"))

