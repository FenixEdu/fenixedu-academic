package net.sourceforge.fenixedu.domain;

/**
 * @author dcs-rjao
 * 
 * 19/Mar/2003
 */

public class Mark extends Mark_Base {

    public Mark() {
    }
    
    public Mark(Integer idInternal) {
        setIdInternal(idInternal);
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof Mark) {
            Mark mark = (Mark) obj;
            resultado = this.getAttend().equals(mark.getAttend())
                    && this.getEvaluation().equals(mark.getEvaluation());
        }
        return resultado;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + ": ";
        result += "idInternal = " + getIdInternal() + "; ";
        result += "mark = " + getMark() + "; ";
        result += "published mark = " + getPublishedMark() + "; ";
        result += "evaluation= " + getEvaluation().getIdInternal() + "; ";
        result += "attend = " + getAttend().getIdInternal() + "; ]";

        return result;
    }

}