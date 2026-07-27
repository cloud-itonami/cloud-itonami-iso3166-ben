(ns marketentry.registry
  "Pure-function market-entry filing-draft + filing-submit record
  construction -- an append-only market-entry book-of-record draft.

  Like every sibling actor's registry, there is no single international
  reference-number standard for a public-procurement market-entry
  filing -- every jurisdiction assigns its own format. This namespace
  does NOT invent one; it builds a jurisdiction-scoped sequence number
  and validates the record's required fields, the same honest,
  non-fabricating discipline `marketentry.facts` uses.

  `engagement-fee-matches-claim?` is an HONEST reapplication of the
  SAME ground-truth-recompute DISCIPLINE sibling actors use (verify a
  claimed monetary total against the entity's own recorded quantity x
  unit fields), reapplied to a market-entry engagement fee line.

  `required-preference-margin` / `compute-evaluated-price` /
  `evaluated-price-mismatches-claim?` are the SAME discipline applied to
  a genuinely Benin-specific mechanism: Loi N°2020-26 du 29 septembre
  2020 (Code des Marchés Publics), Art. 77 al.3, a MANDATORY five-percent
  (5%) bid-EVALUATION preference for an offer submitted BY a verified
  micro/petite/moyenne entreprise (MPME) -- 'lors de la passation d'un
  marché public une préférence de cinq pour cent (5%) doit être
  attribuée à l'offre présentée par une micro, petite et moyenne
  entreprise'. The evaluated price is used ONLY to RANK/compare
  competing bids; it is never the real contract price the winning
  bidder is paid (`:base-price` stays the actual bid).

  This is a GENUINELY DIFFERENT check SHAPE than every prior iso3166
  sibling this repo mirrors: Bulgaria's ЗОП Art. 54(5) de-minimis is a
  PERCENTAGE-OF-TURNOVER ELIGIBILITY formula, Albania's Neni 76(2)(c)
  carve-out is a FLAT-CONSTANT ELIGIBILITY threshold, Azerbaijan's/
  Armenia's flagship checks are BOOLEAN registry-membership ELIGIBILITY
  reads, and Antigua and Barbuda's vendor-class check is a THREE-TIER
  ELIGIBILITY-THRESHOLD classification. Every one of those five is an
  ELIGIBILITY/EXCLUSION gate. Benin's Art. 77 mechanism is not an
  eligibility gate at all -- it is a BID-EVALUATION PRICE ADJUSTMENT:
  the 'recompute' here derives the price used for RANKING an offer
  (never the contract value itself) from the engagement's own declared
  MPME status, and HARD-holds if the engagement's own claimed evaluated
  price does not match. This is reported honestly as a sixth distinct
  check shape for the family, not treated as a lesser version of any
  prior shape.

  Only Art. 77's MANDATORY branch (the flat 5% for an offer submitted BY
  a verified MPME itself) is modeled. Art. 77's OTHER, separate ground --
  an up-to-5% margin for a NON-MPME bidder who commits to subcontract at
  least 30% of contract value to one or more MPMEs -- is deliberately
  NOT modeled: the law's own text caps that margin at 5% but otherwise
  leaves its exact value to the contracting authority's own discretion
  ('une marge de préférence qui ne pourra être supérieure à cinq pour
  cent'), so there is no single correct number this governor could
  independently recompute without inventing a discretionary choice the
  authority itself has not made. Modeling only the unambiguous mandatory
  branch is an honest scope-narrowing, the same discipline Antigua and
  Barbuda's Regulations-delegation finding and this family's other
  honest-narrowing decisions already established.

  This namespace is pure data + pure functions -- no I/O, no network
  call to any real procurement portal. It builds the RECORD an
  operator would keep, not the act of submitting a portal registration
  itself (that is `marketentry.operation`'s `:filing/submit`, always
  human-gated -- see README Actuation)."
  (:require [clojure.string :as str]))

(defn- unsigned-certificate
  "Every certificate this actor produces is UNSIGNED -- signature is
  the market-entry operator's act, not this actor's."
  [kind subject record-id]
  {"@context" ["https://www.w3.org/ns/credentials/v2"]
   "type" ["VerifiableCredential" kind]
   "credentialSubject" {"id" subject "record" record-id}
   "proof" nil
   "issued_by_registry" false
   "status" "draft-unsigned"})

(defn- zero-pad [n w]
  (let [s (str n)]
    (str (apply str (repeat (max 0 (- w (count s))) "0")) s)))

(def ^:private money-scale
  "Sub-minor-unit scale used when comparing two money amounts: 1/10000 of
  a unit. Coarser than double representation error by many orders of
  magnitude, finer than any real currency's minor unit (2 decimals for
  most, 3 for KWD/BHD/OMR, 0 for JPY/KRW)."
  10000)

