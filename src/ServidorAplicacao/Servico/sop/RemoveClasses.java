/*
 *
 * Created on 2003/08/15
 */

package ServidorAplicacao.Servico.sop;

/**
 * Serviço AdicionarTurno.
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoShift;
import Dominio.ISchoolClass;
import Dominio.IShift;
import Dominio.SchoolClass;
import Dominio.Shift;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class RemoveClasses implements IService {

    public Boolean run(InfoShift infoShift, List classOIDs) throws FenixServiceException, ExcepcaoPersistencia {

        boolean result = false;

        ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        IShift shift = (IShift) sp.getITurnoPersistente().readByOID(Shift.class,
                infoShift.getIdInternal());

        sp.getITurnoPersistente().simpleLockWrite(shift);

        for (int i = 0; i < classOIDs.size(); i++) {
            ISchoolClass schoolClass = (ISchoolClass) sp.getITurmaPersistente().readByOID(SchoolClass.class,
                    (Integer) classOIDs.get(i));

            shift.getAssociatedClasses().remove(schoolClass);

            sp.getITurmaPersistente().simpleLockWrite(schoolClass);
            schoolClass.getAssociatedShifts().remove(shift);
        }

        result = true;

        return new Boolean(result);
    }

}