package org.fenixedu.bennu;

import org.fenixedu.bennu.spring.BennuSpringModule;

@BennuSpringModule(basePackages = { "org.fenixedu.core.service", "org.fenixedu.academic.ui", "org.fenixedu.core.ui" },
        bundles = "FenixEduAcademicResources")
public class FenixSpringConfiguration {

}
