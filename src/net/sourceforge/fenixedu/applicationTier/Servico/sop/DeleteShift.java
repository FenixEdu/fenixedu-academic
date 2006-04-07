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
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteShift extends Service {

    public Object run(InfoShift infoShift) throws FenixServiceException, ExcepcaoPersistencia {

        boolean result = false;

        if (infoShift != null) {
            deleteShift(infoShift.getIdInternal());
            result = true;
        }

        return Boolean.valueOf(result);
    }

    public static void deleteShift(final Integer shiftID) throws ExcepcaoPersistencia, FenixServiceException {
        Shift shift = rootDomainObject.readShiftByOID(shiftID);
        if (shift != null) {
            if (shift.hasAnyStudents()) {
                throw new FenixServiceException("error.deleteShift.with.students");
            }

            // if the shift has student groups associated it can't be deleted
            if (shift.hasAnyAssociatedStudentGroups()) {
                throw new FenixServiceException("error.deleteShift.with.studentGroups");
            }

            // if the shift has summaries it can't be deleted            
            if (shift.hasAnyAssociatedSummaries()) {
                throw new FenixServiceException("error.deleteShift.with.summaries");
            }

            shift.delete();
        }
    }

}
