package net.sourceforge.fenixedu.domain.research.result.publication;

/**
 * mastersthesis
 *   A Master's thesis.
 *   Required fields: author, title, school, year.
 *   Optional fields: address, month, note.
 * phdthesis
 *   A Ph.D. thesis.
 *   Required fields: author, title, school, year.
 *   Optional fields: address, month, note.
 */
public class Thesis extends Thesis_Base {
    
    public  Thesis() {
        super();
    }
    
    public enum ThesisType {
        PHD_THESIS,
        MASTERS_THESIS;
    }
}
