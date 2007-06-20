/*
 * Created on 31/Jul/2003, 9:20:49
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.domain.Seminaries;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 31/Jul/2003, 9:20:49
 * 
 */
public class CourseEquivalency extends CourseEquivalency_Base {

    public CourseEquivalency() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public void delete() {
	removeCurricularCourse();
	removeModality();
	removeSeminary();
	getThemes().clear();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

}