/*
 * Created on 27/Mai/2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.ExecutionPeriod;
import Dominio.IExecutionPeriod;
import Dominio.IProfessorship;
import Dominio.IResponsibleFor;
import Dominio.ITeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.teacher.professorship.ReadDetailedTeacherProfessorshipsAbstractService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jpvl
 */
public class ReadProfessorships extends ReadDetailedTeacherProfessorshipsAbstractService {

    public List run(IUserView userView, Integer executionPeriodCode) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente persistentSuport;
        persistentSuport = SuportePersistenteOJB.getInstance();

        IPersistentResponsibleFor responsibleForDAO = persistentSuport.getIPersistentResponsibleFor();
        IPersistentProfessorship persistentProfessorship = persistentSuport
                .getIPersistentProfessorship();
        IPersistentTeacher teacherDAO = persistentSuport.getIPersistentTeacher();
        IPersistentExecutionPeriod persistentExecutionPeriod = persistentSuport.getIPersistentExecutionPeriod();

        IExecutionPeriod executionPeriod = null;
        if(executionPeriodCode != null){
        executionPeriod = (IExecutionPeriod)persistentExecutionPeriod.readByOID(ExecutionPeriod.class, executionPeriodCode);
        }
        
        ITeacher teacher = teacherDAO.readTeacherByUsername(userView.getUtilizador());
        

        List professorships = persistentProfessorship.readByTeacher(teacher);
        List professorshipsList = new ArrayList();
        professorshipsList.addAll(professorships);
        
        if(executionPeriod !=  null){
            Iterator iterProfessorships = professorships.iterator();
            while(iterProfessorships.hasNext()){
                IProfessorship professorship = (IProfessorship)iterProfessorships.next();
                if(!professorship.getExecutionCourse().getExecutionPeriod().equals(executionPeriod)){
                    professorshipsList.remove(professorship);
                }
            }
        }
        
        final List responsibleFors = responsibleForDAO.readByTeacher(teacher);
        List responsibleForsList = new ArrayList();
        responsibleForsList.addAll(responsibleFors);
        
        if(executionPeriod !=  null){
            Iterator iterResponsibleFors = responsibleFors.iterator();
            while(iterResponsibleFors.hasNext()){
                IResponsibleFor responsibleFor = (IResponsibleFor)iterResponsibleFors.next();
                if(!responsibleFor.getExecutionCourse().getExecutionPeriod().equals(executionPeriod)){
                    responsibleForsList.remove(responsibleFor);
                }
            }
        }
        
        List detailedProfessorshipList = getDetailedProfessorships(professorshipsList, responsibleForsList,
                persistentSuport);
        return detailedProfessorshipList;
    }
}