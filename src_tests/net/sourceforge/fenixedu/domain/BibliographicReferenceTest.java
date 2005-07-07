/*
 * Created on Jul 7, 2005
 *  by jdnf and mrsp
 */
package net.sourceforge.fenixedu.domain;

public class BibliographicReferenceTest extends DomainTestBase {

    private IExecutionCourse executionCourse;
    private IBibliographicReference bibliographicReference;
    
    protected void setUp() throws Exception {
        super.setUp();
        
        executionCourse = new ExecutionCourse();
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

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testEdit() {
        assertEquals("Size unexpected in AssociatedBibliographicReferences for ExecutionCourse", 1, executionCourse.getAssociatedBibliographicReferencesCount());
        
        try {
            bibliographicReference.edit(null, null, null, null, null);
            fail ("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Different Title in BibliographicReference!", "title", bibliographicReference.getTitle());
            assertEquals("Different Authors in BibliographicReference!", "authors", bibliographicReference.getAuthors());
            assertEquals("Different Reference in BibliographicReference!", "reference", bibliographicReference.getReference());
            assertEquals("Different Year in BibliographicReference!", "year", bibliographicReference.getYear());
            assertTrue("Expected True Optional in BibliographicReference!", bibliographicReference.getOptional());
            assertEquals("Different ExecutionCourse in BibliographicReference!", executionCourse, bibliographicReference.getExecutionCourse());
            assertEquals("Size unexpected in AssociatedBibliographicReferences for ExecutionCourse", 1, executionCourse.getAssociatedBibliographicReferencesCount());
        }
        
        try {
            bibliographicReference.edit("newTitle", null, "newReference", null, null);
            fail ("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Different Title in BibliographicReference!", "title", bibliographicReference.getTitle());
            assertEquals("Different Authors in BibliographicReference!", "authors", bibliographicReference.getAuthors());
            assertEquals("Different Reference in BibliographicReference!", "reference", bibliographicReference.getReference());
            assertEquals("Different Year in BibliographicReference!", "year", bibliographicReference.getYear());
            assertTrue("Expected True Optional in BibliographicReference!", bibliographicReference.getOptional());
            assertEquals("Different ExecutionCourse in BibliographicReference!", executionCourse, bibliographicReference.getExecutionCourse());
            assertEquals("Size unexpected in AssociatedBibliographicReferences for ExecutionCourse", 1, executionCourse.getAssociatedBibliographicReferencesCount());
        }
        
        try {
            bibliographicReference.edit(null, "newAuthors", null, "newYear", null);
            fail ("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Different Title in BibliographicReference!", "title", bibliographicReference.getTitle());
            assertEquals("Different Authors in BibliographicReference!", "authors", bibliographicReference.getAuthors());
            assertEquals("Different Reference in BibliographicReference!", "reference", bibliographicReference.getReference());
            assertEquals("Different Year in BibliographicReference!", "year", bibliographicReference.getYear());
            assertTrue("Expected True Optional in BibliographicReference!", bibliographicReference.getOptional());
            assertEquals("Different ExecutionCourse in BibliographicReference!", executionCourse, bibliographicReference.getExecutionCourse());
            assertEquals("Size unexpected in AssociatedBibliographicReferences for ExecutionCourse", 1, executionCourse.getAssociatedBibliographicReferencesCount());
        }
        
        try {
            bibliographicReference.edit("newTitle", null, null, null, false);
            fail ("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Different Title in BibliographicReference!", "title", bibliographicReference.getTitle());
            assertEquals("Different Authors in BibliographicReference!", "authors", bibliographicReference.getAuthors());
            assertEquals("Different Reference in BibliographicReference!", "reference", bibliographicReference.getReference());
            assertEquals("Different Year in BibliographicReference!", "year", bibliographicReference.getYear());
            assertTrue("Expected True Optional in BibliographicReference!", bibliographicReference.getOptional());
            assertEquals("Different ExecutionCourse in BibliographicReference!", executionCourse, bibliographicReference.getExecutionCourse());
            assertEquals("Size unexpected in AssociatedBibliographicReferences for ExecutionCourse", 1, executionCourse.getAssociatedBibliographicReferencesCount());
        }
        
        bibliographicReference.edit("newTitle", "newAuthors", "newReference", "newYear", false);
        assertEquals("Different Title in BibliographicReference!", "newTitle", bibliographicReference.getTitle());
        assertEquals("Different Authors in BibliographicReference!", "newAuthors", bibliographicReference.getAuthors());
        assertEquals("Different Reference in BibliographicReference!", "newReference", bibliographicReference.getReference());
        assertEquals("Different Year in BibliographicReference!", "newYear", bibliographicReference.getYear());
        assertFalse("Expected False Optional in BibliographicReference!", bibliographicReference.getOptional());
        assertEquals("Different ExecutionCourse in BibliographicReference!", executionCourse, bibliographicReference.getExecutionCourse());
        assertEquals("Size unexpected in AssociatedBibliographicReferences for ExecutionCourse", 1, executionCourse.getAssociatedBibliographicReferencesCount());
        
        bibliographicReference.edit("newTitle", "newAuthors2", "newReference2", "newYear", false);
        assertEquals("Different Title in BibliographicReference!", "newTitle", bibliographicReference.getTitle());
        assertEquals("Different Authors in BibliographicReference!", "newAuthors2", bibliographicReference.getAuthors());
        assertEquals("Different Reference in BibliographicReference!", "newReference2", bibliographicReference.getReference());
        assertEquals("Different Year in BibliographicReference!", "newYear", bibliographicReference.getYear());
        assertFalse("Expected False Optional in BibliographicReference!", bibliographicReference.getOptional());
        assertEquals("Different ExecutionCourse in BibliographicReference!", executionCourse, bibliographicReference.getExecutionCourse());
        assertEquals("Size unexpected in AssociatedBibliographicReferences for ExecutionCourse", 1, executionCourse.getAssociatedBibliographicReferencesCount());
    }

    public void testDelete() {
        assertEquals("Size unexpected in AssociatedBibliographicReferences for ExecutionCourse", 1, executionCourse.getAssociatedBibliographicReferencesCount());
        bibliographicReference.delete();
        assertNull("Expected Null ExecutionCourse!", bibliographicReference.getExecutionCourse());
        assertEquals("Size unexpected in AssociatedBibliographicReferences for ExecutionCourse", 0, executionCourse.getAssociatedBibliographicReferencesCount());
    }

}
