/*
 * Created on 27/Mai/2003
 *
 * 
 */
package DataBeans;

import java.util.List;

/**
 * @author João Mota
 *  
 */
public class InfoStudentSiteExams extends DataTranferObject implements ISiteComponent {
    private List examsToEnroll;

    private List examsEnrolledDistributions;

    /**
     * @param studentDistributions
     */
    public void setExamsEnrolledDistributions(List studentDistributions) {
        this.examsEnrolledDistributions = studentDistributions;
    }

    public InfoExamStudentRoom getInfoExamStudentRoom(int examIdInternal) {
        InfoExamStudentRoom infoExamStudentRoom = null;
        for (int i = 0; i < examsEnrolledDistributions.size(); i++) {
            infoExamStudentRoom = (InfoExamStudentRoom) examsEnrolledDistributions.get(0);
            if (infoExamStudentRoom.getIdInternal().intValue() == examIdInternal) {
                break;
            }
        }
        return infoExamStudentRoom;
    }

    /**
     *  
     */
    public InfoStudentSiteExams() {
    }

    public InfoStudentSiteExams(List examEnrollmentsToEnroll, List studentDistributions) {

        setExamsToEnroll(examEnrollmentsToEnroll);
        setExamsEnrolledDistributions(studentDistributions);
    }

    /**
     * @return
     */
    public List getExamsToEnroll() {
        return examsToEnroll;
    }

    /**
     * @param list
     */
    public void setExamsToEnroll(List list) {
        examsToEnroll = list;
    }

    public boolean equals(Object arg0) {
        boolean result = false;
        if (arg0 instanceof InfoStudentSiteExams) {
            InfoStudentSiteExams component = (InfoStudentSiteExams) arg0;
            result = listEquals(getExamsToEnroll(), component.getExamsToEnroll());

        }
        return result;
    }

    /**
     * @param list
     * @param list2
     */
    private boolean listEquals(List list, List list2) {
        boolean result = false;
        if (list == null && list2 == null) {
            result = true;
        } else if (list != null && list2 != null && list.containsAll(list2) && list2.containsAll(list)) {
            result = true;
        }

        return result;
    }

    /**
     * @return
     */
    public List getExamsEnrolledDistributions() {
        return examsEnrolledDistributions;
    }

}