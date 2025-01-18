(ns paulbutcher.db
  (:require
   [next.jdbc :as jdbc]
   [next.jdbc.result-set :as rs]
   [honey.sql :as sql]
   [honey.sql.helpers :as h]))

(def db-spec
  {:dbtype "postgresql"
   :dbname "htmx_experiment"
   :host "localhost"
   :user "postgres"
   :password "postgres"})

(def ds (jdbc/get-datasource db-spec))

(defn query
  "Execute a HoneySQL query and return results as Clojure maps"
  [honey-query]
  (jdbc/execute! ds (sql/format honey-query)
                {:builder-fn rs/as-unqualified-maps}))

(defn create-tables! []
  (jdbc/execute! ds ["
CREATE TABLE IF NOT EXISTS messages (
  id SERIAL PRIMARY KEY,
  content TEXT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
)"]))

(defn insert-message! [content]
  (query
   (-> (h/insert-into :messages)
       (h/columns :content)
       (h/values [[content]]))))

(defn get-messages []
  (query
   (-> (h/select :*)
       (h/from :messages)
       (h/order-by [:created_at :desc])))) 