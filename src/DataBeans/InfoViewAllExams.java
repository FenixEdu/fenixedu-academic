/*
 * InfoExam.java
 *
 * Created on 2003/05/25
 */

package DataBeans;

import java.util.List;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */

public class InfoViewAllExams extends InfoObject {
    protected InfoExecutionDegree infoExecutionDegree;

    protected Integer curricularYear;

    protected List infoExecutionCourseAndExamsList;

    public InfoViewAllExams() {
    }

    public InfoViewAllExams(InfoExecutionDegree infoExecutionDegree, Integer curricularYear,
            List infoExecutionCourseAndExamsList) {
        this.setInfoExecutionDegree(infoExecutionDegree);
        this.setCurricularYear(curricularYear);
        this.setInfoExecutionCourseAndExamsList(infoExecutionCourseAndExamsList);
    }

    public boolean equals(Object obj) {
        if (obj instanceof InfoViewAllExams) {
            InfoViewAllExams infoViewAllExams = (InfoViewAllExams) obj;
            return this.getInfoExecutionDegree().equals(infoViewAllExams.getInfoExecutionDegree())
                    && this.getCurricularYear().equals(infoViewAllExams.getCurricularYear())
                    && this.getInfoExecutionCourseAndExamsList().size() == infoExecutionCourseAndExamsList
                            .size();
        }
        return false;
    }

    /**
     * @return
     */
    public Integer getCurricularYear() {
        return curricularYear;
    }

    /**
     * @return
     */
    public InfoExecutionDegree getInfoExecutionDegree() {
        return infoExecutionDegree;
    }

    /**
     * @param integer
     */
    public void setCurricularYear(Integer integer) {
        curricularYear = integer;
    }

    /**
     * @param degree
     */
    public void setInfoExecutionDegree(InfoExecutionDegree degree) {
        infoExecutionDegree = degree;
    }

    /**
     * @return
     */
    public List getInfoExecutionCourseAndExamsList() {
        return infoExecutionCourseAndExamsList;
    }

    /**
     * @param list
     */
    public void setInfoExecutionCourseAndExamsList(List list) {
        infoExecutionCourseAndExamsList = list;
    }

}