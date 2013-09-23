(ns done.handler
  (:use [compojure.core]
        [bouncer.core]
        [bouncer.validators]
        [done.utils])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [clostache.parser :as renderer]
            [done.db :as db]
            [done.render :as render]
            [done.session :as session]
            ))

; routes to create a session (login)
(defroutes session-routes
  (POST "/" [email password]
        (let [credentials {:email email :password password}
              val-errors (validate-credentials credentials)]
          (if (empty? val-errors)
            (let [ver-cred (db/verify-credentials credentials)]
              (do
                (case (ver-cred :status)
                  "ok" {:cookies {"session" (session/create-session credentials)}}
                  "not-found" {:status 404 :body "User not found or credentials incorrect"}
                  "failure" (render/error 500 {:error "Error connecting to the database"}))))
            (render/error 400 val-errors))))
  
  (GET "/me" {cookies :cookies}
       (let [session (cookies "session")]
         (if-session-valid session
           (session/expand-session (session :value))))))

; routes to create and delete 'dones'
(defroutes dones-routes
  (GET "/" {cookies :cookies}
       (let [session (cookies "session")]
         (if-session-valid session
           (let [result (db/get-dones (session/expand-session (session :value)))]
             (case (result :status)
               "ok" (render/dones (result :rows))
               "failure" {:status 500 :body "Error while connecting to the database"})))))

  (POST "/" {{text :text} :params cookies :cookies}
        (let [session (cookies "session")]
          (if-session-valid session
            (let [done {:text text :date (today-date) :email (session/expand-session (session :value))}
                  val-errors (validate-done done)]
              (if (not (empty? val-errors))
                (render/error 400 val-errors)
                (case ((db/insert-done done) :status)
                  "ok" {:status 200 :body (str done)}
                  "failure" {:status :500 :body "Error connecting to the database"}))
              ))))

  (DELETE "/:id" {{id :id} :params cookies :cookies}
          (let [session (cookies "session")]
            (if-session-valid session
              (let [result (db/delete-done id)]
                (case (result :status)
                  "ok" ""
                  "failure" {:status 500 :body "Error while deleting resource"}))))))


; routes to create users
(defroutes users-routes
  (GET "/:username" [username] (str "hello " username))

  (POST "/" [email firstname lastname password]
    (let [user {:email email :firstname firstname :lastname lastname :password password}
           val-errors (validate-user user)]
      (if (empty? val-errors)
        (let [ret-status ((db/insert-user user) :status)]
          (case ret-status
            "ok" ""
            "duplicate" (render/error 409 {:error "Email already exists"})
            "failure" (render/error 500 {:error "Error connecting to the database"})))
        (render/error 400 val-errors)))))

(defroutes app-routes
  (GET "/" [] (renderer/render-resource "templates/index.mustache" {:var "filippo"}))
  (context "/api/sessions" [] session-routes)
  (context "/api/users" [] users-routes)
  (context "/api/dones" [] dones-routes)
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
