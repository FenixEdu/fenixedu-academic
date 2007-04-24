package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadRoomByOID extends Service {

    public InfoRoom run(Integer oid) throws ExcepcaoPersistencia {
	final OldRoom room = (OldRoom) rootDomainObject.readResourceByOID( oid);
	return InfoRoom.newInfoFromDomain(room);
    }
}