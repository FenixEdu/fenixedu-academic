/*
 * Created on 04/Sep/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IGrouping;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGrouping;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author joaosa & rmalo
 * 
 */

public class DeleteAllGroupingMembers implements IService {

    public boolean run(Integer objectCode, Integer groupingCode) throws FenixServiceException,
            ExcepcaoPersistencia {
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentGrouping persistentGrouping = persistentSupport.getIPersistentGrouping();
        IPersistentStudentGroup persistentStudentGroup = persistentSupport.getIPersistentStudentGroup();
        IGrouping grouping = (IGrouping) persistentGrouping.readByOID(Grouping.class, groupingCode);

        if (grouping == null) {
            throw new ExistingServiceException();
        }

        List attendsElements = new ArrayList();
        attendsElements.addAll(grouping.getAttends());
        Iterator iterator = attendsElements.iterator();
        while (iterator.hasNext()) {
            IAttends attend = (IAttends) iterator.next();

            boolean found = false;
            Iterator iterStudentsGroups = grouping.getStudentGroups().iterator();
            while (iterStudentsGroups.hasNext() && !found) {

                IStudentGroup studentGroup = (IStudentGroup) iterStudentsGroups.next();
                
                if (studentGroup != null) {
                    studentGroup.removeAttends(attend);
                    found = true;
                }
            }
           grouping.removeAttends(attend);
        }

        return true;
    }
}