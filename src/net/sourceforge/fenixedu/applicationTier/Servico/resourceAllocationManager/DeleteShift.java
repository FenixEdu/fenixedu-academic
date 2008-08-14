/*
 * 
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteShift extends Service {

    public void run(InfoShift infoShift) throws FenixServiceException {
	rootDomainObject.readShiftByOID(infoShift.getIdInternal()).delete();
    }

}
