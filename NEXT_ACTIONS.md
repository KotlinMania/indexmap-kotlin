# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 24/32 (75.0%)
- **Function parity:** 509/802 matched (target 1044) — 63.5%
- **Class/type parity:** 51/106 matched (target 101) — 48.1%
- **Combined symbol parity:** 560/908 matched (target 1145) — 61.7%
- **Average inline-code cosine:** 0.36 (function body across 23 matched files)
- **Average documentation cosine:** 0.40 (doc text across 23 matched files)
- **Cheat-zeroed Files:** 2
- **Critical Issues:** 19 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. map.slice

- **Target:** `map.Slice [PROVENANCE-FALLBACK]`
- **Similarity:** 0.39
- **Dependents:** 5
- **Priority Score:** 5056806.0
- **Functions:** 62/64 matched (target 97)
- **Missing functions:** `check`, `check_mut`
- **Types:** 1/4 matched (target 3)
- **Missing types:** `IntoIter`, `Item`, `Output`
- **Tests:** 10/12 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `map/slice.rs` vs expected `map/slice.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:map/slice.rs` vs expected `map/slice.rs`
- **Proposed provenance header:** `// port-lint: source map/slice.rs` (current: `// port-lint: source map/slice.rs`)
- **Proposed provenance header:** `// port-lint: tests map/slice.rs` (current: `// port-lint: tests map/slice.rs`)
- **Lint issues:** 2

### 2. map.entry

- **Target:** `map.Entry [PROVENANCE-FALLBACK]`
- **Similarity:** 0.53
- **Dependents:** 1
- **Priority Score:** 1012904.7
- **Functions:** 26/27 matched (target 71)
- **Missing functions:** `assert_send_sync`
- **Types:** 2/2 matched (target 6)
- **Missing types:** _none_
- **Tests:** 0/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `map/entry.rs` vs expected `map/entry.rs`
- **Proposed provenance header:** `// port-lint: source map/entry.rs` (current: `// port-lint: source map/entry.rs`)
- **Lint issues:** 1

### 3. indexmap.arbitrary

- **Target:** `indexmap.Arbitrary [PROVENANCE-FALLBACK]`
- **Similarity:** 0.24
- **Dependents:** 1
- **Priority Score:** 1010307.6
- **Functions:** 2/3 matched (target 8)
- **Missing functions:** `shrink`
- **Types:** 0/0 matched (target 6)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `arbitrary.rs` vs expected `arbitrary.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:arbitrary.rs` vs expected `arbitrary.rs`
- **Proposed provenance header:** `// port-lint: source arbitrary.rs` (current: `// port-lint: source arbitrary.rs`)
- **Proposed provenance header:** `// port-lint: tests arbitrary.rs` (current: `// port-lint: tests arbitrary.rs`)
- **Lint issues:** 2

### 4. rayon.set

- **Target:** `rayon.Set [PROVENANCE-FALLBACK]`
- **Similarity:** 0.01
- **Dependents:** 0
- **Priority Score:** 374009.9
- **Functions:** 3/30 matched (target 7)
- **Missing functions:** `fmt`, `clone`, `par_drain`, `par_difference`, `par_symmetric_difference`, `par_intersection`, `par_union`, `par_eq`, `par_is_disjoint`, `par_is_superset`, `par_is_subset`, `drive_unindexed`, `par_sorted_by`, `par_sort_by_key`, `par_sort_unstable`, `par_sort_unstable_by`, `par_sorted_unstable_by`, `par_sort_unstable_by_key`, `par_sort_by_cached_key`, `from_par_iter`, `par_extend`, `insert_order`, `partial_eq_and_eq`, `extend`, `comparisons`, `iter_comparisons`, `check`
- **Types:** 0/10 matched (target 2)
- **Missing types:** `Bucket`, `Item`, `Iter`, `IntoParIter`, `ParIter`, `ParDrain`, `ParDifference`, `ParIntersection`, `ParSymmetricDifference`, `ParUnion`
- **Tests:** 0/6 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `rayon/set.rs` vs expected `rayon/set.rs`
- **Proposed provenance header:** `// port-lint: source rayon/set.rs` (current: `// port-lint: source rayon/set.rs`)
- **Lint issues:** 1

### 5. rayon.map

