/*
 * 
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.applicationTier.Service;

public class DeleteShifts extends Service {

    public Object run(final List<Integer> shiftOIDs) throws FenixServiceException, ExcepcaoPersistencia {
        for (final Integer shiftID : shiftOIDs) {
            DeleteShift.deleteShift(shiftID);
        }
        return Boolean.TRUE;
    }

}