/*
 * Created on 4/Ago/2003
 *
 */
package ServidorAplicacao.Factory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.ISiteComponent;
import DataBeans.InfoSiteAllGroups;
import DataBeans.InfoSiteGroupsByShift;
import DataBeans.InfoSiteProjects;
import DataBeans.InfoSiteStudentGroup;
import DataBeans.InfoSiteStudentInformation;
import DataBeans.InfoStudentGroupAttend;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.GroupProperties;
import Dominio.IDisciplinaExecucao;
import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import Dominio.ITurno;
import Dominio.StudentGroup;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *
 */
public class GroupSiteComponentBuilder {
	
	private static GroupSiteComponentBuilder instance = null;

	public GroupSiteComponentBuilder() {
	}

	public static GroupSiteComponentBuilder getInstance() {
		if (instance == null) {
			instance = new GroupSiteComponentBuilder();
		}
		return instance;
	}
	public ISiteComponent getComponent(
		ISiteComponent component,
		Integer executionCourseCode,
		Integer studentGroupCode,
		Integer groupPropertiesCode)
		throws FenixServiceException {

		// updateSite(executionYearName, executionPeriodName, executionCourseName);

		if (component instanceof InfoSiteProjects) {
			return getInfoSiteProjectsName((InfoSiteProjects) component,executionCourseCode);
		}
		else 
			if (component instanceof InfoSiteAllGroups) {
				return getInfoSiteAllGroups((InfoSiteAllGroups) component,groupPropertiesCode);
			}
			else 
				if (component instanceof InfoSiteStudentGroup) {
					return getInfoSiteStudentGroup((InfoSiteStudentGroup) component,studentGroupCode);
				}
		return null;
	}
	
	
	
	/**
	* @param component
	* @param site
	* @return
	*/
		
	private ISiteComponent getInfoSiteProjectsName(
		InfoSiteProjects component,
		Integer executionCourseCode)
		throws FenixServiceException {
			
			
			List infoGroupPropertiesList = readExecutionCourseProjects(executionCourseCode);
			component.setInfoGroupPropertiesList(infoGroupPropertiesList);
			return component;
		}
	
	
	public List readExecutionCourseProjects(Integer executionCourseCode)throws ExcepcaoInexistente, FenixServiceException {
		
			List projects = null;
		
			try 
			{
				ISuportePersistente sp = SuportePersistenteOJB.getInstance();
				IDisciplinaExecucao executionCourse = (IDisciplinaExecucao)sp.getIDisciplinaExecucaoPersistente().readByOId(new DisciplinaExecucao(executionCourseCode),false);
				
				List executionCourseProjects = sp.getIPersistentGroupProperties().readAllGroupPropertiesByExecutionCourse(executionCourse);
			
				projects = new ArrayList();
				Iterator iterator = executionCourseProjects.iterator();
			
				while (iterator.hasNext()) {
					projects.add(Cloner.copyIGroupProperties2InfoGroupProperties((IGroupProperties)iterator.next()));

				}
			} catch (ExcepcaoPersistencia e) {
				e.printStackTrace();
				throw new FenixServiceException("error.impossibleReadExecutionCourseProjects");
			}
			
			return projects;
		}


	/**
	* @param component
	* @param site
	* @param groupPropertiesCode
	* @return
	*/
		
	private ISiteComponent getInfoSiteAllGroups(
		InfoSiteAllGroups component,
		Integer groupPropertiesCode)
		throws FenixServiceException {
			
			
		List infoSiteGroupsByShiftList = readAllShiftsAndGroupsByProject(groupPropertiesCode);
		component.setInfoSiteGroupsByShiftList(infoSiteGroupsByShiftList);
		return component;
	}
		
		
	public List readAllShiftsAndGroupsByProject(Integer groupPropertiesCode)
			throws ExcepcaoInexistente, FenixServiceException {

			
			List infoSiteGroupsByShiftList =null;

			try {
				ISuportePersistente sp = SuportePersistenteOJB.getInstance();
				IGroupProperties groupProperties =
					(IGroupProperties) sp
						.getIPersistentGroupProperties()
						.readByOId(
						new GroupProperties(groupPropertiesCode),
						false);
				List allStudentsGroup =
					sp
						.getIPersistentStudentGroup()
						.readAllStudentGroupByGroupProperties(
						groupProperties);
			
			
				ITurno shift = null;
				List shiftsInternalList = new ArrayList();
				Iterator iterator = allStudentsGroup.iterator();

				while (iterator.hasNext()) 
				{
					shift = ((IStudentGroup) iterator.next()).getShift();
					if(!shiftsInternalList.contains(shift))
						shiftsInternalList.add(shift);
				}
				Iterator iterator2 = shiftsInternalList.iterator();
				List studentGroupsList = null;
				InfoSiteGroupsByShift infoSiteGroupsByShift=null;
				shift = null;
				infoSiteGroupsByShiftList = new ArrayList(shiftsInternalList.size());
				while (iterator2.hasNext()) 
				{
					shift = (ITurno) iterator2.next();
					
					studentGroupsList = sp.getIPersistentStudentGroup().readAllStudentGroupByGroupPropertiesAndShift(groupProperties,shift);
					List infoStudentGroupList = new ArrayList(studentGroupsList.size());
					Iterator iter = studentGroupsList.iterator();
					while(iter.hasNext())
						infoStudentGroupList.add(Cloner.copyIStudentGroup2InfoStudentGroup((IStudentGroup) iter.next()));
					
					infoSiteGroupsByShift = new InfoSiteGroupsByShift();
					infoSiteGroupsByShift.setInfoStudentGroupsList(infoStudentGroupList);
					infoSiteGroupsByShift.setInfoShift(Cloner.copyIShift2InfoShift(shift));
					infoSiteGroupsByShiftList.add(infoSiteGroupsByShift);
					
				}
				
			} catch (ExcepcaoPersistencia e) {
				e.printStackTrace();
				throw new FenixServiceException("error.impossibleReadAllShiftsByProject");
			}
			System.out.println("-----------------------NO SERVICO-infoSiteGroupsByShiftList"+infoSiteGroupsByShiftList.size());
			System.out.println("NO SERVICO-infoSiteGroupsByShiftList"+infoSiteGroupsByShiftList);
			return infoSiteGroupsByShiftList;
		}
		
