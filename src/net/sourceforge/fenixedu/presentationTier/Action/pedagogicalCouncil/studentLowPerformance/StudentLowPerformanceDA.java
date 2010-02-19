package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

@Mapping(path = "/studentLowPerformance", module = "pedagogicalCouncil")
@Forwards( { @Forward(name = "viewStudentsState", path = "/pedagogicalCouncil/tutorship/viewStudentsState.jsp"),
	@Forward(name = "viewStudentsState", path = "/pedagogicalCouncil/tutorship/viewStudentsState.jsp") })
public class StudentLowPerformanceDA extends FenixDispatchAction {

    protected final String RESOURCE_MODULE = "pedagogicalCouncil";
    protected final String PRESCRIPTION_BEAN = "prescriptionBean";

    public ActionForward viewStudentsState(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	PrescriptionBean prescriptionBean = getContextBean(request);
	if (prescriptionBean.getSelectedPrescriptionEnum() == null) {
	    request.setAttribute(PRESCRIPTION_BEAN, prescriptionBean);
	    return mapping.findForward("viewStudentsState");
	}

	request.setAttribute(PRESCRIPTION_BEAN, prescriptionBean);
	request.setAttribute("studentlowPerformanceBeans", calcStudentsLowPerformance(prescriptionBean));
	return mapping.findForward("viewStudentsState");
    }

    public ActionForward downloadStudentsLowPerformanceList(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	PrescriptionBean prescriptionBean = getContextBean(request);
	if (prescriptionBean.getSelectedPrescriptionEnum() == null) {
	    return null;
	}

	response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-disposition", "attachment; filename=Students" + new DateTime().toString("yyyy-MM-dd")
		+ ".xls");
	ServletOutputStream writer = response.getOutputStream();

	exportStudents(writer, calcStudentsLowPerformance(prescriptionBean));

