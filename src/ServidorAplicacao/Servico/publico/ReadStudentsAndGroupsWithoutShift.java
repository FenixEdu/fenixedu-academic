/*
 * Created on 8/Jan/2005
 *
 */
package ServidorAplicacao.Servico.publico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoSiteStudentAndGroup;
import DataBeans.InfoSiteStudentInformation;
import DataBeans.InfoSiteStudentsAndGroups;
import DataBeans.InfoStudentGroup;
import DataBeans.InfoStudentGroupAttend;
import DataBeans.InfoStudentGroupAttendWithAllUntilPersons;
import Dominio.GroupProperties;
import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author joaosa & rmalo
 *  
 */
public class ReadStudentsAndGroupsWithoutShift implements IService
{

	public ReadStudentsAndGroupsWithoutShift()
	{

	}

	public InfoSiteStudentsAndGroups run(Integer groupPropertiesId)
			throws FenixServiceException
	{
		InfoSiteStudentsAndGroups infoSiteStudentsAndGroups = new InfoSiteStudentsAndGroups();
		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentGroupProperties persistentGroupProperties = sp.getIPersistentGroupProperties();

			IGroupProperties groupProperties = (IGroupProperties) persistentGroupProperties
            .readByOID(GroupProperties.class, groupPropertiesId);
			
			
			if(groupProperties == null){
				throw new ExistingServiceException();
			}
			
			
			List infoSiteStudentsAndGroupsList = new ArrayList();
			List studentGroups = getStudentGroupsWithoutShiftByGroupProperties(groupProperties);
			Iterator iterStudentGroups = studentGroups.iterator();
			while(iterStudentGroups.hasNext()){
				
				List studentGroupAttendList = new ArrayList();
				IStudentGroup studentGroup = (IStudentGroup)iterStudentGroups.next();
				
				
				studentGroupAttendList = sp.getIPersistentStudentGroupAttend()
                .readAllByStudentGroup(studentGroup);
				
				List infoSiteInformationList = new ArrayList();
				Iterator iterStudentGroupAttendList = studentGroupAttendList.iterator();
				InfoSiteStudentInformation infoSiteStudentInformation = null;
				InfoSiteStudentAndGroup infoSiteStudentAndGroup = null;
				InfoStudentGroupAttend infoStudentGroupAttend = null;
				
				while(iterStudentGroupAttendList.hasNext()){
					infoSiteStudentInformation = new InfoSiteStudentInformation();
					infoSiteStudentAndGroup = new InfoSiteStudentAndGroup();
					
					IStudentGroupAttend studentGroupAttend = (IStudentGroupAttend)iterStudentGroupAttendList.next();
					infoSiteStudentAndGroup.setInfoStudentGroup(InfoStudentGroup.newInfoFromDomain(studentGroup));	
					infoStudentGroupAttend = InfoStudentGroupAttendWithAllUntilPersons.newInfoFromDomain(studentGroupAttend);
					
					infoSiteStudentInformation.setNumber(infoStudentGroupAttend
							.getInfoAttend().getAluno().getNumber());

					infoSiteStudentInformation.setName(infoStudentGroupAttend
							.getInfoAttend().getAluno().getInfoPerson().getNome());

					infoSiteStudentInformation.setEmail(infoStudentGroupAttend
							.getInfoAttend().getAluno().getInfoPerson().getEmail());

					infoSiteStudentAndGroup.setInfoSiteStudentInformation(infoSiteStudentInformation);
				
					infoSiteStudentsAndGroupsList.add(infoSiteStudentAndGroup);
				}
			}
			Collections.sort(infoSiteStudentsAndGroupsList, new BeanComparator(
            "infoSiteStudentInformation.number"));
			
			infoSiteStudentsAndGroups.setInfoSiteStudentsAndGroupsList(infoSiteStudentsAndGroupsList);
		}
	 catch (ExcepcaoPersistencia e){
	 	e.printStackTrace();
		throw new FenixServiceException();
	 }
	return infoSiteStudentsAndGroups;
	}
	
	private List getStudentGroupsWithoutShiftByGroupProperties(IGroupProperties groupProperties){
		List result = new ArrayList();
		List studentGroups = groupProperties.getAttendsSet().getStudentGroupsWithoutShift();
		result.addAll(studentGroups);
		return result;
	}
}
