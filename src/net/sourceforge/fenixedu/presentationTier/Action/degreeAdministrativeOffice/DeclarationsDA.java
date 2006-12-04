package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice;

import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.StudentsSearchBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice.RegistrationDeclaration;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.ReportsUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class DeclarationsDA extends FenixDispatchAction {

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
	StudentSearchBeanWithExecutionYear studentsSearchBean = (StudentSearchBeanWithExecutionYear) getRenderedObject();
        if (studentsSearchBean == null) {
            studentsSearchBean = new StudentSearchBeanWithExecutionYear();
        }
        request.setAttribute("studentsSearchBean", studentsSearchBean);
        return mapping.findForward("declarations-search-form");
    }

    public ActionForward registrationDeclaration(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws Exception {

        final Registration registration = getRegistration(request);
        final ExecutionYear executionYear = getExecutionYear(request);

        if (registration != null) {
            final RegistrationDeclaration registrationDeclaration = new RegistrationDeclaration(registration, executionYear, getLoggedPerson(request));
            
            final byte[] data = ReportsUtils.exportToPdf(
        	    "registrationDeclaration", 
        	    registrationDeclaration.getParameters(), 
        	    registrationDeclaration.getResourceBundle(),
        	    registrationDeclaration.getDataSource());
            
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename=" + getReportFileName(registration) + ".pdf");
            response.setContentLength(data.length);
            
            ServletOutputStream writer = response.getOutputStream();
            writer.write(data);
            writer.flush();
            writer.close();
            
            response.flushBuffer();
        }

        return null;
    }
    
    public String getReportFileName(final Registration registration) {
	final StringBuilder result = new StringBuilder();

	result.append(registration.getPerson().getIstUsername());
	result.append("-");
	result.append(new DateTime().toString(DateTimeFormat.forPattern("yyyyMMdd")));
	result.append("-");

	final ResourceBundle enumerationResources = ResourceBundle.getBundle("resources.EnumerationResources", LanguageUtils.getLocale());
	result.append(enumerationResources.getString("AcademicServiceRequestType.DOCUMENT"));
	result.append(enumerationResources.getString(DocumentRequestType.SCHOOL_REGISTRATION_DECLARATION.getName()));

	return result.toString().replace(" ", "");
    }

    private Registration getRegistration(final HttpServletRequest request) {
        final String registrationIDString = request.getParameter("registrationID");
        final Integer registrationID = Integer.valueOf(registrationIDString);
        return rootDomainObject.readRegistrationByOID(registrationID);
    }

    private ExecutionYear getExecutionYear(final HttpServletRequest request) {
        final String executionYearIDString = request.getParameter("executionYearID");
        final Integer executionYearID = Integer.valueOf(executionYearIDString);
        return rootDomainObject.readExecutionYearByOID(executionYearID);
    }

}
