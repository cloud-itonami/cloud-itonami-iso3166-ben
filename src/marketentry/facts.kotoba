(ns marketentry.facts
  "Per-jurisdiction public-procurement market-entry regulatory catalog
  -- the G2-style spec-basis table the Market-Entry Compliance Governor
  checks every `:jurisdiction/assess` proposal against ('did the advisor
  cite an OFFICIAL public source for this jurisdiction's requirements,
  or did it invent one?').

  Benin's real market-entry surface (WebFetch/curl-verified 2026-07-21;
  Benin's official legal-acts portal, sgg.gouv.bj -- Secrétariat Général
  du Gouvernement, Présidence de la République -- rendered directly on
  the FIRST WebFetch/curl attempt for every law in this catalog, unlike
  several other jurisdictions this loop has hit with JS-SPA or TLS
  problems, so every citation below is HIGH confidence, read directly,
  not a secondary-source fallback, unless a specific note below says
  otherwise):

  - The Autorité de Régulation des Marchés Publics (ARMP) is real and
    verified directly from the primary law text, not assumed from its
    plausible-sounding name: Loi N°2020-26 du 29 septembre 2020 portant
    Code des Marchés Publics en République du Bénin (downloaded directly
    from sgg.gouv.bj/doc/loi-2020-26/download and independently
    cross-checked against ARMP's own published copy at
    armp.bj/storage/2021/12/..., byte-for-byte matching text), Article
    17: 'Il est créé une autorité administrative indépendante dénommée
    Autorité de régulation des marchés publics (ARMP). Elle est l'organe
    de régulation de la commande publique et est rattachée à la
    présidence de la République.' Article 18 defines its composition (a
    tripartite Conseil de régulation + a Secrétariat permanent); Article
    19 provides for judicial recourse against its decisions.
  - This iteration specifically investigated, rather than assumed, WHICH
    body actually operates the e-procurement portal -- ARMP itself
    regulates/adjudicates disputes but does NOT operate SIGMAP (Système
    Intégré de Gestion des Marchés Publics, at marches-publics.bj): the
    Code's own Article 5 (in its DECRET N° 2020-598 DU 23 DECEMBRE 2020
    implementing text, part of the same PDF) assigns 'l'administration et
    l'exploitation du Système Intégré de Gestion des Marchés Publics
    (SIGMaP) et du portail web des marchés publics au Bénin' to a
    DIFFERENT body, the Direction Nationale de Contrôle des Marchés
    Publics (DNCMP) -- independently corroborated by the Ministry of
    Economy and Finance's own official page (finances.bj/services/sigmap),
    which names the DNCMP as SIGMAP's administering entity. This
    ARMP-regulates/DNCMP-operates split is a genuinely different
    institutional shape than any prior iso3166 sibling in this family
    (none of which split procurement regulation and e-procurement
    portal operation across two named bodies) -- this catalog cites ARMP
    as `:owner-authority` (the legal/regulatory authority whose Code this
    governor's spec-basis rests on) while naming DNCMP + SIGMAP/
    marches-publics.bj in `:national-spec` rather than conflating the two.
  - Business/tax identity, and the ONE-ACT-VS-TWO-ACTS question this
    loop asks every iteration to investigate for its own country: this
    iteration found Benin is neither a clean one-act model (Albania's
    QKB, Armenia's State Register, Azerbaijan's State Tax Service, each
    a single authority performing a single registration act) nor
    Antigua and Barbuda's sequential two-act model (a Certificate of
    Incorporation from one authority is a PREREQUISITE DOCUMENT for a
    separate, subsequent application to a second authority). Benin is a
    THIRD, distinct variant: legally TWO SEPARATE ACTS by TWO SEPARATE
    AUTHORITIES under TWO SEPARATE LEGAL BASES --
      1. RCCM (Registre du Commerce et du Crédit Mobilier) registration,
         at the greffe of the competent Tribunal de Commerce (confirmed
         directly on the Ministry of Justice's own site,
         justice.gouv.bj/service/43/..., which names 'Tribunal de
         commerce' as the administering court), under OHADA's Acte
         Uniforme relatif au Droit Commercial Général (AUDCG) --
         Articles 44(1)/46(1) (initial registration, natural/legal
         persons respectively), a SUPRANATIONAL instrument, not a
         Beninese statute (see `statute.facts` for the OHADA
         direct-effect finding);
      2. IFU (Identifiant Fiscal Unique) issuance, by the Direction
         Générale des Impôts (DGI), under Décret N°2006-201 du 08 mai
         2006 (downloaded directly from
         sgg.gouv.bj/doc/decret-2006-201/download and read in full),
         Article 1 ('Il est créé un numéro d'Identifiant Fiscal Unique
         (IFU) qui servira à la mise en place d'un répertoire national
         des personnes, institutions et associations'), Article 6
         ('La gestion du répertoire national est confiée à une structure
         de la Direction Générale des Impôts et des Domaines' -- the
         decree's own 2006 text still carries the older 'et des
         Domaines' suffix; the DGI's own 2020 published guide, 'Tout
         savoir sur l'IFU' (impots.finances.gouv.bj), confirms the
         modern name is simply 'Direction Générale des Impôts' and that
         'L'OBTENTION DU NUMÉRO IFU EST GRATUITE' -- free of charge).
    BUT, as of the Chambre de Commerce et d'Industrie du Bénin's own
    published fee schedule (ccibenin.org, 'Les formalités de création
    d'une entreprise et coûts', downloaded directly), these two legally
    separate acts are submitted as PARALLEL line items in a SINGLE
    application processed through ONE guichet unique intake: the
    Guichet Unique de Formalisation des Entreprises (GUFE), now a
    department of the Agence de Promotion des Investissements et des
    Exportations (APIEx) -- established by Décret N°2014-547 du 12
    septembre 2014 (read directly, a FAOLEX-hosted mirror of the
    official gazette text; independently confirmed still current via
    sgg.gouv.bj's own search index listing the same decree), whose
    Article 4 lists 's'occuper des formalités de création d'entreprises'
    among APIEx's own missions and states explicitly 'L'APIEX exerce en
    outre toutes les compétences dévolues au Guichet Unique des
    Formalités des Entreprises (GUFE), à l'Agence Béninoise de
    Promotion des Echanges Commerciaux (ABePEC) et au Centre de
    Promotion des Investissements (CPI)' -- confirming APIEx absorbed
    GUFE (not a separate surviving body), with Articles 29-32
    identifying the internal 'Département de la Formalisation des
    Entreprises' that runs this single-window intake today. This
    catalog cites the DGI as `:corporate-number-owner-authority` (the
    IFU issuer, matching the family's existing 'who issues the tax
    number' convention) and lists both the RCCM and IFU as SEPARATE
    `:required-evidence` items, each naming its own real legal basis,
    rather than collapsing them into one fabricated 'business
    registration' step.
  - `rep-spec-basis`: deliberately nil for BEN. This iteration
    specifically looked in the Code des Marchés Publics for a
    personal-exclusion-grounds provision extending disqualification to a
    bidder's own representatives/directors/officers -- the shape BGR's
    ЗОП Art. 54(2)-(3), ALB's Neni 76(1) and ARM's Article 6(1)(3) each
    document for their own laws -- and found something REAL but
    DIFFERENTLY SCOPED: Article 61 (conflict-of-interest incapacities)
    and Article 62 (restrictions on being declared an awardee, covering
    bankruptcy, penal/fiscal/social convictions, and prior procurement
    exclusion decisions) both close with the identical sentence 'Les
    incapacités et exclusions frappent également les membres des
    groupements et les sous-traitants' -- i.e. Benin's law extends
    exclusion grounds to CONSORTIUM MEMBERS AND SUBCONTRACTORS of a
    bidding entity, not to that entity's OWN representatives, directors
    or officers. That is a genuinely different scope of extension than
    every sibling's rep-spec-basis, so it is not force-fit into this
    accessor -- the same honest-scope-narrowing discipline ATG's catalog
    docstring already established for this family (a real but
    differently-shaped provision is not silently repurposed).
  - The MPME (micro, petite et moyenne entreprise) price-evaluation
    preference margin -- `mpme-preference-margin-spec-basis` -- grounds
    this vertical's FLAGSHIP check, read directly from Loi N°2020-26's
    own text, Article 77: a mandatory five-percent (5%) bid-evaluation
    preference 'lors de la passation d'un marché public une préférence
    de cinq pour cent (5%) doit être attribuée à l'offre présentée par
    une micro, petite et moyenne entreprise' -- see `marketentry.registry`
    for why only this MANDATORY branch is modeled, not Article 77's
    OTHER, discretionary subcontracting-based branch.

  Coverage is reported HONESTLY (see `coverage`): a jurisdiction not in
  this table has NO spec-basis, full stop -- the advisor must not
  fabricate one, and the governor holds if it tries.")

(def catalog
  "iso3 -> requirement map. `:required-evidence` mirrors the generic
  intake/portal-registration/filing evidence set; `:legal-basis` /
  `:owner-authority` / `:provenance` are the G2 citation the governor
  requires before any `:jurisdiction/assess` proposal can commit. BEN
  deliberately carries NO `:rep-owner-authority` -- see the namespace
  docstring's honest-scope-narrowing note. `:mpme-preference-owner-
  authority` / `:mpme-preference-legal-basis` / `:mpme-preference-
  provenance` ground this vertical's flagship governor check
  (`mpme-preference-margin-spec-basis`)."
  {"BEN" {:name "Benin"
          :owner-authority "Autorité de Régulation des Marchés Publics (ARMP) -- an independent administrative authority attached to the Presidency of the Republic"
          :legal-basis "Loi N°2020-26 du 29 septembre 2020 portant Code des Marchés Publics en République du Bénin -- Art. 17 (creates ARMP) + Art. 18 (Conseil de régulation + Secrétariat permanent) + Art. 19 (judicial recourse against ARMP decisions)"
          :national-spec "Système Intégré de Gestion des Marchés Publics (SIGMAP), operated at marches-publics.bj by the Direction Nationale de Contrôle des Marchés Publics (DNCMP) -- a body DIFFERENT from ARMP (Décret N°2020-598 du 23 décembre 2020, Art. 5, one of the Code's own implementing décrets; independently confirmed by the Ministry of Economy and Finance's own finances.bj/services/sigmap page)"
          :provenance "https://sgg.gouv.bj/doc/loi-2020-26/download"
          :required-evidence ["RCCM registration record (Registre du Commerce et du Crédit Mobilier -- immatriculation at the greffe of the competent Tribunal de Commerce, per the Ministry of Justice's own justice.gouv.bj, under OHADA's Acte Uniforme relatif au Droit Commercial Général Art. 44(1)/46(1))"
                              "IFU record (Identifiant Fiscal Unique -- Direction Générale des Impôts, Décret N°2006-201 du 08 mai 2006, issued free of charge)"
                              "SIGMAP/marches-publics.bj tender-participation registration record (Direction Nationale de Contrôle des Marchés Publics)"
                              "Authorized-representative confirmation record"]
          :corporate-number-owner-authority "Direction Générale des Impôts (DGI)"
          :corporate-number-legal-basis "Décret N°2006-201 du 08 mai 2006 portant création d'un numéro d'Identifiant Fiscal Unique (IFU) et d'un répertoire national des personnes, institutions et associations -- Art. 1 (creates the IFU) + Art. 6 (assigns administration to a DGI structure); the IFU is a SEPARATE, distinct legal act from RCCM registration (a different authority, a different decree), even though both are today submitted together through the same APIEx/GUFE guichet-unique intake (Décret N°2014-547 du 12 septembre 2014, Art. 4)"
          :corporate-number-provenance "https://sgg.gouv.bj/doc/decret-2006-201/download"
          :mpme-preference-owner-authority "Autorité de Régulation des Marchés Publics (ARMP) -- the Code's regulator; the preference itself is applied by each autorité contractante during bid evaluation"
          :mpme-preference-legal-basis "Loi N°2020-26 du 29 septembre 2020, Art. 77 al.3: 'lors de la passation d'un marché public une préférence de cinq pour cent (5%) doit être attribuée à l'offre présentée par une micro, petite et moyenne entreprise'"
          :mpme-preference-provenance "https://sgg.gouv.bj/doc/loi-2020-26/download"}
   "USA" {:name "United States"
          :owner-authority "U.S. General Services Administration (GSA) / SAM.gov"
          :legal-basis "Federal Acquisition Regulation (FAR); System for Award Management"
          :national-spec "SAM.gov entity registration + NAICS self-certification"
          :provenance "https://sam.gov/"
          :required-evidence ["EIN record"
                              "SAM.gov registration record"
                              "State business registration record"
                              "Authorized-representative record"]}
   "DEU" {:name "Germany"
          :owner-authority "Beschaffungsamt des BMI / e-Vergabe platforms"
          :legal-basis "Gesetz gegen Wettbewerbsbeschränkungen (GWB) / VgV"
          :national-spec "e-Vergabe supplier registration under EU procurement directives"
          :provenance "https://www.evergabe-online.de/"
          :required-evidence ["Handelsregister extract"
                              "e-Vergabe registration record"
                              "USt-IdNr record"
                              "Authorized-representative record"]}})

(defn spec-basis
  "The jurisdiction's requirement map, or nil -- nil means NO spec-basis,
  and the governor must hold any proposal that tries to assess or file
  on it."
  [iso3]
  (get catalog iso3))

(defn coverage
  "Honest coverage report: how many of the requested jurisdictions actually
  have a spec-basis entry. Never report a missing jurisdiction as covered."
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-iso3166-ben R0: " (count catalog)
                 " jurisdictions seeded with an official spec-basis. "
                 "This is a starting catalog for market-entry navigation, "
                 "not a survey of all ~194 jurisdictions -- extend "
                 "`marketentry.facts/catalog`, never fabricate a "
                 "jurisdiction's requirements.")})))

(defn required-evidence-satisfied?
  "Does `submitted` (a set/coll of evidence keywords or strings) satisfy
  every evidence item listed for `iso3`? Missing spec-basis -> never
  satisfied."
  [iso3 submitted]
  (when-let [{:keys [required-evidence]} (spec-basis iso3)]
    (let [need (count required-evidence)
          have (count (filter (set submitted) required-evidence))]
      (= need have))))

(defn evidence-checklist [iso3]
  (:required-evidence (spec-basis iso3) []))

(defn rep-spec-basis
  "The jurisdiction's representative-related requirement map, or nil when
  this catalog has no such regime. For BEN this is deliberately nil --
  see the `catalog` docstring's honest-scope-narrowing note (Benin's own
  Art. 61/62 exclusion-extension provisions cover consortium members and
  subcontractors, not a bidder's own representatives/directors)."
  [iso3]
  (when-let [sb (spec-basis iso3)]
    (when (:rep-owner-authority sb)
      (select-keys sb [:rep-owner-authority :rep-legal-basis :rep-provenance]))))

(defn corporate-number-spec-basis
  "The jurisdiction's corporate-number / tax-id regime, or nil."
  [iso3]
  (when-let [sb (spec-basis iso3)]
    (when (:corporate-number-owner-authority sb)
      (select-keys sb [:corporate-number-owner-authority
                       :corporate-number-legal-basis
                       :corporate-number-provenance]))))

(defn mpme-preference-margin-spec-basis
  "The jurisdiction's MPME (micro/petite/moyenne entreprise) bid-evaluation
  preference-margin regime, or nil. For BEN this is real and current --
  the flagship check this vertical adds is grounded here (Art. 77)."
  [iso3]
  (when-let [sb (spec-basis iso3)]
    (when (:mpme-preference-owner-authority sb)
      (select-keys sb [:mpme-preference-owner-authority
                       :mpme-preference-legal-basis
                       :mpme-preference-provenance]))))
