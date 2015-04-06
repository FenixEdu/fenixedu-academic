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
package org.fenixedu.academic.ui.spring.controller.teacher;

import static org.fenixedu.academic.predicate.AccessControl.getPerson;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.bennu.portal.servlet.PortalLayoutInjector;
import org.springframework.web.servlet.view.JstlView;

public class TeacherView extends JstlView {

    private final ExecutionCourse executionCourse;

    private final String page;

    public TeacherView(String page, ExecutionCourse executionCourse) {
        this.page = page;
        this.executionCourse = executionCourse;
    }

    @Override
    protected void exposeHelpers(HttpServletRequest request) throws Exception {
        setServletContext(request.getServletContext());
        super.exposeHelpers(request);

        final Professorship professorship = executionCourse.getProfessorship(getPerson());

        Map<String, Object> requestContext = new HashMap<String, Object>();
        requestContext.put("professorship", professorship);
        requestContext.put("executionCourse", executionCourse);
        PortalLayoutInjector.addContextExtension(requestContext);
    }

    @Override
    public String getUrl() {
        return "/teacher/" + page + ".jsp";
    }
}
