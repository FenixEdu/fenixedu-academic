package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.student.StudentsSearchBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SearchForStudentsDA extends FenixDispatchAction {

    public ActionForward prepareSearch(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        StudentsSearchBean studentsSearchBean = (StudentsSearchBean) getRenderedObject();
        if (studentsSearchBean == null) {
            studentsSearchBean = new StudentsSearchBean();
        }
        request.setAttribute("studentsSearchBean", studentsSearchBean);
	return mapping.findForward("search");
    }

}
