/*
 * Created on 8/Jan/2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftWithInfoLessons;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentAndGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentInformation;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentsAndGroups;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentGroupAttend;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentGroupAttendWithAllUntilPersons;
import net.sourceforge.fenixedu.domain.GroupProperties;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.IStudentGroupAttend;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGroupProperties;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

import org.apache.commons.beanutils.BeanComparator;

import pt.utl.ist.berserk.logic.serviceManager.IService;

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
			ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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
			