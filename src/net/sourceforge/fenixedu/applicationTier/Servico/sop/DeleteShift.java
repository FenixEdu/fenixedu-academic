/*
 * 
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteShift extends Service {

    public void run(InfoShift infoShift) throws FenixServiceException, ExcepcaoPersistencia {
	try {
	    rootDomainObject.readShiftByOID(infoShift.getIdInternal()).delete();	    
	} catch (DomainException e) {
	    throw new FenixServiceException(e.getMessage(), e.getArgs());
	}
    }

}
