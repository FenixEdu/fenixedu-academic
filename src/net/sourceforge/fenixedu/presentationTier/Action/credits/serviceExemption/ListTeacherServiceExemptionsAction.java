/*
 * Created on 7/Mar/2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.credits.serviceExemption;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author jpvl
 */
public class ListTeacherServiceExemptionsAction extends Action {

    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
           
        InfoTeacher infoTeacher = (InfoTeacher) request.getAttribute("infoTeacher");       

        final Object[] argsToRead = { Teacher.class, infoTeacher.getIdInternal() };
        Teacher teacher = (Teacher) ServiceUtils.executeService(null, "ReadDomainObject", argsToRead);
                     
        request.setAttribute("infoServiceExemptions", teacher.getServiceExemptionSituations());

        return mapping.findForward("successfull-read");
    }
}