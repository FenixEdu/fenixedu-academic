package net.sourceforge.fenixedu.applicationTier.Servico.degree.finalProject;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.degree.finalProject.InfoTeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.dataTransferObject.degree.finalProject.InfoTeacherDegreeFinalProjectStudentWithStudentAndPerson;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.finalProject.ITeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditTeacherDegreeFinalProjectStudentByOID implements IService {

    public void run(Integer objectID,
            InfoTeacherDegreeFinalProjectStudent infoTeacherDegreeFinalProjectStudent)
            throws ExcepcaoPersistencia, FenixServiceException {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        final IStudent student = (IStudent) persistentSupport.getIPersistentStudent()
                .readStudentByNumberAndDegreeType(
                        infoTeacherDegreeFinalProjectStudent.getInfoStudent().getNumber(),
                        DegreeType.DEGREE);
        if (student == null) {
            throw new FenixServiceException("message.student-not-found");
        }

        final IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentSupport
                .getIPersistentExecutionPeriod().readByOID(ExecutionPeriod.class,
                        infoTeacherDegreeFinalProjectStudent.getInfoExecutionPeriod().getIdInternal());
        if (executionPeriod == null) {
            throw new FenixServiceException("message.execution-period-not-found");
        }

        final InfoTeacher infoTeacher = infoTeacherDegreeFinalProjectStudent.getInfoTeacher();
        final ITeacher teacher = (ITeacher) persistentSupport.getIPersistentTeacher().readByOID(
                Teacher.class, infoTeacher.getIdInternal());
        if (teacher == null) {
            throw new FenixServiceException("message.teacher-not-found");
        }

        checkStudentFinalDegreeProjectPercentage(student, teacher, executionPeriod,
                infoTeacherDegreeFinalProjectStudent.getPercentage());

        ITeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent = getTeacherDegreeFinalProjectStudentFor(
                teacher, student, executionPeriod);
        if (teacherDegreeFinalProjectStudent == null) {
            teacherDegreeFinalProjectStudent = DomainFactory.makeTeacherDegreeFinalProjectStudent(
                    executionPeriod, teacher, student);
        }
        teacherDegreeFinalProjectStudent.setPercentage(infoTeacherDegreeFinalProjectStudent
                .getPercentage());

    }

    private void checkStudentFinalDegreeProjectPercentage(final IStudent student,
            final ITeacher teacher, final IExecutionPeriod executionPeriod, Double percentage)
            throws StudentPercentageExceed {

        for (final ITeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent : student
                .getTeacherDegreeFinalProjectStudent()) {
            if (teacherDegreeFinalProjectStudent.getExecutionPeriod() == executionPeriod
                    && teacherDegreeFinalProjectStudent.getTeacher() != teacher) {
                percentage += teacherDegreeFinalProjectStudent.getPercentage();
            }
        }
        if (percentage > 100) {
            final List<InfoTeacherDegreeFinalProjectStudent> infoTeacherDegreeFinalProjectStudentList = new ArrayList(
                    student.getTeacherDegreeFinalProjectStudentCount());
            for (final ITeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent : student
                    .getTeacherDegreeFinalProjectStudent()) {
                if (teacherDegreeFinalProjectStudent.getExecutionPeriod() == executionPeriod) {
                    infoTeacherDegreeFinalProjectStudentList
                            .add(InfoTeacherDegreeFinalProjectStudentWithStudentAndPerson
                                    .newInfoFromDomain(teacherDegreeFinalProjectStudent));
                }
            }
            throw new StudentPercentageExceed(infoTeacherDegreeFinalProjectStudentList);
        }
    }

    private ITeacherDegreeFinalProjectStudent getTeacherDegreeFinalProjectStudentFor(
            final ITeacher teacher, final IStudent student, final IExecutionPeriod executionPeriod) {
        for (final ITeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent : student
                .getTeacherDegreeFinalProjectStudent()) {
            if (teacherDegreeFinalProjectStudent.getExecutionPeriod() == executionPeriod
                    && teacherDegreeFinalProjectStudent.getTeacher() == teacher) {
                return teacherDegreeFinalProjectStudent;
            }
        }
        return null;
    }

    public class StudentPercentageExceed extends FenixServiceException {
        private List infoTeacherDegreeFinalProjectStudentList;

        public StudentPercentageExceed(List infoTeacherDegreeFinalProjectStudentList) {
            this.infoTeacherDegreeFinalProjectStudentList = infoTeacherDegreeFinalProjectStudentList;
        }

        public List getInfoTeacherDegreeFinalProjectStudentList() {
            return this.infoTeacherDegreeFinalProjectStudentList;
        }
    }
}
