/*
 * Created on 7/Nov/2003
 * 
 * To change the template for this generated file go to Window - Preferences - Java - Code Generation -
 * Code and Comments
 */
package net.sourceforge.fenixedu.domain.gesdis;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class CourseReport extends CourseReport_Base {

    public CourseReport() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void edit(String newReport) {
	if (newReport == null)
	    throw new NullPointerException();

	setReport(newReport);
	setLastModificationDate(Calendar.getInstance().getTime());
    }

    public void delete() {
	setExecutionCourse(null);
	removeRootDomainObject();
	super.deleteDomainObject();
    }

}