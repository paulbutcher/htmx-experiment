(ns paulbutcher.htmx-experiment
  (:require [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]
            [hiccup2.core :refer [html raw]]
            [paulbutcher.db :as db]
            [ring.logger :refer [wrap-with-logger]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]))

(defn message-list
  []
  (str (html [:div#messages
              (for [message (db/get-messages)]
                [:div.message (:content message)])])))

(defn index-page
  []
  (str (html
         [:head [:title "HTMX Example"]
          [:script {:src "https://unpkg.com/htmx.org@2.0.4"}]]
         [:body [:h1 "HTMX Example"]
          [:div
           [:form#message-form
            {:hx-post "/messages" :hx-target "#messages" :hx-swap "outerHTML"}
            (raw (anti-forgery-field))
            [:input#content
             {:type "text" :name "content" :placeholder "Enter a message"}]
            [:button {:type "submit"} "Send"]]]
          [:div#messages-container {:hx-get "/messages" :hx-trigger "load"}]])))

(defn add-message
  [{:keys [content]}]
  (when content (db/insert-message! content))
  (message-list))

(defroutes app-routes
           (GET "/" [] (index-page))
           (GET "/messages" [] (message-list))
           (POST "/messages" {params :params} (add-message params))
           (route/not-found "Not Found"))

(def app
  (-> app-routes
      wrap-params
      (wrap-defaults site-defaults)
      wrap-with-logger))
