# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 8/26 (30.8%)
- **Function parity:** 249/856 matched (target 359) — 29.1%
- **Class/type parity:** 12/95 matched (target 34) — 12.6%
- **Combined symbol parity:** 261/951 matched (target 393) — 27.4%
- **Average inline-code cosine:** 0.34 (function body across 8 matched files)
- **Average documentation cosine:** 0.00 (doc text across 8 matched files)
- **Cheat-zeroed Files:** 2
- **Critical Issues:** 7 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. map.slice

- **Target:** `map.Slice`
- **Similarity:** 0.21
- **Dependents:** 5
- **Priority Score:** 5296808.0
- **Functions:** 38/64 matched (target 57)
- **Missing functions:** `from_slice`, `from_mut_slice`, `from_boxed`, `into_boxed`, `new_mut`, `get_index_mut`, `get_range_mut`, `first_mut`, `last_mut`, `split_at_mut`, `split_at_mut_checked`, `split_first_mut`, `split_last_mut`, `iter_mut`, `values_mut`, `get_disjoint_mut`, `get_disjoint_opt_mut`, `into_iter`, `index_mut`, `check`, `slice_index_mut`, `check_mut`, `slice_new_mut`, `slice_get_index_mut`, `slice_split_first_mut`, `slice_split_last_mut`
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

### 3. map

- **Target:** `indexmap.Map`
- **Similarity:** 0.38
- **Dependents:** 0
- **Priority Score:** 181006.2
- **Functions:** 92/108 matched (target 107)
- **Missing functions:** `as_entries_mut`, `with_entries`, `iter_mut`, `values_mut`, `get_mut`, `get_key_value_mut`, `get_full_mut`, `get_disjoint_mut`, `as_mut_slice`, `into_boxed_slice`, `get_index_mut`, `get_disjoint_indices_mut`, `get_range_mut`, `first_mut`, `last_mut`, `index_mut`
- **Types:** 1/2 matched
- **Missing types:** `Output`

### 4. set

- **Target:** `indexmap.Set`
- **Similarity:** 0.51
- **Dependents:** 0
- **Priority Score:** 140004.9
- **Functions:** 86/97 matched (target 100)
- **Missing functions:** `into_entries`, `as_entries`, `with_entries`, `take`, `as_slice`, `into_boxed_slice`, `index`, `bitand`, `bitor`, `bitxor`, `sub`
- **Types:** 1/3 matched (target 2)
- **Missing types:** `Bucket`, `Output`

### 5. map.entry

- **Target:** `map.Entry`
- **Similarity:** 0.40
- **Dependents:** 0
- **Priority Score:** 102906.0
- **Functions:** 17/27 matched (target 49)
- **Missing functions:** `or_default`, `fmt`, `new`, `into_core`, `get_bucket`, `get_bucket_mut`, `into_bucket`, `key_mut`, `from`, `assert_send_sync`
- **Types:** 2/2 matched (target 6)
- **Missing types:** _none_
- **Tests:** 0/1 matched

### 6. map.mutable

- **Target:** `map.Mutable [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 81010.0
- **Functions:** 0/5 matched (target 0)
- **Missing functions:** `get_full_mut2`, `get_index_mut2`, `iter_mut2`, `retain2`, `key_mut`
- **Types:** 2/5 matched (target 2)
- **Missing types:** `Key`, `Value`, `Sealed`

### 7. lib

- **Target:** `indexmap.Lib`
- **Similarity:** 0.57
- **Dependents:** 0
- **Priority Score:** 32004.3
- **Functions:** 12/15 matched (target 32)
- **Missing functions:** `key`, `value`, `fmt`
- **Types:** 5/5 matched (target 11)
- **Missing types:** _none_

### 8. util

- **Target:** `indexmap.Util`
- **Similarity:** 0.68
- **Dependents:** 0
- **Priority Score:** 403.2
- **Functions:** 4/4 matched (target 14)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 7)
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
