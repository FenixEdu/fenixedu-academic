/*
 * Created on 13/Mai/2003
 *
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

/**
 * @author João Mota
 * @author Fernanda Quitério
 * 
 *  
 */
public class InfoSiteExam extends DataTranferObject implements ISiteComponent {

    private List infoExams;

    /**
     * @return
     */
    public List getInfoExams() {
        return infoExams;
    }

    /**
     * @param list
     */
    public void setInfoExams(List list) {
        infoExams = list;
    }

}