/*
 * Created on 15/Nov/2003
 *
 */
package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoExternalActivity;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ExternalActivity extends ExternalActivity_Base {

    public ExternalActivity() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public ExternalActivity(Teacher teacher, InfoExternalActivity infoExternalActivity) {
	this();
	if (teacher == null)
	    throw new DomainException("The teacher should not be null!");

	setTeacher(teacher);
	this.setActivity(infoExternalActivity.getActivity());
    }

    public void delete() {
	removeTeacher();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public void edit(InfoExternalActivity infoExternalActivity) {

	this.setActivity(infoExternalActivity.getActivity());

    }
}
