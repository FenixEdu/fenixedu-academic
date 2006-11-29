/*
 * Created on Jul 7, 2005
 *  by jdnf and mrsp
 */
package net.sourceforge.fenixedu.domain;

public class BibliographicReferenceTest extends DomainTestBase {

    private ExecutionCourse executionCourse;

    private BibliographicReference bibliographicReference;

    protected void setUp() throws Exception {
        super.setUp();

        executionCourse = new ExecutionCourse("name", "acronym", ExecutionPeriod.readActualExecutionPeriod());
        executionCourse.setIdInternal(0);

        bibliographicReference = new BibliographicReference();
        bibliographicReference.setIdInternal(0);
        bibliographicReference.setTitle("title");
        bibliographicReference.setAuthors("authors");
        bibliographicReference.setReference("reference");
        bibliographicReference.setYear("year");
        bibliographicReference.setOptional(true);
        bibliographicReference.setExecutionCourse(executionCourse);
    }

    public void testEdit() {
        assertEquals("Size unexpected in AssociatedBibliographicReferences for ExecutionCourse", 1,
                executionCourse.getAssociatedBibliographicReferencesCount());

        try {
            bibliographicReference.edit(null, null, null, null, null);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            checkIfBibliographicReferenceAttributesAreCorrect("title", "authors", "reference", "year",
                    true, this.executionCourse, 1);
        }

        try {
            bibliographicReference.edit("newTitle", null, "newReference", null, null);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            checkIfBibliographicReferenceAttributesAreCorrect("title", "authors", "reference", "year",
                    true, this.executionCourse, 1);
        }

        try {
            bibliographicReference.edit(null, "newAuthors", null, "newYear", null);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            checkIfBibliographicReferenceAttributesAreCorrect("title", "authors", "reference", "year",
                    true, this.executionCourse, 1);
        }

        try {
            bibliographicReference.edit("newTitle", null, null, null, false);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            checkIfBibliographicReferenceAttributesAreCorrect("title", "authors", "reference", "year",
                    true, this.executionCourse, 1);
        }

        bibliographicReference.edit("newTitle", "newAuthors", "newReference", "newYear", false);
        checkIfBibliographicReferenceAttributesAreCorrect("newTitle", "newAuthors", "newReference",
                "newYear", false, this.executionCourse, 1);

        bibliographicReference.edit("newTitle", "newAuthors2", "newReference2", "newYear", false);
        checkIfBibliographicReferenceAttributesAreCorrect("newTitle", "newAuthors2", "newReference2",
                "newYear", false, this.executionCourse, 1);
    }

    public void testDelete() {
        assertEquals("Size unexpected in AssociatedBibliographicReferences for ExecutionCourse", 1,
                executionCourse.getAssociatedBibliographicReferencesCount());
        bibliographicReference.delete();
        assertNull("Expected Null ExecutionCourse!", bibliographicReference.getExecutionCourse());
        assertEquals("Size unexpected in AssociatedBibliographicReferences for ExecutionCourse", 0,
                executionCourse.getAssociatedBibliographicReferencesCount());
    }

    public void checkIfBibliographicReferenceAttributesAreCorrect(final String title,
            final String authors, final String reference, final String year, final Boolean optional,
            final ExecutionCourse executionCourse, final int size) {
        
        assertEquals("Different Title in BibliographicReference!", title, bibliographicReference
                .getTitle());
        assertEquals("Different Authors in BibliographicReference!", authors, bibliographicReference
                .getAuthors());
        assertEquals("Different Reference in BibliographicReference!", reference, bibliographicReference
                .getReference());
        assertEquals("Different Year in BibliographicReference!", year, bibliographicReference.getYear());
        assertEquals("Expected True Optional in BibliographicReference!", optional,
                bibliographicReference.getOptional());
        assertEquals("Different ExecutionCourse in BibliographicReference!", executionCourse,
                bibliographicReference.getExecutionCourse());
        assertEquals("Size unexpected in AssociatedBibliographicReferences for ExecutionCourse", size,
                executionCourse.getAssociatedBibliographicReferencesCount());
    }

}
