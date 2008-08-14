/*
 * Created on 15/Nov/2003
 *
 */
package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoWeeklyOcupation;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * @author Jo�o Fialho & Rita Ferreira
 * 
 */
public class WeeklyOcupation extends WeeklyOcupation_Base {

    public WeeklyOcupation() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public WeeklyOcupation(Teacher teacher, InfoWeeklyOcupation infoWeeklyOcupation) {
	this();
	if (teacher == null)
	    throw new DomainException("The teacher should not be null!");
	setTeacher(teacher);
	setBasicProperties(infoWeeklyOcupation);
    }

    public void edit(InfoWeeklyOcupation infoWeeklyOcupation) {
	setBasicProperties(infoWeeklyOcupation);
    }

    private void setBasicProperties(InfoWeeklyOcupation infoWeeklyOcupation) {
	this.setOther(infoWeeklyOcupation.getOther());
	this.setLecture(infoWeeklyOcupation.getLecture());
	this.setManagement(infoWeeklyOcupation.getManagement());
	this.setResearch(infoWeeklyOcupation.getResearch());
	this.setSupport(infoWeeklyOcupation.getSupport());

    }

}
