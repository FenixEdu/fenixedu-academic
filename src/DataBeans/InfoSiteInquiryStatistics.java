/*
 * Created on 9/Fev/2004
 *
 */
package DataBeans;

import java.util.List;

/**
 * 
 * @author Susana Fernandes
 *  
 */
public class InfoSiteInquiryStatistics extends DataTranferObject implements ISiteComponent {

    private List infoInquiryStatistics;

    private InfoExecutionCourse executionCourse;

    public InfoSiteInquiryStatistics() {
    }

    public List getInfoInquiryStatistics() {
        return infoInquiryStatistics;
    }

    public void setInfoInquiryStatistics(List list) {
        infoInquiryStatistics = list;
    }

    public InfoExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    public void setExecutionCourse(InfoExecutionCourse course) {
        executionCourse = course;
    }

}