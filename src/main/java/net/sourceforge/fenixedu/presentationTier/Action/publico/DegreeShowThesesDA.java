package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeSite;
import net.sourceforge.fenixedu.domain.functionalities.AbstractFunctionalityContext;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "publico", path = "/showDegreeTheses", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "showThesisDetails", path = "degree-showDegreeThesisDetails"),
        @Forward(name = "showTheses", path = "degree-showDegreeTheses") })
public class DegreeShowThesesDA extends PublicShowThesesDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        request.setAttribute("degree", getDegree(request));
        return super.execute(mapping, form, request, response);
    }

    public Degree getDegree(HttpServletRequest request) throws FenixActionException {

        FilterFunctionalityContext currentContext =
                (FilterFunctionalityContext) AbstractFunctionalityContext.getCurrentContext(request);
        DegreeSite selectedContainer = (DegreeSite) currentContext.getSelectedContainer();
        Degree degree = selectedContainer.getDegree();

        if (degree == null) {
            throw new FenixActionException();
        }

        request.setAttribute("degreeID", degree.getExternalId());
        request.setAttribute("degree", degree);

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
