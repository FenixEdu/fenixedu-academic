/*
 * Created on 2/Abr/2004
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.Iterator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.AttendsSet;
import Dominio.GroupProperties;
import Dominio.IAttendInAttendsSet;
import Dominio.IAttendsSet;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IGroupProperties;
import Dominio.IGroupPropertiesExecutionCourse;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentAttendInAttendsSet;
import ServidorPersistente.IPersistentAttendsSet;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.IPersistentGroupPropertiesExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão
 *  
 */
public class DeleteGroupProperties implements IService
{

	public DeleteGroupProperties()
	{

	}

	public Boolean run(Integer executionCourseId, Integer groupPropertiesId)
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
			IPersistentAttendsSet persistentAttendsSet = sp.getIPersistentAttendsSet();
			IPersistentAttendInAttendsSet persistentAttendInAttendsSet = sp.getIPersistentAttendInAttendsSet();
			IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();
			IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();

			IGroupProperties groupProperties = (IGroupProperties) persistentGroupProperties.readByOID(GroupProperties.class,
			        groupPropertiesId);

			if (groupProperties == null) {
                throw new ExistingServiceException();
            }
			
			if(!groupProperties.getAttendsSet().getStudentGroups().isEmpty()){
				throw new InvalidSituationServiceException();
			} 
			
			IAttendsSet attendsSet = groupProperties.getAttendsSet();
			Iterator iterAttendInAttendsSet = attendsSet.getAttendInAttendsSet().iterator();
			while(iterAttendInAttendsSet.hasNext()){
				IAttendInAttendsSet attendInAttendsSet = (IAttendInAttendsSet)iterAttendInAttendsSet.next();
				IFrequenta frequenta = attendInAttendsSet.getAttend();
				frequenta.removeAttendInAttendsSet(attendInAttendsSet);
				persistentAttendInAttendsSet.delete(attendInAttendsSet);
			}
			
			Iterator iterGroupPropertiesExecutionCourse = groupProperties.getGroupPropertiesExecutionCourse().iterator();
			while(iterGroupPropertiesExecutionCourse.hasNext()){
				IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse = 
					(IGroupPropertiesExecutionCourse)(iterGroupPropertiesExecutionCourse.next());
				IExecutionCourse executionCourse = groupPropertiesExecutionCourse.getExecutionCourse();
				executionCourse.removeGroupPropertiesExecutionCourse(groupPropertiesExecutionCourse);
				persistentGroupPropertiesExecutionCourse.delete(groupPropertiesExecutionCourse);
				
			}
				persistentAttendsSet.deleteByOID(AttendsSet.class, groupProperties.getAttendsSet().getIdInternal());
				persistentGroupProperties.deleteByOID(GroupProperties.class, groupPropertiesId);
			

			result = Boolean.TRUE;
		} catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException("error.groupProperties.delete");
		}

		return result;
	}
}
