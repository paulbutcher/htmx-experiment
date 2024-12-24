(ns paulbutcher.htmx-experiment
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [hiccup.core :refer [html]]
            [hiccup.page :refer [html5]]))

(defn index-page []
  (html5
   [:head
    [:title "HTMX Example"]
    [:script {:src "https://unpkg.com/htmx.org@1.9.6"}]]
   [:body
    [:h1 "HTMX Example"]
    [:div#greeting
     {:hx-get "/greet"
      :hx-trigger "load"}]]))

(defn greet []
  (html [:div "Hello, World!"]))

(defroutes app-routes
  (GET "/" [] (index-page))
  (GET "/greet" [] (greet))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))

(defn -main
  [& _args]
  (jetty/run-jetty app {:port 3000 :join? false})
  (println "Server started on port 3000"))
