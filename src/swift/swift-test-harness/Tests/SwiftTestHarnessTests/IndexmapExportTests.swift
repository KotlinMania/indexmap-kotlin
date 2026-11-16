import Testing
import Indexmap

@Suite("Indexmap Swift Export Tests")
struct IndexmapExportTests {
    @Test("Borsh ZST guard and constant are exported cleanly")
    func testZstGuardAndConstant() {
        #expect(!ERROR_ZST_FORBIDDEN.isEmpty)
        checkZst(isZst: false)
    }

    @Test("Disjoint mutable slice error enum and formatting work from Swift")
    func testGetDisjointMutErrorBehavior() {
        #expect(io.github.kotlinmania.indexmap.GetDisjointMutError.IndexOutOfBounds.rawValue == 0)
        #expect(io.github.kotlinmania.indexmap.GetDisjointMutError.OverlappingIndices.rawValue == 1)
        #expect(!io.github.kotlinmania.indexmap.GetDisjointMutError.IndexOutOfBounds.fmt().isEmpty)
    }
}
