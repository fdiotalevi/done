(ns done.hello
  (:require [goog.dom :as goog.dom]))

(goog.dom/setTextContent (goog.dom/getElement "test") "hola")
