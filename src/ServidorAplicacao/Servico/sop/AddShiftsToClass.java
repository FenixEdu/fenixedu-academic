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
import Dominio.ITurma;
import Dominio.ITurno;
import Dominio.Turma;
import Dominio.Turno;
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

            ITurma schoolClass = (ITurma) sp.getITurmaPersistente().readByOID(Turma.class,
                    infoClass.getIdInternal());
            sp.getITurmaPersistente().simpleLockWrite(schoolClass);

            for (int i = 0; i < shiftOIDs.size(); i++) {
                ITurno shift = (ITurno) sp.getITurnoPersistente().readByOID(Turno.class,
                        (Integer) shiftOIDs.get(i));

                //				ITurmaTurno classShift = new TurmaTurno(schoolClass, shift);
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