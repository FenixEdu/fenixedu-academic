/*
 * Created on 9/Set/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Advisory;
import net.sourceforge.fenixedu.domain.AttendInAttendsSet;
import net.sourceforge.fenixedu.domain.GroupProperties;
import net.sourceforge.fenixedu.domain.IAdvisory;
import net.sourceforge.fenixedu.domain.IAttendInAttendsSet;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IAttendsSet;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IGroupPropertiesExecutionCourse;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentAttendInAttendsSet;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGroupProperties;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGroupPropertiesExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.ProposalState;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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

			ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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
			
			IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse = persistentGroupPropertiesExecutionCourse.readByIDs(groupPropertiesId,executionCourseId);
			
			if(groupPropertiesExecutionCourse==null){
				throw new ExistingServiceException();
			}
			
			IPerson receiverPerson = persistentTeacher.readTeacherByUsername(acceptancePersonUserName).getPerson();
			
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
					IExecutionCourse personExecutionCourse = groupPropertiesExecutionCourseAux.getExecutionCourse();
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