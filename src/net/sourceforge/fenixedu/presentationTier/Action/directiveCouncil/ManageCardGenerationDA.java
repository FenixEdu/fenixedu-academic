package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationBatch;
import net.sourceforge.fenixedu.domain.cardGeneration.Category;
import net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationBatch.CardGenerationBatchCreator;
import net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationBatch.CardGenerationBatchDeleter;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ManageCardGenerationDA extends FenixDispatchAction {

    public ActionForward firstPage(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
	    final HttpServletResponse response) {
	checkForProblemasInCategoryCodes(request);
	setContext(request);
	return mapping.findForward("firstPage");
    }

    public ActionForward showCategoryCodes(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) {
	request.setAttribute("categories", Category.values());
	checkForProblemasInDegrees(request);
	return mapping.findForward("showCategoryCodes");
    }

    public ActionForward showDegreeCodesAndLabels(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final DegreeType degreeType = getDegreeType(request);
	final Set<Degree> degrees = getDegrees(degreeType);
	request.setAttribute("degrees", degrees);
	return mapping.findForward("showDegreeCodesAndLabels");
    }

    public ActionForward editDegree(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final Degree degree = getDegree(request);
	request.setAttribute("degree", degree);
	return mapping.findForward("editDegree");
    }

    public ActionForward createCardGenerationBatch(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final ExecutionYear executionYear = getExecutionYear(request);
	final CardGenerationBatchCreator cardGenerationBatchCreator = new CardGenerationBatchCreator(executionYear);
	executeFactoryMethod(cardGenerationBatchCreator);
	return firstPage(mapping, actionForm, request, response);
    }

    public ActionForward deleteCardGenerationBatch(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final CardGenerationBatch cardGenerationBatch = getCardGenerationBatch(request);
	final CardGenerationBatchDeleter cardGenerationBatchDeleter = new CardGenerationBatchDeleter(cardGenerationBatch);
	executeFactoryMethod(cardGenerationBatchDeleter);
	return firstPage(mapping, actionForm, request, response);
    }

    public ActionForward manageCardGenerationBatch(final ActionMapping mapping, final ActionForm actionForm,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final CardGenerationBatch cardGenerationBatch = getCardGenerationBatch(request);
	request.setAttribute("cardGenerationBatch", cardGenerationBatch);
	return mapping.findForward("manageCardGenerationBatch");
    }
    
    protected Degree getDegree(final HttpServletRequest request) {
	final String degreeIdParam = request.getParameter("degreeID");
	final Integer degreeID = degreeIdParam == null || degreeIdParam.length() == 0 ? null : Integer.valueOf(degreeIdParam);
	return degreeIdParam == null ? null : rootDomainObject.readDegreeByOID(degreeID);
    }

    protected Set<Degree> getDegrees(final DegreeType degreeType) {
	final Set<Degree> degrees = new TreeSet<Degree>();
	for (final Degree degree : rootDomainObject.getDegreesSet()) {
	    if (degreeType == null || degree.getDegreeType() == degreeType) {
		degrees.add(degree);
	    }
	}
	return degrees;
    }

    protected DegreeType getDegreeType(final HttpServletRequest request) {
	final String degreeTypeParam = request.getParameter("degreeType");
	return degreeTypeParam == null || degreeTypeParam.length() == 0 ? null : DegreeType.valueOf(degreeTypeParam);
    }

    protected void checkForProblemasInDegrees(final HttpServletRequest request) {
	final Map<DegreeType, Boolean> problemsMap = new HashMap<DegreeType, Boolean>();
	for (final DegreeType degreeType : DegreeType.values()) {
	    problemsMap.put(degreeType, checkHasProblem(degreeType));
	}
	request.setAttribute("problemsMap", problemsMap);
    }

    protected void checkForProblemasInCategoryCodes(final HttpServletRequest request) {
	request.setAttribute("categoryCondesProblem", anyDegreeHasAnyProblem());
    }

    protected Boolean anyDegreeHasAnyProblem() {
	for (final Degree degree : rootDomainObject.getDegreesSet()) {
	    if (checkHasProblem(degree)) {
		return Boolean.TRUE;
	    }
	}
	return Boolean.FALSE;
    }

    protected Boolean checkHasProblem(final DegreeType degreeType) {
	for (final Degree degree : rootDomainObject.getDegreesSet()) {
	    if (degree.getDegreeType() == degreeType && checkHasProblem(degree)) {
		return Boolean.TRUE;
	    }
	}
	return Boolean.FALSE;
    }

    protected boolean checkHasProblem(final Degree degree) {
	final DegreeType degreeType = degree.getDegreeType();
	if (degreeType == DegreeType.DEGREE || degreeType == DegreeType.BOLONHA_DEGREE || degreeType == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) {
	    if (degree.getMinistryCode() == null) {
		return true;
	    }
	}
	if (degree.getIdCardName().length() > 42) {
	    return true;
	}
	return false;
    }

    protected void setContext(final HttpServletRequest request) {
	CardGenerationContext cardGenerationContext = (CardGenerationContext) getRenderedObject("cardGenerationContext");
	if (cardGenerationContext == null) {
	    final ExecutionYear executionYear = getExecutionYear(request);
	    cardGenerationContext = executionYear == null ? new CardGenerationContext() : new CardGenerationContext(executionYear);
	}
	request.setAttribute("cardGenerationContext", cardGenerationContext);
    }

    private ExecutionYear getExecutionYear(final HttpServletRequest request) {
	final String executionYearParam = request.getParameter("executionYearID");
	final Integer executionYearID = executionYearParam == null || executionYearParam.length() == 0 ? null : Integer.valueOf(executionYearParam);
	return executionYearID == null ? null : rootDomainObject.readExecutionYearByOID(executionYearID);
    }

    protected CardGenerationBatch getCardGenerationBatch(HttpServletRequest request) {
	final String cardGenerationBatchParam = request.getParameter("cardGenerationBatchID");
	final Integer cardGenerationBatchID = cardGenerationBatchParam == null || cardGenerationBatchParam.length() == 0 ? null : Integer.valueOf(cardGenerationBatchParam);
	return cardGenerationBatchID == null ? null : rootDomainObject.readCardGenerationBatchByOID(cardGenerationBatchID);	
    }

}
