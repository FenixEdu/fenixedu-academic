package net.sourceforge.fenixedu.domain;


/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class Qualification extends Qualification_Base {

    public String toString() {
        String result = "Qualification :\n";
        result += "\n  - Internal Code : " + getIdInternal();
        result += "\n  - School : " + getSchool();
        result += "\n  - Title : " + getTitle();
        result += "\n  - Mark : " + getMark();
        result += "\n  - Person : " + getPerson();
        result += "\n  - Last Modication Date : " + getLastModificationDate();
        result += "\n  - Branch : " + getBranch();
        result += "\n  - Specialization Area : " + getSpecializationArea();
        result += "\n  - Degree Recognition : " + getDegreeRecognition();
        result += "\n  - Equivalence School : " + getEquivalenceSchool();
        result += "\n  - Equivalence Date : " + getEquivalenceDate();
        result += "\n  - Country : " + getCountry().getName();

        return result;
    }

}
