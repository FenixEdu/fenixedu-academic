package net.sourceforge.fenixedu.presentationTier.Action.student.delegate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.commons.delegates.DelegateSearchBean;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class DelegatesInfoDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	final Person person = getLoggedPerson(request);
	final Student student = person.getStudent();
	final Degree degree = student.getLastActiveRegistration().getDegree();

	DelegateSearchBean bean = new DelegateSearchBean();
	bean.setDegreeType(degree.getDegreeType());
	bean.setDegree(degree);
	bean.setExecutionYear(ExecutionYear.readCurrentExecutionYear());

	/* DEGREE DELEGATES */
	request.setAttribute("yearDelegates", getYearDelegateBeans(bean));
	request.setAttribute("degreeDelegate", getDelegateSearchBean(bean, FunctionType.DELEGATE_OF_DEGREE));
	request.setAttribute("masterDegreeDelegate", getDelegateSearchBean(bean, FunctionType.DELEGATE_OF_MASTER_DEGREE));
	request.setAttribute("integratedMasterDegreeDelegate", getDelegateSearchBean(bean,
		FunctionType.DELEGATE_OF_INTEGRATED_MASTER_DEGREE));
	request.setAttribute("searchByDegreeBean", bean);

	/* GGAE DELEGATES */
	List<DelegateSearchBean> delegatesFound = getGGAEDelegateBeans();
	Collections.sort(delegatesFound, DelegateSearchBean.DELEGATE_COMPARATOR_BY_EXECUTION_YEAR);

	request.setAttribute("ggaeDelegates", delegatesFound);
	request.setAttribute("searchBean", bean);
	return mapping.findForward("showDegreeDelegates");
    }

    /*
     * AUXILIARY METHODS
     */

    /* Delegates from given degree (not year delegates) */
    private DelegateSearchBean getDelegateSearchBean(DelegateSearchBean bean, FunctionType functionType) {
	List<Student> delegates = new ArrayList<Student>();
	if (bean.getExecutionYear().equals(ExecutionYear.readCurrentExecutionYear())) {
	    delegates.addAll(bean.getDegree().getAllActiveDelegatesByFunctionType(functionType, bean.getExecutionYear()));
	} else {
	    delegates.addAll(bean.getDegree()
		    .getAllDelegatesByExecutionYearAndFunctionType(bean.getExecutionYear(), functionType));
	}
	return (delegates.isEmpty() ? null : new DelegateSearchBean(delegates.get(0).getPerson(), functionType, bean
		.getExecutionYear()));
    }

    /* Year delegates from given degree */
    private List<DelegateSearchBean> getYearDelegateBeans(DelegateSearchBean bean) {
	List<DelegateSearchBean> yearDelegates = new ArrayList<DelegateSearchBean>();
	for (int i = 1; i <= bean.getDegreeType().getYears(); i++) {
	    final CurricularYear curricularYear = CurricularYear.readByYear(i);
	    Student student = null;
	    if (bean.getExecutionYear().equals(ExecutionYear.readCurrentExecutionYear())) {
		student = bean.getDegree().getActiveYearDelegateByCurricularYear(curricularYear);
	    } else {
		student = bean.getDegree().getYearDelegateByExecutionYearAndCurricularYear(bean.getExecutionYear(),
			curricularYear);
	    }
	    if (student != null) {
		DelegateSearchBean delegateBean = new DelegateSearchBean(student.getPerson(), FunctionType.DELEGATE_OF_YEAR,
			curricularYear, bean.getExecutionYear());
		yearDelegates.add(delegateBean);
	    }
	}
	return yearDelegates;
    }

    /* Delegates from all degrees */
    private List<DelegateSearchBean> getGGAEDelegateBeans() {
	final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
	List<DelegateSearchBean> result = new ArrayList<DelegateSearchBean>();

	Set<Function> functions = Function.readAllActiveFunctionsByType(FunctionType.DELEGATE_OF_GGAE);
	for (Function function : functions) {
	    for (PersonFunction personFunction : function.getActivePersonFunctionsStartingIn(currentExecutionYear)) {
		DelegateSearchBean bean = new DelegateSearchBean(personFunction.getPerson(), personFunction);
		result.add(bean);
	    }
	}

	return result;
    }
}
