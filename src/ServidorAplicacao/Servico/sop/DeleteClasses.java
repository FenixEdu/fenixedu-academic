/*
 *
 * Created on 2003/08/15
 */

package ServidorAplicacao.Servico.sop;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.ITurma;
import Dominio.Turma;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class DeleteClasses implements IService {

    public Object run(List classOIDs) throws ExcepcaoPersistencia {

        final ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        for (int i = 0; i < classOIDs.size(); i++) {
            Integer classId = (Integer) classOIDs.get(i);
            final ITurma schoolClass = (ITurma) sp.getITurmaPersistente()
                    .readByOID(Turma.class, classId);
            sp.getITurmaPersistente().simpleLockWrite(schoolClass);
            final List shifts = schoolClass.getAssociatedShifts();
            final List shiftsToRemove = new ArrayList(shifts);
            shifts.removeAll(shiftsToRemove);

            sp.getITurmaPersistente().delete(schoolClass);
        }

        return new Boolean(true);

    }

}