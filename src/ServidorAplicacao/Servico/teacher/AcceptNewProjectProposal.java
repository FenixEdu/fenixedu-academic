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
import Dominio.AttendInAttendsSet;
import Dominio.GroupProperties;
import Dominio.IAdvisory;
import Dominio.IAttendInAttendsSet;
import Dominio.IAttendsSet;
import Dominio.IExecutionCourse;
import Dominio.IAttends;
import Dominio.IGroupProperties;
import Dominio.IGroupPropertiesExecutionCourse;
import Dominio.IPerson;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentAttendInAttendsSet;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.IPersistentGroupPropertiesExecutionCourse;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.ProposalState;

/**
 * @author joaosa & rmalo
 *  
 */
public class AcceptNewProjectProposal implements IService
{

	public AcceptNewProjectProposal()
	{

	}

	public Boolean run(Integer executionCourseId,Integer groupPropertiesId, String acceptancePersonUserName)
			throws FenixServiceException
	{

		Boolean result = Boolean.FALSE;

		if (groupPropertiesId == null)
		{
			return result;
		}

		
		try
		{

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentGroupProperties persistentGroupProperties = sp.getIPersistentGroupProperties();
			IPersistentGroupPropertiesExecutionCourse persistentGroupPropertiesExecutionCourse = sp.getIPersistentGroupPropertiesExecutionCourse();
			IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
			IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();
			IPersistentAttendInAttendsSet persistentAttendInAttendsSet = sp.getIPersistentAttendInAttendsSet();
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
			
			IPerson receiverPerson = ((ITeacher) persistentTeacher.readTeacherByUsername(acceptancePersonUserName)).getPerson();
			
			IExecutionCourse executionCourseAux = groupPropertiesExecutionCourse.getExecutionCourse();
			if(executionCourseAux.getGroupPropertiesByName(groupPropertiesExecutionCourse.getGroupProperties().getName())!=null){
				String name = groupPropertiesExecutionCourse.getGroupProperties().getName();
				throw new InvalidSituationServiceException(name);	
			}
			
			persistentGroupPropertiesExecutionCourse.simpleLockWrite(groupPropertiesExecutionCourse);
	
			
			List attendsSetStudentNumbers = new ArrayList();
			IAttendsSet attendsSet = groupPropertiesExecutionCourse.getGroupProperties().getAttendsSet();
			List attendsInAttendsSet = attendsSet.getAttendInAttendsSet();
			Iterator iterAttendsInAttendsSet = attendsInAttendsSet.iterator();
			while(iterAttendsInAttendsSet.hasNext()){
				IAttendInAttendsSet attendInAttendsSet = (IAttendInAttendsSet)iterAttendsInAttendsSet.next();
				attendsSetStudentNumbers.add(attendInAttendsSet.getAttend().getAluno().getNumber());
			}
			
			IExecutionCourse executionCourse = groupPropertiesExecutionCourse.getExecutionCourse();
			List attends = persistentAttend.readByExecutionCourse(executionCourse);
			Iterator iterAttends = attends.iterator();
			while(iterAttends.hasNext()){
				IAttends attend =(IAttends)iterAttends.next();
				if(!attendsSetStudentNumbers.contains(attend.getAluno().getNumber())){
					IAttendInAttendsSet	attendInAttendsSet = new AttendInAttendsSet(attend,attendsSet);
					persistentAttendInAttendsSet.simpleLockWrite(attendInAttendsSet);
					attendsSet.addAttendInAttendsSet(attendInAttendsSet);
					attend.addAttendInAttendsSet(attendInAttendsSet);
				}
			}
			
			
			IPerson senderPerson = groupPropertiesExecutionCourse.getSenderPerson();		
			List groupPropertiesExecutionCourseList = groupProperties.getGroupPropertiesExecutionCourse();
			Iterator iterGroupPropertiesExecutionCourseList = groupPropertiesExecutionCourseList.iterator();
			List groupTeachers = new ArrayList();
			while(iterGroupPropertiesExecutionCourseList.hasNext()){
				IGroupPropertiesExecutionCourse groupPropertiesExecutionCourseAux = (IGroupPropertiesExecutionCourse)iterGroupPropertiesExecutionCourseList.next();
				if(groupPropertiesExecutionCourseAux.getProposalState().getState().intValue()==1 
					|| groupPropertiesExecutionCourseAux.getProposalState().getState().intValue()==2){
					IExecutionCourse personExecutionCourse = (IExecutionCourse)groupPropertiesExecutionCourseAux.getExecutionCourse();
					List professorships = persistentExecutionCourse.readExecutionCourseTeachers(groupPropertiesExecutionCourseAux.getExecutionCourse().getIdInternal());
					Iterator iterProfessorship = professorships.iterator();
					while(iterProfessorship.hasNext()){
						IProfessorship professorship = (IProfessorship)iterProfessorship.next();
						ITeacher teacher = professorship.getTeacher();
						if(!(teacher.getPerson()).equals(receiverPerson) && !groupTeachers.contains(teacher.getPerson())){
							groupTeachers.add(teacher.getPerson());
						}
					}
					
					//Create Advisory for Teachers already in groupproperties executioncourses
		            IAdvisory advisory = createAcceptAdvisory(executionCourse,personExecutionCourse, groupPropertiesExecutionCourse,receiverPerson,senderPerson);
		            sp.getIPersistentAdvisory().write(advisory, groupTeachers);
		 
				}
			}
	
			List groupAux = new ArrayList();
			List professorshipsAux = persistentExecutionCourse.readExecutionCourseTeachers(executionCourse.getIdInternal());
			
			Iterator iterProfessorshipsAux = professorshipsAux.iterator();
			while(iterProfessorshipsAux.hasNext()){
				IProfessorship professorshipAux = (IProfessorship)iterProfessorshipsAux.next();
				ITeacher teacherAux = professorshipAux.getTeacher();
				if(!(teacherAux.getPerson()).equals(receiverPerson)){
					groupAux.add(teacherAux.getPerson());
				}
			}
			
			//Create Advisory for teachers of the new executioncourse
            IAdvisory advisoryAux = createAcceptAdvisory(executionCourse,executionCourse, groupPropertiesExecutionCourse,receiverPerson,senderPerson);
            sp.getIPersistentAdvisory().write(advisoryAux, groupAux);
			
    		groupPropertiesExecutionCourse.setProposalState(new ProposalState(new Integer(2)));
    		groupPropertiesExecutionCourse.setReceiverPerson(receiverPerson);
			result = Boolean.TRUE;
		} catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException("error.noProjectProposal");
		}

		return result;
	}
	
	private IAdvisory createAcceptAdvisory(IExecutionCourse executionCourse,IExecutionCourse personExecutionCourse,
			IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse, IPerson receiverPerson, IPerson senderPerson) {
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
                .setSubject("Proposta Enviada Aceite");
        
        String msg;
        msg = new String(
                    "A proposta de co-avaliação do agrupamento " + groupPropertiesExecutionCourse.getGroupProperties().getName() 
					+ ", enviada pelo docente " + senderPerson.getNome()
					+ "da disciplina " + groupPropertiesExecutionCourse.getSenderExecutionCourse().getNome()
					+ " foi aceite pelo docente " + receiverPerson.getNome() 
					+ " da disciplina " + executionCourse.getNome() + "!" 
					+ "<br>A partir deste momento poder-se-á dirijir à área de gestão de grupos da disciplina " 
					+ personExecutionCourse.getNome()+ " para gerir a nova co-avaliação.");
        
        advisory.setMessage(msg);
        advisory.setOnlyShowOnce(new Boolean(true));
        return advisory;
    }


}