/*
 *
 * Created on 2003/08/13
 */

package ServidorAplicacao.Servico.sop;

/**
 * Serviço AdicionarTurno.
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.List;

import DataBeans.InfoClass;
import Dominio.ISchoolClass;
import Dominio.IShift;
import Dominio.SchoolClass;
import Dominio.Shift;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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