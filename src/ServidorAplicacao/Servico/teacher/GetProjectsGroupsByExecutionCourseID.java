/*
 * Created on 10/Set/2003, 20:47:24
 * 
 * By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoGroupProjectStudents;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentGroup;
import DataBeans.InfoStudentGroupWithAll;
import Dominio.IExecutionCourse;
import Dominio.IAttends;
import Dominio.IGroupProperties;
import Dominio.IStudent;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import ServidorAplicacao.IServico;
import ServidorApresentacao.Action.Seminaries.Exceptions.BDException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 10/Set/2003, 20:47:24
 *  
 */
public class GetProjectsGroupsByExecutionCourseID implements IServico
{
    private static GetProjectsGroupsByExecutionCourseID service = new GetProjectsGroupsByExecutionCourseID();

    /**
     * The singleton access method of this class.
     */
    public static GetProjectsGroupsByExecutionCourseID getService()
    {
        return service;
    }

    /**
     * The actor of this class.
     */
    private GetProjectsGroupsByExecutionCourseID() {
    }

    /**
     * Returns The Service Name
     */
    public final String getNome()
    {
        return "teacher.GetProjectsGroupsByExecutionCourseID";
    }

    public List run( Integer id ) throws BDException
    {
        List infosGroupProjectStudents = new LinkedList();
        try
        {
            ISuportePersistente persistenceSupport = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = persistenceSupport
                            .getIPersistentExecutionCourse();
            IPersistentStudentGroupAttend persistentGroupAttend = persistenceSupport
                            .getIPersistentStudentGroupAttend();
            IPersistentStudentGroup persistentStudentGroup = persistenceSupport
                            .getIPersistentStudentGroup();
            List ids = new ArrayList();
            ids.add(id);
            List projects = ((IExecutionCourse)persistentExecutionCourse.readByExecutionCourseIds(ids).get(0)).getGroupProperties();

            for (Iterator projectIterator = projects.iterator(); projectIterator.hasNext(); )
            {
                IGroupProperties project = (IGroupProperties) projectIterator.next();
                List projectGroups = persistentStudentGroup
                                .readAllStudentGroupByAttendsSet(project.getAttendsSet());

                for (Iterator groupsIterator = projectGroups.iterator(); groupsIterator.hasNext(); )
                {
                    IStudentGroup group = (IStudentGroup) groupsIterator.next();

                    List attendacies = persistentGroupAttend.readAllByStudentGroup(group);

                    List infoStudents = (List) CollectionUtils.collect(attendacies, new Transformer()
                    {

                        public Object transform( Object input )
                        {
                            IStudentGroupAttend studentGroupAttend = (IStudentGroupAttend) input;
                            IAttends attendacy = studentGroupAttend.getAttend();
                            IStudent student = attendacy.getAluno();
                            //CLONER
                            //InfoStudent infoStudent = Cloner.copyIStudent2InfoStudent(student);
                            InfoStudent infoStudent = InfoStudent.newInfoFromDomain(student);
                            return infoStudent;
                        }
                    });
                    //CLONER
                    //InfoStudentGroup infoStudentGroup = Cloner.copyIStudentGroup2InfoStudentGroup(group);
                    InfoStudentGroup infoStudentGroup = InfoStudentGroupWithAll.newInfoFromDomain(group);
                    
                    InfoGroupProjectStudents info = new InfoGroupProjectStudents();
                    info.setStudentList(infoStudents);
                    info.setStudentGroup(infoStudentGroup);
                    infosGroupProjectStudents.add(info);
                }
            }

            return infosGroupProjectStudents;
        }
        catch (ExcepcaoPersistencia ex)
        {
            throw new BDException(
                            "Got an error while trying to read a execution course projects' groups", ex);
        }
    }
}
