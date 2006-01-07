/*
 * Created on 25/01/2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.PeriodState;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author joaosa and rmalo
 */
public class ReadOpenExecutionPeriodsByTeacherExecutionCourses implements IService {

    public List run(IUserView userView) throws FenixServiceException, ExcepcaoPersistencia {
        
        final List<InfoExecutionPeriod> result = new ArrayList();
        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentTeacher persistentTeacher = persistentSupport.getIPersistentTeacher();
        
        final Teacher teacher = persistentTeacher.readTeacherByUsername(userView.getUtilizador());
        final List<ExecutionPeriod> executionPeriods = new ArrayList();
        
        final Iterator associatedProfessorships = teacher.getProfessorshipsIterator();
        while (associatedProfessorships.hasNext()) {
            Professorship professorship = (Professorship) associatedProfessorships.next();
            ExecutionPeriod executionPeriod = professorship.getExecutionCourse().getExecutionPeriod();
            PeriodState periodState = executionPeriod.getState();
            if (!executionPeriods.contains(executionPeriod) && (periodState.getStateCode().equals("C") || periodState.getStateCode().equals("O"))) {
                executionPeriods.add(executionPeriod);
            }
        }
               
        for (final ExecutionPeriod executionPeriod : executionPeriods) {
            result.add(InfoExecutionPeriodWithInfoExecutionYear.newInfoFromDomain(executionPeriod));
        }
        return result;
    }
}
