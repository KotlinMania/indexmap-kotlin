# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 14/26 (53.8%)
- **Function parity:** 381/741 matched (target 758) — 51.4%
- **Class/type parity:** 41/95 matched (target 69) — 43.2%
- **Combined symbol parity:** 422/836 matched (target 827) — 50.5%
- **Average inline-code cosine:** 0.31 (function body across 14 matched files)
- **Average documentation cosine:** 0.00 (doc text across 14 matched files)
- **Cheat-zeroed Files:** 3
- **Critical Issues:** 12 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. map.slice

- **Target:** `map.Slice`
- **Similarity:** 0.31
- **Dependents:** 5
- **Priority Score:** 5106807.0
- **Functions:** 57/64 matched (target 85)
- **Missing functions:** `check`, `slice_index_mut`, `check_mut`, `slice_new_mut`, `slice_get_index_mut`, `slice_split_first_mut`, `slice_split_last_mut`
- **Types:** 1/4 matched (target 3)
- **Missing types:** `IntoIter`, `Item`, `Output`
- **Tests:** 5/12 matched

### 2. rayon.map

- **Target:** `map.Typealiases [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 343410.0
- **Functions:** 0/25 matched (target 0)
- **Missing functions:** `into_par_iter`, `fmt`, `clone`, `par_drain`, `par_keys`, `par_values`, `par_eq`, `par_values_mut`, `par_sort_keys`, `par_sort_by`, `par_sorted_by`, `par_sort_by_key`, `par_sort_unstable_keys`, `par_sort_unstable_by`, `par_sorted_unstable_by`, `par_sort_unstable_by_key`, `par_sort_by_cached_key`, `from_par_iter`, `par_extend`, `insert_order`, `partial_eq_and_eq`, `extend`, `keys`, `values`, `values_mut`
- **Types:** 0/9 matched (target 1)
- **Missing types:** `Item`, `Iter`, `IntoParIter`, `ParIter`, `ParIterMut`, `ParDrain`, `ParKeys`, `ParValues`, `ParValuesMut`
- **Tests:** 0/6 matched
- **Provenance warning:** port-lint provenance header matched only by basename: `map.rs` vs expected `rayon/map.rs`
- **Proposed provenance header:** `// port-lint: source rayon/map.rs` (current: `// port-lint: source map.rs`)
- **Lint issues:** 1

### 3. map.entry

- **Target:** `map.Entry`
- **Similarity:** 0.43
- **Dependents:** 0
- **Priority Score:** 82905.7
- **Functions:** 19/27 matched (target 56)
- **Missing functions:** `new`, `into_core`, `get_bucket`, `get_bucket_mut`, `into_bucket`, `key_mut`, `from`, `assert_send_sync`
- **Types:** 2/2 matched (target 6)
- **Missing types:** _none_
- **Tests:** 0/1 matched

### 4. map.iter

- **Target:** `map.Iter`
- **Similarity:** 0.18
- **Dependents:** 0
- **Priority Score:** 82808.2
- **Functions:** 8/14 matched (target 103)
- **Missing functions:** `into_iter`, `new`, `fmt`, `index`, `drop`, `size_hint`
- **Types:** 12/14 matched (target 13)
- **Missing types:** `Item`, `Output`

### 5. set.iter

- **Target:** `set.Iter`
- **Similarity:** 0.17
- **Dependents:** 0
- **Priority Score:** 82308.3
- **Functions:** 6/12 matched (target 55)
- **Missing functions:** `into_iter`, `new`, `fmt`, `size_hint`, `fold`, `rfold`
- **Types:** 9/11 matched (target 10)
- **Missing types:** `Item`, `UnitValue`

### 6. map.mutable

- **Target:** `map.Mutable`
- **Similarity:** 0.09
- **Dependents:** 0
- **Priority Score:** 71009.1
- **Functions:** 1/5 matched (target 1)
- **Missing functions:** `get_full_mut2`, `get_index_mut2`, `iter_mut2`, `retain2`
- **Types:** 2/5 matched (target 2)
- **Missing types:** `Key`, `Value`, `Sealed`

### 7. set.mutable

- **Target:** `set.Mutable [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 50610.0
- **Functions:** 0/3 matched (target 0)
- **Missing functions:** `get_full_mut2`, `get_index_mut2`, `retain2`
- **Types:** 1/3 matched (target 1)
- **Missing types:** `Value`, `Sealed`

### 8. map.raw_entry_v1

- **Target:** `map.RawEntryV1`
- **Similarity:** 0.47
- **Dependents:** 0
- **Priority Score:** 44205.3
- **Functions:** 32/35 matched (target 52)
- **Missing functions:** `raw_entry_v1`, `raw_entry_mut_v1`, `assert_send_sync`
- **Types:** 6/7 matched (target 9)
- **Missing types:** `Sealed`
- **Tests:** 0/1 matched

### 9. set.slice

- **Target:** `set.Slice`
- **Similarity:** 0.30
- **Dependents:** 0
- **Priority Score:** 43907.0
- **Functions:** 34/35 matched (target 59)
- **Missing functions:** `check`
- **Types:** 1/4 matched (target 2)
- **Missing types:** `IntoIter`, `Item`, `Output`
- **Tests:** 1/2 matched

### 10. set

- **Target:** `indexmap.Set`
- **Similarity:** 0.59
- **Dependents:** 0
- **Priority Score:** 30004.1
- **Functions:** 97/97 matched (target 148)
- **Missing functions:** _none_
- **Types:** 1/3 matched (target 2)
- **Missing types:** `Bucket`, `Output`

### 11. map

- **Target:** `indexmap.Map`
- **Similarity:** 0.45
- **Dependents:** 0
- **Priority Score:** 21005.5
- **Functions:** 108/108 matched (target 147)
- **Missing functions:** _none_
- **Types:** 1/2 matched
- **Missing types:** `Output`

### 12. lib

- **Target:** `indexmap.Lib`
- **Similarity:** 0.71
- **Dependents:** 0
- **Priority Score:** 2002.9
- **Functions:** 15/15 matched (target 36)
- **Missing functions:** _none_
- **Types:** 5/5 matched (target 11)
- **Missing types:** _none_

### 13. util

- **Target:** `indexmap.Util`
- **Similarity:** 0.68
- **Dependents:** 0
- **Priority Score:** 403.2
- **Functions:** 4/4 matched (target 14)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 7)
- **Missing types:** _none_

### 14. macros

- **Target:** `indexmap.Macros [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 2)
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

## Reexport / Wiring Modules

These files match `reexport_modules` patterns in `.ast_distance_config.json`. They are filtered out of
normal priority and missing-file ladders because they are wiring
modules, not direct logic ports. Consult them for call-site routing;
do not treat them as the next implementation target by default.

### Missing

| Source | Expected target | Deps | Source path | Expected path |
|--------|-----------------|------|-------------|---------------|
| `rayon.mod` | `rayon.Mod` | 0 | `rayon/mod.rs` | `rayon/Mod.kt` |

