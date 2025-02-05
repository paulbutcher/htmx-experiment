(ns paulbutcher.lambda
  (:gen-class :implements
              [com.amazonaws.services.lambda.runtime.RequestStreamHandler])
  (:require [paulbutcher.ring-lambda-adapter :refer [handle-request]]
            [paulbutcher.htmx-experiment :refer [app]]))

(defn -handleRequest [_ is os _context] (handle-request app is os))
