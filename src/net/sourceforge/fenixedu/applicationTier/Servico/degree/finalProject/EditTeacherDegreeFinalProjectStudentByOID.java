package net.sourceforge.fenixedu.applicationTier.Servico.degree.finalProject;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.degree.finalProject.InfoTeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.dataTransferObject.degree.finalProject.InfoTeacherDegreeFinalProjectStudentWithStudentAndPerson;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.finalProject.TeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditTeacherDegreeFinalProjectStudentByOID extends Service {

    public void run(Integer objectID,
            InfoTeacherDegreeFinalProjectStudent infoTeacherDegreeFinalProjectStudent)
            throws ExcepcaoPersistencia, FenixServiceException {

        final Student student = Student.readStudentByNumberAndDegreeType(
                infoTeacherDegreeFinalProjectStudent.getInfoStudent().getNumber(), DegreeType.DEGREE);
        if (student == null) {
            throw new FenixServiceException("message.student-not-found");
        }

        final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(infoTeacherDegreeFinalProjectStudent.getInfoExecutionPeriod().getIdInternal());
        if (executionPeriod == null) {
            throw new FenixServiceException("message.execution-period-not-found");
        }

        final InfoTeacher infoTeacher = infoTeacherDegreeFinalProjectStudent.getInfoTeacher();
        final Teacher teacher = rootDomainObject.readTeacherByOID(infoTeacher.getIdInternal());
        if (teacher == null) {
            throw new FenixServiceException("message.teacher-not-found");
        }

        checkStudentFinalDegreeProjectPercentage(student, teacher, executionPeriod,
                infoTeacherDegreeFinalProjectStudent.getPercentage());

        TeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent = getTeacherDegreeFinalProjectStudentFor(
                teacher, student, executionPeriod);
        if (teacherDegreeFinalProjectStudent == null) {
            teacherDegreeFinalProjectStudent = new TeacherDegreeFinalProjectStudent(
                    executionPeriod, teacher, student);
        }
        teacherDegreeFinalProjectStudent.setPercentage(infoTeacherDegreeFinalProjectStudent
                .getPercentage());

    }

    private void checkStudentFinalDegreeProjectPercentage(final Student student, final Teacher teacher,
            final ExecutionPeriod executionPeriod, Double percentage) throws StudentPercentageExceed {

        for (final TeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent : student
                .getTeacherDegreeFinalProjectStudent()) {
            if (teacherDegreeFinalProjectStudent.getExecutionPeriod() == executionPeriod
                    && teacherDegreeFinalProjectStudent.getTeacher() != teacher) {
                percentage += teacherDegreeFinalProjectStudent.getPercentage();
            }
        }
        if (percentage > 100) {
            final List<InfoTeacherDegreeFinalProjectStudent> infoTeacherDegreeFinalProjectStudentList = new ArrayList<InfoTeacherDegreeFinalProjectStudent>(
                    student.getTeacherDegreeFinalProjectStudentCount());
            for (final TeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent : student
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

    private TeacherDegreeFinalProjectStudent getTeacherDegreeFinalProjectStudentFor(
            final Teacher teacher, final Student student, final ExecutionPeriod executionPeriod) {
        for (final TeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent : student
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
