/**
 * 
 * Created on 14/Oct/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.exams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadExecutionDegreesByExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author Ana e Ricardo
 */
@Mapping(module = "resourceAllocationManager", path = "/chooseDegreeAndYearContext", input = "/chooseDegreeAndYearContext.do?method=prepare&page=0", attribute = "showExamsManagementForm", formBean = "showExamsManagementForm", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "chooseDegreeAndYearContext", path = "df.page.chooseDegreeAndYear"),
		@Forward(name = "viewExamsMap", path = "/showExamsManagement.do?method=view") })
@Exceptions(value = { @ExceptionHandling(type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException.class, key = "resources.Action.exceptions.FenixActionException", handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class ChooseDegreeAndYearContextDA extends FenixContextDispatchAction {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.struts.actions.DispatchAction#dispatchMethod(org.apache.struts
     * .action.ActionMapping, org.apache.struts.action.ActionForm,
     * javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.String)
     */
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	IUserView userView = UserView.getUser();

	InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
		.getAttribute(PresentationConstants.EXECUTION_PERIOD);

	/* Criar o bean de anos curriculares */
	List anosCurriculares = createCurricularYearList();
	request.setAttribute(PresentationConstants.LABELLIST_CURRICULAR_YEARS, anosCurriculares);

	/* Cria o form bean com as licenciaturas em execucao. */

	List executionDegreeList = (List) ReadExecutionDegreesByExecutionYear.run(infoExecutionPeriod.getInfoExecutionYear());

	List licenciaturas = new ArrayList();

	licenciaturas.add(new LabelValueBean("", ""));

	Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());

	Iterator iterator = executionDegreeList.iterator();

	while (iterator.hasNext()) {
	    InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
	    String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

	    name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getDegreeType().toString() + " em " + name;

	    name += duplicateInfoDegree(executionDegreeList, infoExecutionDegree) ? "-"
		    + infoExecutionDegree.getInfoDegreeCurricularPlan().getName() : "";

	    licenciaturas.add(new LabelValueBean(name, infoExecutionDegree.getIdInternal().toString()));
	}

	request.setAttribute(PresentationConstants.DEGREES, licenciaturas);

	return mapping.findForward("chooseDegreeAndYearContext");
    }

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	DynaValidatorForm chooseDegreeAndYearForm = (DynaValidatorForm) form;

	String executionDegree = (String) chooseDegreeAndYearForm.get("executionDegree");
	String curricularYear = (String) chooseDegreeAndYearForm.get("curricularYear");

	request.setAttribute(PresentationConstants.EXECUTION_DEGREE_OID, executionDegree);
	request.setAttribute(PresentationConstants.CURRICULAR_YEAR_OID, curricularYear);

	ContextUtils.setExecutionDegreeContext(request);
	ContextUtils.setCurricularYearContext(request);

	return mapping.findForward("viewExamsMap");

    }

    /**
     * Method createCurricularYearList.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     */
    private List createCurricularYearList() {

	List anosCurriculares = new ArrayList();

	anosCurriculares.add(new LabelValueBean("", ""));
	anosCurriculares.add(new LabelValueBean("1 º", "1"));
	anosCurriculares.add(new LabelValueBean("2 º", "2"));
	anosCurriculares.add(new LabelValueBean("3 º", "3"));
	anosCurriculares.add(new LabelValueBean("4 º", "4"));
	anosCurriculares.add(new LabelValueBean("5 º", "5"));

	return anosCurriculares;
    }

    private boolean duplicateInfoDegree(List executionDegreeList, InfoExecutionDegree infoExecutionDegree) {
	InfoDegree infoDegree = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree();
	Iterator iterator = executionDegreeList.iterator();

	while (iterator.hasNext()) {
	    InfoExecutionDegree infoExecutionDegree2 = (InfoExecutionDegree) iterator.next();
	    if (infoDegree.equals(infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree())
		    && !(infoExecutionDegree.equals(infoExecutionDegree2)))
		return true;

	}
	return false;
    }
}