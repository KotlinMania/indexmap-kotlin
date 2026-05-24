# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 4/26 (15.4%)
- **Function parity:** 43/875 matched (target 80) — 4.9%
- **Class/type parity:** 6/95 matched (target 20) — 6.3%
- **Combined symbol parity:** 49/970 matched (target 100) — 5.1%
- **Average inline-code cosine:** 0.27 (function body across 3 matched files)
- **Average documentation cosine:** 0.00 (doc text across 3 matched files)
- **Cheat-zeroed Files:** 1
- **Critical Issues:** 3 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. map

- **Target:** `indexmap.Map`
- **Similarity:** 0.12
- **Dependents:** 0
- **Priority Score:** 831008.8
- **Functions:** 27/108 matched (target 29)
- **Missing functions:** `clone`, `clone_from`, `fmt`, `with_capacity_and_hasher`, `with_hasher`, `into_entries`, `as_entries`, `as_entries_mut`, `with_entries`, `hasher`, `iter`, `iter_mut`, `into_keys`, `values_mut`, `into_values`, `drain`, `extract_if`, `split_off`, `reserve`, `reserve_exact`, `try_reserve`, `try_reserve_exact`, `shrink_to_fit`, `shrink_to`, `insert_sorted`, `insert_sorted_by`, `insert_sorted_by_key`, `replace_index`, `entry`, `splice`, `append`, `hash`, `get_mut`, `get_key_value_mut`, `get_full_mut`, `get_disjoint_mut`, `remove`, `remove_entry`, `swap_remove_entry`, `swap_remove_full`, `shift_remove_entry`, `shift_remove_full`, `pop`, `pop_if`, `retain`, `sort_keys`, `sort_by`, `sorted_by`, `sort_by_key`, `sort_unstable_keys`, `sort_unstable_by`, `sorted_unstable_by`, `sort_unstable_by_key`, `sort_by_cached_key`, `binary_search_keys`, `binary_search_by`, `binary_search_by_key`, `is_sorted`, `is_sorted_by`, `is_sorted_by_key`, `partition_point`, `reverse`, `as_slice`, `as_mut_slice`, `into_boxed_slice`, `get_index_mut`, `get_index_entry`, `get_disjoint_indices_mut`, `get_range`, `get_range_mut`, `first_mut`, `first_entry`, `last_mut`, `last_entry`, `index`, `index_mut`, `from_iter`, `from`, `extend`, `default`, `eq`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `Output`

### 2. rayon.map

- **Target:** `indexmap.MapTest [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 343410.0
- **Functions:** 0/25 matched (target 5)
- **Missing functions:** `into_par_iter`, `fmt`, `clone`, `par_drain`, `par_keys`, `par_values`, `par_eq`, `par_values_mut`, `par_sort_keys`, `par_sort_by`, `par_sorted_by`, `par_sort_by_key`, `par_sort_unstable_keys`, `par_sort_unstable_by`, `par_sorted_unstable_by`, `par_sort_unstable_by_key`, `par_sort_by_cached_key`, `from_par_iter`, `par_extend`, `insert_order`, `partial_eq_and_eq`, `extend`, `keys`, `values`, `values_mut`
- **Types:** 0/9 matched (target 1)
- **Missing types:** `Item`, `Iter`, `IntoParIter`, `ParIter`, `ParIterMut`, `ParDrain`, `ParKeys`, `ParValues`, `ParValuesMut`
- **Tests:** 0/6 matched
- **Provenance warning:** port-lint provenance header matched only by basename: `map.rs` vs expected `rayon/map.rs`
- **Proposed provenance header:** `// port-lint: source rayon/map.rs` (current: `// port-lint: source map.rs`)
- **Lint issues:** 1

### 3. lib

- **Target:** `indexmap.Lib [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 32010.0
- **Functions:** 12/15 matched (target 32)
- **Missing functions:** `key`, `value`, `fmt`
- **Types:** 5/5 matched (target 11)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/lib.rs` vs expected `lib.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/lib.rs` vs expected `lib.rs`
- **Proposed provenance header:** `// port-lint: source lib.rs` (current: `// port-lint: source src/lib.rs`)
- **Proposed provenance header:** `// port-lint: source lib.rs` (current: `// port-lint: source src/lib.rs`)
- **Lint issues:** 2

### 4. util

- **Target:** `indexmap.Util [PROVENANCE-FALLBACK]`
- **Similarity:** 0.68
- **Dependents:** 0
- **Priority Score:** 403.2
- **Functions:** 4/4 matched (target 14)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 7)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/util.rs` vs expected `util.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `src/util.rs` vs expected `util.rs`
- **Proposed provenance header:** `// port-lint: source util.rs` (current: `// port-lint: source src/util.rs`)
- **Proposed provenance header:** `// port-lint: source util.rs` (current: `// port-lint: source src/util.rs`)
- **Lint issues:** 2

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
