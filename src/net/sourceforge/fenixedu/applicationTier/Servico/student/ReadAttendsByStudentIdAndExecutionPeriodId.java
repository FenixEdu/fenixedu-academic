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
	private static ReadAttendsByStudentIdAndExecutionPeriodId service =
		new ReadAttendsByStudentIdAndExecutionPeriodId();
	
	public ReadAttendsByStudentIdAndExecutionPeriodId() {
	}

	public String getNome() {
		return "student.ReadAttendsByStudentIdAndExecutionPeriodId";
	}

    /**
     * @return Returns the service.
     */
    public static ReadAttendsByStudentIdAndExecutionPeriodId getService() {
        return service;
    }
    
	public List<InfoFrequenta> run(
			Integer studentId, Integer executionPeriodId, Boolean onlyEnrolledCourses)
	throws ExcepcaoPersistencia {

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();
		
		List<IAttends> attendsList = persistentAttend.readByStudentIdAndExecutionPeriodId(studentId, executionPeriodId);
		List<InfoFrequenta> infoAttendsList =
			new ArrayList<InfoFrequenta>();
		
		for(IAttends attends : attendsList) {
			
			if(!(onlyEnrolledCourses && (attends.getEnrolment() == null))) {
				infoAttendsList.add(InfoFrequenta.newInfoFromDomain(attends));				
			}
			
		}
		
		return infoAttendsList;
	}

}
