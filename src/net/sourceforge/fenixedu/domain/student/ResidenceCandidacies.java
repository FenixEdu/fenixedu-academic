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
        boolean result = false;
        if (obj instanceof IResidenceCandidacies) {
            IResidenceCandidacies residenceCandidacy = (IResidenceCandidacies) obj;
            result = (this.getStudent().equals(residenceCandidacy.getStudent()) && this.getCreationDate()
                    .equals(residenceCandidacy.getCreationDate()));
        }
        return result;
    }

}