/*
 *
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DeleteClasses implements IService {

    public Object run(List classOIDs) throws ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        for (int i = 0; i < classOIDs.size(); i++) {
            Integer classId = (Integer) classOIDs.get(i);
            final ISchoolClass schoolClass = (ISchoolClass) sp.getITurmaPersistente()
                    .readByOID(SchoolClass.class, classId);
            sp.getITurmaPersistente().simpleLockWrite(schoolClass);
            final List shifts = schoolClass.getAssociatedShifts();
            final List shiftsToRemove = new ArrayList(shifts);
            shifts.removeAll(shiftsToRemove);

            sp.getITurmaPersistente().delete(schoolClass);
        }

        return new Boolean(true);

    }

}