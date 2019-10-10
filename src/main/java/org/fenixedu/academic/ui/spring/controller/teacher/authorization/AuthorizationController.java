/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.spring.controller.teacher.authorization;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.TeacherAuthorization;
import org.fenixedu.academic.domain.TeacherCategory;
import org.fenixedu.academic.ui.spring.controller.AcademicAdministrationSpringApplication;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Manage teacher authorizations controller
 * 
 * 
 * @author Sérgio Silva (sergio.silva@tecnico.ulisboa.pt)
 *
 */
@SpringFunctionality(app = AcademicAdministrationSpringApplication.class, title = "teacher.authorizations.title", accessGroup = "#managers")
@RequestMapping("/teacher/authorizations")
public class AuthorizationController {

	@Autowired
	AuthorizationService service;

	@Autowired
	MessageSource messageService;

	/***
	 * Helper method that returns webview resource for this context
	 * 
	 * @param string
	 * @return
	 */
	private String view(String string) {
		return "fenixedu-academic/teacher/authorization/" + string;
	}

	/***
	 * Helper method to redirect to home view with search parameters
	 * 
	 * @param search deparment and period used to filter the home view
	 * @return
	 */
	private String redirectHome(SearchBean search, RedirectAttributes attrs) {
		attrs.addAttribute("department",
				search.getDepartment() == null ? null : search.getDepartment().getExternalId());
		attrs.addAttribute("period", search.getPeriod().getExternalId());
		return "redirect:/teacher/authorizations";
	}

	/***
	 * Functionality entry point
	 * 
	 * @param model
	 * @param search
	 * @return
	 */

	@RequestMapping(method = GET)
	public String home(Model model, @ModelAttribute FormBean search) {
		if (search.getPeriod() == null) {
			search.setPeriod(service.getCurrentPeriod());
		}
		model.addAttribute("search", search);
		model.addAttribute("departments", service.getDepartments());
		model.addAttribute("periods", service.getExecutionPeriods());
		model.addAttribute("authorizations", service.searchAuthorizations(search));
		return view("show");
	}

	/***
	 * Shows all teacher categories
	 * 
	 * @param model
	 * @return
	 */

	@RequestMapping(method = GET, value = "categories")
	public String categories(Model model) {
		model.addAttribute("categories", service.getCategories());
		return view("categories/show");
	}

	/***
	 * Shows form view to create a teacher category
	 * 
	 * @param model
	 * @return
	 */

	@RequestMapping(method = GET, value = "categories/create")
	public String showCreate(Model model) {
		model.addAttribute("form", new CategoryBean());
		return view("categories/create");
	}

	/***
	 * Shows form view to edit an existing teacher category
	 * 
	 * @param model
	 * @param category the category to be edited
	 * @return
	 */
	@RequestMapping(method = GET, value = "categories/{category}")
	public String showEdit(Model model, @PathVariable TeacherCategory category) {
		model.addAttribute("form", category);
		return view("categories/create");
	}

	/***
	 * Action to edit or create a teacher category
	 * 
	 * @param model
	 * @param category the category to edit. If null it will create a new one
	 * @param form     the category information,
	 *                 {@link TeacherCategory#setCode(String)} must be unique.
	 * @return
	 */
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

	/**
	 * 
	 * Action to delete a teacher category
	 * 
	 * @param model
	 * @param category
	 * @return
	 */
	@RequestMapping(method = GET, value = "categories/delete/{category}")
	public String delete(Model model, @PathVariable TeacherCategory category) {

		try {
			service.deleteCategory(category);
		} catch (Exception e) {
			model.addAttribute("error", e.getLocalizedMessage());
		}

		model.addAttribute("categories", service.getCategories());
		return view("categories/show");
	}

	/***
	 * Downloads a CSV file with the authorization information filtered by
	 * {@link SearchBean}
	 * 
	 * @param model
	 * @param search
	 * @param attrs
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(method = GET, value = "download")
	public String download(Model model, @ModelAttribute SearchBean search, RedirectAttributes attrs,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "filename=" + service.getCsvFilename(search));
		service.dumpCSV(search, response.getOutputStream());
		response.flushBuffer();
		return redirectHome(search, attrs);
	}

	/***
	 * Shows webview with instructions to upload teacher authorizations CSV file
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(method = GET, value = "upload")
	public String showUpload(Model model) {
		model.addAttribute("currentUser", Authenticate.getUser());
		model.addAttribute("categories", service.getCategories());
		model.addAttribute("departments", service.getDepartments());
		model.addAttribute("periods", service.getExecutionPeriods());
		return view("upload");
	}

	/***
	 * Creates authorizations based on the CSV file
	 * 
	 * @param model
	 * @param period the period where to import the teacher authorizations
	 * @param csv
	 * @return
	 */
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

	/***
	 * Shows form to create a new teacher authorization with the previous
	 * 
	 * @param model
	 * @param search
	 * @return
	 */
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

	/***
	 * Entrypoint to create a new teacher authorization
	 * 
	 * @param model
	 * @param form
	 * @param attrs
	 * @return
	 */
	@RequestMapping(method = POST, value = "create")
	public String create(Locale loc, Model model, @ModelAttribute FormBean form, final RedirectAttributes attrs,
			BindingResult result) {
		if (form.getUser() == null) {
			model.addAttribute("error", messageService.getMessage("label.valid.username", null, loc));
			return showCreate(model, form);
		} else {
			service.createTeacherAuthorization(form);
			return redirectHome(form, attrs);
		}
	}

	/***
	 * Shows webview of all revoked teacher authorizations
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(method = GET, value = "revoked")
	public String revoked(Model model) {
		model.addAttribute("authorizations", service.getRevokedAuthorizations());
		return view("revoked");
	}

	/***
	 * Endpoint to revoke a specific teacher authorization
	 * 
	 * @param model
	 * @param authorization teacher authorization to revoke
	 * @param search
	 * @return
	 */
	@RequestMapping(method = POST, value = "{authorization}/revoke")
	public String revoke(Model model, @PathVariable TeacherAuthorization authorization,
			@Value("null") @ModelAttribute SearchBean search, final RedirectAttributes attrs) {

		service.revoke(authorization);
		return redirectHome(search, attrs);
	}

	private String redirectHome(FormBean search, RedirectAttributes attrs) {
		attrs.addAttribute("department",
				search.getDepartment() == null ? null : search.getDepartment().getExternalId());
		attrs.addAttribute("period", search.getPeriod().getExternalId());
		return "redirect:/teacher/authorizations";
	}
}
