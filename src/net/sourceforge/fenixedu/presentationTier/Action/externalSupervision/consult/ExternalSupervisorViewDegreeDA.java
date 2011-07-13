package net.sourceforge.fenixedu.presentationTier.Action.externalSupervision.consult;

import java.io.IOException;
import java.math.RoundingMode;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.RegistrationProtocol;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/viewDegree", module = "externalSupervision")
@Forwards( {
    	@Forward(name = "selectDegree", path = "/externalSupervision/consult/selectDegree.jsp")})
public class ExternalSupervisorViewDegreeDA extends FenixDispatchAction{
    
    public ActionForward beginTaskFlow(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
	final IUserView userView = UserView.getUser();
	final Person supervisor = userView.getPerson();
	
	RegistrationProtocol protocol = supervisor.getOnlyRegistrationProtocol();
	ExternalSupervisorViewsBean bean;
	
	if(protocol == null){
	    bean = new ExternalSupervisorViewsBean(ExecutionYear.readCurrentExecutionYear());
	    bean.setMegavisor(true);
	    boolean selectProtocol = true;
	    request.setAttribute("selectProtocol", selectProtocol);
	    
	} else {
	    bean  = new ExternalSupervisorViewsBean(ExecutionYear.readCurrentExecutionYear(), protocol);
	}
	
	request.setAttribute("sessionBean", bean);
	return mapping.findForward("selectDegree");
    }
    
    public ActionForward degreePostback(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
	ExternalSupervisorViewsBean bean = getRenderedObject("sessionBean");
	
	if(bean.getMegavisor()){
	    boolean selectProtocol = true;
	    request.setAttribute("selectProtocol", selectProtocol);
	}
	
	RenderUtils.invalidateViewState();
	request.setAttribute("sessionBean", bean);
	
	return mapping.findForward("selectDegree");
    }
    
    public ActionForward showStudents(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
	ExternalSupervisorViewsBean bean = getRenderedObject("sessionBean");
	RenderUtils.invalidateViewState();
	
	if(bean == null){
	    final String registrationProtocolId = request.getParameter("registrationProtocolId");
	    RegistrationProtocol registrationProtocol = AbstractDomainObject.fromExternalId(registrationProtocolId);
	    
	    final String executionYearId = request.getParameter("executionYearId");
	    ExecutionYear executionYear = AbstractDomainObject.fromExternalId(executionYearId);
	    
	    final String executionDegreeId = request.getParameter("executionDegreeId");
	    ExecutionDegree executionDegree = AbstractDomainObject.fromExternalId(executionDegreeId);
	    
	    Boolean megavisor =  Boolean.valueOf(request.getParameter("megavisor"));
	    
	    bean = new ExternalSupervisorViewsBean(executionYear, registrationProtocol);
	    bean.setExecutionDegree(executionDegree);
	    bean.setDegreeType(executionDegree.getDegreeType());
	    bean.setMegavisor(megavisor);
	}
	
	if(bean.getMegavisor()){
	    boolean selectProtocol = true;
	    request.setAttribute("selectProtocol", selectProtocol);
	}
	
	bean.generateStudentsFromDegree();
	Boolean degreeSelected = true;
	
	request.setAttribute("sessionBean", bean);
	request.setAttribute("hasChosenDegree", degreeSelected);
	
	return mapping.findForward("selectDegree");
    }
    

