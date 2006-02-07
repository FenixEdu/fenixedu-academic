package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.space.CampusInformation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditCampus extends Service {

    public void run(final Integer campusInformationID, final Boolean asNewVersion, final String name) throws ExcepcaoPersistencia {
    	final CampusInformation campusInformation = (CampusInformation) persistentObject.readByOID(CampusInformation.class, campusInformationID);
        if (asNewVersion.booleanValue()) {
        	final Campus campus = (Campus) campusInformation.getSpace();
        	campus.edit(name);
        } else {
        	campusInformation.edit(name);
        }
    }

}
