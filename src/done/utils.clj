(ns done.utils
  (:use [bouncer.core]
        [bouncer.validators])
  (:require [done.render :as render]))

; various bits and pieces here

; bouncer validator
(defvalidator valid-email
  "Validates value is an email address.

  It implements a simple check to verify there's only a '@' and
  at least one point after the '@'

  For use with validation functions such as `validate` or `valid?`"
  {:default-message-format "%s must be a valid email address"}
  [value]
  (and (required value) (matches value #"^[^@]+@[^@\\.]+[\\.].+")))


(defn validate-user
  [user]
    ((validate user :email valid-email :firstname required :lastname required :password required) 0))

(defn validate-credentials
  [credentials]
   ((validate credentials :email valid-email :password required) 0))

(defn validate-done
  [done]
  ((validate done :text required :date required :email valid-email) 0))

(defn today-date
  []
  (..
   (java.text.SimpleDateFormat. "yyyyMMdd")
   (format (java.util.Date.))))

(defmacro if-session-valid
  [session body]
  `(if (nil? ~session)
      {:status 403 :body "Not authorised"}
      ~body))

(defmacro check-status
  [status and-return result]
  `(case ~status
     "ok" ~result
     "not-found" {:status 404 :body "Entity not found"}
     "failure" {:status 500 :body "Unexpected error while performing the operation"}
     "duplicate" {:status 409 :body "This entity already exists"}))

(defmacro if-validate
  [validation body]
  `(if (empty? ~validation)
    ~body
    (render/error 400 ~validation)))
