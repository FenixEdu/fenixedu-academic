package pt.utl.ist.scripts.process.exportData.academic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.Credits;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.annotation.Task;
import org.fenixedu.bennu.scheduler.custom.CustomTask;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

@Task(englishTitle = "ExportOutoingStudentsInformation", readOnly = true)
public class ExportOutoingStudentsInformation extends CustomTask {

    @Override
    public void runTask() throws IOException {
        Spreadsheet spreadsheet = new Spreadsheet("Estudantes Mobilidade");
        spreadsheet
                .setHeaders("Nº aluno, Curso, Ano lectivo mobilidade, País de mobilidade, Instituição de mobilidade, Nº créditos"
                        .split(","));
        for (final Student student : Bennu.getInstance().getStudentsSet()) {
            for (final Registration registration : student.getRegistrationsSet()) {
                if (registration.isBolonha()) {
                    for (final StudentCurricularPlan scp : registration.getStudentCurricularPlansSet()) {
                        Map<ExecutionSemester, MobilityCreditsEnrolments> mobilityCreditsMap =
                                new HashMap<ExecutionSemester, MobilityCreditsEnrolments>();
                        for (final Credits credit : scp.getCreditsSet()) {
                            if (credit.isAllEnrolmentsAreExternal()) {
                                IEnrolment iEnrolment = credit.getIEnrolments().iterator().next();
                                if (iEnrolment.getAcademicUnit() == null
                                        || (iEnrolment.getAcademicUnit() != null
                                                && iEnrolment.getAcademicUnit().getCountry() != null && !iEnrolment
                                                .getAcademicUnit().getCountry().getCode().equalsIgnoreCase("pt"))) {
                                    ExecutionSemester executionSemester = credit.getExecutionPeriod();
                                    MobilityCreditsEnrolments mobilityCreditsEnrolments =
                                            mobilityCreditsMap.get(executionSemester);
                                    if (mobilityCreditsEnrolments == null) {
                                        mobilityCreditsEnrolments = new MobilityCreditsEnrolments();
                                        mobilityCreditsMap.put(executionSemester, mobilityCreditsEnrolments);
                                    }
                                    mobilityCreditsEnrolments.addCredits(credit.getGivenCredits());
                                    mobilityCreditsEnrolments.setIEnrolment(iEnrolment);
                                }
                            }
                        }
                        writeIEnrolments(scp, mobilityCreditsMap, spreadsheet);
                    }
                }
            }
        }
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        spreadsheet.exportToXLSSheet(byteArrayOS);
        output("Estudantes_Mobilidade.xls", byteArrayOS.toByteArray());
    }

    private void writeIEnrolments(final StudentCurricularPlan scp,
            Map<ExecutionSemester, MobilityCreditsEnrolments> mobilityCreditsMap, final Spreadsheet spreadsheet) {
        for (ExecutionSemester executionSemester : mobilityCreditsMap.keySet()) {
            Row row = spreadsheet.addRow();
            row.setCell(scp.getRegistration().getNumber());
            row.setCell(scp.getDegree().getNameI18N().getContent());
            row.setCell(executionSemester.getExecutionYear().getName());
            MobilityCreditsEnrolments mobilityCreditsEnrolments = mobilityCreditsMap.get(executionSemester);
            Unit academicUnit = mobilityCreditsEnrolments.getiEnrolment().getAcademicUnit();
            if (academicUnit != null) {
                row.setCell(academicUnit.getCountry().getName());
                row.setCell(academicUnit.getName());
            } else {
                row.setCell("");
                row.setCell(mobilityCreditsEnrolments.getiEnrolment().getDescription());
            }
            row.setCell(mobilityCreditsEnrolments.getCredits());
        }
    }

    private class MobilityCreditsEnrolments implements Serializable {

        private static final long serialVersionUID = 1L;

        private double credits;
        private IEnrolment iEnrolment;

        public MobilityCreditsEnrolments() {
            credits = 0;
        }

        public double getCredits() {
            return credits;
        }

        public void addCredits(double credits) {
            this.credits += credits;
        }

        public void setIEnrolment(IEnrolment iEnrolment) {
            this.iEnrolment = iEnrolment;
        }

        public IEnrolment getiEnrolment() {
            return iEnrolment;
        }
    }
}
