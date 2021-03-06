(defproject done "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [com.google.guava/guava "15.0"]
                 [de.ubercode.clostache/clostache "1.3.1"]
                 [org.clojure/java.jdbc "0.3.0-alpha4"]
                 [mysql/mysql-connector-java "5.1.6"]
                 [com.jolbox/bonecp "0.8.0-rc3" :exclusions [org.slf4j/slf4j-api com.google.guava/guava]]
                 [org.slf4j/slf4j-log4j12 "1.7.5"]
                 [log4j/log4j "1.2.17" :exclusions [javax.mail/mail
                                                    javax.jms/jms
                                                    com.sun.jdmk/jmxtools
                                                    com.sun.jmx/jmxri]]
                 [bouncer "0.3.0-alpha1"]
                 [org.clojure/data.json "0.2.2"]
                 [com.typesafe/config "1.0.2"]
                 [com.lambdaworks/scrypt "1.4.0"]]
  :plugins [[lein-ring "0.8.5"]]
  :ring {:handler done.handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.5"]]}})
