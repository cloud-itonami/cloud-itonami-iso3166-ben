(ns marketentry.governor
  "Market-Entry Compliance Governor -- the independent compliance layer
  that earns the MarketEntry-LLM the right to commit. The LLM has no
  notion of Benin procurement law, whether a claimed engagement fee
  actually equals base + months x rate, whether the engagement's own
  declared MPME-preference-adjusted evaluated price actually reflects
  Loi N°2020-26 Art. 77's mandatory 5% margin, whether an IFU (Identifiant
  Fiscal Unique) has been verified for a filing that requires it, or when
  a draft stops being a draft and becomes a real-world marches-publics.bj
  (SIGMAP) portal submission, so this MUST be a separate system able to
  *reject* a proposal and fall back to HOLD.

  `:itonami.blueprint/governor` is `:market-entry-compliance-governor`
  (shared family keyword on blueprints).

  This blueprint's own text (docs/business-model.md Trust Controls:
  'any actual portal registration or filing submission requires
  Market-Entry Compliance Governor clearance and always escalates to
  human sign-off'; 'a false or fabricated regulatory-requirement claim
  is a HARD hold') names exactly the checks below.

  Six checks, in priority order, ALL HARD violations: a human
  approver CANNOT override them. The confidence/actuation gate is
  SOFT: it asks a human to look (low confidence / actuation), and the
  human may approve -- but see `marketentry.phase`: for `:stake
  :actuation/draft-filing`/`:actuation/submit-filing` NO phase ever
  allows auto-commit either. Two independent layers agree that
  actuation is always a human call.

    1. Spec-basis                  -- did the jurisdiction proposal cite
                                       an OFFICIAL source
                                       (`marketentry.facts`), or invent
                                       one?
    2. Evidence incomplete         -- for `:filing/draft`/
                                       `:filing/submit`, has the
                                       jurisdiction actually been
                                       assessed with a full evidence
                                       checklist on file?
    3. MPME preference-margin
       mismatch                     -- for `:filing/submit`,
                                       INDEPENDENTLY recompute the Loi
                                       N°2020-26 Art. 77 al.3 mandatory
                                       5% bid-EVALUATION preference an
                                       engagement is entitled to (only
                                       when its own declared
                                       `:mpme-status?` is verified true),
                                       and HARD-hold if the engagement's
                                       own declared
                                       `:claimed-evaluated-price` does
                                       not match. FLAGSHIP genuinely new
                                       check for the iso3166 family
                                       (grep-verified absent as a
                                       governor check function name
                                       fleet-wide at build time) -- a
                                       CONDITIONAL BID-EVALUATION
                                       PRICE-ADJUSTMENT recompute, a
                                       sixth distinct check SHAPE
                                       alongside Bulgaria's
                                       percentage-of-turnover formula,
                                       Albania's flat-constant
                                       threshold, Azerbaijan's/Armenia's
                                       boolean registry-membership
                                       reads, and Antigua and Barbuda's
                                       three-tier threshold
                                       classification -- the first in
                                       this family to recompute a bid-
                                       EVALUATION price adjustment
                                       rather than an ELIGIBILITY gate.
    4. Engagement fee mismatch     -- for `:filing/submit`,
                                       INDEPENDENTLY recompute whether
                                       the engagement's own `:claimed-
                                       fee` equals `base-fee +
                                       monthly-rate x monitoring-
                                       months` -- honest reapplication
                                       of the ground-truth-recompute
                                       discipline sibling actors use.
    5. IFU unverified               -- for `:filing/submit`, when the
                                       engagement declares
                                       `:requires-ifu? true`,
                                       INDEPENDENTLY check
                                       `:ifu-verified?`. CONDITIONAL on
                                       the engagement's own ground
                                       truth. Grounded in the
                                       Identifiant Fiscal Unique (IFU)
                                       the Direction Générale des Impôts
                                       issues under Décret N°2006-201 --
                                       a SEPARATE legal act from RCCM
                                       registration, even though both
                                       are today submitted together
                                       through the same APIEx/GUFE
                                       guichet-unique intake.
    6. Confidence floor / actuation
       gate                          -- LLM confidence below threshold,
                                       OR the op is `:filing/draft`/
                                       `:filing/submit` (REAL acts)
                                       -> escalate.

  Two more guards, double-draft/double-submit prevention, are enforced
  off dedicated `:drafted?`/`:submitted?` facts (never a `:status`
  value)."
  (:require [marketentry.facts :as facts]
            [marketentry.registry :as registry]
            [marketentry.store :as store]))

(def confidence-floor 0.6)

(def high-stakes
  "Stakes grave enough to always require a human, even when clean.
  Drafting a real portal package and submitting a real portal
  registration are the two real-world actuation events this actor
  performs."
  #{:actuation/draft-filing :actuation/submit-filing})

;; ----------------------------- checks -----------------------------

(defn- spec-basis-violations
  "A `:jurisdiction/assess` (or `:filing/draft`/`:filing/submit`)
  proposal with no spec-basis citation is a HARD violation -- never
  invent a jurisdiction's market-entry requirements."
  [{:keys [op]} proposal]
  (when (contains? #{:jurisdiction/assess :filing/draft :filing/submit} op)
    (let [value (:value proposal)]
      (when (or (empty? (:cites proposal))
                (and (contains? value :spec-basis) (nil? (:spec-basis value))))
        [{:rule :no-spec-basis
          :detail "公式spec-basisの引用が無い提案は法域要件として扱えない"}]))))

(defn- evidence-incomplete-violations
  "For `:filing/draft`/`:filing/submit`, the jurisdiction's required
  registration evidence must actually be satisfied."
  [{:keys [op subject]} st]
  (when (contains? #{:filing/draft :filing/submit} op)
    (let [e (store/engagement st subject)
          assessment (store/assessment-of st subject)]
      (when-not (and assessment
                     (facts/required-evidence-satisfied?
                      (:jurisdiction e) (:checklist assessment)))
        [{:rule :evidence-incomplete
          :detail "法域の必要書類(RCCM登録/IFU/SIGMAP登録/代理人確認等)が充足していない状態での提案"}]))))

(defn- mpme-preference-margin-mismatch-violations
  "For `:filing/submit`, INDEPENDENTLY recompute Benin's Loi N°2020-26
  Art. 77 al.3 mandatory MPME bid-evaluation preference margin and
  HARD-hold if the engagement's own declared evaluated price does not
  match -- the flagship genuinely new check this vertical adds. This is
  evaluated for EVERY `:filing/submit`, unconditionally (not gated
  behind a `:requires-X?` engagement flag), the same 'always applies'
  shape Bulgaria's tax-arrears check and Azerbaijan's/Armenia's
  registry-membership checks use."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (let [e (store/engagement st subject)]
      (when (registry/evaluated-price-mismatches-claim? e)
        [{:rule :mpme-preference-margin-mismatch
          :detail (str subject " の申告評価価格(" (:claimed-evaluated-price e)
                      ")が独立再計算値(Art.77, "
                      (registry/compute-evaluated-price e)
                      ")と一致しない -- MPME優遇マージンの適用が不正確")}]))))

(defn- engagement-fee-mismatch-violations
  "For `:filing/submit`, INDEPENDENTLY recompute whether the
  engagement's own claimed fee equals base + months x rate."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (let [e (store/engagement st subject)]
      (when-not (registry/engagement-fee-matches-claim? e)
        [{:rule :engagement-fee-mismatch
          :detail (str subject " の申告手数料(" (:claimed-fee e)
                      ")が独立再計算値(" (registry/compute-engagement-fee e) ")と一致しない")}]))))

(defn- ifu-unverified-violations
  "For `:filing/submit`, when the engagement declares `:requires-ifu?
  true`, INDEPENDENTLY check `:ifu-verified?` -- CONDITIONAL on the
  engagement's own ground truth. Grounded in the Identifiant Fiscal
  Unique (IFU) the Direction Générale des Impôts issues under Décret
  N°2006-201, a separate legal act from RCCM registration."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (let [e (store/engagement st subject)]
      (when (and (true? (:requires-ifu? e))
                 (not (true? (:ifu-verified? e))))
        [{:rule :ifu-unverified
          :detail (str subject " はIFU(Identifiant Fiscal Unique)確認を要するが未確認 -- 提出提案は進められない")}]))))

(defn- already-drafted-violations
  "For `:filing/draft`, refuses to draft the SAME engagement twice."
  [{:keys [op subject]} st]
  (when (= op :filing/draft)
    (when (store/engagement-already-drafted? st subject)
      [{:rule :already-drafted
        :detail (str subject " は既にドラフト済み")}])))

(defn- already-submitted-violations
  "For `:filing/submit`, refuses to submit the SAME engagement twice."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (when (store/engagement-already-submitted? st subject)
      [{:rule :already-submitted
        :detail (str subject " は既に提出済み")}])))

(defn check
  "Censors a MarketEntry-LLM proposal against the governor rules.
  Returns {:ok? bool :violations [..] :confidence c :escalate? bool
  :high-stakes? bool :hard? bool}."
  [request _context proposal st]
  (let [hard (into []
                   (concat (spec-basis-violations request proposal)
                           (evidence-incomplete-violations request st)
                           (mpme-preference-margin-mismatch-violations request st)
                           (engagement-fee-mismatch-violations request st)
                           (ifu-unverified-violations request st)
                           (already-drafted-violations request st)
                           (already-submitted-violations request st)))
        conf (:confidence proposal 0.0)
        low? (< conf confidence-floor)
        stakes? (boolean (high-stakes (:stake proposal)))
        hard? (boolean (seq hard))]
    {:ok?          (and (not hard?) (not low?) (not stakes?))
     :violations   hard
     :confidence   conf
     :hard?        hard?
     :escalate?    (and (not hard?) (or low? stakes?))
     :high-stakes? stakes?}))

(defn hold-fact
  "The audit fact written when a proposal is rejected (HOLD)."
  [request context verdict]
  {:t          :governor-hold
   :op         (:op request)
   :actor      (:actor-id context)
   :subject    (:subject request)
   :disposition :hold
   :basis      (mapv :rule (:violations verdict))
   :violations (:violations verdict)
   :confidence (:confidence verdict)})
