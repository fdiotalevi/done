(ns done.handler
  (:use [compojure.core]
        [bouncer.core]
        [bouncer.validators])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [clostache.parser :as renderer]
            [done.db :as db]
            [done.render :as render]
            ))

(defroutes session-routes
  (POST "/" [username password] (str username password)))

(defroutes users-routes
  (GET "/:username" [username] (str "hello " username))

  (POST "/" [email firstname lastname password]
    (let [user {:email email :firstname firstname :lastname lastname :password password}
           val-errors (validate user :email required :firstname required :lastname required :password required)]
      (if (empty? (val-errors 0)) (db/insert-user user) (render/error 400 val-errors)))))

(defroutes app-routes
  (GET "/" [] (renderer/render-resource "templates/index.mustache" {:var "filippo"}))
  (context "/api/sessions" [] session-routes)
  (context "/api/users" [] users-routes)
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
