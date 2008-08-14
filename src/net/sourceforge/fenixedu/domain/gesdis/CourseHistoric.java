/*
 * Created on 17/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.domain.gesdis;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class CourseHistoric extends CourseHistoric_Base {

    public CourseHistoric() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	removeCurricularCourse();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

}