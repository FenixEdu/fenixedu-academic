package net.sourceforge.fenixedu.domain.credits.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.teacher.DegreeProjectTutorialService;

import org.apache.commons.lang.StringUtils;

public class ProjectTutorialServiceBean implements Serializable {

    protected Professorship professorship;
    protected Attends attend;
    protected Integer percentage;
    protected DegreeProjectTutorialService degreeProjectTutorialService;
    protected List<DegreeProjectTutorialService> othersDegreeProjectTutorialService = new ArrayList<DegreeProjectTutorialService>();

    public ProjectTutorialServiceBean(Professorship professorship, Attends attend) {
	this.professorship = professorship;
	this.attend = attend;
	for (DegreeProjectTutorialService degreeProjectTutorialService : attend.getDegreeProjectTutorialServices()) {
	    if (degreeProjectTutorialService.getProfessorship().equals(professorship)) {
		this.percentage = degreeProjectTutorialService.getPercentageValue();
		this.degreeProjectTutorialService = degreeProjectTutorialService;
	    } else {
		othersDegreeProjectTutorialService.add(degreeProjectTutorialService);
	    }
	}
    }

    public Professorship getProfessorship() {
	return professorship;
    }

    public void setProfessorship(Professorship professorship) {
	this.professorship = professorship;
    }

    public Attends getAttend() {
	return attend;
    }

    public void setAttend(Attends attend) {
	this.attend = attend;
    }

    public Integer getPercentage() {
	return percentage;
    }

    public void setPercentage(Integer percentage) {
	this.percentage = percentage;
    }

    public List<DegreeProjectTutorialService> getOthersDegreeProjectTutorialService() {
	return othersDegreeProjectTutorialService;
    }

    public void setOthersDegreeProjectTutorialService(List<DegreeProjectTutorialService> othersDegreeProjectTutorialService) {
	this.othersDegreeProjectTutorialService = othersDegreeProjectTutorialService;
    }

    public DegreeProjectTutorialService getDegreeProjectTutorialService() {
	return degreeProjectTutorialService;
    }

    public void setDegreeProjectTutorialService(DegreeProjectTutorialService degreeProjectTutorialService) {
	this.degreeProjectTutorialService = degreeProjectTutorialService;
    }

    public String getOthersDegreeProjectTutorialServiceString() {
	List<String> result = new ArrayList<String>();
	for (DegreeProjectTutorialService degreeProjectTutorialService : getOthersDegreeProjectTutorialService()) {
	    result.add(degreeProjectTutorialService.getProfessorship().getTeacher().getPerson().getPresentationName() + " - "
		    + degreeProjectTutorialService.getPercentageValue() + "%");
	}
	return StringUtils.join(result, "<br/>");
    }
}