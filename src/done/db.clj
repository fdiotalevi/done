(ns done.db
  (:import [com.jolbox.bonecp BoneCPDataSource]
           [com.mysql.jdbc.exceptions.jdbc4 MySQLIntegrityConstraintViolationException]
           [java.sql SQLException])
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.java.jdbc.sql :as sql])
  )

(def db-spec
  {:classname "com.mysql.jdbc.Driver"
   :subprotocol "mysql"
   :subname "//127.0.0.1:3306/done"
   :user "root"
   :password "root"})


(defn pool
  [spec]
  (let [max-pool 10 min-pool 5 partitions 3
        cpds (doto (BoneCPDataSource.)
               (.setJdbcUrl (str "jdbc:" (:subprotocol spec) ":" (:subname spec)))
               (.setUsername (:user spec))
               (.setPassword (:password spec))
               (.setMinConnectionsPerPartition (inc (int (/ min-pool partitions))))
               (.setMaxConnectionsPerPartition (inc (int (/ max-pool partitions))))
               (.setPartitionCount partitions)
               (.setStatisticsEnabled true)
               ;; test connections every 25 mins (default is 240):
               (.setIdleConnectionTestPeriodInMinutes 25)
               ;; allow connections to be idle for 3 hours (default is 60 minutes):
               (.setIdleMaxAgeInMinutes (* 3 60))
               ;; consult the BoneCP documentation for your database:
               (.setConnectionTestStatement "/* ping *\\/ SELECT 1"))]
    {:datasource cpds}))


(def pooled-db (delay (pool db-spec)))

(def mysql @pooled-db)

(defn insert-user
  [user-map]
  (try
    (do (jdbc/insert! mysql :user user-map) {:status "ok"})
    (catch MySQLIntegrityConstraintViolationException e (hash-map :status "duplicate"))
    (catch SQLException e (hash-map :status "failure"))))

(defn verify-credentials
  [credentials]
  (try
    (let [rows (jdbc/query mysql (sql/select * :user (sql/where
       {:email (credentials :email) :password (credentials :password)})))]
      (if (empty? rows)
        {:status "not-found"}
        {:status "ok" :rows rows}))
    (catch SQLException e {:status "failure"})))
