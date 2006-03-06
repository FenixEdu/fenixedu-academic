package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.space.SpaceInformation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteSpaceInformation extends Service {

    public void run(final Integer spaceInformationID) throws ExcepcaoPersistencia {
        final SpaceInformation spaceInformation = (SpaceInformation) persistentObject.readByOID(SpaceInformation.class, spaceInformationID);
        spaceInformation.delete();
    }

}
