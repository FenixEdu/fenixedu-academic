/*
 * Created on 9/Jun/2003
 *
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

/**
 * @author João Mota
 *  
 */
public class InfoSiteStudentExamDistributions extends DataTranferObject implements ISiteComponent {

    private List examDistributions;

    /**
     *  
     */
    public InfoSiteStudentExamDistributions() {
    }

    public InfoSiteStudentExamDistributions(List examDistributions) {
        setExamDistributions(examDistributions);
    }

    /**
     * @return
     */
    public List getExamDistributions() {
        return examDistributions;
    }

    /**
     * @param list
     */
    public void setExamDistributions(List list) {
        examDistributions = list;
    }

}