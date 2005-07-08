/*
 * Created on 10/Set/2003, 20:47:24
 * 
 * By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoGroupProjectStudents;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentGroupWithAll;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.IStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 10/Set/2003, 20:47:24
 * 
 */
public class GetProjectsGroupsByExecutionCourseID implements IService {
    
    public List run(Integer id) throws BDException, ExcepcaoPersistencia {
        List infosGroupProjectStudents = new LinkedList();

        ISuportePersistente persistenceSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        IPersistentExecutionCourse persistentExecutionCourse = persistenceSupport
                .getIPersistentExecutionCourse();
        List ids = new ArrayList();
        ids.add(id);
        List projects = ((IExecutionCourse) persistentExecutionCourse.readByExecutionCourseIds(ids).get(
                0)).getGroupProperties();

        for (Iterator projectIterator = projects.iterator(); projectIterator.hasNext();) {
            IGroupProperties project = (IGroupProperties) projectIterator.next();
            List projectGroups = project.getAttendsSet().getStudentGroups();
            for (Iterator groupsIterator = projectGroups.iterator(); groupsIterator.hasNext();) {
                IStudentGroup group = (IStudentGroup) groupsIterator.next();

                List attendacies = group.getStudentGroupAttends();

                List infoStudents = (List) CollectionUtils.collect(attendacies, new Transformer() {

                    public Object transform(Object input) {
                        IStudentGroupAttend studentGroupAttend = (IStudentGroupAttend) input;
                        IAttends attendacy = studentGroupAttend.getAttend();
                        IStudent student = attendacy.getAluno();

                        InfoStudent infoStudent = InfoStudent.newInfoFromDomain(student);
                        return infoStudent;
                    }
                });

                InfoStudentGroup infoStudentGroup = InfoStudentGroupWithAll.newInfoFromDomain(group);

                InfoGroupProjectStudents info = new InfoGroupProjectStudents();
                info.setStudentList(infoStudents);
                info.setStudentGroup(infoStudentGroup);
                infosGroupProjectStudents.add(info);
            }
        }
        return infosGroupProjectStudents;
    }
}
