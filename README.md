# paulbutcher/htmx-experiment

Run the project's tests:

    $ clojure -T:build test

Debug:

    $ clojure -M:dev
    user=> :dbg
    user=> (-main)

Run locally:

    $ clojure -M:dev:run

Check for outdated dependencies:

    $ clojure -T:build outdated

Update outdated dependencies:

    $ clojure -T:build upgrade

Run the project's CI pipeline and build an uberjar:

    $ clojure -T:build ci

Deploy that uberjar:

    $ sam deploy

Discover AWS resources:

    $ aws resourcegroupstaggingapi get-resources --region us-east-1
