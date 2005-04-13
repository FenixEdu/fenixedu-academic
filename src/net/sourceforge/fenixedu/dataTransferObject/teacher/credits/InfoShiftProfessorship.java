/*
 * Created on 14/Mai/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher.credits;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.IShiftProfessorship;

/**
 * @author jpvl
 */
public class InfoShiftProfessorship extends InfoObject {
    private InfoProfessorship infoProfessorship = null;

    private InfoShift infoShift = null;

    private Double percentage = null;

    /**
     * @return
     */
    public InfoProfessorship getInfoProfessorship() {
        return infoProfessorship;
    }

    /**
     * @return
     */
    public InfoShift getInfoShift() {
        return infoShift;
    }

    /**
     * @return
     */
    public Double getPercentage() {
        return percentage;
    }

    /**
     * @param ship
     */
    public void setInfoProfessorship(InfoProfessorship ship) {
        infoProfessorship = ship;
    }

    /**
     * @param shift
     */
    public void setInfoShift(InfoShift shift) {
        infoShift = shift;
    }

    /**
     * @param double1
     */
    public void setPercentage(Double double1) {
        percentage = double1;
    }

    public void copyFromDomain(IShiftProfessorship shifProfessorship) {
        super.copyFromDomain(shifProfessorship);
        if (shifProfessorship != null) {
            setPercentage(shifProfessorship.getPercentage());
        }
    }
    
    public static InfoShiftProfessorship newInfoFromDomain(IShiftProfessorship shifProfessorship) {
        InfoShiftProfessorship infoShiftProfessorship = null;
        if (shifProfessorship != null) {
            infoShiftProfessorship = new InfoShiftProfessorship();
            infoShiftProfessorship.copyFromDomain(shifProfessorship);
        }
        return infoShiftProfessorship;
    }
}