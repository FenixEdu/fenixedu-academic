/*
 *
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Serviço AdicionarTurno.
 * 
 * @author Luis Cruz & Sara Ribeiro
 * @author Pedro Santos e Rita Carvalho
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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