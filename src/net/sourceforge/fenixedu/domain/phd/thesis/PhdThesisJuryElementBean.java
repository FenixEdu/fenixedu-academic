package net.sourceforge.fenixedu.domain.phd.thesis;

import net.sourceforge.fenixedu.domain.phd.PhdParticipantBean;

public class PhdThesisJuryElementBean extends PhdParticipantBean {

    private static final long serialVersionUID = -5365333247731361583L;

    private boolean reporter;

    public boolean isReporter() {
	return reporter;
    }

    public void setReporter(boolean reporter) {
	this.reporter = reporter;
    }

}
