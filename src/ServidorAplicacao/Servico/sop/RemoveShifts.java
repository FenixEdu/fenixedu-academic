/*
 *
 * Created on 2003/08/15
 */

package ServidorAplicacao.Servico.sop;

/**
 * Serviço AdicionarTurno.
 * 
 * @author Luis Cruz & Sara Ribeiro
 * @author Pedro Santos e Rita Carvalho
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoClass;
import Dominio.ISchoolClass;
import Dominio.IShift;
import Dominio.SchoolClass;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class RemoveShifts implements IService {

    public Boolean run(InfoClass infoClass, List shiftOIDs) throws FenixServiceException,
            ExcepcaoPersistencia {
        final ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        final ISchoolClass schoolClass = (ISchoolClass) sp.getITurmaPersistente().readByOID(SchoolClass.class,
                infoClass.getIdInternal());
        sp.getITurmaPersistente().simpleLockWrite(schoolClass);
        final List shifts = schoolClass.getAssociatedShifts();
        final List shiftsToRemove = new ArrayList(shifts.size());
        
        Iterator iter = shifts.iterator();
        while (iter.hasNext()){
            IShift shift = (IShift) iter.next();
            
            if (shiftOIDs.contains(shift.getIdInternal())){
                shiftsToRemove.add(shift);
            }
        }
                
        shifts.removeAll(shiftsToRemove);

        return new Boolean(true);
    }

}