package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.LerTurmas;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.RequestUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author João Mota
 */
@Mapping(module = "publico", path = "/viewClassesNew", attribute = "chooseSearchContextForm",
        formBean = "chooseSearchContextForm", scope = "request", validate = false)
@Forwards(value = { @Forward(name = "Sucess", path = "viewClasses") })
public class ViewClassesActionNew extends FenixContextAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {
        final ActionErrors errors = new ActionErrors();

        try {
            super.execute(mapping, form, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Boolean inEnglish = (Boolean) request.getAttribute("inEnglish");
        if (inEnglish == null) {
            inEnglish = getLocale(request).getLanguage().equals(Locale.ENGLISH.getLanguage());
        }
        request.setAttribute("inEnglish", inEnglish);

        // index
        Integer index = (Integer) request.getAttribute("index");
        request.setAttribute("index", index);

        // degreeID
        Integer degreeId = (Integer) request.getAttribute("degreeID");
        request.setAttribute("degreeID", degreeId);

        // degreeCurricularPlanID
        Integer degreeCurricularPlanId = (Integer) request.getAttribute("degreeCurricularPlanID");
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);
        final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanId);
        if (!degreeCurricularPlan.getDegree().getIdInternal().equals(degreeId)) {
            throw new FenixActionException();
        } else {
            request.setAttribute("degree", degreeCurricularPlan.getDegree());
        }

        // lista
        List<LabelValueBean> executionPeriodsLabelValueList = buildExecutionPeriodsLabelValueList(degreeCurricularPlanId);
        if (executionPeriodsLabelValueList.size() > 1) {
            request.setAttribute("lista", executionPeriodsLabelValueList);
        } else {
            request.removeAttribute("lista");
        }

        // indice
        final DynaActionForm escolherContextoForm = (DynaActionForm) form;
        Integer indice = (Integer) escolherContextoForm.get("indice");
        escolherContextoForm.set("indice", indice);
        request.setAttribute("indice", indice);

        InfoExecutionPeriod infoExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);
        request.setAttribute(PresentationConstants.EXECUTION_PERIOD, infoExecutionPeriod);
        request.setAttribute(PresentationConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod.getIdInternal().toString());

        final ExecutionSemester executionSemester =
                rootDomainObject.readExecutionSemesterByOID(infoExecutionPeriod.getIdInternal());
        ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionSemester.getExecutionYear());
        if (executionDegree != null) {
            // infoExecutionDegree
            InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
            RequestUtils.setExecutionDegreeToRequest(request, infoExecutionDegree);

            request.setAttribute(PresentationConstants.INFO_DEGREE_CURRICULAR_PLAN,
                    infoExecutionDegree.getInfoDegreeCurricularPlan());

            List<InfoClass> classList = LerTurmas.run(infoExecutionDegree, infoExecutionPeriod, null);

            if (!classList.isEmpty()) {
                ComparatorChain comparatorChain = new ComparatorChain();
                comparatorChain.addComparator(new BeanComparator("anoCurricular"));
                Collections.sort(classList, comparatorChain);
                request.setAttribute("classList", classList);
            }
        }

        return mapping.findForward("Sucess");
    }

}