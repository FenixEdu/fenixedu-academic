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
/*
 * Created on Dec 19, 2003 by jpvl
 *  
 */
package org.fenixedu.academic.ui.struts.action.teacher.professorship.exception.handler;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;
import org.fenixedu.academic.dto.InfoProfessorship;
import org.fenixedu.academic.service.services.teacher.professorship.ResponsibleForValidator.MaxResponsibleForExceed;

/**
 * @author jpvl
 */
public class MaxResponsibleForExceedHandler extends ExceptionHandler {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.struts.action.ExceptionHandler#execute(java.lang.Exception,
     * org.apache.struts.config.ExceptionConfig,
     * org.apache.struts.action.ActionMapping,
     * org.apache.struts.action.ActionForm,
     * javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward execute(Exception ex, ExceptionConfig ae, ActionMapping mapping, ActionForm formInstance,
            HttpServletRequest request, HttpServletResponse response) {
        MaxResponsibleForExceed maxResponsibleForExceedException = (MaxResponsibleForExceed) ex;

        Object args[] = new Object[2];

        args[0] = maxResponsibleForExceedException.getInfoExecutionCourse().getNome();

        Iterator iterator = maxResponsibleForExceedException.getInfoResponsiblefors().iterator();
        StringBuilder arg1 = new StringBuilder();
        while (iterator.hasNext()) {
            InfoProfessorship infoResponsibleFor = (InfoProfessorship) iterator.next();
            String teacherId = infoResponsibleFor.getInfoTeacher().getPerson().getUsername();
            String teacherName = infoResponsibleFor.getInfoTeacher().getInfoPerson().getNome();
            // MARK DELTA
            arg1.append("<a href='teacherSearchForExecutionCourseAssociation.do?method=doSearch&teacherId=").append(teacherId)
                    .append("'>").append(teacherId).append("</a>").append("-").append(teacherName);
            if (iterator.hasNext()) {
                arg1.append(", ");
            }
        }
        args[1] = arg1;
        String key = ae.getKey();
        ActionError error = new ActionError(key, args);
        ActionForward actionForward = new ActionForward(ae.getPath());
        storeException(request, key, error, actionForward, ae.getScope());
        return actionForward;
    }
}