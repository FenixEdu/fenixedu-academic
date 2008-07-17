package net.sourceforge.fenixedu.dataTransferObject.alumni.formation;

import java.io.Serializable;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.EducationArea;

public class AlumniEducationArea implements Serializable {

    public static Comparator<AlumniEducationArea> COMPARATOR_BY_CODE = new Comparator<AlumniEducationArea>() {
	public int compare(AlumniEducationArea leftEducationArea, AlumniEducationArea rightEducationArea) {
	    int comparationResult = leftEducationArea.getEducationArea().getCode().compareTo(
		    rightEducationArea.getEducationArea().getCode());
	    return (comparationResult == 0) ? leftEducationArea.getEducationArea().getIdInternal().compareTo(
		    rightEducationArea.getEducationArea().getIdInternal()) : comparationResult;
	}
    };

    private DomainReference<EducationArea> educationArea;

    public AlumniEducationArea(EducationArea area) {
	educationArea = new DomainReference<EducationArea>(area);
    }

    public EducationArea getEducationArea() {
	return (this.educationArea != null) ? this.educationArea.getObject() : null;
    }

    public void setEducationArea(EducationArea educationArea) {
	this.educationArea = (educationArea != null) ? new DomainReference<EducationArea>(educationArea) : null;
    }

    //FIXME
    public String getCodeIndentationValue() {
	String indent = "";
	for (int i = 1; i < getEducationArea().getCode().length(); i++) {
	    indent += "&nbsp;&nbsp;";
	}
	return indent;
    }

}
