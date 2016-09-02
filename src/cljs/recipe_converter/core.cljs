(ns recipe-converter.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [clojure.string :as string]))

(def input-text (atom ""))
(def output-text (atom ""))

;; check input is a recipe.
(defn parse-recipe [r] r)

;; set the output-text to the converted recipe
(defn convert-recipe [r]
  (reset! output-text r))

;; -------------------------
;; Views

(defn home-page []
  [:div [:h2 "Welcome to recipe converter"]
   [:p "Paste your American recipe here:"]
   [:textarea {:placeholder "input"
               :value @input-text
               :on-change #(do
                             (def t (-> % .-target .-value))
                             (reset! input-text t)
                             (convert-recipe (parse-recipe t)))}]
   [:textarea {:placeholder "output"
               :disabled true
               :value @output-text}]])

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (secretary/dispatch! path))
     :path-exists?
     (fn [path]
       (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
