package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

/**
 * @author Fernanda Quitério 25/06/2003
 *  
 */
public class InfoSiteEvaluation extends DataTranferObject implements ISiteComponent {

    private List infoEvaluations;

    /**
     * @return
     */
    public List getInfoEvaluations() {
        return infoEvaluations;
    }

    /**
     * @param infoEvaluations
     */
    public void setInfoEvaluations(List infoEvaluations) {
        this.infoEvaluations = infoEvaluations;
    }

}