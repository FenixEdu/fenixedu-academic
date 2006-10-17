package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.StudentsSearchBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.ReportsUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class DeclarationsDA extends FenixDispatchAction {

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        StudentsSearchBean studentsSearchBean = (StudentsSearchBean) getRenderedObject();
        if (studentsSearchBean == null) {
            studentsSearchBean = new StudentsSearchBean();
        }
        request.setAttribute("studentsSearchBean", studentsSearchBean);
        return mapping.findForward("declarations-search-form");
    }

    public ActionForward registrationDeclaration(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws Exception {

        final Registration registration = getRegistration(request);

        if (registration != null) {
            final Map parameters = new HashMap();
            final List list = new ArrayList();
            net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice.RegistrationDeclaration registrationDeclaration
                    = new net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice.RegistrationDeclaration(registration, getLoggedPerson(request));
            list.add(registrationDeclaration);
            parameters.put("RegistrationDeclaration", registrationDeclaration);
            final byte[] data = ReportsUtils.exportToPdf("registrationDeclaration", parameters, net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice.RegistrationDeclaration.resourceBundle, list);
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename=registrationDeclaration.pdf");
            response.setContentLength(data.length);
            ServletOutputStream writer = response.getOutputStream();
            writer.write(data);
            writer.flush();
            writer.close();
            response.flushBuffer();
        }

        return null;
    }

    private Registration getRegistration(final HttpServletRequest request) {
        final String registrationIDString = request.getParameter("registrationID");
        final Integer registrationID = Integer.valueOf(registrationIDString);
        return rootDomainObject.readRegistrationByOID(registrationID);
    }

}
