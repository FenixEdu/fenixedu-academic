package net.sourceforge.fenixedu.domain.research.result.publication;

/**
 * Inbook
 *   A part of a book, which may be a chapter and/or a range of pages.
 *   Required fields: author or editor, title, chapter and/or pages, publisher, year.
 *   Optional fields: volumer, series, address, edition, month, note.
 * Incollection
 *   A part of a book with its own title.
 *   Required fields: author, title, booktitle, publisher, year.
 *   Optional fields: editor, pages, organization, publisher, address, month, note.
 *   
 *   String title (title in Inbook and booktitle in Incollection)
 *   String chapterSection (chapter in Inbook and title in Incollection)
 */
public class BookPart extends BookPart_Base {
    
    public  BookPart() {
        super();
    }
    
    public enum BookPartType {
        INBOOK,
        INCOLLECTION;
    }
    
}
