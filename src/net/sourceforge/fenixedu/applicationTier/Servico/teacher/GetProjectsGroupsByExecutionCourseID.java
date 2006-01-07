/*
 * Created on 10/Set/2003, 20:47:24
 * 
 * By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoGroupProjectStudents;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentGroup;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 10/Set/2003, 20:47:24
 * 
 */
public class GetProjectsGroupsByExecutionCourseID implements IService {

    public List run(Integer executionCourseID) throws BDException, ExcepcaoPersistencia {

        final List infosGroupProjectStudents = new LinkedList();

        final ISuportePersistente persistenceSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentExecutionCourse persistentExecutionCourse = persistenceSupport
                .getIPersistentExecutionCourse();

        final ExecutionCourse executionCourse = (ExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, executionCourseID);
        final List<Grouping> groupings = executionCourse.getGroupings();

        for (final Grouping grouping : groupings) {
            final List<StudentGroup> studentGroups = grouping.getStudentGroups();
            for (final StudentGroup studentGroup : studentGroups) {
                List<Attends> attends = studentGroup.getAttends();
                List infoStudents = new ArrayList();
                for (final Attends attend : attends) {
                    infoStudents.add(InfoStudent.newInfoFromDomain(attend.getAluno()));
                }
                InfoStudentGroup infoStudentGroup = InfoStudentGroup.newInfoFromDomain(studentGroup);
                InfoGroupProjectStudents infoGroupProjectStudents = new InfoGroupProjectStudents();
                infoGroupProjectStudents.setStudentList(infoStudents);
                infoGroupProjectStudents.setStudentGroup(infoStudentGroup);
                infosGroupProjectStudents.add(infoGroupProjectStudents);
            }
        }
        return infosGroupProjectStudents;
    }
}
