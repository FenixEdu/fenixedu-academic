package net.sourceforge.fenixedu.presentationTier.Action.manager.competenceCourses;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.department.ReadAllDepartments;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement.CreateEditCompetenceCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement.ReadCompetenceCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCompetenceCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

public class CreateEditCompetenceCourseDispatchAction extends FenixDispatchAction {

    public ActionForward prepareCreate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        IUserView userView = UserView.getUser();

        List<InfoDepartment> departmentList = null;
        try {
            departmentList = ReadAllDepartments.run();
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        request.setAttribute("departments", departmentList);
        return mapping.findForward("createCompetenceCourse");
    }

    public ActionForward createCompetenceCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        IUserView userView = UserView.getUser();
        DynaActionForm actionForm = (DynaActionForm) form;

        String code = (String) actionForm.get("code");
        String name = (String) actionForm.get("name");
        String[] departmentIDs = (String[]) actionForm.get("departmentIDs");

        InfoCompetenceCourse competenceCourse = null;
        try {
            competenceCourse = CreateEditCompetenceCourse.run(null, code, name, departmentIDs);
        } catch (InvalidArgumentsServiceException invalidArgumentsServiceException) {
            throw new FenixActionException(invalidArgumentsServiceException.getMessage());
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        request.setAttribute("competenceCourse", competenceCourse);
        return mapping.findForward("showCompetenceCourse");
    }

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        IUserView userView = UserView.getUser();

        String competenceCourseID = request.getParameter("competenceCourse");

        InfoCompetenceCourse competenceCourse = null;
        List<InfoDepartment> infoDepartments = null;
        try {
            competenceCourse = ReadCompetenceCourse.run(competenceCourseID);
            infoDepartments = ReadAllDepartments.run();
        } catch (NotExistingServiceException notExistingServiceException) {

        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        request.setAttribute("departments", infoDepartments);
        request.setAttribute("competenceCourse", competenceCourse);

        DynaActionForm actionForm = (DynaActionForm) form;
        actionForm.set("competenceCourseID", competenceCourse.getExternalId());
        actionForm.set("code", competenceCourse.getCode());
        actionForm.set("name", competenceCourse.getName());
        List<String> departmentIDs = new ArrayList<String>();
        for (InfoDepartment department : competenceCourse.getDepartments()) {
            departmentIDs.add(department.getExternalId());
        }
        String[] a = new String[departmentIDs.size()];
        departmentIDs.toArray(a);
        actionForm.set("departmentIDs", a);

        return mapping.findForward("edit");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {
        IUserView userView = UserView.getUser();
        DynaActionForm actionForm = (DynaActionForm) form;
        String competenceCourseID = (String) actionForm.get("competenceCourseID");
        String code = (String) actionForm.get("code");
        String name = (String) actionForm.get("name");
        String[] departmentIDs = (String[]) actionForm.get("departmentIDs");

        InfoCompetenceCourse competenceCourse = null;
        try {
            competenceCourse = CreateEditCompetenceCourse.run(competenceCourseID, code, name, departmentIDs);

        } catch (InvalidArgumentsServiceException invalidArgumentsServiceException) {
            throw new FenixActionException(invalidArgumentsServiceException.getMessage());
        } catch (NonExistingServiceException nonExistingServiceException) {
            throw new FenixActionException(nonExistingServiceException.getMessage());
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        request.setAttribute("competenceCourse", competenceCourse);
        return mapping.findForward("showCompetenceCourse");
    }
}