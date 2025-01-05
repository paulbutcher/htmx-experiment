(ns paulbutcher.htmx-experiment
  (:require
   [compojure.core :refer [defroutes GET]]
   [compojure.route :as route]
   [hiccup2.core :refer [html]]
   [ring.adapter.jetty :as jetty]
   [ring.middleware.defaults :refer [site-defaults wrap-defaults]])
  (:gen-class))

(defn index-page []
  (str
   (html
    [:head
     [:title "HTMX Example"]
     [:script {:src "https://unpkg.com/htmx.org@1.9.6"}]]
    [:body
     [:h1 "HTMX Example"]
     [:div#greeting
      {:hx-get "/greet"
       :hx-trigger "load"}]])))

(defn greet []
  (str (html [:div "Hello, World!"])))

(defroutes app-routes
  (GET "/" [] (index-page))
  (GET "/greet" [] (greet))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))

(defn -main
  [& _]
  (jetty/run-jetty app {:port 8080 :host "0.0.0.0" :join? false}))
