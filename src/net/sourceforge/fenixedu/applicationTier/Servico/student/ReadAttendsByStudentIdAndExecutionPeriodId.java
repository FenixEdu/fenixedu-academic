/*
 * Created on 7/Mai/2005 - 13:54:46
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoFrequenta;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class ReadAttendsByStudentIdAndExecutionPeriodId implements IService {

    public List<InfoFrequenta> run(Integer studentId, Integer executionPeriodId,
            Boolean onlyEnrolledCourses) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();

        final List<IAttends> attendsList = persistentAttend.readByStudentIdAndExecutionPeriodId(
                studentId, executionPeriodId);
        final List<InfoFrequenta> infoAttendsList = new ArrayList<InfoFrequenta>(attendsList.size());

        for (final IAttends attends : attendsList) {
            if (!(onlyEnrolledCourses.booleanValue() && (attends.getEnrolment() == null))) {
                infoAttendsList.add(InfoFrequenta.newInfoFromDomain(attends));
            }
        }

        return infoAttendsList;
    }

}
