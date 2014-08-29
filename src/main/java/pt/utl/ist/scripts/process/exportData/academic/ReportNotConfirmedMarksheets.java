package pt.utl.ist.scripts.process.exportData.academic;

import java.io.ByteArrayOutputStream;

import net.sourceforge.fenixedu.domain.MarkSheet;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.custom.CustomTask;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class ReportNotConfirmedMarksheets extends CustomTask {

    @Override
    public void runTask() throws Exception {
        final Spreadsheet spreadsheet = new Spreadsheet("PautasPorConfirmar");
        spreadsheet.setHeader("Plano Curricular\t");
        spreadsheet.setHeader("Unidade Curricular\t");
        spreadsheet.setHeader("Número Responsável\t");
        spreadsheet.setHeader("Responsável\n");

        for (final MarkSheet markSheet : Bennu.getInstance().getMarkSheetsSet()) {
            if (markSheet.isNotConfirmed()) {
                final Row row = spreadsheet.addRow();
                row.setCell(markSheet.getCurricularCourse().getDegreeCurricularPlan().getName());
                row.setCell(markSheet.getCurricularCourse().getName());
                row.setCell(markSheet.getResponsibleTeacher().getPerson().getEmployee().getEmployeeNumber());
                row.setCell(markSheet.getResponsibleTeacher().getPerson().getName());
            }
        }
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        spreadsheet.exportToXLSSheet(byteArrayOS);

        output("pautas_por_confirmar.xls", byteArrayOS.toByteArray());
    }
}