- **Target:** `rayon.Map [PROVENANCE-FALLBACK]`
- **Similarity:** 0.05
- **Dependents:** 0
- **Priority Score:** 253409.5
- **Functions:** 5/25 matched (target 11)
- **Missing functions:** `fmt`, `clone`, `par_drain`, `par_eq`, `par_values_mut`, `par_sorted_by`, `par_sort_by_key`, `par_sort_unstable_keys`, `par_sort_unstable_by`, `par_sorted_unstable_by`, `par_sort_unstable_by_key`, `par_sort_by_cached_key`, `from_par_iter`, `par_extend`, `insert_order`, `partial_eq_and_eq`, `extend`, `keys`, `values`, `values_mut`
- **Types:** 4/9 matched (target 4)
- **Missing types:** `Item`, `Iter`, `ParIterMut`, `ParDrain`, `ParValuesMut`
- **Tests:** 0/6 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `rayon/map.rs` vs expected `rayon/map.rs`
- **Proposed provenance header:** `// port-lint: source rayon/map.rs` (current: `// port-lint: source rayon/map.rs`)
- **Lint issues:** 1

### 6. indexmap.serde

- **Target:** `indexmap.Serde [PROVENANCE-FALLBACK]`
- **Similarity:** 0.13
- **Dependents:** 0
- **Priority Score:** 81108.7
- **Functions:** 3/7 matched (target 8)
- **Missing functions:** `expecting`, `visit_map`, `into_deserializer`, `visit_seq`
- **Types:** 0/4 matched (target 3)
- **Missing types:** `IndexMapVisitor`, `Value`, `Deserializer`, `IndexSetVisitor`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `serde.rs` vs expected `serde.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:serde.rs` vs expected `serde.rs`
- **Proposed provenance header:** `// port-lint: source serde.rs` (current: `// port-lint: source serde.rs`)
- **Proposed provenance header:** `// port-lint: tests serde.rs` (current: `// port-lint: tests serde.rs`)
- **Lint issues:** 2

### 7. map.mutable

- **Target:** `map.Mutable [PROVENANCE-FALLBACK]`
- **Similarity:** 0.09
- **Dependents:** 0
- **Priority Score:** 71009.1
- **Functions:** 1/5 matched (target 1)
- **Missing functions:** `get_full_mut2`, `get_index_mut2`, `iter_mut2`, `retain2`
- **Types:** 2/5 matched (target 2)
- **Missing types:** `Key`, `Value`, `Sealed`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `map/mutable.rs` vs expected `map/mutable.rs`
- **Proposed provenance header:** `// port-lint: source map/mutable.rs` (current: `// port-lint: source map/mutable.rs`)
- **Lint issues:** 1

### 8. map.iter

- **Target:** `map.Iter [PROVENANCE-FALLBACK]`
- **Similarity:** 0.32
- **Dependents:** 0
- **Priority Score:** 52806.8
- **Functions:** 11/14 matched (target 154)
- **Missing functions:** `into_iter`, `index`, `drop`
- **Types:** 12/14 matched (target 13)
- **Missing types:** `Item`, `Output`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `map/iter.rs` vs expected `map/iter.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:map/iter.rs` vs expected `map/iter.rs`
- **Proposed provenance header:** `// port-lint: source map/iter.rs` (current: `// port-lint: source map/iter.rs`)
- **Proposed provenance header:** `// port-lint: tests map/iter.rs` (current: `// port-lint: tests map/iter.rs`)
- **Lint issues:** 2

### 9. set.slice

- **Target:** `set.Slice [PROVENANCE-FALLBACK]`
- **Similarity:** 0.31
- **Dependents:** 0
- **Priority Score:** 43906.9
- **Functions:** 34/35 matched (target 60)
- **Missing functions:** `check`
- **Types:** 1/4 matched (target 2)
- **Missing types:** `IntoIter`, `Item`, `Output`
- **Tests:** 1/2 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `set/slice.rs` vs expected `set/slice.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:set/slice.rs` vs expected `set/slice.rs`
- **Proposed provenance header:** `// port-lint: source set/slice.rs` (current: `// port-lint: source set/slice.rs`)
- **Proposed provenance header:** `// port-lint: tests set/slice.rs` (current: `// port-lint: tests set/slice.rs`)
- **Lint issues:** 2

### 10. map.serde_seq

- **Target:** `map.SerdeSeq [PROVENANCE-FALLBACK]`
- **Similarity:** 0.09
- **Dependents:** 0
- **Priority Score:** 40609.1
- **Functions:** 2/4 matched (target 2)
- **Missing functions:** `expecting`, `visit_seq`
- **Types:** 0/2 matched (target 1)
- **Missing types:** `SeqVisitor`, `Value`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `map/serde_seq.rs` vs expected `map/serde_seq.rs`
- **Proposed provenance header:** `// port-lint: source map/serde_seq.rs` (current: `// port-lint: source map/serde_seq.rs`)
- **Lint issues:** 1

