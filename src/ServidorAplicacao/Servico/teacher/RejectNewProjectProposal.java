/*
 * Created on 9/Set/2004
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Advisory;
import Dominio.GroupProperties;
import Dominio.IAdvisory;
import Dominio.IExecutionCourse;
import Dominio.IGroupProperties;
import Dominio.IGroupPropertiesExecutionCourse;
import Dominio.IPessoa;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
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
public class RejectNewProjectProposal implements IService
{

	public RejectNewProjectProposal()
	{

	}

	public Boolean run(Integer executionCourseId,Integer groupPropertiesId, String rejectorUserName)
			throws FenixServiceException
	{

		Boolean result = Boolean.FALSE;

		if (groupPropertiesId == null){
			return result;
		}

		
		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentGroupProperties persistentGroupProperties = sp.getIPersistentGroupProperties();
			IPersistentGroupPropertiesExecutionCourse persistentGroupPropertiesExecutionCourse = sp.getIPersistentGroupPropertiesExecutionCourse();
			IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

			IGroupProperties groupProperties = (IGroupProperties) persistentGroupProperties
            .readByOID(GroupProperties.class, groupPropertiesId);
			
			if(groupProperties == null){
				throw new NotAuthorizedException();
			}
			
			IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse = (IGroupPropertiesExecutionCourse) persistentGroupPropertiesExecutionCourse.readByIDs(groupPropertiesId,executionCourseId);
			
			if(groupPropertiesExecutionCourse==null){
				throw new ExistingServiceException();
			}
			
			IPessoa receiverPerson = ((ITeacher) persistentTeacher.readTeacherByUsername(rejectorUserName)).getPerson();
			
			IExecutionCourse executionCourse =  groupPropertiesExecutionCourse.getExecutionCourse();
			groupPropertiesExecutionCourse.setReceiverPerson(receiverPerson);
			groupPropertiesExecutionCourse.getProposalState().setState(3);
			executionCourse.removeGroupPropertiesExecutionCourse(groupPropertiesExecutionCourse);
			groupProperties.removeGroupPropertiesExecutionCourse(groupPropertiesExecutionCourse);
			
			persistentGroupPropertiesExecutionCourse.delete(groupPropertiesExecutionCourse);

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
			
						if(!(teacher.getPerson()).equals(receiverPerson) && !group.contains(teacher.getPerson())){
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
				if(!(teacherAux.getPerson()).equals(receiverPerson) && !group.contains(teacherAux.getPerson())){
					group.add(teacherAux.getPerson());
				}
			}
			
			
			
			
			IPessoa senderPerson = groupPropertiesExecutionCourse.getSenderPerson();
			
			// Create Advisory
            IAdvisory advisory = createRejectAdvisory(executionCourse,senderPerson, receiverPerson,groupPropertiesExecutionCourse);
            sp.getIPersistentAdvisory().write(advisory, group);
			
            result = Boolean.TRUE;
			
		} catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException("error.groupPropertiesExecutionCourse.delete");
		}

		return result;
	}
	
	
	
	
	private IAdvisory createRejectAdvisory(IExecutionCourse executionCourse,IPessoa senderPerson, IPessoa receiverPerson,IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse) {
        IAdvisory advisory = new Advisory();
        advisory.setCreated(new Date(Calendar.getInstance().getTimeInMillis()));
        if(groupPropertiesExecutionCourse.getGroupProperties().getEnrolmentEndDay()!=null){
        	advisory.setExpires(groupPropertiesExecutionCourse.getGroupProperties().getEnrolmentEndDay().getTime());
        }
        else {
        	advisory.setExpires(new Date(Calendar.getInstance().getTimeInMillis()+ 1728000000));
        }
        advisory.setSender("Docente " + receiverPerson.getNome() + " da disciplina " + executionCourse.getNome());

        advisory
                .setSubject("Proposta Enviada Rejeitada");
        
        String msg;
        msg = new String(
                    "A proposta de co-avaliação do agrupamento " + groupPropertiesExecutionCourse.getGroupProperties().getName() +
					", enviada pelo docente " + senderPerson.getNome()+ " da disciplina " +
					groupPropertiesExecutionCourse.getSenderExecutionCourse().getNome()+
					" foi rejeitada pelo docente " + receiverPerson.getNome() + " da disciplina " + executionCourse.getNome() + "!");
        
        advisory.setMessage(msg);
        advisory.setOnlyShowOnce(new Boolean(true));
        return advisory;
    }
}