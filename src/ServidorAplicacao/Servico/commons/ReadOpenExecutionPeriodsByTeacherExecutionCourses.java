/*
 * Created on 25/01/2005
 */
package ServidorAplicacao.Servico.commons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.util.Cloner;
import Dominio.IExecutionPeriod;
import Dominio.IProfessorship;
import Dominio.IResponsibleFor;
import Dominio.ITeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.PeriodState;

/**
 * @author joaosa and rmalo
 */
public class ReadOpenExecutionPeriodsByTeacherExecutionCourses implements IService {

    public List run(IUserView userView) throws FenixServiceException {

        List result = new ArrayList();
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
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
                PeriodState periodState = (PeriodState)executionPeriod.getState();
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
                PeriodState periodState = (PeriodState)executionPeriod.getState();
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