	/**
	* @param component
	* @param site
	* @param groupPropertiesCode
	* @return
	*/
		
	private ISiteComponent getInfoSiteStudentGroup(
		InfoSiteStudentGroup component,
		Integer studentGroupCode)
		throws FenixServiceException {
			
			
		List infoSiteStudents = readStudentGroupInformation(studentGroupCode);
		component.setInfoSiteStudentInformationList(infoSiteStudents);
		return component;
		}
			
	
		
		public List readStudentGroupInformation(Integer studentGroupCode) {
		
			List studentGroupAttendInformationList = null;
			try 
			{
				ISuportePersistente sp = SuportePersistenteOJB.getInstance();
				
				//IGroupProperties groupProperties =(IGroupProperties) sp.getIPersistentGroupProperties().readByOId(new GroupProperties(groupPropertiesCode), false);
				
				IStudentGroup studentGroup= (IStudentGroup) sp.getIPersistentStudentGroup().readByOId(new StudentGroup(studentGroupCode), false);
				 
				List studentGroupAttendList = sp.getIPersistentStudentGroupAttend().readAllByStudentGroup(studentGroup);
				
				studentGroupAttendInformationList = new ArrayList(studentGroupAttendList.size());
				Iterator iter = studentGroupAttendList.iterator();
				InfoSiteStudentInformation  infoSiteStudentInformation;
				InfoStudentGroupAttend infoStudentGroupAttend = null;
				int size = 0;
				while(iter.hasNext())
				{
					infoSiteStudentInformation = new InfoSiteStudentInformation();
					infoStudentGroupAttend = Cloner.copyIStudentGroupAttend2InfoStudentGroupAttend((IStudentGroupAttend) iter.next());
					infoSiteStudentInformation.setNumber(infoStudentGroupAttend.getInfoAttend().getAluno().getNumber());
					infoSiteStudentInformation.setName(infoStudentGroupAttend.getInfoAttend().getAluno().getInfoPerson().getNome());
					infoSiteStudentInformation.setEmail(infoStudentGroupAttend.getInfoAttend().getAluno().getInfoPerson().getEmail());
					studentGroupAttendInformationList = insertWithOrder(studentGroupAttendInformationList,infoSiteStudentInformation);
					
				}
			
				
			} catch (ExcepcaoPersistencia ex) {
					ex.printStackTrace();
			}
			return studentGroupAttendInformationList;
		}
		
		
		
	public static List insertWithOrder(List infoSiteStudentGroupsList,InfoSiteStudentInformation infoSiteStudentInformation)
	{
			int size = infoSiteStudentGroupsList.size();
			List finalList = null;
			int lastNumber;
			if(size==0)
			{
				infoSiteStudentGroupsList.add(infoSiteStudentInformation);
				return infoSiteStudentGroupsList;
			}
			if(size==1)
			{
				lastNumber =((InfoSiteStudentInformation)(infoSiteStudentGroupsList.get(0))).getNumber().intValue(); 
				if(lastNumber<infoSiteStudentInformation.getNumber().intValue())
				{
					infoSiteStudentGroupsList.add(infoSiteStudentInformation);
					return infoSiteStudentGroupsList;
				}
				else
				{	
					infoSiteStudentGroupsList.add(0,infoSiteStudentInformation);
					return infoSiteStudentGroupsList;
				}
			}
			else
				
			lastNumber =((InfoSiteStudentInformation)(infoSiteStudentGroupsList.get(size-1))).getNumber().intValue();
			
				
				if(lastNumber<infoSiteStudentInformation.getNumber().intValue())
				{
				
					infoSiteStudentGroupsList.add(infoSiteStudentInformation);
					 return infoSiteStudentGroupsList;
				}
				else
				{
					finalList = insertWithOrder(infoSiteStudentGroupsList.subList(0,size-2),infoSiteStudentInformation); 
					finalList.add(infoSiteStudentGroupsList.get(size-1));
					return finalList;
				}
	}

}
