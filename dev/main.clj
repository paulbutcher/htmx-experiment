(ns main
  (:require
   [ring.adapter.jetty :as jetty]
   [paulbutcher.htmx-experiment :refer [app]]))

(defn -main
  [& _]
  (jetty/run-jetty app {:port 8080 :host "0.0.0.0" :join? false}))
