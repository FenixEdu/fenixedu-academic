package ServidorApresentacao.Action.degreeAdministrativeOffice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author David Santos
 */

public class PrepareExecutionPeriodForEnrolmentContextAction extends Action {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List infoExecutionPeriodsList = null;
		try {
			HttpSession session = request.getSession();
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			infoExecutionPeriodsList = (List) ServiceUtils.executeService(userView, "ReadAllExecutionPeriodsForEnrollment", null);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		request.setAttribute(SessionConstants.EXECUTION_PERIOD, PrepareExecutionPeriodForEnrolmentContextAction.getExecutionPeriodsLableValueBeanList(infoExecutionPeriodsList));

		return mapping.findForward("chooseStudentAndExecutionPeriodForEnrollment");
	}

	public static List getExecutionPeriodsLableValueBeanList(List infoExecutionPeriodsList) {
		ArrayList result = null;
		if ( (infoExecutionPeriodsList != null) && (!infoExecutionPeriodsList.isEmpty()) ) {
			result = new ArrayList();
			Iterator iterator = infoExecutionPeriodsList.iterator();
			while(iterator.hasNext()) {
				InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) iterator.next();
				result.add(new LabelValueBean(infoExecutionPeriod.getInfoExecutionYear().getYear() + " - " + infoExecutionPeriod.getName(), infoExecutionPeriod.getIdInternal().toString()));
//				result.add(new LabelValueBean(infoExecutionPeriod.getInfoExecutionYear().getYear(), infoExecutionPeriod.getIdInternal().toString()));
			}
		}
		PrepareExecutionPeriodForEnrolmentContextAction.sort(result);
		result.add(0, new LabelValueBean("Escolha", ""));
		return result;	
	}

	public static void sort(List listOfLabelValueBeans) {
		ComparatorChain comparatorChain = new ComparatorChain();
		comparatorChain.addComparator(new BeanComparator("label"));
		if(listOfLabelValueBeans != null) {
			Collections.sort(listOfLabelValueBeans, comparatorChain);
		}
	}
}