package org.fenixedu.academic.ui.spring.controller.scientificCouncil;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.degreeStructure.CurricularStage;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.DepartmentUnit;
import org.fenixedu.academic.service.services.bolonhaManager.EditCompetenceCourse;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.spring.controller.AcademicAdministrationSpringApplication;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.DynamicGroup;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;
import pt.ist.fenixframework.FenixFramework;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
@SpringFunctionality(app = AcademicAdministrationSpringApplication.class, title = "competence.course.management.title")
@RequestMapping("/competence-management")
public class CompetenceCourseController {

    @RequestMapping(method = RequestMethod.GET)
    public String home(@RequestParam(required = false) CurricularStage curricularStage, @RequestParam(required = false)
            DepartmentUnit
            departmentUnit, Model model, User user) {
        boolean isBolonhaManager = isBolonhaManager(user);
        boolean isScientificCouncilMember = isScientificCouncilMember(user);

        List<DepartmentUnit> departmentUnits = getDepartmentUnits(user, isBolonhaManager, isScientificCouncilMember);
        model.addAttribute("departmentUnits", departmentUnits);
        model.addAttribute("isBolonhaManager", isBolonhaManager);
        model.addAttribute("isScientificCouncilMember", isScientificCouncilMember);
        model.addAttribute("curricularStage", curricularStage);

        if (departmentUnit == null) {
            departmentUnit = departmentUnits.stream().findAny().orElse(null);
        }

        if (departmentUnit != null) {
            Group competenceCoursesManagementGroup = departmentUnit.getDepartment().getCompetenceCourseMembersGroup();
            if (competenceCoursesManagementGroup != null) {
                model.addAttribute("groupMembers", competenceCoursesManagementGroup.getMembers().collect(Collectors.toSet()));
            }
            model.addAttribute("scientificAreaUnits", departmentUnit.getScientificAreaUnits());
        }

        model.addAttribute("departmentUnit", departmentUnit);
        return resolveView("home");
    }

    @RequestMapping(value = "toggle", method = RequestMethod.GET)
    public String toggle(@RequestParam DepartmentUnit departmentUnit, @RequestParam CompetenceCourse
            competenceCourse, RedirectAttributes redirectAttributes) {
        try {
            CurricularStage changed =
                    (competenceCourse.getCurricularStage().equals(CurricularStage.PUBLISHED) ? CurricularStage.APPROVED : CurricularStage
                            .PUBLISHED);
            EditCompetenceCourse.runEditCompetenceCourse(competenceCourse.getExternalId(), changed);
        } catch (FenixServiceException e) {
            redirectAttributes.addFlashAttribute("error", (BundleUtil.getString(Bundle.BOLONHA, e.getMessage())));
        } catch (DomainException e) {
            redirectAttributes.addFlashAttribute("error", (BundleUtil.getString(Bundle.DOMAIN_EXCEPTION, e.getMessage())));
        }
        redirectAttributes.addAttribute("departmentUnit", departmentUnit.getExternalId());
        return "redirect:/competence-management";
    }

