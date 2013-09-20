(ns done.test.session
  (:use [clojure.test]
        [done.session]
   ))



(deftest can-encrypt
  (testing "can encrypt and decrypt a string"
    (let  [a-string "fil@jshf.com" password "jhsgahjdgshjd" encrypted (encrypt a-string password)]
      (is (= a-string (decrypt encrypted password))))))

(deftest session-creation
  (testing "can create a session"
    (is (not (nil? (create-session {:email "email" :password "password"})))))
  (testing "can recognise a created session"
    (let [s (create-session {:email "email" :password "password"})]
      (is (= "email" (expand-session s))))))
