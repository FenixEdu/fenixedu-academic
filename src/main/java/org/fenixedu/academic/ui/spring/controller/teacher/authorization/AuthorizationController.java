package org.fenixedu.academic.ui.spring.controller.teacher.authorization;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.TeacherAuthorization;
import org.fenixedu.academic.domain.TeacherCategory;
import org.fenixedu.academic.ui.spring.controller.AcademicAdministrationSpringApplication;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@SpringFunctionality(app = AcademicAdministrationSpringApplication.class, title = "teacher.authorizations.title",
        accessGroup = "academic(MANAGE_TEACHER_AUTHORIZATIONS)")
@RequestMapping("/teacher/authorizations")
public class AuthorizationController {

    @Autowired
    AuthorizationService service;

    private String view(String string) {
        return "fenixedu-academic/teacher/authorization/" + string;
    }

    @RequestMapping(method = GET, value = "categories")
    public String categories(Model model) {
        model.addAttribute("categories", service.getCategories());
        return view("categories/show");
    }

    @RequestMapping(method = GET, value = "categories/create")
    public String showCreate(Model model) {
        model.addAttribute("form", new CategoryBean());
        return view("categories/create");
    }

    @RequestMapping(method = GET, value = "categories/{category}")
    public String showEdit(Model model, @PathVariable TeacherCategory category) {
        model.addAttribute("form", category);
        return view("categories/create");
    }

    @RequestMapping(method = POST, value = "categories/{category}")
    public String createOrEdit(Model model, @Value("null") @PathVariable TeacherCategory category,
            @ModelAttribute CategoryBean form) {
        try {
            if (category != null) {
                service.editCategory(category, form);
            } else {
                service.createCategory(form);
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getLocalizedMessage());
            model.addAttribute("form", form);
            return view("categories/create");
        }

        return "redirect:/teacher/authorizations/categories";
    }

    @RequestMapping(method = GET, value = "download")
    public String download(Model model, @ModelAttribute FormBean search, RedirectAttributes attrs, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "filename=" + service.getCsvFilename(search));
        service.dumpCSV(search, response.getOutputStream());
        response.flushBuffer();
        return redirectHome(search, attrs);
    }

    @RequestMapping(method = GET, value = "upload")
    public String showUpload(Model model) {
        model.addAttribute("currentUser", Authenticate.getUser());
        model.addAttribute("categories", service.getCategories());
        model.addAttribute("departments", service.getDepartments());
        model.addAttribute("periods", service.getExecutionPeriods());
        return view("upload");
    }

    @RequestMapping(method = POST, value = "upload")
    public String upload(Model model, @RequestParam ExecutionSemester period, @RequestParam MultipartFile csv) {
        try {
            List<TeacherAuthorization> imported = service.importCSV(period, csv);
            model.addAttribute("authorizations", imported);
        } catch (RuntimeException re) {
            model.addAttribute("error", re.getLocalizedMessage());
        }
        return view("upload-finished");
    }

    @RequestMapping(method = GET)
    public String home(Model model, @ModelAttribute FormBean search) {
        if (search.getPeriod() == null) {
            search.setPeriod(service.getExecutionPeriods().isEmpty() ? null : service.getExecutionPeriods().get(0));
        }
        model.addAttribute("search", search);
        model.addAttribute("departments", service.getDepartments());
        model.addAttribute("periods", service.getExecutionPeriods());
        model.addAttribute("authorizations", service.searchAuthorizations(search));
        return view("show");
    }

    @RequestMapping(method = GET, value = "create")
    public String showCreate(Model model, @Value("null") @ModelAttribute FormBean search) {
        if (search.getPeriod() == null) {
            search.setPeriod(service.getExecutionPeriods().isEmpty() ? null : service.getExecutionPeriods().get(0));
        }
        model.addAttribute("formBean", search);
        model.addAttribute("departments", service.getDepartments());
        model.addAttribute("periods", service.getExecutionPeriods());
        model.addAttribute("categories", service.getCategories());
        return view("create");
    }

    @RequestMapping(method = POST, value = "create")
    public String create(Model model, @ModelAttribute FormBean form, final RedirectAttributes attrs) {
        service.createTeacherAuthorization(form);
        return redirectHome(form, attrs);
    }

    @RequestMapping(method = GET, value = "revoked")
    public String revoked(Model model) {
        model.addAttribute("authorizations", service.getRevokedAuthorizations());
        return view("revoked");
    }

    @RequestMapping(method = POST, value = "{authorization}/revoke")
    public String revoke(Model model, @PathVariable TeacherAuthorization authorization,
            @Value("null") @ModelAttribute FormBean search, final RedirectAttributes attrs) {
        service.revoke(authorization);
        return redirectHome(search, attrs);
    }

    private String redirectHome(FormBean search, RedirectAttributes attrs) {
        attrs.addAttribute("department", search.getDepartment() == null ? null : search.getDepartment().getExternalId());
        attrs.addAttribute("period", search.getPeriod().getExternalId());
        return "redirect:/teacher/authorizations";
    }
}
