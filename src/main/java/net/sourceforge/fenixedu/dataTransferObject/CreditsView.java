/*
 * Created on 1/Jul/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits;

/**
 * @author jpvl
 */
public class CreditsView extends InfoObject {

    private InfoCredits infoCredits;

    private List infoProfessorshipList;

    /**
     * @return
     */
    public InfoCredits getInfoCredits() {
        return this.infoCredits;
    }

    /**
     * @param infoCredits
     */
    public void setInfoCredits(InfoCredits infoCredits) {
        this.infoCredits = infoCredits;
    }

    /**
     * @return
     */
    public List getInfoProfessorshipList() {
        return this.infoProfessorshipList;
    }

    /**
     * @param infoProfessorshipList
     */
    public void setInfoProfessorshipList(List infoProfessorshipList) {
        this.infoProfessorshipList = infoProfessorshipList;
    }

}