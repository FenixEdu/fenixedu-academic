/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.fenixedu.academic.dto.InfoExecutionDegree;
import org.fenixedu.academic.dto.InfoExecutionPeriod;
import org.fenixedu.academic.service.services.commons.ReadNotClosedPublicExecutionPeriodsByExecutionYear;
import org.fenixedu.academic.service.services.publico.ReadPublicExecutionDegreeByDCPID;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.ui.struts.action.utils.ContextUtils;

public abstract class FenixContextDispatchAction extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ContextUtils.setContextSelectionBean(request, getRenderedObject());

        ContextUtils.setExecutionPeriodContext(request);

        ContextUtils.prepareChangeExecutionDegreeAndCurricularYear(request);

        ActionForward actionForward = super.execute(mapping, actionForm, request, response);

        return actionForward;
    }

    public static String getFromRequest(String parameter, HttpServletRequest request) {
        if (request.getParameter(parameter) != null) {
            return request.getParameter(parameter);
        } else if (request.getAttribute(parameter) != null) {
            if (request.getAttribute(parameter) instanceof String) {
                return (String) request.getAttribute(parameter);
            }
        }
        return null;
    }

    public static Boolean getFromRequestBoolean(String parameter, HttpServletRequest request) {
        return (request.getParameter(parameter) != null) ? Boolean.valueOf(request.getParameter(parameter)) : (Boolean) request
                .getAttribute(parameter);
    }

    protected List<LabelValueBean> buildExecutionPeriodsLabelValueList(String degreeCurricularPlanId) throws FenixActionException {
        List<InfoExecutionDegree> infoExecutionDegreeList = new ArrayList<InfoExecutionDegree>();
        try {

            infoExecutionDegreeList = ReadPublicExecutionDegreeByDCPID.run(degreeCurricularPlanId);
        } catch (Exception e) {
            throw new FenixActionException(e);
        }

        List<LabelValueBean> result = new ArrayList<LabelValueBean>();
        for (InfoExecutionDegree infoExecutionDegree : infoExecutionDegreeList) {

            try {
                List<InfoExecutionPeriod> infoExecutionPeriodsList =
                        ReadNotClosedPublicExecutionPeriodsByExecutionYear.run(infoExecutionDegree.getInfoExecutionYear());

                for (InfoExecutionPeriod infoExecutionPeriodIter : infoExecutionPeriodsList) {
                    result.add(new LabelValueBean(infoExecutionPeriodIter.getName() + " - "
                            + infoExecutionPeriodIter.getInfoExecutionYear().getYear(), infoExecutionPeriodIter.getExternalId()
                            .toString()));
                }
            } catch (Exception e) {
                throw new FenixActionException(e);
            }
        }

        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("value"));
        Collections.sort(result, comparatorChain);
        Collections.reverse(result);

        return result;
    }

}