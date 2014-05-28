/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/loginForIdentificationCard", module = "external")
public class RedirectToIdentificationCardInterfaceDA extends FenixDispatchAction {

    public ActionForward redirect(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final User user = Authenticate.getUser();
        if (user == null) {
            return new ActionForward(
                    "https://id.ist.utl.pt/cas/login?service=https%3A%2F%2Fbarra.tecnico.ulisboa.pt%2Flogin%2F%3Fnext%3Dhttps%253A%252F%252Fid.ist.utl.pt%252Fcas%252Flogin%253Fservice%253Dhttps%253A%252F%252Ffenix.ist.utl.pt%252Fexternal%252FloginForIdentificationCard.do%253Fmethod%253Dredirect",
                    true);
        } else if (user.getPerson().hasRole(RoleType.PERSON)) {
            final ActionForward actionForward = new ActionForward();
            actionForward.setRedirect(true);
            actionForward.setModule("");

            final String path = "/person/identificationCard.do?method=prepare";

            actionForward.setPath(path + "&" + GenericChecksumRewriter.CHECKSUM_ATTRIBUTE_NAME + "="
                    + GenericChecksumRewriter.calculateChecksum(path, request.getSession()));
            return actionForward;
        } else {
            return null;
        }
    }

}