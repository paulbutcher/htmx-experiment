(ns build
  (:refer-clojure :exclude [test])
  (:require [clojure.tools.build.api :as b]
            [antq.tool :as antq]))

(def main 'paulbutcher.lambda)
(def class-dir "target/classes")

(defn help [_]
  (println "Use `clojure -A:deps -T:build help/doc` instead"))

(defn outdated "Look for outdated dependencies" [_]
  (antq/outdated))

(defn upgrade "Upgrade outdated dependencies" [_]
  (antq/outdated {:upgrade true}))

(defn test "Run all the tests." [opts]
  (let [basis    (b/create-basis {:aliases [:test]})
        cmds     (b/java-command
                  {:basis     basis
                   :main      'clojure.main
                   :main-args ["-m" "cognitect.test-runner"]})
        {:keys [exit]} (b/process cmds)]
    (when-not (zero? exit) (throw (ex-info "Tests failed" {}))))
  opts)

(defn- uber-opts [opts]
  (assoc opts
         :main main
         :uber-file "target/standalone.jar"
         :basis (b/create-basis {})
         :class-dir class-dir
         :src-dirs ["src"]
         :ns-compile [main]))

(defn uber "Build an uberjar" [opts]
  (b/delete {:path "target"})
  (let [opts (uber-opts opts)]
    (println "\nCopying source...")
    (b/copy-dir {:src-dirs ["resources" "src"] :target-dir class-dir})
    (println (str "\nCompiling " main "..."))
    (b/compile-clj opts)
    (println "\nBuilding JAR...")
    (b/uber opts)))

(defn ci "Run the CI pipeline" [opts]
  (doto opts
    test
    outdated
    uber))
