/*
 * Created on 26/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoGroupProperties;
import net.sourceforge.fenixedu.dataTransferObject.InfoGroupPropertiesWithInfoGroupPropertiesExecutionCourseAccepted;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteProjects;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author asnr and scpo
 *  
 */
public class ReadExecutionCourseProjects implements IServico {

    private static ReadExecutionCourseProjects _servico = new ReadExecutionCourseProjects();

    /**
     * The singleton access method of this class.
     */
    public static ReadExecutionCourseProjects getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private ReadExecutionCourseProjects() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "ReadExecutionCourseProjects";
    }

    public ISiteComponent run(Integer executionCourseCode, String userName)
            throws FenixServiceException {

        InfoSiteProjects infoSiteProjects = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IExecutionCourse executionCourse = (IExecutionCourse) sp
                    .getIPersistentExecutionCourse().readByOID(
                            ExecutionCourse.class, executionCourseCode);
            
            List executionCourseProjects = executionCourse.getGroupProperties();

            if (executionCourseProjects.size() != 0) {
                infoSiteProjects = new InfoSiteProjects();
                
                List infoGroupPropertiesList = new ArrayList();
                Iterator iterator = executionCourseProjects.iterator();

                while (iterator.hasNext()) {
                    IGroupProperties groupProperties = (IGroupProperties) iterator
                            .next();
                    
                    IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
					.getInstance();
                    IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
					.getGroupEnrolmentStrategyInstance(groupProperties);
                	
                   
                    if (strategy.checkEnrolmentDate(groupProperties, Calendar.getInstance()) 
                    		&& strategy.checkStudentInAttendsSet(groupProperties,userName)){
                   
                    InfoGroupProperties infoGroupProperties = InfoGroupPropertiesWithInfoGroupPropertiesExecutionCourseAccepted.newInfoFromDomain(groupProperties);
                    infoGroupPropertiesList.add(infoGroupProperties);
                    }

                }

                infoSiteProjects
                        .setInfoGroupPropertiesList(infoGroupPropertiesList);
            }
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException(
                    "error.impossibleReadExecutionCourseProjects");
        }
        return infoSiteProjects;
    }
}