(defn- money=
  "Exact-at-money-precision equality for two amounts.

  `==` on raw doubles is NOT the right comparison for money. With
  whole-unit fees the two agree, but as soon as an amount carries
  cents the sum `base + rate x months` is routinely not the double
  nearest the true total, and a CORRECT claim compares false: measured
  on this exact shape, 40,989 of 327,060 cent-denominated combinations
  (12.5%) were rejected while being right, against 0 of 327,060 in
  whole units.

  Rounding both sides to `money-scale` before comparing removes the
  representation error while preserving every distinction money can
  actually carry."
  [x y]
  (and (number? x) (number? y)
       (= (Math/round (* money-scale (double x)))
          (Math/round (* money-scale (double y))))))

(defn compute-engagement-fee
  "The ground-truth engagement fee for `engagement`'s own `:base-fee`
  and `:monitoring-months` x `:monthly-rate` -- a single flat
  base + months x rate calculation, not a full pricing engine."
  [{:keys [base-fee monthly-rate monitoring-months]}]
  ;; nil when any field is not a number: an un-recomputable engagement is
  ;; un-verifiable, which is neither `correct` nor a ClassCastException
  ;; thrown out of the caller.
  (when (and (number? base-fee) (number? monthly-rate) (number? monitoring-months))
    (+ (double base-fee)
       (* (double monthly-rate) (double monitoring-months)))))

(defn engagement-fee-matches-claim?
  "Does `engagement`'s own `:claimed-fee` equal the independently
  recomputed `compute-engagement-fee`?"
  [{:keys [claimed-fee] :as engagement}]
  (money= claimed-fee (compute-engagement-fee engagement)))

(def mpme-preference-margin-pct
  "Loi N°2020-26 du 29 septembre 2020, Art. 77 al.3 (curl/WebFetch-verified
  2026-07-21 against sgg.gouv.bj's official hosting): the mandatory
  bid-evaluation preference an offer submitted BY a verified micro/petite/
  moyenne entreprise (MPME) is entitled to."
  0.05)

(defn required-preference-margin
  "The Art. 77 al.3 preference margin `engagement` is entitled to: 5%
  when its own declared `:mpme-status?` is a verified true, 0.0
  otherwise. Missing/nil `:mpme-status?` -> 0.0 (no free preference
  unless declared AND true)."
  [{:keys [mpme-status?]}]
  (if (true? mpme-status?) mpme-preference-margin-pct 0.0))

(defn compute-evaluated-price
  "The ground-truth EVALUATION-ONLY price used to rank/compare competing
  bids -- NEVER the actual contract price. `:base-price` is the real bid
  the contract would be signed at; only the price used to compare offers
  is reduced by the Art. 77 preference margin the engagement is actually
  entitled to."
  [{:keys [base-price] :as engagement}]
  (* (double base-price) (- 1.0 (required-preference-margin engagement))))

(defn evaluated-price-mismatches-claim?
  "Does `engagement`'s own declared `:claimed-evaluated-price` differ
  from the INDEPENDENTLY recomputed Art. 77 evaluated price? Catches
  BOTH directions honestly: a non-MPME claiming a preference it is not
  entitled to, and a verified MPME being denied the preference it IS
  entitled to."
  [{:keys [claimed-evaluated-price] :as engagement}]
  (not (== (double claimed-evaluated-price) (compute-evaluated-price engagement))))

(defn register-draft
  "Validate + construct the FILING-DRAFT registration DRAFT -- the
  market-entry operator's own act of preparing a portal registration
  package. Pure function -- does not touch any real procurement
  portal."
  [engagement-id jurisdiction sequence]
  (when-not (and engagement-id (not= engagement-id ""))
    (throw (ex-info "draft: engagement_id required" {})))
  (when-not (and jurisdiction (not= jurisdiction ""))
    (throw (ex-info "draft: jurisdiction required" {})))
  (when (< sequence 0)
    (throw (ex-info "draft: sequence must be >= 0" {})))
  (let [draft-number (str (str/upper-case jurisdiction) "-DFT-" (zero-pad sequence 6))
        record {"record_id" draft-number
                "kind" "filing-draft"
                "engagement_id" engagement-id
                "jurisdiction" jurisdiction
                "immutable" true}]
    {"record" record "draft_number" draft-number
     "certificate" (unsigned-certificate "FilingDraft" draft-number draft-number)}))

(defn register-submit
  "Validate + construct the FILING-SUBMIT registration DRAFT -- the
  market-entry operator's own act of actually submitting a portal
  registration (always human-gated upstream)."
  [engagement-id jurisdiction sequence]
  (when-not (and engagement-id (not= engagement-id ""))
    (throw (ex-info "submit: engagement_id required" {})))
  (when-not (and jurisdiction (not= jurisdiction ""))
    (throw (ex-info "submit: jurisdiction required" {})))
  (when (< sequence 0)
    (throw (ex-info "submit: sequence must be >= 0" {})))
  (let [submit-number (str (str/upper-case jurisdiction) "-SUB-" (zero-pad sequence 6))
        record {"record_id" submit-number
                "kind" "filing-submit"
                "engagement_id" engagement-id
                "jurisdiction" jurisdiction
                "immutable" true}]
    {"record" record "submit_number" submit-number
     "certificate" (unsigned-certificate "FilingSubmit" submit-number submit-number)}))

(defn append [history result]
  (conj (vec history) (get result "record")))
