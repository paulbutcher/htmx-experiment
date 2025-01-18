(ns paulbutcher.htmx-experiment
  (:require
   [compojure.core :refer [defroutes GET POST]]
   [compojure.route :as route]
   [hiccup2.core :refer [html]]
   [hiccup.util :refer [raw-string]]
   [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
   [ring.middleware.params :refer [wrap-params]]
   [ring.logger :refer [wrap-with-logger]]
   [ring.util.anti-forgery :refer [anti-forgery-field]]
   [paulbutcher.db :as db]))

;; Initialize database tables when namespace is loaded
(db/create-tables!)

(defn message-list []
  (str
   (html
    [:div#messages
     (for [message (db/get-messages)]
       [:div.message
        {:class "mb-2 p-2 border rounded"}
        (:content message)])])))

(defn index-page []
  (str
   (html
    [:head
     [:title "HTMX Example"]
     [:script {:src "https://unpkg.com/htmx.org@2.0.4"}]
     [:meta {:name "csrf-token" :content (anti-forgery-field)}]
     [:script (raw-string "
       document.addEventListener('DOMContentLoaded', function() {
         document.body.addEventListener('htmx:configRequest', function(evt) {
           evt.detail.headers['X-CSRF-Token'] = document.querySelector('meta[name=\"csrf-token\"]').getAttribute('content');
         });
       });")]
     [:link {:href "https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" :rel "stylesheet"}]]
    [:body.container.mx-auto.p-4
     [:h1.text-2xl.mb-4 "HTMX Example"]
     [:div.mb-4
      [:form#message-form
       {:hx-post "/messages"
        :hx-target "#messages"
        :hx-swap "outerHTML"}
       (raw-string (anti-forgery-field))
       [:input#content.border.rounded.p-2.mr-2
        {:type "text"
         :name "content"
         :placeholder "Enter a message"}]
       [:button.bg-blue-500.text-white.px-4.py-2.rounded
        {:type "submit"}
        "Send"]]]
     [:div#messages-container
      {:hx-get "/messages"
       :hx-trigger "load"}]])))

(defn add-message [{:keys [content]}]
  (when content
    (db/insert-message! content))
  (message-list))

(defroutes app-routes
  (GET "/" [] (index-page))
  (GET "/messages" [] (message-list))
  (POST "/messages" {params :params} (add-message params))
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      wrap-with-logger
      wrap-params
      (wrap-defaults site-defaults)))
