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

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.view.JstlView;

public class TeacherView extends JstlView {

    private final String page;

    public TeacherView(String page) {
        this.page = page;
    }

    @Override
    protected void exposeHelpers(HttpServletRequest request) throws Exception {
        setServletContext(request.getServletContext());
        super.exposeHelpers(request);
        request.setAttribute("teacher$actual$page", "/teacher/" + page + ".jsp");
    }

    @Override
    public String getUrl() {
        return "/teacher/executionCourse/executionCourseFrame.jsp";
    }

}
