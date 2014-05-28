/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author João Mota
 */
@Mapping(module = "publico", path = "/viewClassesNew", attribute = "chooseSearchContextForm",
        formBean = "chooseSearchContextForm", scope = "request", validate = false)
@Forwards(value = { @Forward(name = "Sucess", path = "viewClasses") })
public class ViewClassesActionNew extends FenixContextAction {

    private static final Logger logger = LoggerFactory.getLogger(ViewClassesActionNew.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {
        final ActionErrors errors = new ActionErrors();

        try {
            super.execute(mapping, form, request, response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
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
        String degreeId = (String) request.getAttribute("degreeID");
        request.setAttribute("degreeID", degreeId);

        // degreeCurricularPlanID
        String degreeCurricularPlanId = (String) request.getAttribute("degreeCurricularPlanID");
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);
        final DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanId);
        if (!degreeCurricularPlan.getDegree().getExternalId().equals(degreeId)) {
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
        String indice = (String) escolherContextoForm.get("indice");
        escolherContextoForm.set("indice", indice);
        request.setAttribute("indice", indice);

        InfoExecutionPeriod infoExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);
        request.setAttribute(PresentationConstants.EXECUTION_PERIOD, infoExecutionPeriod);
        request.setAttribute(PresentationConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod.getExternalId().toString());

        final ExecutionSemester executionSemester = FenixFramework.getDomainObject(infoExecutionPeriod.getExternalId());
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
                comparatorChain.addComparator(new BeanComparator("nome"));
                Collections.sort(classList, comparatorChain);

                request.setAttribute("classList", classList);
            }
        }

        return mapping.findForward("Sucess");
    }

}