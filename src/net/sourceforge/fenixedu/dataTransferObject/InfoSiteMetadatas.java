/*
 * Created on 23/Jul/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

/**
 * @author Susana Fernandes
 */

public class InfoSiteMetadatas extends DataTranferObject implements ISiteComponent {
    private List infoMetadatas;

    private InfoExecutionCourse executionCourse;

    public InfoSiteMetadatas() {
    }

    public InfoExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    public List getInfoMetadatas() {
        return infoMetadatas;
    }

    public void setExecutionCourse(InfoExecutionCourse course) {
        executionCourse = course;
    }

    public void setInfoMetadatas(List list) {
        infoMetadatas = list;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoSiteMetadatas) {
            InfoSiteMetadatas infoSiteMetadatas = (InfoSiteMetadatas) obj;
            result = getExecutionCourse().equals(infoSiteMetadatas.getExecutionCourse())
                    && getInfoMetadatas().containsAll(infoSiteMetadatas.getInfoMetadatas())
                    && infoSiteMetadatas.getInfoMetadatas().containsAll(getInfoMetadatas());
        }
        return result;
    }

}