(ns magewars-spellbook.handler
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.response :as resp]
            [magewars-spellbook.cards :as cards]
            [magewars-spellbook.mages :as mages]))

(defroutes app-routes
  (GET "/" [] (-> (resp/resource-response "index.html")
                  (resp/content-type "text/html")))
  (GET "/data" []
       (fn [req]
         (-> (resp/response (str {:cards (cards/cards :core)
                                  :mages mages/all}))
             (resp/content-type "text/plain"))))
  (route/not-found "Not Found"))

(def middleware (assoc-in site-defaults [:static :resources] "/"))

(def app
  (-> app-routes
      (wrap-defaults middleware)))
