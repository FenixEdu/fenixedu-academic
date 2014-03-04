package net.sourceforge.fenixedu.presentationTier.Action.alumni;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniSearchBean;
import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.alumni.AlumniApplication.AlumniAcademicPathApp;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = AlumniAcademicPathApp.class, path = "search-alumni", titleKey = "link.search.alumni")
@Mapping(module = "alumni", path = "/searchAlumni")
@Forwards({ @Forward(name = "viewAlumniDetails", path = "/alumni/viewAlumniDetails.jsp"),
        @Forward(name = "showAlumniList", path = "/alumni/showAlumniList.jsp") })
public class AlumniSearchDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward showAlumniList(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        AlumniSearchBean searchBean = reconstructBeanFromRequest(request);

        if (!StringUtils.isEmpty(searchBean.getName()) || searchBean.getDegreeType() != null) {

            List<Registration> resultRegistrations = readAlumniRegistrations(searchBean);

            if (request.getParameter("sort") != null && request.getParameter("sort").length() > 0) {
                resultRegistrations = RenderUtils.sortCollectionWithCriteria(resultRegistrations, request.getParameter("sort"));
            } else {
                resultRegistrations = RenderUtils.sortCollectionWithCriteria(resultRegistrations, "student.person.name");
            }

            searchBean.setAlumni(new ArrayList<Registration>(resultRegistrations));
            searchBean.setTotalItems(resultRegistrations.size());
        }

        request.setAttribute("searchAlumniBean", searchBean);
        return mapping.findForward("showAlumniList");
    }

    public ActionForward degreeTypePostback(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        AlumniSearchBean searchBean = reconstructBeanFromRequest(request);
        RenderUtils.invalidateViewState();
        request.setAttribute("searchAlumniBean", searchBean);
        return mapping.findForward("showAlumniList");
    }

    protected AlumniSearchBean reconstructBeanFromRequest(HttpServletRequest request) {

        final IViewState viewState = RenderUtils.getViewState("searchAlumniBean");
        if (viewState != null) {

            return (AlumniSearchBean) viewState.getMetaObject().getObject();

        } else if (request.getParameter("beansearch") != null && request.getParameter("beansearch").length() > 0) {
            return AlumniSearchBean.getBeanFromParameters(request.getParameter("beansearch"));
        }

        return new AlumniSearchBean();
    }

    public ActionForward visualizeAlumni(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("alumniData", getDomainObject(request, "studentId"));
        return mapping.findForward("viewAlumniDetails");
    }

    protected List<Registration> readAlumniRegistrations(AlumniSearchBean searchBean) {
        return Alumni.readAlumniRegistrations(searchBean);
    }
}
