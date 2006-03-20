package net.sourceforge.fenixedu.presentationTier.Action.research;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.ResearchInterest;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ViewCurriculumDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        List<ResearchInterest> orderedInterests = getOrderedInterests(request);

        request.setAttribute("researchInterests", orderedInterests);
        
        return mapping.findForward("Success");
    }

    private List<ResearchInterest> getOrderedInterests(HttpServletRequest request) throws FenixFilterException, FenixServiceException {
        List<ResearchInterest> researchInterests = getUserView(request).getPerson()
                .getResearchInterests();

        List<ResearchInterest> orderedInterests = new ArrayList<ResearchInterest>(researchInterests);
        Collections.sort(orderedInterests, new Comparator<ResearchInterest>() {
            public int compare(ResearchInterest researchInterest1, ResearchInterest researchInterest2) {
                return researchInterest1.getOrder().compareTo(researchInterest2.getOrder());
            }
        });
        return orderedInterests;
    }

}