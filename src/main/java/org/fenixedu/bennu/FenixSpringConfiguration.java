package org.fenixedu.bennu;

import org.fenixedu.bennu.spring.BennuSpringModule;

@BennuSpringModule(basePackages = { "org.fenixedu.core.service", "org.fenixedu.academic.ui.spring" },
        bundles = "ApplicationResources")
public class FenixSpringConfiguration {

}
