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
package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.IrsDeclarationLink;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.person.PersonApplication.PersonalAreaApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

@StrutsFunctionality(app = PersonalAreaApp.class, descriptionKey = "label.irs.information", path = "irs-declaration",
        titleKey = "label.irs.information")
@Mapping(module = "person", path = "/irsDeclaration", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "edit.IRSDeclaration.link", path = "/person/editIRSDeclarationLink.jsp"),
        @Forward(name = "view.irsDocument.information", path = "/person/irs/irsDocumentInformation.jsp") })
public class IRSDeclarationAction extends FenixDispatchAction {

    public static class IRSDeclarationBean implements Serializable {
        MultiLanguageString title;
        Boolean available;
        String irsLink;

        public IRSDeclarationBean(IrsDeclarationLink irsDeclarationLink) {
            title = irsDeclarationLink.getTitle();
            available = irsDeclarationLink.getAvailable();
            irsLink = irsDeclarationLink.getIrsLink();
        }

        public MultiLanguageString getTitle() {
            return title;
        }

        public void setTitle(MultiLanguageString title) {
            this.title = title;
        }

        public Boolean getAvailable() {
            return available;
        }

        public void setAvailable(Boolean available) {
            this.available = available;
        }

        public String getIrsLink() {
            return irsLink;
        }

        public void setIrsLink(String irsLink) {
            this.irsLink = irsLink;
        }
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        final IrsDeclarationLink irsDeclarationLink = IrsDeclarationLink.getInstance();
        final IRSDeclarationBean declarationBean = new IRSDeclarationBean(irsDeclarationLink);
        request.setAttribute("declarationBean", declarationBean);
        return mapping.findForward("edit.IRSDeclaration.link");
    }

    public ActionForward editBean(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        final IRSDeclarationBean declarationBean = getRenderedObject("declarationBean");
        IrsDeclarationLink.set(declarationBean.getTitle(), declarationBean.getAvailable(), declarationBean.getIrsLink());
        return mapping.findForward("edit.IRSDeclaration.link");
    }

    @EntryPoint
    public ActionForward viewIrsDocumentInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("loggedPerson", AccessControl.getPerson());

        return mapping.findForward("view.irsDocument.information");
    }

}
