/*
 * Created on 7/Mai/2005 - 16:05:02
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.dataTransferObject.InfoFrequenta;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class ReadAttendsByOID implements IService {

	private static ReadAttendsByOID service =
		new ReadAttendsByOID();
	
	public ReadAttendsByOID() {
	}

	public String getNome() {
		return "student.ReadAttendsByOID";
	}

    /**
     * @return Returns the service.
     */
    public static ReadAttendsByOID getService() {
        return service;
    }
	
	public InfoFrequenta run(Integer idInternal) throws ExcepcaoPersistencia {
		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();
		
		IAttends attends = (IAttends)persistentAttend.readByOID(Attends.class, idInternal);
		
		return InfoFrequenta.newInfoFromDomain(attends);

	}

    

}
