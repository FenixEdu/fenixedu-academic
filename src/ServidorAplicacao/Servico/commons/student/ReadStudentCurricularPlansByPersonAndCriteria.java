/*
 * Created on Oct 19, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ServidorAplicacao.Servico.commons.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoEnrolment;
import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear;
import DataBeans.InfoStudentCurricularPlanWithInfoStudentAndDegreeAndBranch;
import DataBeans.util.InfoStudentCurricularPlansWithSelectedEnrollments;
import Dominio.IEnrollment;
import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.utils.EnrollmentPredicates;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrollmentStateSelectionType;
import Util.StudentCurricularPlanIDDomainType;
import Util.TipoCurso;

/**
 * @author André Fernandes / João Brito
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ReadStudentCurricularPlansByPersonAndCriteria implements IService
{
	public ReadStudentCurricularPlansByPersonAndCriteria()
	{
	};

	/*
	 * devolve InfoXxxx com:
	 * - StudentCurricularPlan's do aluno
	 * - Enrolments do aluno (filtrados por um criterio)
	 * */
	public InfoStudentCurricularPlansWithSelectedEnrollments run(String username, StudentCurricularPlanIDDomainType curricularPlanID, EnrollmentStateSelectionType criterio) throws ExcepcaoInexistente, FenixServiceException
	{
		// curricularPlanID pode ser: ID do CP do aluno, 'all ou 'newest
		// criterio define que IEnrolments vamos ver: pode ser 'aprovado ou null (selecciona todos) 

	    ISuportePersistente sp = null;
        List studentCurricularPlans = new ArrayList();
        
        Predicate predicado = null;
        
        if (criterio.equals(EnrollmentStateSelectionType.APPROVED))
        {
            predicado = EnrollmentPredicates.getApprovedPredicate();
        }
        else if (criterio.equals(EnrollmentStateSelectionType.NONE))
        {
            predicado = EnrollmentPredicates.getNonePredicate(); // nenhum
        }
        else
        {
            predicado = EnrollmentPredicates.getAllPredicate(); // todos
        }
        
        try
        {
            
            sp = SuportePersistenteOJB.getInstance();
            
            if (curricularPlanID.isAll() || curricularPlanID.isNewest())
            {
                IPessoa person = (IPessoa)sp.getIPessoaPersistente().lerPessoaPorUsername(username);
                List students = sp.getIPersistentStudent().readbyPerson(person);
                List studentCPsTemp = null;
                
                Iterator studentsIterator = students.iterator();
                
                // para cada Student que esta Person é
                // juntar todos os SCP
                while(studentsIterator.hasNext())
                {
                    IStudent student = (IStudent)studentsIterator.next();
                            
                    TipoCurso degreeType = student.getDegreeType();
                    Integer studentNumber = student.getNumber();

                    //seleccionar todos os planos do aluno
		        
                    studentCPsTemp = sp.getIStudentCurricularPlanPersistente().readByStudentNumberAndDegreeType(studentNumber, degreeType);
                    
                    studentCurricularPlans.addAll(studentCPsTemp);
                }
		  
                if (curricularPlanID.isNewest())
                {
                    //seleccionar o mais recente
	            
	                IStudentCurricularPlan planoRecente = null;
	                IStudentCurricularPlan planoTemp = null;
	                Iterator iterator = studentCurricularPlans.iterator();
	            
		            while (iterator.hasNext())
		            {
		                planoTemp = (IStudentCurricularPlan)iterator.next();
		                
		                if (planoRecente == null || 
		                    planoRecente.getStartDate().before(planoTemp.getStartDate()))
		                {
		                    planoRecente = planoTemp;
		                }
		            }
		            
		            studentCurricularPlans = new ArrayList();
		            studentCurricularPlans.add(planoRecente);
                }
		    }
		    else // um SCP em particular
		    {
		        //obter o CP especificado como curricularPlanID
		        
		        studentCurricularPlans.add(	                
		            sp.getIStudentCurricularPlanPersistente().readByOID(StudentCurricularPlan.class, curricularPlanID.getId()));
		    }
	    
        }
        catch (ExcepcaoPersistencia ex)
        {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }
        
	    
        InfoStudentCurricularPlansWithSelectedEnrollments currPlanEnrol = new InfoStudentCurricularPlansWithSelectedEnrollments();
        
        
        Iterator iteratorInfo = studentCurricularPlans.iterator();
        IStudentCurricularPlan studentCurricularPlan = null;
        
        while (iteratorInfo.hasNext())
        {
            //criacao da info a retornar a partir dos objectos de dominio pretendidos
            
            studentCurricularPlan = (IStudentCurricularPlan)iteratorInfo.next();
            
            List enrollments = studentCurricularPlan.getEnrolments();// lista de IEnrollment's
            List selectedEnrollments = ((List)CollectionUtils.select(enrollments,predicado));
            
            List infoSelectedEnrollments = new ArrayList();
            Iterator selectedEnrollmentsIterator = selectedEnrollments.iterator();
            
            // outras classes foram mexidas para isto estar ok
            GetEnrolmentGrade getEnrolmentGrade = new GetEnrolmentGrade();
            
            while (selectedEnrollmentsIterator.hasNext())
            {
                IEnrollment enrollment = (IEnrollment)selectedEnrollmentsIterator.next();
                InfoEnrolment infoEnrollment = InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear.newInfoFromDomain(enrollment);
                
                InfoEnrolmentEvaluation infoEnrolmentEvaluation = getEnrolmentGrade.run(enrollment);
                infoEnrollment.setInfoEnrolmentEvaluation(infoEnrolmentEvaluation);
                
                infoSelectedEnrollments.add(infoEnrollment);
            }

            currPlanEnrol.addInfoStudentCurricularPlan(InfoStudentCurricularPlanWithInfoStudentAndDegreeAndBranch.newInfoFromDomain(studentCurricularPlan),
                    										infoSelectedEnrollments);    
        }
        
        return currPlanEnrol;
	}

}
