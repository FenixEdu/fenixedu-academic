package org.fenixedu.academic.ui.spring;

import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/academic")
@SpringApplication(group = "logged", path = "academic", title = "title.academic")
@SpringFunctionality(app = AcademicController.class, title = "title.academic")
public class AcademicController {
}
