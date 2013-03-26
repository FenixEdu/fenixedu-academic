package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.utilities;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.util.StringFormatter;

public class MasterDegreeDiplomaReport extends DiplomaReport {

    private static final long serialVersionUID = 1L;

    public MasterDegreeDiplomaReport(final StudentDiplomaInformation studentDiplomaInformation) {
        super(studentDiplomaInformation);

    }

    @Override
    public String getReportTemplateKey() {
        return "net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaRequest.MASTER_DEGREE";
    }

    @Override
    protected void fillReportSpecificParameters() {
        addParameter("degreeFilteredName", StringFormatter.prettyPrint(this.studentDiplomaInformation.getDegreeName()));

        addParameter("conclusionStatus",
                "curso de " + getEnumerationBundle().getString(DegreeType.MASTER_DEGREE.getQualifiedName()).toLowerCase());

        addParameter("graduateTitle",
                getEnumerationBundle().getString(DegreeType.MASTER_DEGREE.getQualifiedName() + ".graduate.title") + " em "
                        + StringFormatter.prettyPrint(this.studentDiplomaInformation.getDegreeName()));

    }

}
