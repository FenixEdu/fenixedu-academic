/*
 * Created on 1/Jul/2003
 *
 * 
 */
package ServidorAplicacao.Servico.sop;

import Dominio.ITurma;
import Dominio.ITurno;
import Dominio.Turma;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 * 
 * 1/Jul/2003 fenix-branch ServidorAplicacao.Servico.sop
 *  
 */
public class AddShiftToClasses implements IServico {

    /**
     *  
     */
    public AddShiftToClasses() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "AddShiftToClasses";
    }

    private static AddShiftToClasses service = new AddShiftToClasses();

    public static AddShiftToClasses getService() {
        return service;
    }

    public void run(Integer keyShift, String[] classesList) throws FenixServiceException {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            ITurnoPersistente persistentShift = sp.getITurnoPersistente();
            ITurmaPersistente persistentClass = sp.getITurmaPersistente();

            ITurno shift = new Turno(keyShift);
            shift = (ITurno) persistentShift.readByOID(Turno.class, keyShift);
            if (shift == null || classesList == null) {
                throw new InvalidArgumentsServiceException();
            }
            persistentShift.simpleLockWrite(shift);
            int iter = 0;
            int length = classesList.length;
            while (iter < length) {
                Integer keyClass = new Integer(classesList[iter]);
                ITurma dClass = (ITurma) persistentClass.readByOID(Turma.class, keyClass);
                if (dClass == null) {
                    throw new InvalidArgumentsServiceException();
                }
                shift.getAssociatedClasses().add(dClass);
                persistentClass.simpleLockWrite(dClass);
                dClass.getAssociatedShifts().add(shift);

                iter++;
            }

        } catch (ExcepcaoPersistencia e) {
        }

    }

}