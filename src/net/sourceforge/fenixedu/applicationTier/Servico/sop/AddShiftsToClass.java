/*
 *
 * Created on 2003/08/13
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Serviço AdicionarTurno.
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

public class AddShiftsToClass implements IServico {

    private static AddShiftsToClass _servico = new AddShiftsToClass();

    /**
     * The singleton access method of this class.
     */
    public static AddShiftsToClass getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private AddShiftsToClass() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "AddShiftsToClass";
    }

    public Boolean run(InfoClass infoClass, List shiftOIDs) throws FenixServiceException {

        boolean result = false;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            ISchoolClass schoolClass = (ISchoolClass) sp.getITurmaPersistente().readByOID(SchoolClass.class,
                    infoClass.getIdInternal());
            sp.getITurmaPersistente().simpleLockWrite(schoolClass);

            for (int i = 0; i < shiftOIDs.size(); i++) {
                IShift shift = (IShift) sp.getITurnoPersistente().readByOID(Shift.class,
                        (Integer) shiftOIDs.get(i));

                //				ISchoolClassShift classShift = new SchoolClassShift(schoolClass, shift);
                //				try {
                //					sp.getITurmaTurnoPersistente().lockWrite(classShift);
                //				} catch (ExistingPersistentException e) {
                //					throw new ExistingServiceException(e);
                //				}
                schoolClass.getAssociatedShifts().add(shift);
                sp.getITurnoPersistente().simpleLockWrite(shift);
                shift.getAssociatedClasses().add(schoolClass);
            }

            result = true;
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex.getMessage());
        }

        return new Boolean(result);
    }

}