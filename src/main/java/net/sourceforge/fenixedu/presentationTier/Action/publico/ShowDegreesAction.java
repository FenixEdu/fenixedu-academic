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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.ReadMasterDegrees;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadNonMasterExecutionDegreesByExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Tânia Pousão Created on 9/Out/2003
 */
public class ShowDegreesAction extends FenixContextDispatchAction {

    public ActionForward nonMaster(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();

        Boolean inEnglish = new Boolean(false);
        InfoExecutionPeriod infoExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);
        InfoExecutionYear infoExecutionYear = null;
        if (infoExecutionPeriod != null) {
            infoExecutionYear = infoExecutionPeriod.getInfoExecutionYear();
        }

        List executionDegreesList = null;
        try {
            // ReadExecutionDegreesByExecutionYear
            executionDegreesList = ReadNonMasterExecutionDegreesByExecutionYear.run(infoExecutionYear);
        } catch (FenixServiceException e) {
            errors.add("impossibleDegreeList", new ActionError("error.impossibleDegreeList"));
            saveErrors(request, errors);
        }

        // buil a list of degrees by execution degrees list
        List degreesList = buildDegreesList(executionDegreesList);

        // put both list in request

        request.setAttribute("degreesList", degreesList);
        request.setAttribute("inEnglish", inEnglish);
        return mapping.findForward("showDegrees");
    }

    public ActionForward master(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionErrors errors = new ActionErrors();

        InfoExecutionPeriod infoExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);
        InfoExecutionYear infoExecutionYear = null;
        String ano = null;
        if (infoExecutionPeriod != null) {
            infoExecutionYear = infoExecutionPeriod.getInfoExecutionYear();
            ano = infoExecutionYear.getYear();
        }

        List executionDegreesList = null;
        try {
            // ReadExecutionDegreesByExecutionYear
            executionDegreesList = ReadMasterDegrees.run(ano);
        } catch (FenixServiceException e) {
            errors.add("impossibleDegreeList", new ActionError("error.impossibleDegreeList"));
            saveErrors(request, errors);
        }

        // buil a list of degrees by execution degrees list
        List degreesList = buildDegreesList(executionDegreesList);

        // put both list in request
        request.setAttribute("degreesList", degreesList);

        return mapping.findForward("showDegrees");
    }

    private List buildDegreesList(List executionDegreesList) {
        if (executionDegreesList == null) {
            return null;
        }

        List degreesList = new ArrayList();

        ListIterator listIterator = executionDegreesList.listIterator();
        while (listIterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) listIterator.next();

            if (!degreesList.contains(infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree())) {

                degreesList.add(infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree());
            }
        }

        // order list by alphabetic order of the code
        Collections.sort(degreesList, new BeanComparator("nome"));

        return degreesList;
    }
}