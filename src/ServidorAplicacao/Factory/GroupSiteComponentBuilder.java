/*
 * Created on 4/Ago/2003
 *  
 */
package ServidorAplicacao.Factory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import DataBeans.ISiteComponent;
import DataBeans.InfoGroupProperties;
import DataBeans.InfoGroupPropertiesWithInfoGroupPropertiesExecutionCourseAccepted;
import DataBeans.InfoLesson;
import DataBeans.InfoShiftWithInfoLessons;
import DataBeans.InfoSiteGroupsByShift;
import DataBeans.InfoSiteProjects;
import DataBeans.InfoSiteShift;
import DataBeans.InfoSiteShiftsAndGroups;
import DataBeans.InfoSiteStudentGroup;
import DataBeans.InfoSiteStudentInformation;
import DataBeans.InfoStudentGroup;
import DataBeans.InfoStudentGroupAttend;
import DataBeans.InfoStudentGroupAttendWithAllUntilPersons;
import Dominio.ExecutionCourse;
import Dominio.GroupProperties;
import Dominio.IExecutionCourse;
import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import Dominio.ITurno;
import Dominio.StudentGroup;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
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

    public ISiteComponent getComponent(ISiteComponent component,
            Integer executionCourseCode, Integer groupPropertiesCode,
            Integer code) throws FenixServiceException {

        if (component instanceof InfoSiteProjects) {
            return getInfoSiteProjectsName((InfoSiteProjects) component,
                    executionCourseCode);
        } else if (component instanceof InfoSiteShiftsAndGroups) {
            return getInfoSiteShiftsAndGroups(
                    (InfoSiteShiftsAndGroups) component, groupPropertiesCode);

        } else if (component instanceof InfoSiteStudentGroup) {
            return getInfoSiteStudentGroupInformation(
                    (InfoSiteStudentGroup) component, code);
        }
        return null;
    }

    /**
     * @param component
     * @param site
     * @return
     */

    private ISiteComponent getInfoSiteProjectsName(InfoSiteProjects component,
            Integer executionCourseCode) throws FenixServiceException {

        List infoGroupPropertiesList = readExecutionCourseProjects(executionCourseCode);
        component.setInfoGroupPropertiesList(infoGroupPropertiesList);
        return component;
    }

    public List readExecutionCourseProjects(Integer executionCourseCode)
            throws ExcepcaoInexistente, FenixServiceException {

        List projects = null;
        IGroupProperties groupProperties; 

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IExecutionCourse executionCourse = (IExecutionCourse) sp
                    .getIPersistentExecutionCourse().readByOID(
                            ExecutionCourse.class, executionCourseCode);

            List executionCourseProjects = executionCourse.getGroupProperties();

            projects = new ArrayList();
            Iterator iterator = executionCourseProjects.iterator();

            while (iterator.hasNext()) {

                groupProperties = (IGroupProperties) iterator.next();
            	
            	InfoGroupProperties infoGroupProperties = InfoGroupPropertiesWithInfoGroupPropertiesExecutionCourseAccepted.newInfoFromDomain(groupProperties);
            	
            	projects.add(infoGroupProperties);

            }
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException(
                    "error.impossibleReadExecutionCourseProjects");
        }

        return projects;
    }

    
    /**
     * @param component
     * @param site
     * @param groupPropertiesCode
     * @return
     */

    private ISiteComponent getInfoSiteShiftsAndGroups(
            InfoSiteShiftsAndGroups component, Integer groupPropertiesCode)
            throws FenixServiceException {

        List infoSiteShiftsAndGroups = readShiftsAndGroups(groupPropertiesCode);
        component.setInfoSiteGroupsByShiftList(infoSiteShiftsAndGroups);
        return component;
    }

    public List readShiftsAndGroups(Integer groupPropertiesCode)
            throws ExcepcaoInexistente, FenixServiceException {

        List infoSiteShiftsAndGroups = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            ITurnoPersistente persistentShift = sp.getITurnoPersistente();
            IPersistentStudentGroup persistentStudentGroup = sp
                    .getIPersistentStudentGroup();

            IGroupProperties groupProperties = (IGroupProperties) sp
                    .getIPersistentGroupProperties().readByOID(
                            GroupProperties.class, groupPropertiesCode);
            
            if(groupProperties == null)return null;

            IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
			.getInstance();
            IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
			.getGroupEnrolmentStrategyInstance(groupProperties);
            
            if(strategy.checkHasShift(groupProperties)){
            	infoSiteShiftsAndGroups = new ArrayList();
            
            	Iterator iterExecutionCourses = groupProperties.getExecutionCourses().iterator();
            	List allShifts=new ArrayList();
            	while(iterExecutionCourses.hasNext()){
            
            		List someShifts = persistentShift.readByExecutionCourseAndType(
            				(IExecutionCourse)iterExecutionCourses.next(), groupProperties
							.getShiftType().getTipo());

            		allShifts.addAll(someShifts);
            	}
            
            	List allStudentsGroup = groupProperties.getAttendsSet().getStudentGroupsWithShift();
            	
            	if (allStudentsGroup.size() != 0) {

            		Iterator iterator = allStudentsGroup.iterator();
            		while (iterator.hasNext()) {
            			ITurno shift = ((IStudentGroup) iterator.next()).getShift();
            			if (!allShifts.contains(shift)) {
            				allShifts.add(shift);

            			}
            		}
            	}

            	if (allShifts.size() != 0) {
            		Iterator iter = allShifts.iterator();
            		infoSiteShiftsAndGroups = new ArrayList();
            		InfoSiteGroupsByShift infoSiteGroupsByShift = null;
            		InfoSiteShift infoSiteShift = null;

            		while (iter.hasNext()) {
           			ITurno shift = (ITurno) iter.next();
    					List allStudentGroups = persistentStudentGroup
						.readAllStudentGroupByAttendsSetAndShift(groupProperties.getAttendsSet(), shift);

    					infoSiteShift = new InfoSiteShift();
    					infoSiteShift.setInfoShift(InfoShiftWithInfoLessons.newInfoFromDomain(shift));
    					List infoLessons = infoSiteShift.getInfoShift().getInfoLessons();
            			
    					ComparatorChain chainComparator = new ComparatorChain();
            			chainComparator.addComparator(new BeanComparator(
            			"diaSemana.diaSemana"));
            			chainComparator.addComparator(new BeanComparator(
            			"inicio"));
            			chainComparator.addComparator(new BeanComparator(
            			"fim"));
            			chainComparator.addComparator(new BeanComparator(
            			"infoSala.nome"));
                   
            			Collections.sort(infoLessons, chainComparator);
            			
            			Iterator iterLessons =  infoLessons.iterator();
    				    StringBuffer weekDay = new StringBuffer();
    				    StringBuffer beginDay = new StringBuffer();
    				    StringBuffer endDay = new StringBuffer();
    				    StringBuffer room = new StringBuffer();
    				    while(iterLessons.hasNext()){
    				    	InfoLesson infoLesson = (InfoLesson)iterLessons.next();
    				    	weekDay.append(infoLesson.getDiaSemana().getDiaSemana());
    				    	beginDay.append(infoLesson.getInicio().getTimeInMillis());
    				    	endDay.append(infoLesson.getFim().getTimeInMillis());
    				    	room.append(infoLesson.getInfoSala().getNome());
    				    }
    				    
    				    infoSiteShift.setOrderByWeekDay(weekDay.toString());
    				    infoSiteShift.setOrderByBeginHour(beginDay.toString());
    				    infoSiteShift.setOrderByEndHour(endDay.toString());
    				    infoSiteShift.setOrderByRoom(room.toString());
                    
            			infoSiteShift.setNrOfGroups(new Integer(allStudentGroups
            					.size()));

            			infoSiteGroupsByShift = new InfoSiteGroupsByShift();
            			infoSiteGroupsByShift.setInfoSiteShift(infoSiteShift);

            			List infoSiteStudentGroupsList = null;
            			if (allStudentGroups.size() != 0) {
            				infoSiteStudentGroupsList = new ArrayList();
            				Iterator iterGroups = allStudentGroups.iterator();

            				while (iterGroups.hasNext()) {
            					InfoSiteStudentGroup infoSiteStudentGroup = new InfoSiteStudentGroup();
    							InfoStudentGroup infoStudentGroup = new InfoStudentGroup();
    							infoStudentGroup = InfoStudentGroup.newInfoFromDomain((IStudentGroup) iterGroups.next());
    							infoSiteStudentGroup
								.setInfoStudentGroup(infoStudentGroup);
    							infoSiteStudentGroupsList.add(infoSiteStudentGroup);
            				}
            				Collections.sort(infoSiteStudentGroupsList,
            						new BeanComparator(
            						"infoStudentGroup.groupNumber"));

            			}

            			infoSiteGroupsByShift
						.setInfoSiteStudentGroupsList(infoSiteStudentGroupsList);

            			infoSiteShiftsAndGroups.add(infoSiteGroupsByShift);
            		}
            		/* Sort the list of shifts */

            		ComparatorChain chainComparator = new ComparatorChain();
            		chainComparator.addComparator(new BeanComparator(
            		"infoSiteShift.infoShift.tipo"));
            		chainComparator.addComparator(new BeanComparator(
    				"infoSiteShift.orderByWeekDay"));
    				chainComparator.addComparator(new BeanComparator(
    				"infoSiteShift.orderByBeginHour"));
    				chainComparator.addComparator(new BeanComparator(
    				"infoSiteShift.orderByEndHour"));
    				chainComparator.addComparator(new BeanComparator(
    				"infoSiteShift.orderByRoom"));
            		
            		Collections.sort(infoSiteShiftsAndGroups, chainComparator);
            	}
            	
            	
            	 if(!groupProperties.getAttendsSet().getStudentGroupsWithoutShift().isEmpty()){
                   	InfoSiteGroupsByShift infoSiteGroupsByShift = null;
                   	InfoSiteShift infoSiteShift = new InfoSiteShift();

                   infoSiteGroupsByShift = new InfoSiteGroupsByShift();                   
                   List allStudentGroups = groupProperties.getAttendsSet().getStudentGroupsWithoutShift();

                   infoSiteShift.setNrOfGroups(new Integer(allStudentGroups
    					.size()));         
                   
                   infoSiteGroupsByShift.setInfoSiteShift(infoSiteShift);
                   
                   
                   List infoSiteStudentGroupsList = null;
                   if (allStudentGroups.size() != 0) {
                   	infoSiteStudentGroupsList = new ArrayList();
                   	Iterator iterGroups = allStudentGroups.iterator();

                    while (iterGroups.hasNext()) {
                        InfoSiteStudentGroup infoSiteStudentGroup = new InfoSiteStudentGroup();
                        InfoStudentGroup infoStudentGroup = new InfoStudentGroup();
                        infoStudentGroup = InfoStudentGroup.newInfoFromDomain((IStudentGroup) iterGroups.next());
                        infoSiteStudentGroup
                                .setInfoStudentGroup(infoStudentGroup);
                        infoSiteStudentGroupsList.add(infoSiteStudentGroup);
                    }
                   }
                   
                       
                   infoSiteGroupsByShift
    			   .setInfoSiteStudentGroupsList(infoSiteStudentGroupsList);

                   infoSiteShiftsAndGroups.add(infoSiteGroupsByShift);
                   
                	}
            	
            }
            
            else{
            	
            	infoSiteShiftsAndGroups = new ArrayList();
            	
    			if(!groupProperties.getAttendsSet().getStudentGroupsWithShift().isEmpty()){
    				
    				List allShifts=new ArrayList();
    				List allStudentsGroup = groupProperties.getAttendsSet().getStudentGroupsWithShift();
    				if (allStudentsGroup.size() != 0) {
    					Iterator iterator = allStudentsGroup.iterator();
    					while (iterator.hasNext()) {
    						ITurno shift = ((IStudentGroup) iterator.next()).getShift();
    						if (!allShifts.contains(shift)) {
    							allShifts.add(shift);
    						}
    					}
    				}

    				if (allShifts.size() != 0) {
    					Iterator iter = allShifts.iterator();
    					InfoSiteGroupsByShift infoSiteGroupsByShiftAux = null;
    					InfoSiteShift infoSiteShiftAux = null;

    					while (iter.hasNext()) {
    						ITurno shift = (ITurno) iter.next();
    						List allStudentGroupsAux = persistentStudentGroup
							.readAllStudentGroupByAttendsSetAndShift(groupProperties.getAttendsSet(), shift);
    						infoSiteShiftAux = new InfoSiteShift();
    						infoSiteShiftAux.setInfoShift(InfoShiftWithInfoLessons.newInfoFromDomain(shift));
    						List infoLessons = infoSiteShiftAux.getInfoShift().getInfoLessons();
    						ComparatorChain chainComparator = new ComparatorChain();
    						chainComparator.addComparator(new BeanComparator("diaSemana.diaSemana"));
    						chainComparator.addComparator(new BeanComparator("inicio"));
    						chainComparator.addComparator(new BeanComparator("fim"));
    						chainComparator.addComparator(new BeanComparator("infoSala.nome"));
    						Collections.sort(infoLessons, chainComparator);
    		          
    						Iterator iterLessons =  infoLessons.iterator();
        				    StringBuffer weekDay = new StringBuffer();
        				    StringBuffer beginDay = new StringBuffer();
        				    StringBuffer endDay = new StringBuffer();
        				    StringBuffer room = new StringBuffer();
        				    while(iterLessons.hasNext()){
        				    	InfoLesson infoLesson = (InfoLesson)iterLessons.next();
        				    	weekDay.append(infoLesson.getDiaSemana().getDiaSemana());
        				    	beginDay.append(infoLesson.getInicio().getTimeInMillis());
        				    	endDay.append(infoLesson.getFim().getTimeInMillis());
        				    	room.append(infoLesson.getInfoSala().getNome());
        				    }
        				    
        				    infoSiteShiftAux.setOrderByWeekDay(weekDay.toString());
        				    infoSiteShiftAux.setOrderByBeginHour(beginDay.toString());
        				    infoSiteShiftAux.setOrderByEndHour(endDay.toString());
        				    infoSiteShiftAux.setOrderByRoom(room.toString());
    						
    						infoSiteShiftAux.setNrOfGroups(new Integer(allStudentGroupsAux
    		    					.size())); 
    						
    						infoSiteGroupsByShiftAux = new InfoSiteGroupsByShift();
    						infoSiteGroupsByShiftAux.setInfoSiteShift(infoSiteShiftAux);
    						List infoSiteStudentGroupsListAux = null;
    						if (allStudentGroupsAux.size() != 0) {
    							infoSiteStudentGroupsListAux = new ArrayList();
    							Iterator iterGroups = allStudentGroupsAux.iterator();
    							while (iterGroups.hasNext()) {
    								InfoSiteStudentGroup infoSiteStudentGroup = new InfoSiteStudentGroup();
    								InfoStudentGroup infoStudentGroup = new InfoStudentGroup();
    								infoStudentGroup = InfoStudentGroup.newInfoFromDomain((IStudentGroup) iterGroups.next());
    								infoSiteStudentGroup
									.setInfoStudentGroup(infoStudentGroup);
    								infoSiteStudentGroupsListAux.add(infoSiteStudentGroup);
    							}
    							Collections.sort(infoSiteStudentGroupsListAux,
    									new BeanComparator(
    									"infoStudentGroup.groupNumber"));

    						}
    						infoSiteGroupsByShiftAux
							.setInfoSiteStudentGroupsList(infoSiteStudentGroupsListAux);

    						infoSiteShiftsAndGroups.add(infoSiteGroupsByShiftAux);
    					}
    		    	
    		        /* Sort the list of shifts */

    					ComparatorChain chainComparator = new ComparatorChain();
    					chainComparator.addComparator(new BeanComparator(
    					"infoSiteShift.infoShift.tipo"));
    					chainComparator.addComparator(new BeanComparator(
        				"infoSiteShift.orderByWeekDay"));
        				chainComparator.addComparator(new BeanComparator(
        				"infoSiteShift.orderByBeginHour"));
        				chainComparator.addComparator(new BeanComparator(
        				"infoSiteShift.orderByEndHour"));
        				chainComparator.addComparator(new BeanComparator(
        				"infoSiteShift.orderByRoom"));

        				Collections.sort(infoSiteShiftsAndGroups, chainComparator);
    				}
    			}
            	
                InfoSiteGroupsByShift infoSiteGroupsByShift = null;
                InfoSiteShift infoSiteShift = new InfoSiteShift();

                infoSiteGroupsByShift = new InfoSiteGroupsByShift();
                    
                List allStudentGroups = groupProperties.getAttendsSet().getStudentGroupsWithoutShift();

                infoSiteShift.setNrOfGroups(new Integer(allStudentGroups
    					.size()));           
                
                infoSiteGroupsByShift.setInfoSiteShift(infoSiteShift);
                
                    List infoSiteStudentGroupsList = null;
                    if (allStudentGroups.size() != 0) {
                        infoSiteStudentGroupsList = new ArrayList();
                        Iterator iterGroups = allStudentGroups.iterator();

                        while (iterGroups.hasNext()) {
                            InfoSiteStudentGroup infoSiteStudentGroup = new InfoSiteStudentGroup();
                            InfoStudentGroup infoStudentGroup = new InfoStudentGroup();
                            infoStudentGroup = InfoStudentGroup.newInfoFromDomain((IStudentGroup) iterGroups.next());
                            infoSiteStudentGroup
                                    .setInfoStudentGroup(infoStudentGroup);
                            infoSiteStudentGroupsList.add(infoSiteStudentGroup);

                        }
                    }
                    
                    infoSiteGroupsByShift
                    .setInfoSiteStudentGroupsList(infoSiteStudentGroupsList);

            infoSiteShiftsAndGroups.add(infoSiteGroupsByShift);
            }
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException("error.impossibleReadProjectShifts");
        }

        return infoSiteShiftsAndGroups;
    }

    /**
     * @param component
     * @param site
     * @param groupPropertiesCode
     * @return
     */

    private ISiteComponent getInfoSiteStudentGroupInformation(
            InfoSiteStudentGroup component, Integer studentGroupCode)
            throws FenixServiceException {

        List infoSiteStudents = readStudentGroupInformation(studentGroupCode);
        component.setInfoSiteStudentInformationList(infoSiteStudents);
        return component;
    }

    public List readStudentGroupInformation(Integer studentGroupCode)
            throws FenixServiceException {

        List studentGroupAttendInformationList = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IStudentGroup studentGroup = (IStudentGroup) sp
                    .getIPersistentStudentGroup().readByOID(StudentGroup.class,
                            studentGroupCode);

            List studentGroupAttendList = sp.getIPersistentStudentGroupAttend()
                    .readAllByStudentGroup(studentGroup);

            studentGroupAttendInformationList = new ArrayList(
                    studentGroupAttendList.size());
            Iterator iter = studentGroupAttendList.iterator();
            InfoSiteStudentInformation infoSiteStudentInformation = null;
            InfoStudentGroupAttend infoStudentGroupAttend = null;

            while (iter.hasNext()) {

            	 infoSiteStudentInformation = new InfoSiteStudentInformation();

                 infoStudentGroupAttend = InfoStudentGroupAttendWithAllUntilPersons.newInfoFromDomain((IStudentGroupAttend) iter.next());
                     
                 infoSiteStudentInformation.setNumber(infoStudentGroupAttend
                         .getInfoAttend().getAluno().getNumber());

                 infoSiteStudentInformation.setName(infoStudentGroupAttend
                         .getInfoAttend().getAluno().getInfoPerson().getNome());

                 infoSiteStudentInformation.setEmail(infoStudentGroupAttend
                         .getInfoAttend().getAluno().getInfoPerson().getEmail());

                 infoSiteStudentInformation.setUsername(infoStudentGroupAttend
                         .getInfoAttend().getAluno().getInfoPerson().getUsername());

                 studentGroupAttendInformationList.add(infoSiteStudentInformation);

            }
           
            Collections.sort(studentGroupAttendInformationList,
                    new BeanComparator("number"));

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
            throw new FenixServiceException(
                    "error.impossibleReadStudentGroupInformation");
        }
        return studentGroupAttendInformationList;
    }

}