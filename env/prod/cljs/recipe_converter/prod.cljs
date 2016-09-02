(ns recipe-converter.prod
  (:require [recipe-converter.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
