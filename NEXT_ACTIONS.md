# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 26/26 (100.0%)
- **Function parity:** 653/706 matched (target 1228) — 92.5%
- **Class/type parity:** 74/95 matched (target 132) — 77.9%
- **Combined symbol parity:** 727/801 matched (target 1360) — 90.8%
- **Average inline-code cosine:** 0.44 (function body across 25 matched files)
- **Average documentation cosine:** 0.38 (doc text across 25 matched files)
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

- **Target:** `map.Slice`
- **Similarity:** 0.39
- **Dependents:** 5
- **Priority Score:** 5026806.0
- **Functions:** 62/64 matched (target 99)
- **Missing functions:** `check`, `check_mut`
- **Types:** 4/4 matched (target 9)
- **Missing types:** _none_
- **Tests:** 10/12 matched

### 2. rayon.set

- **Target:** `rayon.Set`
- **Similarity:** 0.01
- **Dependents:** 0
- **Priority Score:** 374009.9
- **Functions:** 3/30 matched (target 7)
- **Missing functions:** `fmt`, `clone`, `par_drain`, `par_difference`, `par_symmetric_difference`, `par_intersection`, `par_union`, `par_eq`, `par_is_disjoint`, `par_is_superset`, `par_is_subset`, `drive_unindexed`, `par_sorted_by`, `par_sort_by_key`, `par_sort_unstable`, `par_sort_unstable_by`, `par_sorted_unstable_by`, `par_sort_unstable_by_key`, `par_sort_by_cached_key`, `from_par_iter`, `par_extend`, `insert_order`, `partial_eq_and_eq`, `extend`, `comparisons`, `iter_comparisons`, `check`
- **Types:** 0/10 matched (target 2)
- **Missing types:** `Bucket`, `Item`, `Iter`, `IntoParIter`, `ParIter`, `ParDrain`, `ParDifference`, `ParIntersection`, `ParSymmetricDifference`, `ParUnion`
- **Tests:** 0/6 matched

### 3. rayon.map

- **Target:** `rayon.Map`
- **Similarity:** 0.05
- **Dependents:** 0
- **Priority Score:** 253409.5
- **Functions:** 5/25 matched (target 11)
- **Missing functions:** `fmt`, `clone`, `par_drain`, `par_eq`, `par_values_mut`, `par_sorted_by`, `par_sort_by_key`, `par_sort_unstable_keys`, `par_sort_unstable_by`, `par_sorted_unstable_by`, `par_sort_unstable_by_key`, `par_sort_by_cached_key`, `from_par_iter`, `par_extend`, `insert_order`, `partial_eq_and_eq`, `extend`, `keys`, `values`, `values_mut`
- **Types:** 4/9 matched (target 4)
- **Missing types:** `Item`, `Iter`, `ParIterMut`, `ParDrain`, `ParValuesMut`
- **Tests:** 0/6 matched

### 4. map.tests

- **Target:** `map.MapTests [PROVENANCE-FALLBACK]`
- **Similarity:** 0.57
- **Dependents:** 0
- **Priority Score:** 26904.3
- **Functions:** 66/66 matched (target 78)
- **Missing functions:** _none_
- **Types:** 1/3 matched (target 5)
- **Missing types:** `K`, `V`
- **Tests:** 63/63 matched
- **Provenance warning:** port-lint provenance header matched only by basename: `tests:tests/tests.rs` vs expected `map/tests.rs`
- **Proposed provenance header:** `// port-lint: tests map/tests.rs` (current: `// port-lint: tests tests/tests.rs`)
- **Lint issues:** 1

### 5. inner

- **Target:** `inner.Inner`
- **Similarity:** 0.70
- **Dependents:** 0
- **Priority Score:** 25703.0
- **Functions:** 52/54 matched (target 68)
- **Missing functions:** `par_drain`, `assert_send_sync`
- **Types:** 3/3 matched (target 4)
- **Missing types:** _none_
- **Tests:** 0/1 matched

### 6. serde

- **Target:** `indexmap.Serde`
- **Similarity:** 0.36
- **Dependents:** 0
- **Priority Score:** 21106.4
- **Functions:** 7/7 matched (target 17)
- **Missing functions:** _none_
- **Types:** 2/4 matched (target 5)
- **Missing types:** `Value`, `Deserializer`

### 7. set.tests

- **Target:** `set.SetTests`
- **Similarity:** 0.77
- **Dependents:** 0
- **Priority Score:** 15702.3
- **Functions:** 56/56 matched (target 57)
- **Missing functions:** _none_
- **Types:** 0/1 matched
- **Missing types:** `Item`
- **Tests:** 53/53 matched

### 8. set.slice

- **Target:** `set.Slice`
- **Similarity:** 0.32
- **Dependents:** 0
- **Priority Score:** 13906.8
- **Functions:** 34/35 matched (target 61)
- **Missing functions:** `check`
- **Types:** 4/4 matched (target 5)
- **Missing types:** _none_
- **Tests:** 1/2 matched

