package net.sourceforge.fenixedu.domain.credits.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Professorship;

public class ProjectTutorialServiceBean implements Serializable {

    protected Professorship professorship;

    protected List<Attends> orientations = new ArrayList<Attends>();

    public ProjectTutorialServiceBean(Professorship professorship) {
	this.professorship = professorship;
	if (professorship.getDegreeProjectTutorialService() != null) {
	    orientations.addAll(professorship.getDegreeProjectTutorialService().getAttends());
	}
    }

    public Professorship getProfessorship() {
	return professorship;
    }

    public void setProfessorship(Professorship professorship) {
	this.professorship = professorship;
    }

    public List<Attends> getOrientations() {
	return orientations;
    }

    public void setOrientations(List<Attends> orientations) {
	this.orientations = orientations;
    }

    public List<Attends> getAssignedOrientations() {
	List<Attends> attends = new ArrayList<Attends>();
	for (Attends attend : getProfessorship().getExecutionCourse().getAttends()) {
	    if (attend.getDegreeProjectTutorialService() != null
		    && !attend.getDegreeProjectTutorialService().getProfessorship().equals(getProfessorship())) {
		attends.add(attend);
	    }
	}
	return attends;
    }

}