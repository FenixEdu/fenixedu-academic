/*
 * Created on 5/Fev/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ITestScope;

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

    public void copyFromDomain(ITestScope testScope) {
        super.copyFromDomain(testScope);
        if (testScope != null) {
            if (testScope.getClassName().equals(ExecutionCourse.class.getName()))
                setInfoObject(InfoExecutionCourse.newInfoFromDomain((IExecutionCourse) testScope
                        .getDomainObject()));
        }
    }

    public static InfoTestScope newInfoFromDomain(ITestScope testScope) {
        InfoTestScope infoTestScope = null;
        if (testScope != null) {
            infoTestScope = new InfoTestScope();
            infoTestScope.copyFromDomain(testScope);
        }
        return infoTestScope;
    }

}