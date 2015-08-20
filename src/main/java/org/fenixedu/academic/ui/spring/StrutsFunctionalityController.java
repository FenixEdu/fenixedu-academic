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
package org.fenixedu.academic.ui.spring;

import javax.servlet.http.HttpServletRequest;

import org.fenixedu.bennu.core.domain.exceptions.AuthorizationException;
import org.fenixedu.bennu.core.rest.JsonAwareResource;
import org.fenixedu.bennu.portal.domain.MenuFunctionality;
import org.fenixedu.bennu.portal.model.Functionality;
import org.fenixedu.bennu.portal.servlet.BennuPortalDispatcher;
import org.fenixedu.bennu.struts.portal.RenderersAnnotationProcessor;
import org.springframework.web.bind.annotation.ModelAttribute;

public abstract class StrutsFunctionalityController extends JsonAwareResource {

    protected abstract Class<?> getFunctionalityType();

    @ModelAttribute
    private void selectStrutsFunctionality(HttpServletRequest request) {
        Functionality functionality = RenderersAnnotationProcessor.getFunctionalityForType(getFunctionalityType());
        MenuFunctionality menuItem = MenuFunctionality.findFunctionality(functionality.getProvider(), functionality.getKey());
        if (menuItem == null || !menuItem.isAvailableForCurrentUser()) {
            throw AuthorizationException.unauthorized();
        }
        BennuPortalDispatcher.selectFunctionality(request, menuItem);
    }
}
