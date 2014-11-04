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
