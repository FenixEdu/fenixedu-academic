/*
 * 
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;

public class DeleteShift extends FenixService {

    public void run(InfoShift infoShift) throws FenixServiceException {
	rootDomainObject.readShiftByOID(infoShift.getIdInternal()).delete();
    }

}
