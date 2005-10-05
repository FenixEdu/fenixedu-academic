package net.sourceforge.fenixedu.domain;

public class Mark extends Mark_Base {

    public Mark() {
        super();
    }

    public Mark(final IAttends attends, final IEvaluation evaluation, final String mark) {
        setAttend(attends);
        setEvaluation(evaluation);
        setMark(mark);
        setPublishedMark(null);
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
