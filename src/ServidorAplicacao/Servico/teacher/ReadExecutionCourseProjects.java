/*
 * Created on 29/Set/2003, 9:16:22
 * 
 * By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.ISiteComponent;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoGroupProperties;
import DataBeans.InfoSiteProjects;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IGroupProperties;
import Dominio.IGroupPropertiesExecutionCourse;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
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
//to read the projects which has opened enrollments use the same service in
// student package (ReadExecutionCourseProjects)
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
        return "teacher.ReadExecutionCourseProjects";
    }

    public ISiteComponent run(Integer executionCourseCode)
            throws FenixServiceException {

        InfoSiteProjects infoSiteProjects = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IExecutionCourse executionCourse = (IExecutionCourse) sp
                    .getIPersistentExecutionCourse().readByOID(
                            ExecutionCourse.class, executionCourseCode);

            List executionCourseProjects = new ArrayList();
            List groupPropertiesExecutionCourseList = executionCourse.getGroupPropertiesExecutionCourse();
            Iterator iterGroupPropertiesExecutionCourseList = groupPropertiesExecutionCourseList.iterator(); 
            while(iterGroupPropertiesExecutionCourseList.hasNext()){
            	IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse = (IGroupPropertiesExecutionCourse)iterGroupPropertiesExecutionCourseList.next();
            	if(groupPropertiesExecutionCourse.getProposalState().getState().intValue() == 1
            			|| groupPropertiesExecutionCourse.getProposalState().getState().intValue() == 2){
            		executionCourseProjects.add(groupPropertiesExecutionCourse.getGroupProperties());
            	}
            }

            
            if (executionCourseProjects.size() != 0) {
                infoSiteProjects = new InfoSiteProjects();

                List infoGroupPropertiesList = new ArrayList();
                Iterator iterator = executionCourseProjects.iterator();

                while (iterator.hasNext()) {
                    IGroupProperties groupProperties = (IGroupProperties) iterator
                            .next();
                    //CLONER
                    //infoGroupPropertiesList
                    //.add(Cloner
                    //.copyIGroupProperties2InfoGroupProperties(groupProperties));

                    InfoGroupProperties infoGroupProperties = InfoGroupProperties
                            .newInfoFromDomain(groupProperties);
                    infoGroupPropertiesList.add(infoGroupProperties);
                }

                infoSiteProjects
                        .setInfoGroupPropertiesList(infoGroupPropertiesList);
                InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse
                .newInfoFromDomain(executionCourse);
                infoSiteProjects.setInfoExecutionCourse(infoExecutionCourse);
            }
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException(
                    "error.impossibleReadExecutionCourseProjects");
        }
        return infoSiteProjects;
    }

}