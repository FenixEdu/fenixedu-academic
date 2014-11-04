package org.fenixedu.core.ui;

import javax.servlet.http.HttpServletRequest;

import org.fenixedu.bennu.portal.domain.MenuFunctionality;
import org.fenixedu.bennu.portal.model.Functionality;
import org.fenixedu.bennu.portal.servlet.BennuPortalDispatcher;
import org.fenixedu.bennu.struts.portal.RenderersAnnotationProcessor;
import org.springframework.web.bind.annotation.ModelAttribute;

public abstract class StrutsFunctionalityController {

    protected abstract Class<?> getFunctionalityType();

    @ModelAttribute
    private void selectStrutsFunctionality(HttpServletRequest request) {
        Functionality functionality = RenderersAnnotationProcessor.getFunctionalityForType(getFunctionalityType());
        BennuPortalDispatcher.selectFunctionality(request,
                MenuFunctionality.findFunctionality(functionality.getProvider(), functionality.getKey()));
    }
}
