/*
 * Created on 5/Fev/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.onlineTests;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.TestScope;

/**
 * 
 * @author Susana Fernandes
 * 
 */
public class InfoTestScope extends InfoObject {
    private InfoObject infoObject;

    public InfoTestScope() {
    }

    public InfoObject getInfoObject() {
	return infoObject;
    }

    public void setInfoObject(InfoObject object) {
	infoObject = object;
    }

    public void copyFromDomain(TestScope testScope) {
	super.copyFromDomain(testScope);
	if (testScope != null) {
	    if (testScope.getClassName().equals(ExecutionCourse.class.getName()))
		setInfoObject(InfoExecutionCourse.newInfoFromDomain((ExecutionCourse) testScope.getDomainObject()));
	}
    }

    public static InfoTestScope newInfoFromDomain(TestScope testScope) {
	InfoTestScope infoTestScope = null;
	if (testScope != null) {
	    infoTestScope = new InfoTestScope();
	    infoTestScope.copyFromDomain(testScope);
	}
	return infoTestScope;
    }

}