(ns paulbutcher.db
  (:require [honey.sql :as sql]
            [honey.sql.helpers :as h]
            [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]))

(defn create-tables!
  [ds]
  (jdbc/execute!
    ds
    ["
CREATE TABLE IF NOT EXISTS messages (
  id SERIAL PRIMARY KEY,
  content TEXT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
)"]))

(def ds
  (delay (let [db-spec {:dbtype "postgresql"
                        :dbname "htmx_experiment"
                        :host (or (System/getenv "DB_HOST") "localhost")
                        :user "postgres"
                        :password (or (System/getenv "DB_PASSWORD") "postgres")}
               ds (jdbc/get-datasource db-spec)]
           (create-tables! ds)
           ds)))

(defn query
  "Execute a HoneySQL query and return results as Clojure maps"
  [honey-query]
  (jdbc/execute! @ds
                 (sql/format honey-query)
                 {:builder-fn rs/as-unqualified-maps}))

(defn insert-message!
  [content]
  (query (-> (h/insert-into :messages)
             (h/columns :content)
             (h/values [[content]]))))

(defn get-messages
  []
  (query (-> (h/select :*)
             (h/from :messages)
             (h/order-by [:created_at :desc]))))