	writer.flush();
	response.flushBuffer();
	return null;
    }

    private PrescriptionBean getContextBean(HttpServletRequest request) {
	PrescriptionBean bean = (PrescriptionBean) getRenderedObject(PRESCRIPTION_BEAN);
	RenderUtils.invalidateViewState(PRESCRIPTION_BEAN);
	if (bean == null) {
	    return new PrescriptionBean(null);
	}
	return bean;
    }

    private List<StudentLowPerformanceBean> calcStudentsLowPerformance(PrescriptionBean prescriptionBean) {
	LinkedList<StudentLowPerformanceBean> studentLowPerformanceBeans = new LinkedList<StudentLowPerformanceBean>();

	for (AbstractPrescriptionRule abstractPrescriptionRule : readPrescriptionUntil2009_2010(prescriptionBean)) {
	    studentLowPerformanceBeans.addAll(calcstudentsLowPerformanceBean(abstractPrescriptionRule.getRegistrationStart(),
		    AbstractPrescriptionRule.readPrescriptionRules(prescriptionBean)));
	}
	return studentLowPerformanceBeans;
    }

    private List<StudentLowPerformanceBean> calcstudentsLowPerformanceBean(ExecutionYear registrationsInExecutionYear,
	    List<AbstractPrescriptionRule> prescriptionRules) {
	List<StudentLowPerformanceBean> studentLowPerformanceBeans = new LinkedList<StudentLowPerformanceBean>();

	for (Registration registration : registrationsInExecutionYear.getStudents()) {
	    if (isValidRegistration(registration)) {
		StudentLowPerformanceBean s = calcStudentCycleLowPerformanceBean(registration, null, prescriptionRules);
		if (s != null) {
		    studentLowPerformanceBeans.add(s);
		}
	    }

	}
	Collections.sort(studentLowPerformanceBeans, new Comparator<StudentLowPerformanceBean>() {
	    public int compare(StudentLowPerformanceBean s1, StudentLowPerformanceBean s2) {
		return s1.getStudent().getNumber() - s2.getStudent().getNumber();
	    }
	});
	return studentLowPerformanceBeans;
    }

    /*
     * private List<StudentLowPerformanceBean>
     * calcCompositeCycleStudentLowPerformanceBean(Registration registration,
     * PrescriptionRule[] prescriptionRules ) { List<StudentLowPerformanceBean>
     * studentLowPerformanceBeans = new LinkedList<StudentLowPerformanceBean>();
     * for (CycleType cycleType :
     * registration.getDegree().getDegreeType().getCycleTypes()) { if (cycleType
     * == CycleType.FIRST_CYCLE || cycleType == CycleType.SECOND_CYCLE) {
     * StudentLowPerformanceBean s =
     * calcStudentCycleLowPerformanceBean(registration, cycleType,
     * prescriptionRules); if (s != null) { studentLowPerformanceBeans.add(s); }
     * } } return studentLowPerformanceBeans; }
     */

    private StudentLowPerformanceBean calcStudentCycleLowPerformanceBean(Registration registration, CycleType cycleType,
	    List<AbstractPrescriptionRule> prescriptionRules) {

	int numberOfEntriesStudentInSecretary = 0;

	List<Registration> fullRegistrationPath = getFullRegistrationPath(registration);

	// Historic Student
	for (Registration reg : fullRegistrationPath) {
	    numberOfEntriesStudentInSecretary += getNumberOfEntriesStudentInSecretary(reg);
	}

	BigDecimal sumEcts = registration.getCurriculum().getSumEctsCredits();

	if (isLowPerformanceStudent(sumEcts, numberOfEntriesStudentInSecretary, prescriptionRules)) {
	    Student student = registration.getStudent();
	    String studentState = workingStudent(student);
	    studentState += parcialStudent(registration);
	    studentState += flunkedtStudent(fullRegistrationPath);
	    return new StudentLowPerformanceBean(student, sumEcts, registration.getDegree(), numberOfEntriesStudentInSecretary,
		    student.getPerson().getDefaultEmailAddressValue(), studentState, fullRegistrationPath.get(0).getStartDate()
			    .toString("yyyy-MM-dd"));
	}
	return null;
    }

    private int getNumberOfEntriesStudentInSecretary(Registration registration) {
	int numberOfEntriesStudentInSecretary = 0;
	for (ExecutionYear execYear : ExecutionYear.readExecutionYears(registration.getStartExecutionYear(), ExecutionYear
		.readCurrentExecutionYear())) {
	    RegistrationState registrationState = registration.getLastRegistrationState(execYear);
	    if (registrationState != null && registrationState.isActive()) {
		numberOfEntriesStudentInSecretary += 1;

	    }
	}
	return numberOfEntriesStudentInSecretary;

    }

    private boolean isLowPerformanceStudent(BigDecimal ects, int numberOfEntriesStudentInSecretary,
	    List<AbstractPrescriptionRule> prescriptionRules) {
	for (AbstractPrescriptionRule prescriptionRule : prescriptionRules) {
	    if ((prescriptionRule.isPrescript(ects, numberOfEntriesStudentInSecretary))) {
		return true;
	    }
	}
	return false;
    }

    // valid until 2009_2010 (replace by
    // AbstractPrescriptionRule.readPrescriptionRules(prescriptionBean))
    private List<AbstractPrescriptionRule> readPrescriptionUntil2009_2010(PrescriptionBean prescriptionBean) {
	List<AbstractPrescriptionRule> abstractPrescriptionRules = new LinkedList<AbstractPrescriptionRule>();
	for (AbstractPrescriptionRule abstractPrescriptionRule : getPrescriptionUntil2009_2010()) {
	    if (abstractPrescriptionRule.isContains(prescriptionBean.getSelectedPrescriptionEnum())) {
		abstractPrescriptionRules.add(abstractPrescriptionRule);
	    }
	}
	return abstractPrescriptionRules;
    }

    private AbstractPrescriptionRule[] getPrescriptionUntil2009_2010() {
	return new AbstractPrescriptionRule[] { new PrescriptionRuleMomentOne(), new PrescriptionRuleMomentTwo(),
		new PrescriptionRuleMomentTree(), new PrescriptionRuleTreeEntries(), new PrescriptionRuleFourEntries() };
    }

    private String workingStudent(Student student) {
	return student.isWorkingStudent() ? getResourceMessage("label.tutorship.lowPerformance.workingStudent") + ";" : "";
    }

    private String parcialStudent(Registration registration) {
	return (registration.isPartialRegime(ExecutionYear.readCurrentExecutionYear()) ? getResourceMessage("label.tutorship.lowPerformance.parcialStudent")
		+ ";"
		: "");
    }

    private String flunkedtStudent(List<Registration> registrations) {
	for (Registration registration : registrations) {
	    for (RegistrationState registrationState : registration.getRegistrationStates())
		if (registrationState.getStateType() == RegistrationStateType.FLUNKED) {
		    return getResourceMessage("label.tutorship.lowPerformance.flunkedStudent") + ";";
		}
	}
	return "";
    }

    private void exportStudents(final ServletOutputStream writer, List<StudentLowPerformanceBean> studentLowPerformanceBeans)
	    throws IOException {
	final Spreadsheet spreadsheet = new Spreadsheet("Students " + new DateTime().toString("yyyy-MM-dd"));

	spreadsheet.setHeader(getResourceMessage("label.tutorship.lowPerformance.name"));
	spreadsheet.setHeader(getResourceMessage("label.tutorship.lowPerformance.number"));
	spreadsheet.setHeader(getResourceMessage("label.tutorship.lowPerformance.degreeName"));
	spreadsheet.setHeader(getResourceMessage("label.tutorship.lowPerformance.degreeType"));
	spreadsheet.setHeader(getResourceMessage("label.tutorship.lowPerformance.sumEcts"));
	spreadsheet.setHeader(getResourceMessage("label.tutorship.lowPerformance.email"));
	spreadsheet.setHeader(getResourceMessage("label.tutorship.lowPerformance.regime"));
	spreadsheet.setHeader(getResourceMessage("label.tutorship.lowPerformance.numberOfEntriesStudentInSecretary"));
	spreadsheet.setHeader(getResourceMessage("label.tutorship.lowPerformance.registrationStart"));

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

	}
	spreadsheet.exportToXLSSheet(writer);
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

    private String getResourceMessage(String key) {
	return getResourceMessageFromModuleOrApplication(RESOURCE_MODULE, key);
    }

    private boolean isValidRegistration(Registration registration) {
	return registration.isBolonha() && registration.isActive() && !registration.getDegreeType().isThirdCycle()
		&& !registration.getDegreeType().isEmpty();
    }

}