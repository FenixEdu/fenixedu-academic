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
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.IResponsibleFor;
import net.sourceforge.fenixedu.domain.ITeacher;
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
        
        final ITeacher teacher = persistentTeacher.readTeacherByUsername(userView.getUtilizador());
        final List<IExecutionPeriod> executionPeriods = new ArrayList();
        
        final Iterator associatedProfessorships = teacher.getProfessorshipsIterator();
        while (associatedProfessorships.hasNext()) {
            IProfessorship professorship = (IProfessorship) associatedProfessorships.next();
            IExecutionPeriod executionPeriod = professorship.getExecutionCourse().getExecutionPeriod();
            PeriodState periodState = executionPeriod.getState();
            if (!executionPeriods.contains(executionPeriod) && (periodState.getStateCode().equals("C") || periodState.getStateCode().equals("O"))) {
                executionPeriods.add(executionPeriod);
            }
        }
        
        final Iterator associatedResponsibleFor = teacher.getAssociatedResponsiblesIterator();
        while (associatedResponsibleFor.hasNext()) {
            IResponsibleFor responsibleFor = (IResponsibleFor) associatedResponsibleFor.next();
            IExecutionPeriod executionPeriod = responsibleFor.getExecutionCourse().getExecutionPeriod();
            PeriodState periodState = executionPeriod.getState();
            if (!executionPeriods.contains(executionPeriod) && (periodState.getStateCode().equals("C") || periodState.getStateCode().equals("O"))) {
                executionPeriods.add(executionPeriod);
            }
        }
        
        for (final IExecutionPeriod executionPeriod : executionPeriods) {
            result.add(InfoExecutionPeriod.newInfoFromDomain(executionPeriod));
        }
        return result;
    }
}