### 11. indexmap.inner

- **Target:** `inner.Inner [PROVENANCE-FALLBACK]`
- **Similarity:** 0.70
- **Dependents:** 0
- **Priority Score:** 35703.0
- **Functions:** 52/54 matched (target 67)
- **Missing functions:** `par_drain`, `assert_send_sync`
- **Types:** 2/3 matched
- **Missing types:** `Entries`
- **Tests:** 0/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `inner.rs` vs expected `inner.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:inner.rs` vs expected `inner.rs`
- **Proposed provenance header:** `// port-lint: source inner.rs` (current: `// port-lint: source inner.rs`)
- **Proposed provenance header:** `// port-lint: tests inner.rs` (current: `// port-lint: tests inner.rs`)
- **Lint issues:** 2

### 12. map.raw_entry_v1

- **Target:** `map.RawEntryV1 [PROVENANCE-FALLBACK]`
- **Similarity:** 0.50
- **Dependents:** 0
- **Priority Score:** 34205.0
- **Functions:** 33/35 matched (target 61)
- **Missing functions:** `raw_entry_v1`, `raw_entry_mut_v1`
- **Types:** 6/7 matched (target 9)
- **Missing types:** `Sealed`
- **Tests:** 1/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `map/raw_entry_v1.rs` vs expected `map/raw_entry_v1.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:map/raw_entry_v1.rs` vs expected `map/raw_entry_v1.rs`
- **Proposed provenance header:** `// port-lint: source map/raw_entry_v1.rs` (current: `// port-lint: source map/raw_entry_v1.rs`)
- **Proposed provenance header:** `// port-lint: tests map/raw_entry_v1.rs` (current: `// port-lint: tests map/raw_entry_v1.rs`)
- **Lint issues:** 2

### 13. indexmap.set

- **Target:** `indexmap.Set [PROVENANCE-FALLBACK]`
- **Similarity:** 0.59
- **Dependents:** 0
- **Priority Score:** 30004.1
- **Functions:** 97/97 matched (target 148)
- **Missing functions:** _none_
- **Types:** 1/3 matched (target 2)
- **Missing types:** `Bucket`, `Output`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `set.rs` vs expected `set.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:set.rs` vs expected `set.rs`
- **Proposed provenance header:** `// port-lint: source set.rs` (current: `// port-lint: source set.rs`)
- **Proposed provenance header:** `// port-lint: tests set.rs` (current: `// port-lint: tests set.rs`)
- **Lint issues:** 2

### 14. inner.entry

- **Target:** `inner.Entry [PROVENANCE-FALLBACK]`
- **Similarity:** 0.64
- **Dependents:** 0
- **Priority Score:** 23303.6
- **Functions:** 29/31 matched (target 32)
- **Missing functions:** `new`, `from`
- **Types:** 2/2 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `inner/entry.rs` vs expected `inner/entry.rs`
- **Proposed provenance header:** `// port-lint: source inner/entry.rs` (current: `// port-lint: source inner/entry.rs`)
- **Lint issues:** 1

### 15. set.iter

- **Target:** `set.Iter [PROVENANCE-FALLBACK]`
- **Similarity:** 0.27
- **Dependents:** 0
- **Priority Score:** 22307.3
- **Functions:** 11/12 matched (target 77)
- **Missing functions:** `into_iter`
- **Types:** 10/11 matched
- **Missing types:** `Item`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `set/iter.rs` vs expected `set/iter.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:set/iter.rs` vs expected `set/iter.rs`
- **Proposed provenance header:** `// port-lint: source set/iter.rs` (current: `// port-lint: source set/iter.rs`)
- **Proposed provenance header:** `// port-lint: tests set/iter.rs` (current: `// port-lint: tests set/iter.rs`)
- **Lint issues:** 2

### 16. indexmap.map

- **Target:** `indexmap.Map [PROVENANCE-FALLBACK]`
- **Similarity:** 0.45
- **Dependents:** 0
- **Priority Score:** 21005.5
- **Functions:** 108/108 matched (target 147)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `Output`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `map.rs` vs expected `map.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:map.rs` vs expected `map.rs`
- **Proposed provenance header:** `// port-lint: source map.rs` (current: `// port-lint: source map.rs`)
- **Proposed provenance header:** `// port-lint: tests map.rs` (current: `// port-lint: tests map.rs`)
- **Lint issues:** 2

