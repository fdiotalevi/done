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
        (let [credentials {:email email :password password}]
          (if-validate (validate-credentials credentials)
            (let [ver-cred (db/verify-credentials credentials)]
              (check-status-and
               (ver-cred :status)
               {:cookies {"session" (session/create-session credentials)}})))))
  
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
             (check-status-and
              (result :status)
              (render/dones (result :rows)))))))

  (POST "/" {{text :text} :params cookies :cookies}
        (let [session (cookies "session")]
          (if-session-valid session
            (let [done {:text text :date (today-date) :email (session/expand-session (session :value))}]
              (if-validate (validate-done done)
                (check-status-and
                 ((db/insert-done done) :status)
                 {:status 200 :body (str done)}))))))

  (DELETE "/:id" {{id :id} :params cookies :cookies}
          (let [session (cookies "session")]
            (if-session-valid session
              (let [result (db/delete-done id)]
                (check-status-and
                 (result :status)
                 ""))))))

; routes to create users
(defroutes users-routes
  (GET "/:username" [username] (str "hello " username))

  (POST "/" [email firstname lastname password]
    (let [user {:email email :firstname firstname :lastname lastname :password password}]
      (if-validate (validate-user user)
        (let [ret-status ((db/insert-user user) :status)]
          (check-status-and
           ret-status
           ""))))))

(defroutes app-routes
  (GET "/" [] (renderer/render-resource "templates/index.mustache" {:var "filippo"}))
  (context "/api/sessions" [] session-routes)
  (context "/api/users" [] users-routes)
  (context "/api/dones" [] dones-routes)
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
