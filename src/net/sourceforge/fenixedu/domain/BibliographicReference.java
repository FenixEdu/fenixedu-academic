package net.sourceforge.fenixedu.domain;

/**
 * @author PTRLV
 *  
 */
public class BibliographicReference extends BibliographicReference_Base {

    public String toString() {
        String result = "[REFERENCIA BIBLIOGRAFICA";
        result += ", codInt=" + getIdInternal();
        result += ", disciplinaExecucao=" + getExecutionCourse();
        result += ", titulo=" + getTitle();
        result += ", autor=" + getAuthors();
        result += ", ano=" + getYear();
        result += ", referencia=" + getReference();
        result += ", facultativo=" + getOptional();
        result += "]";
        return result;
    }
    
    public boolean equals(Object obj) {
        if (obj instanceof IBibliographicReference) {
            final IBibliographicReference bibliographicReference = (IBibliographicReference) obj;
            return this.getIdInternal().equals(bibliographicReference.getIdInternal());
        }
        return false;
    }

}