### 9. map.entry

- **Target:** `map.Entry`
- **Similarity:** 0.56
- **Dependents:** 0
- **Priority Score:** 12904.4
- **Functions:** 26/27 matched (target 73)
- **Missing functions:** `assert_send_sync`
- **Types:** 2/2 matched (target 6)
- **Missing types:** _none_
- **Tests:** 0/1 matched

### 10. map

- **Target:** `indexmap.Map`
- **Similarity:** 0.45
- **Dependents:** 0
- **Priority Score:** 11005.5
- **Functions:** 108/108 matched (target 147)
- **Missing functions:** _none_
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_

### 11. map.serde_seq

- **Target:** `map.SerdeSeq`
- **Similarity:** 0.30
- **Dependents:** 0
- **Priority Score:** 10607.0
- **Functions:** 4/4 matched (target 7)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `Value`

### 12. set

- **Target:** `indexmap.Set`
- **Similarity:** 0.59
- **Dependents:** 0
- **Priority Score:** 10004.1
- **Functions:** 97/97 matched (target 148)
- **Missing functions:** _none_
- **Types:** 3/3 matched (target 4)
- **Missing types:** _none_

### 13. map.raw_entry_v1

- **Target:** `map.RawEntryV1`
- **Similarity:** 0.53
- **Dependents:** 0
- **Priority Score:** 4204.7
- **Functions:** 35/35 matched (target 63)
- **Missing functions:** _none_
- **Types:** 7/7 matched (target 10)
- **Missing types:** _none_
- **Tests:** 1/1 matched

### 14. inner.entry

- **Target:** `inner.Entry`
- **Similarity:** 0.67
- **Dependents:** 0
- **Priority Score:** 3303.3
- **Functions:** 31/31 matched (target 34)
- **Missing functions:** _none_
- **Types:** 2/2 matched
- **Missing types:** _none_

### 15. map.iter

- **Target:** `map.Iter`
- **Similarity:** 0.34
- **Dependents:** 0
- **Priority Score:** 2806.6
- **Functions:** 14/14 matched (target 162)
- **Missing functions:** _none_
- **Types:** 14/14 matched (target 15)
- **Missing types:** _none_

### 16. set.iter

- **Target:** `set.Iter`
- **Similarity:** 0.28
- **Dependents:** 0
- **Priority Score:** 2307.2
- **Functions:** 12/12 matched (target 79)
- **Missing functions:** _none_
- **Types:** 11/11 matched (target 12)
- **Missing types:** _none_

### 17. lib

- **Target:** `indexmap.Lib`
- **Similarity:** 0.71
- **Dependents:** 0
- **Priority Score:** 2002.9
- **Functions:** 15/15 matched (target 36)
- **Missing functions:** _none_
- **Types:** 5/5 matched (target 11)
- **Missing types:** _none_

### 18. map.mutable

- **Target:** `map.Mutable`
- **Similarity:** 0.37
- **Dependents:** 0
- **Priority Score:** 1006.3
- **Functions:** 5/5 matched
- **Missing functions:** _none_
- **Types:** 5/5 matched
- **Missing types:** _none_

### 19. set.mutable

- **Target:** `set.Mutable`
- **Similarity:** 0.63
- **Dependents:** 0
- **Priority Score:** 603.7
- **Functions:** 3/3 matched
- **Missing functions:** _none_
- **Types:** 3/3 matched
- **Missing types:** _none_

### 20. borsh

- **Target:** `indexmap.Borsh`
- **Similarity:** 0.38
- **Dependents:** 0
- **Priority Score:** 506.2
- **Functions:** 5/5 matched (target 19)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 7)
- **Missing types:** _none_
- **Tests:** 2/2 matched

### 21. inner.extract

- **Target:** `inner.Extract`
- **Similarity:** 0.61
- **Dependents:** 0
- **Priority Score:** 503.9
- **Functions:** 4/4 matched
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_

### 22. util

- **Target:** `indexmap.Util`
- **Similarity:** 0.68
- **Dependents:** 0
- **Priority Score:** 403.2
- **Functions:** 4/4 matched (target 14)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 7)
- **Missing types:** _none_

### 23. arbitrary

- **Target:** `indexmap.Arbitrary`
- **Similarity:** 0.29
- **Dependents:** 0
- **Priority Score:** 307.1
- **Functions:** 3/3 matched (target 11)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 6)
- **Missing types:** _none_

### 24. rayon.mod

- **Target:** `rayon.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 110.0
- **Functions:** 1/1 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 25. sval

- **Target:** `indexmap.Sval`
- **Similarity:** 0.48
- **Dependents:** 0
- **Priority Score:** 105.2
- **Functions:** 1/1 matched (target 18)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 3)
- **Missing types:** _none_

### 26. macros

- **Target:** `indexmap.Macros [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 6)
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

