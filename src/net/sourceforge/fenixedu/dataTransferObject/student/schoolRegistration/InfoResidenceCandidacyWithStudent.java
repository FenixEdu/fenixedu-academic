/*
 * Created on Aug 15, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.student.schoolRegistration;

import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.student.ResidenceCandidacies;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class InfoResidenceCandidacyWithStudent extends InfoResidenceCandidacy {

    public void copyFromDomain(ResidenceCandidacies residenceCandidacy) {
	super.copyFromDomain(residenceCandidacy);
	if (residenceCandidacy != null) {
	    setInfoStudent(InfoStudent.newInfoFromDomain(residenceCandidacy
		    .getStudentDataByExecutionYear().getStudent().getRegistrations().get(0)));
	}
    }

    public static InfoResidenceCandidacy newInfoFromDomain(ResidenceCandidacies residenceCandidacy) {
	InfoResidenceCandidacyWithStudent infoResidenceCandidacy = null;
	if (residenceCandidacy != null) {
	    infoResidenceCandidacy = new InfoResidenceCandidacyWithStudent();
	    infoResidenceCandidacy.copyFromDomain(residenceCandidacy);
	}
	return infoResidenceCandidacy;
    }
}