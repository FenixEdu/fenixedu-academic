/*
 * Created on 29/Set/2003, 9:16:22
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.ISiteComponent;
import DataBeans.InfoSiteProjects;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IGroupProperties;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 29/Set/2003, 9:16:22
 * 
 */
//by gedl |AT| rnl |DOT| ist |DOT| utl |DOT| pt on 29/Set/2003
//this service reads ALL the execution course's projects
//to read the projects which has opened enrollments use the same service in student package (ReadExecutionCourseProjects)
public class ReadExecutionCourseProjects implements IServico
{
    private static ReadExecutionCourseProjects _servico = new ReadExecutionCourseProjects();
        /**
         * The singleton access method of this class.
         **/
        public static ReadExecutionCourseProjects getService() {
            return _servico;
        }

        /**
         * The actor of this class.
         **/
        private ReadExecutionCourseProjects() {
        }

        /**
         * Devolve o nome do servico
         **/
        public final String getNome() {
            return "teacher.ReadExecutionCourseProjects";
        }

        public ISiteComponent run(Integer executionCourseCode) throws FenixServiceException {

            InfoSiteProjects infoSiteProjects = null;

            try {
                ISuportePersistente sp = SuportePersistenteOJB.getInstance();
                IExecutionCourse executionCourse =
                    (IExecutionCourse) sp.getIPersistentExecutionCourse().readByOId(
                        new ExecutionCourse(executionCourseCode),
                        false);

                List executionCourseProjects = sp.getIPersistentGroupProperties().readAllGroupPropertiesByExecutionCourse(executionCourse);

                if (executionCourseProjects.size() != 0) 
                {
                    infoSiteProjects = new InfoSiteProjects();
                    IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
                    IGroupEnrolmentStrategy strategy = null;

                    List infoGroupPropertiesList = new ArrayList();
                    Iterator iterator = executionCourseProjects.iterator();

                    while (iterator.hasNext()) {
                        IGroupProperties groupProperties = (IGroupProperties) iterator.next();
                        strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(groupProperties);
                            infoGroupPropertiesList.add(Cloner.copyIGroupProperties2InfoGroupProperties(groupProperties));

                    }
                
                    infoSiteProjects.setInfoGroupPropertiesList(infoGroupPropertiesList);
                }
            } catch (ExcepcaoPersistencia e) {
                e.printStackTrace();
                throw new FenixServiceException("error.impossibleReadExecutionCourseProjects");
            }
            return infoSiteProjects;
        }

}
