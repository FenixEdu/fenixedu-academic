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
import DataBeans.InfoShiftWithInfoLessons;
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
import Dominio.IShift;
import Dominio.Shift;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author joaosa & rmalo
 *  
 */
public class ReadStudentsAndGroupsByShiftID implements IService
{

	public ReadStudentsAndGroupsByShiftID()
	{

	}

	public InfoSiteStudentsAndGroups run(Integer groupPropertiesId,Integer shiftId)
			throws FenixServiceException
	{
		InfoSiteStudentsAndGroups infoSiteStudentsAndGroups = new InfoSiteStudentsAndGroups();
		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentGroupProperties persistentGroupProperties = sp.getIPersistentGroupProperties();
			ITurnoPersistente persistentShift =  sp.getITurnoPersistente();

			IGroupProperties groupProperties = (IGroupProperties) persistentGroupProperties
            .readByOID(GroupProperties.class, groupPropertiesId);
			IShift shift = (IShift) persistentShift
            .readByOID(Shift.class, shiftId);
			
			
			if(groupProperties == null){
				throw new ExistingServiceException();
			}
			
			infoSiteStudentsAndGroups.setInfoShift(InfoShiftWithInfoLessons.newInfoFromDomain(shift));
			List infoSiteStudentsAndGroupsList = new ArrayList();
			List studentGroups = getStudentGroupsByShiftAndGroupProperties(groupProperties, shift);
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
	
	private List getStudentGroupsByShiftAndGroupProperties(IGroupProperties groupProperties,IShift shift){
		List result = new ArrayList();
		List studentGroups = groupProperties.getAttendsSet().getStudentGroupsWithShift();
		Iterator iter = studentGroups.iterator();
		while(iter.hasNext()){
			IStudentGroup sg=(IStudentGroup)iter.next();
			if(sg.getShift().equals(shift)){
				result.add(sg);
			}
		} 
		return result;
	}
}
			