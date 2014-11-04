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
package net.sourceforge.fenixedu.presentationTier.Action.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadNotClosedPublicExecutionPeriodsByExecutionYear;
import net.sourceforge.fenixedu.applicationTier.Servico.publico.ReadPublicExecutionDegreeByDCPID;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

public abstract class FenixContextAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ContextUtils.setExecutionPeriodContext(request);

        ContextUtils.prepareChangeExecutionDegreeAndCurricularYear(request);

        return super.execute(mapping, actionForm, request, response);
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