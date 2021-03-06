(ns {{namespace}}.server.server
  (:require
    [clojure.pprint :refer [pprint]]
    [io.pedestal.http :as http]
    [mount.core :refer [defstate]]
    dv.crux-node
    [{{namespace}}.server.config :refer [config]]
    [{{namespace}}.server.service :as service]
    [taoensso.timbre :as log]))

(defstate http-server
  :start
  (let [pedestal-config       (::http/config config)
        service (merge (service/make-service-map) pedestal-config)
        port (::http/port pedestal-config)]
    (when (nil? port)
      (throw (Exception. "You must set a port as the environment variable PORT.")))
    (log/info "Final service config: ")
    (pprint service)
    (log/info "Starting server on port" port)
    (-> service http/create-server http/start))

  :stop
  (http/stop http-server))