    @RequestMapping(value = "department/{department}/members", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity departmentMembers(@PathVariable Department department, User loggedUser) {
        final JsonArray members = new JsonArray();
        if (isScientificCouncilMember(loggedUser) || isBolonhaManager(loggedUser)) {
            Group competenceCourseMembersGroup = department.getCompetenceCourseMembersGroup();
            if (competenceCourseMembersGroup != null) {
                competenceCourseMembersGroup.getMembers().forEach(user -> {
                    JsonObject member = getJsonUser(user);
                    members.add(member);
                });
            }
            return ResponseEntity.ok(members.toString());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @RequestMapping(value = "department/{department}/{userToRevoke}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity revokeUser(@PathVariable Department department, @PathVariable User userToRevoke, User loggedUser) {
        return manageDepartmentGroup(department, userToRevoke, loggedUser, department.getCompetenceCourseMembersGroup()::revoke);
    }

    @RequestMapping(value = "department/{department}/{userToGrant}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity grantUser(@PathVariable Department department, @PathVariable User userToGrant, User loggedUser) {
        return manageDepartmentGroup(department, userToGrant, loggedUser, department.getCompetenceCourseMembersGroup()
                ::grant);
    }

    private ResponseEntity manageDepartmentGroup(Department department, User user, User loggedUser, Function<User, Group>
            supplier) {
        if (isScientificCouncilMember(loggedUser)) {
            FenixFramework.atomic(() -> {
                department.setCompetenceCourseMembersGroup(supplier.apply(user));
            });
            return ResponseEntity.ok(getJsonUser(user).toString());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    private List<DepartmentUnit> getDepartmentUnits(User user, boolean isBolonhaManager, boolean isScientificCouncilMember) {
        Stream<DepartmentUnit> departmentUnitStream = Stream.empty();
        if (isBolonhaManager || isScientificCouncilMember) {
            departmentUnitStream =
                    Bennu.getInstance().getDepartmentsSet().stream().sorted(Comparator.comparing(Department::getName))
                            .map(Department::getDepartmentUnit);

            if (!isScientificCouncilMember) {
                departmentUnitStream = departmentUnitStream.filter(du -> du.getDepartment().getCompetenceCourseMembersGroup()
                        .isMember(user));
            }
        }
        return departmentUnitStream.collect(Collectors.toList());
    }

    private String resolveView(String templateName) {
        return "fenixedu-academic/competence-courses/" + templateName;
    }

    private boolean isMember(String dynamicGroupName, User user) {
        return DynamicGroup.get(dynamicGroupName).isMember(user);
    }

    private boolean isScientificCouncilMember(User user) {
        return isMember("scientificCouncil", user);
    }

    private boolean isBolonhaManager(User user) {
        return isMember("bolonhaManager", user);
    }

    private JsonObject getJsonUser(User user) {
        JsonObject member = new JsonObject();
        member.addProperty("username", user.getUsername());
        member.addProperty("displayName", user.getDisplayName());
        return member;
    }

    @RequestMapping(value = "department/{department}/downloadCompetenceCourseInformation", method = RequestMethod.GET)
    public void downloadCompetenceCourseInformation(@PathVariable(required = false) DepartmentUnit department,
                                                    final HttpServletResponse response) throws Exception {
        final String name = "CompetenceCourseInformation_" + department.getAcronym();
        response.setHeader("Content-Disposition", "filename=" + name + ".xls");
        response.setContentType("application/vnd.ms-excel");
        final User loggedUser = Authenticate.getUser();
        if (isScientificCouncilMember(loggedUser) || isBolonhaManager(loggedUser)) {
            final Spreadsheet spreadsheet = DumpCompetenceCourseInformation.dumpInformation(department);
            spreadsheet.exportToXLSSheet(response.getOutputStream());
        }
        response.flushBuffer();
    }

    @RequestMapping(value = "{competenceCourse}/externalUrl", method = RequestMethod.GET)
    public String competenceCourseExternalUrl(@PathVariable(required = false) CompetenceCourse competenceCourse) {
        return "/competenceCourse/externalUrl";
    }

    @RequestMapping(value = "{competenceCourse}/externalUrl", method = RequestMethod.POST)
    public String changeCompetenceCourseExternalUrl(final @PathVariable(required = false) CompetenceCourse competenceCourse,
                                                    final @RequestParam(required = false) String externalUrl,
                                                    final HttpServletRequest request) {
        final User loggedUser = Authenticate.getUser();
        if (isScientificCouncilMember(loggedUser)) {
            competenceCourse.changeExternalUrl(externalUrl);
        }

        final String path = request.getContextPath()
                + "/scientificCouncil/competenceCourses/showCompetenceCourse.faces?action=ccm"
                + "&competenceCourseID=" + competenceCourse.getExternalId()
                + "&selectedDepartmentUnitID=" + competenceCourse.getDepartmentUnit().getExternalId();
        return "redirect:" + path + "&_request_checksum_=" + GenericChecksumRewriter.calculateChecksum(
                request.getContextPath() + path, request.getSession(false));
    }

}
