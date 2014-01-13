package net.sourceforge.fenixedu.presentationTier.Action.department;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.commons.UnitFunctionalities;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.ProjectSubmissionsManagementDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "departmentAdmOffice", path = "/departmentFunctionalities", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "uploadFile", path = "department-upload-file", tileProperties = @Tile(
                title = "private.department.departmentspecific.files")),
        @Forward(name = "manageFiles", path = "department-manage-files", tileProperties = @Tile(
                title = "private.department.departmentspecific.files")),
        @Forward(name = "editUploaders", path = "department-edit-uploaders", tileProperties = @Tile(
                title = "private.department.departmentspecific.files")),
        @Forward(name = "managePersistedGroups", path = "manage-persisted-groups", tileProperties = @Tile(
                title = "private.department.departmentspecific.files")),
        @Forward(name = "editFile", path = "department-edit-file", tileProperties = @Tile(
                title = "private.department.departmentspecific.files")),
        @Forward(name = "editPersistedGroup", path = "edit-persisted-group", tileProperties = @Tile(
                title = "private.department.departmentspecific.files")),
        @Forward(name = "createPersistedGroup", path = "create-persisted-group", tileProperties = @Tile(
                title = "private.department.departmentspecific.files")) })
public class DepartmentFunctionalities extends UnitFunctionalities {

    final ProjectSubmissionsManagementDispatchAction action = new ProjectSubmissionsManagementDispatchAction();

    public ActionForward showProjects(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Unit unit = getUnit(request);
        final Department department = unit.getDepartment();
        Map<ExecutionCourse, Set<Project>> coursesProjects = new HashMap<ExecutionCourse, Set<Project>>();
        for (Project project : department.getProjects()) {
            for (ExecutionCourse course : project.getAssociatedExecutionCourses()) {
                Set<Project> projects = coursesProjects.get(course);
                if (projects == null) {
                    projects = new HashSet<Project>();
                    coursesProjects.put(course, projects);
                }
                projects.add(project);
            }
        }
        request.setAttribute("coursesProjects", coursesProjects);
        return mapping.findForward("showProjects");
    }

    public ActionForward viewLastProjectSubmissionForEachGroup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        // final ActionForward forward =
        // mapping.findForward("viewLastProjectSubmissionForEachGroup");
        action.execute(mapping, form, request, response);
        return action.viewLastProjectSubmissionForEachGroup(mapping, form, request, response);

    }

    public ActionForward viewProjectSubmissionsByGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // final ActionForward forward =
        // mapping.findForward("viewProjectSubmissionsByGroup");
        action.execute(mapping, form, request, response);
        return action.viewProjectSubmissionsByGroup(mapping, form, request, response);
        // return forward;
    }

    public ActionForward downloadProjectsInZipFormat(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, IOException, ServletException {
        return action.downloadProjectsInZipFormat(mapping, form, request, response);
    }

    public ActionForward prepareSelectiveDownload(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, IOException, ServletException {
        return action.prepareSelectiveDownload(mapping, form, request, response);
    }

    public ActionForward selectiveDownload(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, IOException, ServletException {
        return action.selectiveDownload(mapping, form, request, response);
    }

}
