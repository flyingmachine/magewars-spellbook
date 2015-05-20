(set-env!
 :source-paths   #{"src/clj" "src/cljs"}
 :resource-paths #{"resources"}
 :dependencies '[[adzerk/boot-cljs      "0.0-2814-4"]
                 [adzerk/boot-cljs-repl "0.1.10-SNAPSHOT" :scope "test"]
                 [adzerk/boot-reload    "0.2.6"           :scope "test"]
                 [cljsjs/boot-cljsjs     "0.4.7"      :scope "test"]
                 [environ               "1.0.0"]
                 [danielsz/boot-environ "0.0.1"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]

                 ;; server
                 [ring/ring-core        "1.3.2" ]
                 [ring/ring-devel       "1.3.2" ]
                 [ring/ring-defaults    "0.1.4"]
                 [org.danielsz/system   "0.1.8-SNAPSHOT"]
                 [liberator             "0.12.2"]
                 [http-kit              "2.1.16"]
                 [com.flyingmachine/liberator-unbound "0.1.1"]
                 [compojure             "1.3.3"]

                 ;; client
                 [org.omcljs/om "0.8.8"]
                 [om-sync "0.1.1"]
                 [cljs-ajax "0.3.11"]
                 [cljsjs/react-with-addons "0.13.1-0"]])

(require
 '[adzerk.boot-cljs      :refer [cljs]]
 '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload    :refer [reload]]
 '[cljsjs.boot-cljsjs    :refer [from-cljsjs]]
 '[reloaded.repl         :refer [init start stop go reset]]
 '[magewars-deckbuilder.systems :refer [dev-system]]
 '[system.boot :refer    [system]])

(deftask environ [e env FOO=BAR {kw edn} "The environment map"]
  (with-pre-wrap fileset
    (alter-var-root #'environ.core/env merge env)
    fileset))

(deftask dev
  "Run a restartable systemin the REPL"
  []
  (comp (environ :env {:http-port 3000})
        (watch :verbose true)
        (system :sys #'dev-system :hot-reload true :files ["handler.clj"])
        (reload)
        (from-cljsjs)
        (cljs :compiler-options {:output-to "main.js" :source-map "main.js.map"})
        (repl :server true)))