### 17. set.mutable

- **Target:** `set.Mutable [PROVENANCE-FALLBACK]`
- **Similarity:** 0.63
- **Dependents:** 0
- **Priority Score:** 20603.7
- **Functions:** 3/3 matched
- **Missing functions:** _none_
- **Types:** 1/3 matched (target 1)
- **Missing types:** `Value`, `Sealed`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `set/mutable.rs` vs expected `set/mutable.rs`
- **Proposed provenance header:** `// port-lint: source set/mutable.rs` (current: `// port-lint: source set/mutable.rs`)
- **Lint issues:** 1

### 18. indexmap.borsh

- **Target:** `indexmap.Borsh [PROVENANCE-FALLBACK]`
- **Similarity:** 0.26
- **Dependents:** 0
- **Priority Score:** 10507.4
- **Functions:** 4/5 matched (target 15)
- **Missing functions:** `deserialize_reader`
- **Types:** 0/0 matched (target 7)
- **Missing types:** _none_
- **Tests:** 2/2 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `borsh.rs` vs expected `borsh.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:borsh.rs` vs expected `borsh.rs`
- **Proposed provenance header:** `// port-lint: source borsh.rs` (current: `// port-lint: source borsh.rs`)
- **Proposed provenance header:** `// port-lint: tests borsh.rs` (current: `// port-lint: tests borsh.rs`)
- **Lint issues:** 2

### 19. inner.extract

- **Target:** `inner.Extract [PROVENANCE-FALLBACK]`
- **Similarity:** 0.60
- **Dependents:** 0
- **Priority Score:** 10504.0
- **Functions:** 3/4 matched (target 3)
- **Missing functions:** `drop`
- **Types:** 1/1 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `inner/extract.rs` vs expected `inner/extract.rs`
- **Proposed provenance header:** `// port-lint: source inner/extract.rs` (current: `// port-lint: source inner/extract.rs`)
- **Lint issues:** 1

### 20. indexmap.sval

- **Target:** `indexmap.Sval [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10110.0
- **Functions:** 0/1 matched (target 15)
- **Missing functions:** `stream`
- **Types:** 0/0 matched (target 3)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `sval.rs` vs expected `sval.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:sval.rs` vs expected `sval.rs`
- **Proposed provenance header:** `// port-lint: source sval.rs` (current: `// port-lint: source sval.rs`)
- **Proposed provenance header:** `// port-lint: tests sval.rs` (current: `// port-lint: tests sval.rs`)
- **Lint issues:** 2

### 21. indexmap.lib

- **Target:** `indexmap.Lib [PROVENANCE-FALLBACK]`
- **Similarity:** 0.71
- **Dependents:** 0
- **Priority Score:** 2002.9
- **Functions:** 15/15 matched (target 36)
- **Missing functions:** _none_
- **Types:** 5/5 matched (target 11)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `lib.rs` vs expected `lib.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:lib.rs` vs expected `lib.rs`
- **Proposed provenance header:** `// port-lint: source lib.rs` (current: `// port-lint: source lib.rs`)
- **Proposed provenance header:** `// port-lint: tests lib.rs` (current: `// port-lint: tests lib.rs`)
- **Lint issues:** 2

### 22. indexmap.util

- **Target:** `indexmap.Util [PROVENANCE-FALLBACK]`
- **Similarity:** 0.68
- **Dependents:** 0
- **Priority Score:** 403.2
- **Functions:** 4/4 matched (target 14)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 7)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `util.rs` vs expected `util.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:util.rs` vs expected `util.rs`
- **Proposed provenance header:** `// port-lint: source util.rs` (current: `// port-lint: source util.rs`)
- **Proposed provenance header:** `// port-lint: tests util.rs` (current: `// port-lint: tests util.rs`)
- **Lint issues:** 2

### 23. rayon.mod

- **Target:** `rayon.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 110.0
- **Functions:** 1/1 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `rayon/mod.rs` vs expected `rayon/mod.rs`
- **Proposed provenance header:** `// port-lint: source rayon/mod.rs` (current: `// port-lint: source rayon/mod.rs`)
- **Lint issues:** 1

### 24. indexmap.macros

- **Target:** `indexmap.Macros [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 6)
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `macros.rs` vs expected `macros.rs`
- **Proposed provenance header:** `// port-lint: source macros.rs` (current: `// port-lint: source macros.rs`)
- **Lint issues:** 1

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

