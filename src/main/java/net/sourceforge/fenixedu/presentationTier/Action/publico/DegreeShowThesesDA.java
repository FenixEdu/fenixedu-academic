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
package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeSite;
import net.sourceforge.fenixedu.domain.Site.SiteMapper;
import net.sourceforge.fenixedu.domain.cms.OldCmsSemanticURLHandler;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Strings;

@Mapping(module = "publico", path = "/showDegreeTheses", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "showThesisDetails", path = "degree-showDegreeThesisDetails"),
        @Forward(name = "showTheses", path = "degree-showDegreeTheses") })
public class DegreeShowThesesDA extends PublicShowThesesDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Degree degree = getDegree(request);
        if (degree == null) {
            return new ActionForward("/notFound.jsp");
        }
        request.setAttribute("degree", degree);
        return super.execute(mapping, form, request, response);
    }

    private Degree getDegree(HttpServletRequest request) {
        final DegreeSite site = SiteMapper.getSite(request);
        Degree degree = null;
        if (site != null) {
            degree = site.getDegree();
        } else {
            String degreeId = FenixContextDispatchAction.getFromRequest("degreeID", request);
            if (!Strings.isNullOrEmpty(degreeId) && !"null".equals(degreeId)) {
                DomainObject obj = FenixFramework.getDomainObject(degreeId);
                if (obj instanceof Degree) {
                    degree = (Degree) obj;
                }
            }
        }
        if (degree != null) {
            request.setAttribute("degreeID", degree.getExternalId());
            request.setAttribute("degree", degree);
            OldCmsSemanticURLHandler.selectSite(request, degree.getSite());
        }
        return degree;
    }

    @Override
    protected ThesisFilterBean getFilterBean(HttpServletRequest request) throws Exception {
        ThesisFilterBean bean = super.getFilterBean(request);
        bean.setDegree(getDegree(request));
        bean.setDegreeOptions(Degree.readNotEmptyDegrees());
        return bean;
    }

}
