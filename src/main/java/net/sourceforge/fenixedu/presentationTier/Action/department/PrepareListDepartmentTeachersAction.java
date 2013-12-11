/*
 * Created on Dec 1, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.department;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.department.ReadDepartmentByUser;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.bennu.core.security.Authenticate;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author jpvl
 */
@Mapping(module = "departmentAdmOffice", path = "/prepareListDepartmentTeachers", input = "/index.do",
        attribute = "searchTeachersDepartmentForm", formBean = "searchTeachersDepartmentForm", scope = "request")
@Forwards(value = { @Forward(name = "successfull-prepare", path = "/listDepartmentTeachers.do?method=doSearch") })
public class PrepareListDepartmentTeachersAction extends Action {

    /*
     * (non-Javadoc)
     * 
     * @seeorg.apache.struts.action.Action#execute(org.apache.struts.action.
     * ActionMapping, org.apache.struts.action.ActionForm,
     * javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        User userView = Authenticate.getUser();
        InfoDepartment infoDepartment = ReadDepartmentByUser.run(userView.getUsername());
        request.setAttribute("infoDepartment", infoDepartment);

        ActionForward actionForward = buildActionForward(mapping.findForward("successfull-prepare"), infoDepartment);
        return actionForward;
    }

    /**
     * @param forward
     * @param infoDepartment
     * @return
     */
    private ActionForward buildActionForward(ActionForward forward, InfoDepartment infoDepartment) {
        ActionForward forwardBuilded = new ActionForward();
        forwardBuilded.setName(forward.getName());
        forwardBuilded.setContextRelative(forward.getContextRelative());
        forwardBuilded.setRedirect(forward.getRedirect());
        StringBuilder path = new StringBuilder(forward.getPath());
        path.append("&externalId=").append(infoDepartment.getExternalId());
        forwardBuilded.setPath(path.toString());
        return forwardBuilded;
    }
}