/*
 * Created on 24/Ago/2004
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import Dominio.Advisory;
import Dominio.ExecutionCourse;
import Dominio.GroupProperties;
import Dominio.IAdvisory;
import Dominio.IExecutionCourse;
import Dominio.IGroupProperties;
import Dominio.IGroupPropertiesExecutionCourse;
import Dominio.IPessoa;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.IPersistentGroupPropertiesExecutionCourse;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author joaosa & rmalo
 *
 */

public class DeleteProjectProposal implements IServico {

    private static DeleteProjectProposal service = new DeleteProjectProposal();

    /**
     * The singleton access method of this class.
     */
    public static DeleteProjectProposal getService() {
        return service;
    }

    /**
     * The constructor of this class.
     */
    private DeleteProjectProposal() {
    }

    /**
     * The name of the service
     */
    public final String getNome() {
        return "DeleteProjectProposal";
    }

    /**
     * Executes the service.
     */

    public boolean run(Integer objectCode, Integer groupPropertiesCode,Integer executionCourseCode,String withdrawalPersonUsername)
    throws FenixServiceException {

        
    
        IPersistentGroupPropertiesExecutionCourse persistentGroupPropertiesExecutionCourse = null;
        IPersistentGroupProperties persistentGroupProperties = null;
        IPersistentExecutionCourse persistentExecutionCourse = null;
     
        try {

            ISuportePersistente persistentSupport = SuportePersistenteOJB
                    .getInstance();

            persistentGroupProperties = persistentSupport.getIPersistentGroupProperties();
            persistentExecutionCourse = persistentSupport.getIPersistentExecutionCourse();
            persistentGroupPropertiesExecutionCourse = persistentSupport.getIPersistentGroupPropertiesExecutionCourse();
            IPersistentTeacher persistentTeacher = persistentSupport.getIPersistentTeacher();

			IPessoa withdrawalPerson = ((ITeacher) persistentTeacher.readTeacherByUsername(withdrawalPersonUsername)).getPerson();
            IGroupProperties groupProperties = (IGroupProperties) persistentGroupProperties.readByOID(GroupProperties.class,groupPropertiesCode);
            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class,executionCourseCode);
            IExecutionCourse startExecutionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class,objectCode);
            
            if(groupProperties==null){
				throw new InvalidArgumentsServiceException("error.noGroupProperties");
			}
			if(executionCourse==null || startExecutionCourse==null){
				throw new InvalidArgumentsServiceException("error.noExecutionCourse");
			}
			
            IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse = (IGroupPropertiesExecutionCourse) persistentGroupPropertiesExecutionCourse.readBy(groupProperties,executionCourse);
			
            if(groupPropertiesExecutionCourse==null){
				throw new InvalidArgumentsServiceException("error.noProjectProposal");
			}

            
            	groupProperties.removeGroupPropertiesExecutionCourse(groupPropertiesExecutionCourse);
            	executionCourse.removeGroupPropertiesExecutionCourse(groupPropertiesExecutionCourse);
            	persistentGroupPropertiesExecutionCourse.delete(groupPropertiesExecutionCourse);

            	// List teachers to advise
            	List group = new ArrayList();
    			
    			List groupPropertiesExecutionCourseList = groupProperties.getGroupPropertiesExecutionCourse();
    			Iterator iterGroupPropertiesExecutionCourseList = groupPropertiesExecutionCourseList.iterator();
    			
    			while(iterGroupPropertiesExecutionCourseList.hasNext()){
    			
    				IGroupPropertiesExecutionCourse groupPropertiesExecutionCourseAux = (IGroupPropertiesExecutionCourse)iterGroupPropertiesExecutionCourseList.next();
    				if(groupPropertiesExecutionCourseAux.getProposalState().getState().intValue()==1 
    					|| groupPropertiesExecutionCourseAux.getProposalState().getState().intValue()==2){
    			
    					List professorships = persistentExecutionCourse.readExecutionCourseTeachers(groupPropertiesExecutionCourseAux.getExecutionCourse().getIdInternal());
    			
    					Iterator iterProfessorship = professorships.iterator();
    					while(iterProfessorship.hasNext()){
    						IProfessorship professorship = (IProfessorship)iterProfessorship.next();
    						ITeacher teacher = professorship.getTeacher();
    			
    						if(!(teacher.getPerson()).equals(withdrawalPerson) && !group.contains(teacher.getPerson())){
    							group.add(teacher.getPerson());
    						}
    					}
    				}
    			}
    			
    			List professorshipsAux = persistentExecutionCourse.readExecutionCourseTeachers(executionCourse.getIdInternal());
    			
    			Iterator iterProfessorshipsAux = professorshipsAux.iterator();
    			while(iterProfessorshipsAux.hasNext()){
    				IProfessorship professorshipAux = (IProfessorship)iterProfessorshipsAux.next();
    				ITeacher teacherAux = professorshipAux.getTeacher();
    				if(!(teacherAux.getPerson()).equals(withdrawalPerson) && !group.contains(teacherAux.getPerson())){
    					group.add(teacherAux.getPerson());
    				}
    			}
    			
    			// Create Advisory
                IAdvisory advisory = createDeleteProjectProposalAdvisory(executionCourse,startExecutionCourse,withdrawalPerson,groupPropertiesExecutionCourse);
                persistentSupport.getIPersistentAdvisory().write(advisory, group);
    			
    			
            	

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }
        return true;
    }
    
    
    private IAdvisory createDeleteProjectProposalAdvisory(IExecutionCourse goalExecutionCourse,IExecutionCourse startExecutionCourse,IPessoa withdrawalPerson, IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse) {
        IAdvisory advisory = new Advisory();
        advisory.setCreated(new Date(Calendar.getInstance().getTimeInMillis()));
        if(groupPropertiesExecutionCourse.getGroupProperties().getEnrolmentEndDay()!=null){
        	advisory.setExpires(groupPropertiesExecutionCourse.getGroupProperties().getEnrolmentEndDay().getTime());
        }
        else {
        	advisory.setExpires(new Date(Calendar.getInstance().getTimeInMillis()+ 1728000000));
        }
        advisory.setSender("Docente " + withdrawalPerson.getNome() + " da disciplina " + startExecutionCourse.getNome());

        advisory
                .setSubject("Desistência de proposta de Co-Avaliação");
        
        String msg;
        msg = new String(
        			"O Docente " + withdrawalPerson.getNome() + " da disciplina " + startExecutionCourse.getNome() + 
                    " desistiu da proposta de co-avaliação para a disciplina " + goalExecutionCourse.getNome() + 
					" relativa ao agrupamento " + groupPropertiesExecutionCourse.getGroupProperties().getName() + " previamente enviada pelo docente " +  
					groupPropertiesExecutionCourse.getSenderPerson().getNome() + " da disciplina " +
					groupPropertiesExecutionCourse.getSenderExecutionCourse().getNome()+ "!");
        
        advisory.setMessage(msg);
        advisory.setOnlyShowOnce(new Boolean(true));
        return advisory;
    }
    
    
    
    
}