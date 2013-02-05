package net.sourceforge.fenixedu.presentationTier.docs.thesis;

import net.sourceforge.fenixedu.domain.thesis.Thesis;

import org.joda.time.DateTime;

/**
 * 
 * @author Joao Carvalho
 * 
 */

public class ThesisJuryReportDocument extends ThesisDocument {

    private static final long serialVersionUID = 1L;

    public ThesisJuryReportDocument(Thesis thesis) {
        super(thesis);
    }

    @Override
    public void fillGeneric() {
        Thesis thesis = getThesis();
        String discussed;

        if (thesis.getDiscussed() != null) {
            DateTime discussedTime = thesis.getDiscussed();
            discussed = discussedTime.getDayOfMonth() + " / " + discussedTime.getMonthOfYear() + " / " + discussedTime.getYear();
        } else {
            discussed = EMPTY_STR;
        }

        addParameter("date", discussed);
        addParameter("grade", neverNull(getThesis().getMark()));
    }

    @Override
    public String getReportFileName() {
        return "acta-juri-tese-" + getThesis().getStudent().getNumber();
    }

    @Override
    protected String neverNull(String value) {
        return value == null ? EMPTY_STR : value;
    }

    protected String neverNull(Integer value) {
        return value == null ? EMPTY_STR : value.toString();
    }

}