    public ActionForward exportXLS(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException{
	ExternalSupervisorViewsBean bean = getRenderedObject("sessionBean");
	final Spreadsheet spreadsheet = generateSpreadsheet(bean);

	response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-disposition", "attachment; filename=" + getFilename(bean) + ".xls");
	spreadsheet.exportToXLSSheet(response.getOutputStream());
	response.getOutputStream().flush();
	response.flushBuffer();
	return null;
    }
    
    private String getFilename(ExternalSupervisorViewsBean bean) {
	StringBuilder strBuilder = new StringBuilder();
	strBuilder.append(ResourceBundle.getBundle("resources.ApplicationResources", Language.getLocale()).getString(
		"label.students.lowercase"));
	strBuilder.append("_");
	strBuilder.append(bean.getProtocol().getRegistrationAgreement().getName());
	strBuilder.append("_");
	strBuilder.append(bean.getExecutionDegree().getDegree().getSigla());
	strBuilder.append("_");
	strBuilder.append(bean.getExecutionYear().getName());
	return strBuilder.toString();
    }
    
    private Spreadsheet generateSpreadsheet(ExternalSupervisorViewsBean bean) {
	final Spreadsheet spreadsheet = createSpreadSheet();
	for (final StudentCurricularPlan studentCurricularPlan : bean.generateAllStudentCurricularPlans()) {
	    final Row row = spreadsheet.addRow();

	    row.setCell(studentCurricularPlan.getPerson().getUsername());
	    row.setCell(studentCurricularPlan.getRegistration().getNumber());
	    row.setCell(studentCurricularPlan.getPerson().getName());
	    row.setCell(studentCurricularPlan.getPerson().getInstitutionalOrDefaultEmailAddressValue());
	    row.setCell(studentCurricularPlan.getPerson().getIdDocumentType().getLocalizedName());
	    row.setCell(studentCurricularPlan.getPerson().getDocumentIdNumber());
	    row.setCell(studentCurricularPlan.getDegree().getSigla());
	    row.setCell(studentCurricularPlan.getName());
	    row.setCell(studentCurricularPlan.getStartDateYearMonthDay().toString());
	    row.setCell(studentCurricularPlan.getEndDate() == null ? "" : studentCurricularPlan.getEndDate().toString());
	    row.setCell(studentCurricularPlan.getRegistration().getActiveStateType().getDescription());
	    row.setCell(studentCurricularPlan.getRegistration().getNumberOfCurriculumEntries());
	    row.setCell(studentCurricularPlan.getRegistration().getEctsCredits());
	    row.setCell(getAverageInformation(studentCurricularPlan));
	    row.setCell(studentCurricularPlan.getRegistration().getCurricularYear());

	}

	return spreadsheet;
    }
    
    private String getAverageInformation(final StudentCurricularPlan studentCurricularPlan) {
	final Registration registration = studentCurricularPlan.getRegistration();

	if (registration.isConcluded()) {
	    if (registration.isRegistrationConclusionProcessed()
		    && (!registration.isBolonha() || studentCurricularPlan.getInternalCycleCurriculumGroupsSize().intValue() == 1)) {
		return registration.getAverage().setScale(2, RoundingMode.HALF_EVEN).toPlainString();
	    } else {
		return " - ";
	    }
	} else {
	    return registration.getAverage().setScale(2, RoundingMode.HALF_EVEN).toPlainString();
	}
    }
    
    private Spreadsheet createSpreadSheet() {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.ApplicationResources", Language.getLocale());
	final Spreadsheet spreadsheet = new Spreadsheet(bundle.getString("list.students"));

	spreadsheet.setHeaders(new String[] {
		
	bundle.getString("label.istid"),

	bundle.getString("label.number"),

	bundle.getString("label.name"),

	bundle.getString("label.email"),
	
	bundle.getString("label.identificationDocumentType"),
	
	bundle.getString("label.identificationDocumentNumber"),
	
	bundle.getString("label.Degree"),
	
	bundle.getString("label.curricularPlan"),
	
	bundle.getString("label.net.sourceforge.fenixedu.domain.student.Registration.startDate"),
	
	bundle.getString("label.net.sourceforge.fenixedu.domain.student.Registration.conclusionDate"),

	bundle.getString("label.student.curricular.plan.state"),

	bundle.getString("label.number.approved.curricular.courses"),

	bundle.getString("label.ects"),

	bundle.getString("label.average"),

	bundle.getString("label.student.curricular.year"),

	" ", " " });

	return spreadsheet;
    }

}
