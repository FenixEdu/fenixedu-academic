/*
 * Created on 14/Mai/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher.credits;

import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorshipWithAll;
import net.sourceforge.fenixedu.domain.IShiftProfessorship;

/**
 * @author jpvl
 */
public class InfoShiftProfessorshipAndTeacher extends InfoShiftProfessorship {

    public void copyFromDomain(IShiftProfessorship shifProfessorship) {
        super.copyFromDomain(shifProfessorship);
        if (shifProfessorship != null) {
            setInfoProfessorship(InfoProfessorshipWithAll.newInfoFromDomain(shifProfessorship.getProfessorship()));
        }
    }
    
    public static InfoShiftProfessorshipAndTeacher newInfoFromDomain(IShiftProfessorship shifProfessorship) {
        InfoShiftProfessorshipAndTeacher infoShiftProfessorship = null;
        if (shifProfessorship != null) {
            infoShiftProfessorship = new InfoShiftProfessorshipAndTeacher();
            infoShiftProfessorship.copyFromDomain(shifProfessorship);
        }
        return infoShiftProfessorship;
    }
}