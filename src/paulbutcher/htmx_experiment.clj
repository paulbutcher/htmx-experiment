(ns paulbutcher.htmx-experiment
  (:require
   [compojure.core :refer [defroutes GET]]
   [compojure.route :as route]
   [hiccup2.core :refer [html]]
   [ring.middleware.defaults :refer [site-defaults wrap-defaults]]))

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
