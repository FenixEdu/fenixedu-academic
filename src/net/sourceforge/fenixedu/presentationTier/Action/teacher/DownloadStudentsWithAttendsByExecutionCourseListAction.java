/*
 * Created on Jan 10, 2005
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.SearchExecutionCourseAttendsBean;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * @author Andre Fernandes / Joao Brito
 */
public class DownloadStudentsWithAttendsByExecutionCourseListAction extends FenixDispatchAction {

    private static final String SEPARATOR = "\t";

    private static final String NEWLINE = "\n";

    private static final String NULL = "--";

    private static final String NOT_AVAILABLE = "N/A";

    private static final String STUDENT_NUMBER = "Número";

    private static final String NUMBER_OF_ENROLLMENTS = "Número total de Inscrições";

    private static final String ATTENDACY_TYPE = "Tipo de Inscrição";

    private static final String ATTENDACY_TYPE_NORMAL = "Normal";

    private static final String ATTENDACY_TYPE_NOT_ENROLLED = "Não Inscrito";

    private static final String ATTENDACY_TYPE_IMPROVEMENT = "Melhoria";

    private static final String COURSE = "Degree";

    private static final String NAME = "Nome";

    private static final String GROUP = "Agrupamento: ";

    private static final String EMAIL = "E-Mail";

    private static final String SHIFT = "Turno ";

    private static final String THEORETICAL = "Teórico";

    private static final String LABORATORIAL = "Laboratorial";

    private static final String PRACTICAL = "Prático";

    private static final String THEO_PRACTICAL = "Teórico-Prático";

    private static final String SUMMARY = "Resumo:";

    private static final String NUMBER_ENROLLMENTS = "Número de inscrições";

    private static final String NUMBER_STUDENTS = "Número de alunos";

    private final ResourceBundle enumerationResources = ResourceBundle.getBundle("resources.EnumerationResources",
	    Language.getLocale());

    @Override
    public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException {

	final SearchExecutionCourseAttendsBean executionCourseAttendsBean = getRenderedObject("downloadViewState");
	executionCourseAttendsBean.getExecutionCourse().searchAttends(executionCourseAttendsBean);

	final List<Attends> attendsResult = new ArrayList<Attends>(executionCourseAttendsBean.getAttendsResult());
	Collections.sort(attendsResult, Attends.COMPARATOR_BY_STUDENT_NUMBER);

	String fileContents = new String();

	// building the table header
	fileContents += STUDENT_NUMBER + SEPARATOR;
	fileContents += NUMBER_OF_ENROLLMENTS + SEPARATOR;
	fileContents += ATTENDACY_TYPE + SEPARATOR;
	fileContents += COURSE + SEPARATOR;
	fileContents += NAME + SEPARATOR;

	final List<Grouping> groupings = new ArrayList<Grouping>(executionCourseAttendsBean.getExecutionCourse().getGroupings());
	Collections.sort(groupings, Grouping.COMPARATOR_BY_ENROLMENT_BEGIN_DATE);

	if (!groupings.isEmpty()) {
	    for (final Grouping grouping : groupings) {
		fileContents += GROUP + grouping.getName() + SEPARATOR;
	    }
	}

	fileContents += EMAIL + SEPARATOR;

	final List<ShiftType> shiftTypes = new ArrayList<ShiftType>(executionCourseAttendsBean.getExecutionCourse()
		.getShiftTypes());
	Collections.sort(shiftTypes);
	for (final ShiftType shiftType : shiftTypes) {
	    fileContents += SHIFT + enumerationResources.getString(shiftType.getName()) + SEPARATOR;
	}

	fileContents += NEWLINE;

	// building each line
	for (final Attends attends : attendsResult) {
	    fileContents += attends.getRegistration().getStudent().getNumber().toString() + SEPARATOR;
	    if (attends.getEnrolment() == null) {
		fileContents += NULL + SEPARATOR;
	    } else {
		fileContents += attends.getEnrolment().getNumberOfTotalEnrolmentsInThisCourse(
			attends.getEnrolment().getExecutionPeriod())
			+ SEPARATOR;
	    }
	    fileContents += enumerationResources.getString(attends.getAttendsStateType().getQualifiedName()) + SEPARATOR;
	    fileContents += attends.getStudentCurricularPlanFromAttends().getDegreeCurricularPlan().getName() + SEPARATOR;
	    fileContents += attends.getRegistration().getStudent().getPerson().getName() + SEPARATOR;
	    for (final Grouping grouping : groupings) {
		final StudentGroup studentGroup = attends.getStudentGroupByGrouping(grouping);
		if (studentGroup == null) {
		    fileContents += NOT_AVAILABLE + SEPARATOR;
		} else {
		    fileContents += studentGroup.getGroupNumber() + SEPARATOR;
		}
	    }

	    final String email = attends.getRegistration().getStudent().getPerson().getEmail();
	    fileContents += (email != null ? email : "") + SEPARATOR;

	    for (final ShiftType shiftType : shiftTypes) {
		final Shift shift = attends.getRegistration().getShiftFor(executionCourseAttendsBean.getExecutionCourse(),
			shiftType);
		if (shift == null) {
		    fileContents += NOT_AVAILABLE + SEPARATOR;
		} else {
		    fileContents += shift.getNome() + SEPARATOR;
		}
	    }
	    fileContents += NEWLINE;
	}

	fileContents += NEWLINE;
	fileContents += SUMMARY + NEWLINE;

	fileContents += NUMBER_ENROLLMENTS + SEPARATOR + NUMBER_STUDENTS + NEWLINE;
	final SortedSet<Integer> keys = new TreeSet<Integer>(executionCourseAttendsBean.getEnrolmentsNumberMap().keySet());
	for (final Integer key : keys) {
	    fileContents += key + SEPARATOR + executionCourseAttendsBean.getEnrolmentsNumberMap().get(key) + NEWLINE;
	}

	try {
	    final ServletOutputStream writer = response.getOutputStream();
	    System.out.println("Charset OutputStream: " + response.getCharacterEncoding());
	    response.setContentType("plain/text");
	    System.out.println("Charset OutputStream: " + response.getCharacterEncoding());
	    final StringBuilder fileName = new StringBuilder();
	    final YearMonthDay currentDate = new YearMonthDay();
	    fileName.append("listaDeAlunos_");
	    fileName.append(executionCourseAttendsBean.getExecutionCourse().getSigla()).append("_")
		    .append(currentDate.getDayOfMonth());
	    fileName.append("-").append(currentDate.getMonthOfYear()).append("-").append(currentDate.getYear());
	    fileName.append(".tsv");
	    response.setHeader("Content-disposition", "attachment; filename=" + fileName);
	    writer.print(fileContents);
	    writer.flush();
	    response.flushBuffer();
	} catch (final IOException e1) {
	    throw new FenixActionException(e1);
	}

	return null;
    }

}