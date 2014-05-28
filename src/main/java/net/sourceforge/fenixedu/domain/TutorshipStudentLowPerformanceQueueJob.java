/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance.AbstractPrescriptionRule;
import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance.StudentLowPerformanceBean;

import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class TutorshipStudentLowPerformanceQueueJob extends TutorshipStudentLowPerformanceQueueJob_Base {

    public TutorshipStudentLowPerformanceQueueJob() {
        super();
    }

    public TutorshipStudentLowPerformanceQueueJob(PrescriptionEnum prescriptionEnum, ExecutionYear executionYear) {
        super();
        setPrescriptionEnum(prescriptionEnum);
        setExecutionYear(executionYear);
    }

    @Override
    public QueueJobResult execute() throws Exception {

        final List<StudentLowPerformanceBean> studentLowPerformanceBeans =
                calcstudentsLowPerformanceBean(AbstractPrescriptionRule.readPrescriptionRules(getPrescriptionEnum()));

        Spreadsheet spreadsheet = createSpreadsheet(studentLowPerformanceBeans);
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        spreadsheet.exportToXLSSheet(byteArrayOS);

        final QueueJobResult queueJobResult = new QueueJobResult();

        queueJobResult.setContentType("application/txt");
        queueJobResult.setContent(byteArrayOS.toByteArray());

        return queueJobResult;

    }

    @Override
    public String getFilename() {
        return "Students_" + new DateTime().toString("yyyy-MM-dd") + ".xls";
    }

    private List<StudentLowPerformanceBean> calcstudentsLowPerformanceBean(List<AbstractPrescriptionRule> prescriptionRules) {
        LinkedList<StudentLowPerformanceBean> studentLowPerformanceBeans = new LinkedList<StudentLowPerformanceBean>();

        final List<AbstractPrescriptionRule> abstractPrescriptionRules =
                AbstractPrescriptionRule.readPrescriptionRules(getPrescriptionEnum());
        for (AbstractPrescriptionRule abstractPrescriptionRule : prescriptionRules) {

            for (Registration registration : abstractPrescriptionRule.getRegistrationStart(getExecutionYear()).getStudents()) {
                if (isValidRegistration(registration)) {
                    StudentLowPerformanceBean s = calcStudentCycleLowPerformanceBean(registration, abstractPrescriptionRules);
                    if (s != null) {
                        studentLowPerformanceBeans.add(s);
                    }
                }

                else if (!registration.isBolonha() && isValidSourceLink(registration)) {
                    for (Registration destinationRegistration : registration.getDestinyRegistrations()) {
                        if ((destinationRegistration.getDegreeType() != DegreeType.BOLONHA_DEGREE)
                                && (destinationRegistration.getDegreeType() != DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE)) {
                            continue;
                        }
                        if (!isValidRegistration(destinationRegistration)) {
                            continue;
                        }
                        StudentLowPerformanceBean s =
                                calcStudentCycleLowPerformanceBean(destinationRegistration, abstractPrescriptionRules);
                        if (s != null) {
                            studentLowPerformanceBeans.add(s);
                        }
                    }
                }
            }

        }
        return studentLowPerformanceBeans;
    }

    private StudentLowPerformanceBean calcStudentCycleLowPerformanceBean(Registration registration,
            List<AbstractPrescriptionRule> prescriptionRules) {

        int numberOfEntriesStudentInSecretary = 0;

        List<Registration> fullRegistrationPath = getFullRegistrationPath(registration);

        // Historic Student
        for (Registration reg : fullRegistrationPath) {
            numberOfEntriesStudentInSecretary += getNumberOfEntriesStudentInSecretary(reg);
        }

        BigDecimal sumEcts = registration.getCurriculum().getSumEctsCredits();

        if (isLowPerformanceStudent(registration, sumEcts, numberOfEntriesStudentInSecretary, prescriptionRules)) {
            Student student = registration.getStudent();
            String studentState = workingStudent(student);
            studentState += parcialStudent(registration);
            studentState += flunkedStudent(fullRegistrationPath);
            return new StudentLowPerformanceBean(student, sumEcts, registration.getDegree(), numberOfEntriesStudentInSecretary,
                    student.getPerson().getDefaultEmailAddressValue(), studentState, fullRegistrationPath.iterator().next()
                            .getStartDate().toString("yyyy-MM-dd"));
        }
        return null;
    }

    private int getNumberOfEntriesStudentInSecretary(Registration registration) {
        int numberOfEntriesStudentInSecretary = 0;
        for (ExecutionYear execYear : ExecutionYear.readExecutionYears(registration.getStartExecutionYear(), getExecutionYear())) {
            RegistrationState registrationState = registration.getLastRegistrationState(execYear);
            if (registrationState != null && registrationState.isActive()) {
                numberOfEntriesStudentInSecretary += 1;
            }
        }
        return numberOfEntriesStudentInSecretary;

    }

    private boolean isLowPerformanceStudent(Registration registration, BigDecimal ects, int numberOfEntriesStudentInSecretary,
            List<AbstractPrescriptionRule> prescriptionRules) {
        for (AbstractPrescriptionRule prescriptionRule : prescriptionRules) {
            if ((prescriptionRule.isPrescript(registration, ects, numberOfEntriesStudentInSecretary, getExecutionYear()))) {
                return true;
            }
        }
        return false;
    }

    private String workingStudent(Student student) {
        return student.isWorkingStudent() ? "Trabalhor Estudante" + ";" : "";
    }

    private String parcialStudent(Registration registration) {
        return (registration.isPartialRegime(getExecutionYear()) ? "Estudante Parcial" + ";" : "");
    }

    private String flunkedStudent(List<Registration> registrations) {
        for (Registration registration : registrations) {
            for (RegistrationState registrationState : registration.getRegistrationStates()) {
                if (registrationState.getStateType() == RegistrationStateType.FLUNKED) {
                    return "Prescrito" + ";";
                }
            }
        }
        return "";
    }

    private Spreadsheet createSpreadsheet(List<StudentLowPerformanceBean> studentLowPerformanceBeans) throws IOException {
        Spreadsheet spreadsheet = new Spreadsheet(getFilename());

        spreadsheet.setHeader("Nome");
        spreadsheet.setHeader("Número");
        spreadsheet.setHeader("Nome do Curso");
        spreadsheet.setHeader("Ciclo");
        spreadsheet.setHeader("Créditos Alcançados");
        spreadsheet.setHeader("Regime");
        spreadsheet.setHeader("Email");
        spreadsheet.setHeader("Número de Inscrições");
        spreadsheet.setHeader("Início da Matrícula");
        spreadsheet.setHeader("Tutor");
        spreadsheet.setHeader("Email do Tutor");

        for (StudentLowPerformanceBean studentLowPerformanceBean : studentLowPerformanceBeans) {
            final Row row = spreadsheet.addRow();
            row.setCell(studentLowPerformanceBean.getStudent().getName());
            row.setCell(studentLowPerformanceBean.getStudent().getNumber().toString());
            row.setCell(studentLowPerformanceBean.getDegree().getName());
            row.setCell(studentLowPerformanceBean.getDegree().getDegreeType().getLocalizedName());
            row.setCell(studentLowPerformanceBean.getSumEcts().toString());
            row.setCell(studentLowPerformanceBean.getRegime());
            row.setCell(studentLowPerformanceBean.getEmail());
            row.setCell(studentLowPerformanceBean.getNumberOfEntriesStudentInSecretary());
            row.setCell(studentLowPerformanceBean.getRegistrationStart());
            List<Tutorship> tutorships = studentLowPerformanceBean.getStudent().getActiveTutorships();
            if (!tutorships.isEmpty()) {
                row.setCell(tutorships.iterator().next().getTeacher().getPerson().getPresentationName());
                row.setCell(tutorships.iterator().next().getTeacher().getPerson().getInstitutionalOrDefaultEmailAddressValue());
            } else {
                row.setCell("");
                row.setCell("");
            }
        }

        return spreadsheet;
    }

    // Historic Student
    protected static List<Registration> getFullRegistrationPath(final Registration current) {
        if (current.getDegreeType() == DegreeType.BOLONHA_DEGREE
                || current.getDegreeType() == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) {
            List<Registration> path = new ArrayList<Registration>();
            path.add(current);
            Registration source;
            if (current.hasSourceRegistration()
                    && (!(source = current.getSourceRegistration()).isBolonha() || isValidSourceLink(source))) {
                path.addAll(getFullRegistrationPath(source));
            }

            Collections.sort(path, Registration.COMPARATOR_BY_START_DATE);
            return path;
        } else {
            return Collections.singletonList(current);
        }
    }

    protected static boolean isValidSourceLink(Registration source) {
        return source.getActiveStateType().equals(RegistrationStateType.TRANSITED)
                || source.getActiveStateType().equals(RegistrationStateType.FLUNKED)
                || source.getActiveStateType().equals(RegistrationStateType.INTERNAL_ABANDON)
                || source.getActiveStateType().equals(RegistrationStateType.EXTERNAL_ABANDON)
                || source.getActiveStateType().equals(RegistrationStateType.INTERRUPTED);
    }

    private boolean isValidRegistration(Registration registration) {
        return registration.isBolonha() && registration.isActive()
                && (registration.getDegreeType().isFirstCycle() || registration.getDegreeType().isSecondCycle())
                && !registration.getDegreeType().isEmpty();
    }

    @Atomic
    public static TutorshipStudentLowPerformanceQueueJob createTutorshipStudentLowPerformanceQueueJob(
            PrescriptionEnum prescriptionEnum, ExecutionYear executionYear) {
        final TutorshipStudentLowPerformanceQueueJob tutorshipStudentLowPerformanceQueueJob =
                new TutorshipStudentLowPerformanceQueueJob(prescriptionEnum, executionYear);
        return tutorshipStudentLowPerformanceQueueJob;
    }

    @Deprecated
    public boolean hasPrescriptionEnum() {
        return getPrescriptionEnum() != null;
    }

}
