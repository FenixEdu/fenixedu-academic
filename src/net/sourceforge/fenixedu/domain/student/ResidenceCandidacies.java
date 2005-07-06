/*
 * Created on Aug 3, 2004
 *
 */
package net.sourceforge.fenixedu.domain.student;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class ResidenceCandidacies extends ResidenceCandidacies_Base {

    public String toString() {
        String result = "ResidenceCandidancy :\n";
        result += "\n  - InternalId : " + getIdInternal();
        result += "\n  - Student : " + getStudent();
        result += "\n  - Creation Date : " + getCreationDate();
        result += "\n  - Observations : " + getObservations();
        result += "\n  - Dislocated : " + getDislocated();
        return result;
    }

    public boolean equals(Object obj) {
        if (obj instanceof IResidenceCandidacies) {
            final IResidenceCandidacies residenceCandidacies = (IResidenceCandidacies) obj;
            return this.getIdInternal().equals(residenceCandidacies.getIdInternal());
        }
        return false;
    }

}
