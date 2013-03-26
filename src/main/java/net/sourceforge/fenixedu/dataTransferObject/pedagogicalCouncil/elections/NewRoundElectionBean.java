package net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.elections;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.elections.DelegateElection;
import net.sourceforge.fenixedu.domain.student.Student;

public class NewRoundElectionBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<Student> candidates;

    public NewRoundElectionBean(List<Student> candidates, DelegateElection delegateElection) {
        setCandidates(candidates);
    }

    public boolean containsCandidates() {
        if (getCandidates() != null && getCandidates().size() > 0) {
            return true;
        }
        return false;
    }

    public void setCandidates(List<Student> candidates) {
        this.candidates = candidates;
    }

    public List<Student> getCandidates() {
        return candidates;
    }

}
