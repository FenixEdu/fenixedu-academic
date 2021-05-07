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
package org.fenixedu.academic.ui.struts.action.scientificCouncil.curricularPlans;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.DegreeOfficialPublication;
import org.fenixedu.academic.domain.DegreeSpecializationArea;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.FenixActionForm;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.commons.FenixActionForward;
import org.fenixedu.academic.ui.struts.action.scientificCouncil.ScientificCouncilApplication.ScientificCurricularPlansManagement;
import org.fenixedu.academic.ui.struts.action.scientificCouncil.curricularPlans.OfficialPublicationBean.SpecializationName;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/curricularPlans/editOfficialPublication", module = "scientificCouncil", formBeanClass = FenixActionForm.class,
        functionality = ScientificCurricularPlansManagement.class)
@Forwards({
        @Forward(name = "prepare", path = "/scientificCouncil/curricularPlans/editOfficialPublication.jsp"),
        @Forward(name = "prepareSpecializationArea", path = "/scientificCouncil/curricularPlans/editDegreeSpecializationArea.jsp"),
        @Forward(name = "editDegree", path = "/scientificCouncil/curricularPlans/editDegree.jsp"),
        @Forward(name = "deletePublication", path = "/scientificCouncil/curricularPlans/deleteDegreeOfficialPublication.jsp") })
public class EditDegreeOfficialPublicationDA extends FenixDispatchAction {

    public ActionForward preparePubs(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        DegreeOfficialPublication degreeOfficialPublication = (DegreeOfficialPublication) request.getAttribute("officialPub");

        if (degreeOfficialPublication == null) {
            degreeOfficialPublication = FenixFramework.getDomainObject(request.getParameter("officialPubId"));
        }
        if (degreeOfficialPublication == null) {
            degreeOfficialPublication = (DegreeOfficialPublication) getRenderedObject("officialPub");
        }

        OfficialPublicationBean officialPublicationBean = new OfficialPublicationBean(degreeOfficialPublication);

        request.setAttribute("officialPub", degreeOfficialPublication);
        request.setAttribute("pubBean", officialPublicationBean);
        request.setAttribute("referenceBean", officialPublicationBean);

        return mapping.findForward("prepare");
    }

    public ActionForward updateOfficialPub(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        boolean error = false;
        OfficialPublicationBean bean = (OfficialPublicationBean) getRenderedObject("referenceBean");

        if (bean.getNewReference().compareTo("") == 0) {
            addErrorMessage(request, "error", "confirm.error.edit.reference.officialPublication");
            error = true;
        }

        if (!error) {
            bean.getDegreeOfficialPublication().changeOfficialreference(bean.getNewReference(), bean.getPublication(),bean.getLinkReference(),bean.getIncludeInDiplomaSuplement());
            addActionMessage("success", request, "confirm.success.edit.reference.officialPublication");
        }

        request.setAttribute("officialPub", bean.getDegreeOfficialPublication());
        return preparePubs(mapping, form, request, response);
    }

    public ActionForward updatePubs(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        RenderUtils.invalidateViewState("pubBean");
        return mapping.findForward("prepare");
    }

    public ActionForward updateArea(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        SpecializationName name = getRenderedObject("specializationArea");

        if (name.getEnName().compareTo("") == 0 || name.getPtName().compareTo("") == 0) {
            addErrorMessage(request, "error", "confirm.error.edit.name.specializationArea");
        } else {
            // update whether the names have been changed

            name.update();
        }
        request.setAttribute("officialPub", name.getSpecializationArea().getOfficialPublication());
        return preparePubs(mapping, form, request, response);
    }

    public ActionForward removeSpecializationArea(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        DegreeSpecializationArea specializationArea = FenixFramework.getDomainObject(request.getParameter("specializationId"));

        DegreeOfficialPublication degreeOfficialPublication = specializationArea.getOfficialPublication();
        OfficialPublicationBean bean = new OfficialPublicationBean(degreeOfficialPublication);

        bean.removeSpecializationArea(specializationArea);

        request.setAttribute("officialPub", degreeOfficialPublication);
        return preparePubs(mapping, form, request, response);
    }

    public ActionForward editSpecializationArea(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        DegreeSpecializationArea area = FenixFramework.getDomainObject(request.getParameter("specializationId"));
        request.setAttribute("specializationArea",
                new OfficialPublicationBean(area.getOfficialPublication()).new SpecializationName(area));
        return mapping.findForward("prepareSpecializationArea");
    }

    public ActionForward createNewSpecializationArea(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        OfficialPublicationBean bean = getRenderedObject("pubBean");

        DegreeOfficialPublication degreeOfficialPublication = bean.getDegreeOfficialPublication();

        if (bean.getNewNameEn().compareTo("") == 0 || bean.getNewNamePt().compareTo("") == 0) {
            addErrorMessage(request, "error", "confirm.error.edit.name.specializationArea");
            request.setAttribute("officialPub", degreeOfficialPublication);
            return preparePubs(mapping, form, request, response);
        }

        degreeOfficialPublication.createSpecializationArea(bean.getNewNameEn(), bean.getNewNamePt());
        request.setAttribute("officialPub", degreeOfficialPublication);
        return preparePubs(mapping, form, request, response);
    }

    public ActionForward prepareDeleteDegreeOfficialPublication(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        String degreeId = request.getParameter("degreeId");
        String officialPubId = request.getParameter("officialPubId");

        DegreeOfficialPublication publication = FenixFramework.getDomainObject(officialPubId);

        request.setAttribute("publication", publication);
        request.setAttribute("degreeId", degreeId);

        return mapping.findForward("deletePublication");
    }

    public ActionForward goToEditDegree(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String degreeId = request.getParameter("degreeId");

        return new FenixActionForward(request, new ActionForward("/curricularPlans/editDegree.faces?degreeId=" + degreeId, false));
    }

    public ActionForward deleteDegreeOfficialPublication(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        String degreeId = request.getParameter("degreeId");
        String officialPubId = request.getParameter("officialPubId");

        DegreeOfficialPublication publication = FenixFramework.getDomainObject(officialPubId);

        publication.delete();

        return new FenixActionForward(request, new ActionForward("/curricularPlans/editDegree.faces?degreeId=" + degreeId, false));
    }
}
