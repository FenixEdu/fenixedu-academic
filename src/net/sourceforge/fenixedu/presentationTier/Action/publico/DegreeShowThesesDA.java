package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class DegreeShowThesesDA extends PublicShowThesesDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse resposne) throws Exception {
        request.setAttribute("degree", getDegree(request));
        
        return super.execute(mapping, form, request, resposne);
    }
    
    public Degree getDegree(HttpServletRequest request) throws FenixActionException {
        Integer degreeId = getIntegerFromRequest(request, "degreeID");
        Degree degree = rootDomainObject.readDegreeByOID(degreeId);
        
        if (degree == null) {
            throw new FenixActionException();
        }
        
        request.setAttribute("degreeID", degreeId);
        request.setAttribute("degree", degree);

        return degree;
    }

    @Override
    protected ThesisFilterBean getFilterBean(HttpServletRequest request) throws Exception {
        ThesisFilterBean bean = super.getFilterBean(request);
        
        bean.setDegree(getDegree(request));
        bean.setDegreeOptions(RootDomainObject.getInstance().getDegrees());
        
        return bean;
    }
    
}
