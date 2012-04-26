package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;

public class CandidacyProcessSelectDegreesBean implements Serializable {

    private List<Degree> degrees = new ArrayList<Degree>();

    public CandidacyProcessSelectDegreesBean() {
    }

    public List<Degree> getDegrees() {
        return degrees;
    }

    public void setDegrees(List<Degree> degrees) {
        this.degrees = degrees;
    }

}
