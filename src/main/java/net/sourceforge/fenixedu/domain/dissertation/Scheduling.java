package net.sourceforge.fenixedu.domain.dissertation;

import java.util.List;

public class Scheduling extends Scheduling_Base {
    
    public  Scheduling() {
        super();
    }

    private List<DissertationCandidacy> dissertationCandidacies = null;

	public List<DissertationCandidacy> getDissertationCandidacies() {
		return dissertationCandidacies;
	}

	public void setDissertationCandidacies(
			List<DissertationCandidacy> dissertationCandidacies) {
		this.dissertationCandidacies = dissertationCandidacies;
	}
    
    
    
}
