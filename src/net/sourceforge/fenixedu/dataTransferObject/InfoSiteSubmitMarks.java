package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author Tânia Pousão
 * 
 *  
 */
public class InfoSiteSubmitMarks extends DataTranferObject implements ISiteComponent {
    private static final Logger logger = Logger.getLogger(InfoSiteSubmitMarks.class);
    private InfoEvaluation infoEvaluation;

    private Integer submited;

    //errors
    private List notEnrolmented = null;

    private List errorsMarkNotPublished = null;

    private List mestrado = null;

    public boolean equals(Object objectToCompare) {
        boolean result = false;
        
        if (logger.isDebugEnabled()) {
            logger.debug("equalsInfoSiteSubmitMarks");
            try {
                throw new Exception();
            }catch(Exception e) {
                logger.debug(e.getStackTrace().toString());
            }
            
        }

        if (objectToCompare instanceof InfoSiteStudents
                && (((((InfoSiteMarks) objectToCompare).getInfoEvaluation() != null
                        && this.getInfoEvaluation() != null && ((InfoSiteMarks) objectToCompare)
                        .getInfoEvaluation().equals(this.getInfoEvaluation())) || ((InfoSiteMarks) objectToCompare)
                        .getInfoEvaluation() == null
                        && this.getInfoEvaluation() == null))) {
            result = true;
        }

        if (((InfoSiteMarks) objectToCompare).getMarksList() == null && this.getSubmited() == null
                && result == true) {
            return true;
        }

        if (((InfoSiteMarks) objectToCompare).getMarksList() == null || this.getSubmited() == null
                || ((InfoSiteMarks) objectToCompare).getMarksList().size() != this.getSubmited().intValue()) {
            return false;
        }

        /*ListIterator iter1 = ((InfoSiteMarks) objectToCompare).getMarksList().listIterator();
        ListIterator iter2 = this.getMarksList().listIterator();
        while (result && iter1.hasNext()) {
            InfoMark infoMark1 = (InfoMark) iter1.next();
            InfoMark infoMark2 = (InfoMark) iter2.next();
            if (!infoMark1.equals(infoMark2)) {
                result = false;
            }
        }*/

        return result;
    }



    /**
     * @return
     */
    public InfoEvaluation getInfoEvaluation() {
        return infoEvaluation;
    }

    /**
     * @param exam
     */
    public void setInfoEvaluation(InfoEvaluation evaluation) {
        infoEvaluation = evaluation;
    }

    /**
     * @return
     */
    public List getErrorsMarkNotPublished() {
        return errorsMarkNotPublished;
    }

    /**
     * @return
     */
    public List getNotEnrolmented() {
        return notEnrolmented;
    }

    /**
     * @param list
     */
    public void setErrorsMarkNotPublished(List list) {
        errorsMarkNotPublished = list;
    }

    /**
     * @param list
     */
    public void setNotEnrolmented(List list) {
        notEnrolmented = list;
    }

    /**
     * @return Returns the mestrado.
     */
    public List getMestrado() {
        return mestrado;
    }

    /**
     * @param mestrado
     *            The mestrado to set.
     */
    public void setMestrado(List mestrado) {
        this.mestrado = mestrado;
    }

    /**
     * @return Returns the nSubmited.
     */
    public Integer getSubmited() {
        return submited;
    }
    /**
     * @param submited The nSubmited to set.
     */
    public void setSubmited(Integer submited) {
        this.submited = submited;
    }
}