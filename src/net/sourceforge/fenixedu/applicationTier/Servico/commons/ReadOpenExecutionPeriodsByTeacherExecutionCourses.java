/*
 * Created on 25/01/2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.IResponsibleFor;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentResponsibleFor;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.PeriodState;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author joaosa and rmalo
 */
public class ReadOpenExecutionPeriodsByTeacherExecutionCourses implements IService {

    public List run(IUserView userView) throws FenixServiceException {

        List result = new ArrayList();
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentResponsibleFor persistentResponsibleFor = sp.getIPersistentResponsibleFor();
            IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

            ITeacher teacher = persistentTeacher.readTeacherByUsername(userView.getUtilizador());

            List executionPeriods = new ArrayList();
            
            List professorships = persistentProfessorship.readByTeacher(teacher);
            Iterator iterProfessorships = professorships.iterator();
            while(iterProfessorships.hasNext()){
                IProfessorship professorship = (IProfessorship)iterProfessorships.next();
                IExecutionPeriod executionPeriod = professorship.getExecutionCourse().getExecutionPeriod();
                PeriodState periodState = executionPeriod.getState();
                if(!executionPeriods.contains(executionPeriod) && 
                        (periodState.getStateCode().equals("C") || periodState.getStateCode().equals("O"))){
                    executionPeriods.add(executionPeriod);
                }
            }
            
            
            List responsibleFors = persistentResponsibleFor.readByTeacher(teacher);
            Iterator iterResponsibleFors = responsibleFors.iterator(); 
            while(iterResponsibleFors.hasNext()){
                IResponsibleFor responsibleFor = (IResponsibleFor)iterResponsibleFors.next();
                IExecutionPeriod executionPeriod = responsibleFor.getExecutionCourse().getExecutionPeriod();
                PeriodState periodState = executionPeriod.getState();
                if(!executionPeriods.contains(executionPeriod) && 
                        (periodState.getStateCode().equals("C") || periodState.getStateCode().equals("O"))){
                    executionPeriods.add(executionPeriod);
                }

            }
            

            if (executionPeriods != null) {
                for (int i = 0; i < executionPeriods.size(); i++) {
                    result.add(Cloner.get((IExecutionPeriod) executionPeriods.get(i)));
                }
            }
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        return result;
    }
}
