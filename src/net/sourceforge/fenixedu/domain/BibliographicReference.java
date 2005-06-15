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
        boolean result = false;
        if (obj instanceof IBibliographicReference) {
            IBibliographicReference refBiblio = (IBibliographicReference) obj;
            result = getTitle().equals(refBiblio.getTitle())
                    && getAuthors().equals(refBiblio.getAuthors())
                    && getReference().equals(refBiblio.getReference())
                    && (getYear() == refBiblio.getYear());
        }
        return result;
    }

}
