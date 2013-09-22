(ns done.test.utils
  (:use [clojure.test]
        [done.utils]))

(deftest can-validate-user
  (testing "can validate valid users"
    (is (empty? (validate-user {:email "hhh@hh.com" :firstname "f" :lastname "p" :password "p"}))))
  (testing "will raise validation error for empty field or invalid email"
    (is (not (empty? (validate-user {:email "hhh@hh.com" :firstname "f" :lastname "" :password "p"}))))
    (is (not (empty? (validate-user {:email "hhhhh.com" :firstname "f" :lastname "sadsa" :password "p"}))))))

(deftest can-validate-credentials
  (testing "will raise validation error with empty field or invalid email"
    (is (not (empty? (validate-credentials {:email "df" :password "p"}))))
    (is (not (empty? (validate-credentials {:email "" :password "p"})))))
  (testing "can validate valid credentials"
    (is (empty? (validate-credentials {:email "jhj@fds.com" :password "p"})))))

(deftest can-validate-done
  (testing "can-validate-a-good-done"
    (is (empty? (validate-done {:text "t" :email "e@d.com" :date "a"})))))
    
(deftest can-create-date
  (testing "will generate a 8 char string"
    (= 8 (count (today-date)))))
