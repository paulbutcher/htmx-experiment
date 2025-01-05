# paulbutcher/htmx-experiment

Run the project:

    $ clojure -M:run

Run the project's tests:

    $ clojure -T:build test

Check for outdated dependencies:

    $ clojure -T:build outdated

Update outdated dependencies:

    $ clojure -T:build upgrade

Run the project's CI pipeline and build an uberjar:

    $ clojure -T:build ci

Run that uberjar:

    $ java -jar target/standalone.jar
