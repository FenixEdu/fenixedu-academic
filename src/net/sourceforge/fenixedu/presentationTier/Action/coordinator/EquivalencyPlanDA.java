package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.EquivalencePlan;
import net.sourceforge.fenixedu.domain.EquivalencePlanEntry;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.EquivalencePlanEntry.EquivalencePlanEntryCreator;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class EquivalencyPlanDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
	if (degreeCurricularPlan == null) {
	    final Set<DegreeType> degreeTypes = new HashSet<DegreeType>();
	    degreeTypes.add(DegreeType.BOLONHA_DEGREE);
	    degreeTypes.add(DegreeType.BOLONHA_MASTER_DEGREE);
	    degreeTypes.add(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
	    request.setAttribute("degreeCurricularPlans", DegreeCurricularPlan.getDegreeCurricularPlans(degreeTypes));
	} else {
	    request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);
	}
	return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward showPlan(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	return mapping.findForward("showPlan");
    }

    public ActionForward showTable(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	request.setAttribute("viewTable", Boolean.TRUE);
	final DegreeModule degreeModule = getDegreeModule(request);
	if (degreeModule != null) {
	    final EquivalencePlan equivalencePlan = getEquivalencePlan(request);
	    final Set<EquivalencePlanEntry> equivalencePlanEntries = degreeModule.getNewDegreeModuleEquivalencePlanEntries(equivalencePlan);
	    request.setAttribute("equivalencePlanEntries", equivalencePlanEntries);
	}
	return mapping.findForward("showPlan");
    }

    public ActionForward prepareAddEquivalency(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	EquivalencePlanEntryCreator equivalencePlanEntryCreator = (EquivalencePlanEntryCreator) getRenderedObject();
	final EquivalencePlan equivalencePlan;
	if (equivalencePlanEntryCreator == null) {
	    equivalencePlan = getEquivalencePlan(request);
	    final DegreeModule degreeModule = getDegreeModule(request);
	    equivalencePlanEntryCreator = new EquivalencePlanEntryCreator(equivalencePlan);
	    equivalencePlanEntryCreator.setDestinationDegreeModuleToAdd(degreeModule);
	    equivalencePlanEntryCreator.addDestination(degreeModule);
	} else {
	    equivalencePlan = equivalencePlanEntryCreator.getEquivalencePlan();
	    equivalencePlanEntryCreator.setDestinationDegreeModuleToAdd(null);
	    equivalencePlanEntryCreator.setOriginDegreeModuleToAdd(null);
	}
	RenderUtils.invalidateViewState();
	request.setAttribute("equivalencePlan", equivalencePlan);
	request.setAttribute("equivalencePlanEntryCreator", equivalencePlanEntryCreator);
	return mapping.findForward("addEquivalency");
    }

    public ActionForward deleteEquivalency(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	final EquivalencePlanEntry equivalencePlanEntry = getEquivalencePlanEntry(request);
	final Object[] args = { equivalencePlanEntry };
	executeService(request, "DeleteEquivalencePlanEntry", args);
	return mapping.findForward("showPlan");
    }

    private EquivalencePlanEntry getEquivalencePlanEntry(HttpServletRequest request) {
	final String equivalencePlanEntryIDString = request.getParameter("equivalencePlanEntryID");
	final Integer equivalencePlanEntryID = getInteger(equivalencePlanEntryIDString);
	return equivalencePlanEntryID == null ? null : (EquivalencePlanEntry) RootDomainObject.getInstance().readEquivalencePlanEntryByOID(equivalencePlanEntryID);
    }

    private DegreeModule getDegreeModule(HttpServletRequest request) {
	final String degreeModuleIDString = request.getParameter("degreeModuleID");
	final Integer degreeModuleID = getInteger(degreeModuleIDString);
	return degreeModuleID == null ? null : (DegreeModule) RootDomainObject.getInstance().readDegreeModuleByOID(degreeModuleID);
    }

    private EquivalencePlan getEquivalencePlan(HttpServletRequest request) {
	final String equivalencePlanIDString = request.getParameter("equivalencePlanID");
	final Integer equivalencePlanID = getInteger(equivalencePlanIDString);
	return equivalencePlanID == null ? null : RootDomainObject.getInstance()
		.readEquivalencePlanByOID(equivalencePlanID);
    }

    private DegreeCurricularPlan getDegreeCurricularPlan(HttpServletRequest request) {
	final String degreeCurricularPlanIDString = request.getParameter("degreeCurricularPlanID");
	final Integer degreeCurricularPlanID = getInteger(degreeCurricularPlanIDString);
	return degreeCurricularPlanID == null ? null : RootDomainObject.getInstance()
		.readDegreeCurricularPlanByOID(degreeCurricularPlanID);
    }

    private Integer getInteger(final String string) {
	return isValidNumber(string) ? Integer.valueOf(string) : null;
    }

    private boolean isValidNumber(final String string) {
	return string != null && string.length() > 0 && StringUtils.isNumeric(string);
    }

}
