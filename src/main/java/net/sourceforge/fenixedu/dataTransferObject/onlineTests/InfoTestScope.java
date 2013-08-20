/*
 * Created on 5/Fev/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.onlineTests;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.onlineTests.TestScope;

/**
 * 
 * @author Susana Fernandes
 * 
 */
public class InfoTestScope extends InfoObject {
    private InfoExecutionCourse infoObject;

    public InfoTestScope() {
    }

    public InfoExecutionCourse getInfoObject() {
        return infoObject;
    }

    private void setInfoExecutionCourse(InfoExecutionCourse object) {
        infoObject = object;
    }

    public void copyFromDomain(TestScope testScope) {
        super.copyFromDomain(testScope);
        if (testScope != null) {
            setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(testScope.getExecutionCourse()));
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