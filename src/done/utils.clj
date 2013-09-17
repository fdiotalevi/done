(ns done.utils
  (:use [bouncer.core]
        [bouncer.validators]))

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