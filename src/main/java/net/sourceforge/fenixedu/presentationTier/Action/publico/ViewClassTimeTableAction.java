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
/**
 * 
 * Project sop 
 * Package presentationTier.Action.publico 
 * Created on 1/Fev/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.publico.ClassSiteComponentService;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteTimetable;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author João Mota
 * 
 */
public class ViewClassTimeTableAction extends FenixContextAction {

    private static final Logger logger = LoggerFactory.getLogger(ViewClassTimeTableAction.class);

    /**
     * Constructor for ViewClassTimeTableAction.
     */

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {
        try {
            super.execute(mapping, form, request, response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        String className = request.getParameter("className");

        InfoExecutionPeriod infoExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);
        request.setAttribute("degreeInitials", "");
        request.setAttribute("nameDegreeCurricularPlan", "");
        request.setAttribute("degreeCurricularPlanID", "");
        request.setAttribute("degreeID", "");
        String classIdString = request.getParameter("classId");
        String classId = null;
        if (classIdString != null) {
            classId = classIdString;
        } else {
            return mapping.getInputForward();

        }
        InfoSiteTimetable component = new InfoSiteTimetable();

        SiteView siteView = null;

        try {
            siteView =
                    (SiteView) ClassSiteComponentService.run(component, infoExecutionPeriod.getInfoExecutionYear().getYear(),
                            infoExecutionPeriod.getName(), null, null, className, null, classId);
        } catch (FenixServiceException e1) {
            throw new FenixActionException(e1);
        }
        request.setAttribute("siteView", siteView);
        request.setAttribute("className", className);
        return mapping.findForward("Sucess");
    }
}