/*
 * Created on 14/Mai/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher.credits;

import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.domain.ShiftProfessorship;

/**
 * @author jpvl
 */
public class InfoShiftProfessorshipAndTeacher extends InfoShiftProfessorship {

    public void copyFromDomain(ShiftProfessorship shifProfessorship) {
        super.copyFromDomain(shifProfessorship);
        if (shifProfessorship != null) {
            setInfoProfessorship(InfoProfessorship.newInfoFromDomain(shifProfessorship.getProfessorship()));
        }
    }
    
    public static InfoShiftProfessorshipAndTeacher newInfoFromDomain(ShiftProfessorship shifProfessorship) {
        InfoShiftProfessorshipAndTeacher infoShiftProfessorship = null;
        if (shifProfessorship != null) {
            infoShiftProfessorship = new InfoShiftProfessorshipAndTeacher();
            infoShiftProfessorship.copyFromDomain(shifProfessorship);
        }
        return infoShiftProfessorship;
    }
}