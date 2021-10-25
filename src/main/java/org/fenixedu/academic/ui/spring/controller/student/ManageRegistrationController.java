package org.fenixedu.academic.ui.spring.controller.student;

import org.fenixedu.academic.domain.accounting.EventTemplate;
import org.fenixedu.academic.domain.student.RegistrationDataByExecutionYear;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@SpringApplication(path = "registrationSelfService", hint = "Student", group = "anyone", title = "title.manage.registration")
@SpringFunctionality(app = ManageRegistrationController.class, title = "title.manage.registration")
@RequestMapping("/registrationSelfService")
public class ManageRegistrationController {

    private String redirectHome() {
        return "redirect:/registrationSelfService";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String home(Model model) {
        return "student/manageRegistration";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{dataByExecutionYear}/changePlan")
    public String listPlans(final Model model, final @PathVariable RegistrationDataByExecutionYear dataByExecutionYear) {
        final User user = Authenticate.getUser();
        return user == dataByExecutionYear.getRegistration().getPerson().getUser() ? "student/listEventTemplatePlans"
                : redirectHome();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{dataByExecutionYear}/changePlan/{eventTemplate}")
    public String prepareChangePlan(final Model model, final @PathVariable RegistrationDataByExecutionYear dataByExecutionYear,
                                    final @PathVariable EventTemplate eventTemplate) {
        final User user = Authenticate.getUser();
        return user == dataByExecutionYear.getRegistration().getPerson().getUser() ? "student/confirmEventTemplatePlan"
                : redirectHome();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{dataByExecutionYear}/changePlan/{eventTemplate}")
    public String changePlan(final Model model, final @PathVariable RegistrationDataByExecutionYear dataByExecutionYear,
                             final @PathVariable EventTemplate eventTemplate) {
        final User user = Authenticate.getUser();
        if (user == dataByExecutionYear.getRegistration().getPerson().getUser()) {
            eventTemplate.changePlanFor(dataByExecutionYear);
        }
        return redirectHome();
    }

}
