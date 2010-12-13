package net.sourceforge.fenixedu.domain.phd.serviceRequests;

import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;

abstract public class PhdAcademicServiceRequest extends PhdAcademicServiceRequest_Base {

    public PhdAcademicServiceRequest() {
	super();
    }

    protected void init(final PhdAcademicServiceRequestCreateBean bean) {
	super.init(bean);

	check(bean.getPhdIndividualProgramProcess(),
		"error.phd.serviceRequests.PhdAcademicServiceRequest.phdIndividualProgramProcess.is.null", null);

	setPhdIndividualProgramProcess(bean.getPhdIndividualProgramProcess());
    }
}
