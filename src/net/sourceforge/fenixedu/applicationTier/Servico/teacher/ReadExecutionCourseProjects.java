/*
 * Created on 29/Set/2003, 9:16:22
 * 
 * By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteProjects;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IGrouping;
import net.sourceforge.fenixedu.domain.IExportGrouping;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 29/Set/2003, 9:16:22
 * 
 */
// by gedl |AT| rnl |DOT| ist |DOT| utl |DOT| pt on 29/Set/2003
// this service reads ALL the execution course's projects
// to read the projects which has opened enrollments use the same service in
// student package net.sourceforge.fenixedu.(ReadExecutionCourseProjects)
public class ReadExecutionCourseProjects implements IService {

    public ISiteComponent run(Integer executionCourseCode) throws FenixServiceException {

        InfoSiteProjects infoSiteProjects = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IExecutionCourse executionCourse = (IExecutionCourse) sp.getIPersistentExecutionCourse()
                    .readByOID(ExecutionCourse.class, executionCourseCode);

            List executionCourseProjects = new ArrayList();
            List groupPropertiesExecutionCourseList = executionCourse.getExportGroupings();
            Iterator iterGroupPropertiesExecutionCourseList = groupPropertiesExecutionCourseList
                    .iterator();
            while (iterGroupPropertiesExecutionCourseList.hasNext()) {
                IExportGrouping groupPropertiesExecutionCourse = (IExportGrouping) iterGroupPropertiesExecutionCourseList
                        .next();
                if (groupPropertiesExecutionCourse.getProposalState().getState().intValue() == 1
                        || groupPropertiesExecutionCourse.getProposalState().getState().intValue() == 2) {
                    executionCourseProjects.add(groupPropertiesExecutionCourse.getGrouping());
                }
            }

            if (executionCourseProjects.size() != 0) {
                infoSiteProjects = new InfoSiteProjects();

                List infoGroupPropertiesList = new ArrayList();
                Iterator iterator = executionCourseProjects.iterator();

                while (iterator.hasNext()) {
                    IGrouping groupProperties = (IGrouping) iterator.next();

                    InfoGrouping infoGroupProperties = InfoGrouping.newInfoFromDomain(groupProperties);
                    infoGroupPropertiesList.add(infoGroupProperties);
                }

                infoSiteProjects.setInfoGroupPropertiesList(infoGroupPropertiesList);
                InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse
                        .newInfoFromDomain(executionCourse);
                infoSiteProjects.setInfoExecutionCourse(infoExecutionCourse);
            }
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException("error.impossibleReadExecutionCourseProjects");
        }
        return infoSiteProjects;
    }

}