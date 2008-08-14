package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author tfc130
 */
public class PrepararEscolherContextoFormAction extends FenixContextAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	super.execute(mapping, form, request, response);

	InfoExecutionPeriod infoExecutionPeriod = setExecutionContext(request);

	List anosCurriculares = new ArrayList();
	anosCurriculares.add(new LabelValueBean("escolher", ""));
	anosCurriculares.add(new LabelValueBean("1 º", "1"));
	anosCurriculares.add(new LabelValueBean("2 º", "2"));
	anosCurriculares.add(new LabelValueBean("3 º", "3"));
	anosCurriculares.add(new LabelValueBean("4 º", "4"));
	anosCurriculares.add(new LabelValueBean("5 º", "5"));
	request.setAttribute("anosCurriculares", anosCurriculares);

	/* Cria o form bean com as licenciaturas em execucao. */
	Object argsLerLicenciaturas[] = { infoExecutionPeriod.getInfoExecutionYear() };

	List executionDegreeList = (List) ServiceUtils
		.executeService("ReadExecutionDegreesByExecutionYear", argsLerLicenciaturas);

	Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());

	List licenciaturas = new ArrayList();

	licenciaturas.add(new LabelValueBean("escolher", ""));

	Iterator iterator = executionDegreeList.iterator();

	int index = 0;
	while (iterator.hasNext()) {
	    InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
	    String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

	    name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getTipoCurso().toString() + " de " + name;

	    name += duplicateInfoDegree(executionDegreeList, infoExecutionDegree) ? "-"
		    + infoExecutionDegree.getInfoDegreeCurricularPlan().getName() : "";

	    licenciaturas.add(new LabelValueBean(name, String.valueOf(index++)));
	}

	request.setAttribute(SessionConstants.INFO_EXECUTION_DEGREE_LIST_KEY, executionDegreeList);

	request.setAttribute("licenciaturas", licenciaturas);

	return mapping.findForward("Sucesso");
    }

    /**
     * Method existencesOfInfoDegree.
     * 
     * @param executionDegreeList
     * @param infoExecutionDegree
     * @return int
     */
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

    /**
     * Method setExecutionContext.
     * 
     * @param request
     */
    private InfoExecutionPeriod setExecutionContext(HttpServletRequest request) throws Exception {

	InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
		.getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY);
	if (infoExecutionPeriod == null) {
	    IUserView userView = UserView.getUser();
	    infoExecutionPeriod = (InfoExecutionPeriod) ServiceUtils.executeService("ReadCurrentExecutionPeriod", new Object[0]);

	    request.setAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY, infoExecutionPeriod);
	}
	return infoExecutionPeriod;
    }

}