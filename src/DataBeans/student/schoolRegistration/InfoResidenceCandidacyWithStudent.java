/*
 * Created on Aug 15, 2004
 *
 */
package DataBeans.student.schoolRegistration;

import DataBeans.InfoStudent;
import Dominio.student.IResidenceCandidancies;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class InfoResidenceCandidacyWithStudent extends InfoResidenceCandidacy {

    public void copyFromDomain(IResidenceCandidancies residenceCandidacy) {
        super.copyFromDomain(residenceCandidacy);
        if (residenceCandidacy != null) {
            setInfoStudent(InfoStudent.newInfoFromDomain(residenceCandidacy.getStudent()));
        }
    }

    public static InfoResidenceCandidacy newInfoFromDomain(IResidenceCandidancies residenceCandidacy) {
        InfoResidenceCandidacyWithStudent infoResidenceCandidacy = null;
        if (residenceCandidacy != null) {
            infoResidenceCandidacy = new InfoResidenceCandidacyWithStudent();
            infoResidenceCandidacy.copyFromDomain(residenceCandidacy);
        }
        return infoResidenceCandidacy;
    }
}