package net.sourceforge.fenixedu.presentationTier.Action.manager.competenceCourses;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.department.ReadAllDepartments;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement.CreateEditCompetenceCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement.DeleteCompetenceCourses;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement.ReadAllCompetenceCourses;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement.ReadCompetenceCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement.ReadCompetenceCoursesByDepartment;
import net.sourceforge.fenixedu.dataTransferObject.InfoCompetenceCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoDepartment;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

public class CompetenceCourseDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {
        IUserView userView = UserView.getUser();

        List<InfoDepartment> infoDepartments;
        try {
            infoDepartments = ReadAllDepartments.run();
        } catch (FenixServiceException fse) {
            throw new FenixActionException(fse.getMessage());
        }

        request.setAttribute("departments", infoDepartments);
        request.setAttribute("competenceCourses", new ArrayList());
        return mapping.findForward("showCompetenceCourses");
    }

    public ActionForward showDepartmentCompetenceCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        IUserView userView = UserView.getUser();

        DynaActionForm actionForm = (DynaActionForm) form;
        String departmentString = (String) actionForm.get("departmentID");

        List<InfoDepartment> infoDepartments;
        List<InfoCompetenceCourse> infoCompetenceCourses;

        try {
            infoCompetenceCourses = ReadCompetenceCoursesByDepartment.run(departmentString);
            infoDepartments = ReadAllDepartments.run();
        } catch (FenixServiceException fse) {
            throw new FenixActionException(fse.getMessage());
        }

        request.setAttribute("departments", infoDepartments);
        Collections.sort(infoCompetenceCourses, new BeanComparator("name", Collator.getInstance()));
        request.setAttribute("competenceCourses", infoCompetenceCourses);
        return mapping.findForward("showCompetenceCourses");
    }

    public ActionForward deleteCompetenceCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        IUserView userView = UserView.getUser();
        DynaActionForm actionForm = (DynaActionForm) form;

        String[] competenceCoursesIDs = (Integer[]) actionForm.get("competenceCoursesIds");

        try {
            DeleteCompetenceCourses.run(competenceCoursesIDs);
        } catch (DomainException e) {
            return setError(request, mapping, e.getMessage(), "readCompetenceCourses", null);
        }
        return mapping.findForward("readCompetenceCourses");
    }

    public ActionForward chooseDepartment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        IUserView userView = UserView.getUser();

        List<InfoDepartment> infoDepartments;
        try {
            infoDepartments = ReadAllDepartments.run();
        } catch (FenixServiceException fse) {
            throw new FenixActionException(fse.getMessage());
        }

        request.setAttribute("departments", infoDepartments);
        return mapping.findForward("chooseDepartment");
    }

    public ActionForward showAllCompetences(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        IUserView userView = UserView.getUser();

        List<InfoCompetenceCourse> infoCompetenceCoursesList = null;

        // try {

        infoCompetenceCoursesList = ReadAllCompetenceCourses.run();

        // } catch (FenixServiceException fenixServiceException) {
        // throw new FenixActionException(fenixServiceException.getMessage());
        // }

        Collections.sort(infoCompetenceCoursesList, new BeanComparator("name", Collator.getInstance()));

        request.setAttribute("competenceCourses", infoCompetenceCoursesList);
        return mapping.findForward("showAllCompetenceCourses");
    }

    public ActionForward showCompetenceCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        IUserView userView = UserView.getUser();

        String competenceCourseID = request.getParameter("competenceCourseID");

        InfoCompetenceCourse competenceCourse = null;
        try {
            competenceCourse = ReadCompetenceCourse.run(competenceCourseID);
        } catch (NotExistingServiceException notExistingServiceException) {

        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        request.setAttribute("competenceCourse", competenceCourse);
        return mapping.findForward("showCompetenceCourse");
    }

    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {
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
        String departmentID = (Integer) actionForm.get("departmentID");

        InfoCompetenceCourse competenceCourse = null;
        try {
            competenceCourse = CreateEditCompetenceCourse.run(null, code, name, new String[] { departmentID });
        } catch (InvalidArgumentsServiceException invalidArgumentsServiceException) {
            throw new FenixActionException(invalidArgumentsServiceException.getMessage());
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        request.setAttribute("competenceCourse", competenceCourse);
        return mapping.findForward("showCompetenceCourse");
    }

}