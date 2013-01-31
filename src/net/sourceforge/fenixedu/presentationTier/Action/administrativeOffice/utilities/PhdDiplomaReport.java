package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.utilities;

import net.sourceforge.fenixedu.util.StringFormatter;

public class PhdDiplomaReport extends DiplomaReport {

	private static final long serialVersionUID = 1L;

	public PhdDiplomaReport(StudentDiplomaInformation studentDiplomaInformation) {
		super(studentDiplomaInformation);
	}

	@Override
	protected void fillReportSpecificParameters() {
		addParameter("graduateTitle",
				"Doutor" + " em " + StringFormatter.prettyPrint(this.studentDiplomaInformation.getDegreeName()));
		addParameter("classificationResult", this.studentDiplomaInformation.getClassificationResult());
	}

	@Override
	public String getReportTemplateKey() {
		return "net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaRequest.BOLONHA_PHD_PROGRAM";
	}

}
