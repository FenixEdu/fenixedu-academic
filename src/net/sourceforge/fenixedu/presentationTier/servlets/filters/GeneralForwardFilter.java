package net.sourceforge.fenixedu.presentationTier.servlets.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.DegreeCurricularPlanProcessor;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.DegreeProcessor;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.ExamProcessor;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.ExecutionCourseProcessor;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.ExecutionCoursesProcessor;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.ItemProcessor;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.PathElementsProvider;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.PathProcessor;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.ProcessingContext;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.ScheduleProcessor;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.SchoolClassProcessor;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.SectionProcessor;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.SemesterProcessor;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.YearProcessor;

public class GeneralForwardFilter implements Filter {

    private List<PathProcessor> processors = new ArrayList<PathProcessor>();
    private String notFoundURI;
   
    public void init(FilterConfig config) throws ServletException {
        
        this.notFoundURI = config.getInitParameter("notFoundURI");
        
        String siteListURI = config.getInitParameter("siteListURI");
        String siteURI = config.getInitParameter("siteURI");
        String scheduleListURI = config.getInitParameter("scheduleListURI");
        String classScheduleURI = config.getInitParameter("classScheduleURI");
        String examListURI = config.getInitParameter("examListURI");
        String degreeURI = config.getInitParameter("degreeURI");
        String sectionURI = config.getInitParameter("sectionURI");
        String itemURI = config.getInitParameter("itemURI");

        DegreeProcessor degreeProcessor = new DegreeProcessor(degreeURI);
        ExecutionCoursesProcessor executionCourses = new ExecutionCoursesProcessor(siteListURI);
        DegreeCurricularPlanProcessor degreeCurricularPlan = new DegreeCurricularPlanProcessor();
        ExecutionCourseProcessor executionCourse = new ExecutionCourseProcessor(siteURI);
        YearProcessor year = new YearProcessor(siteURI);
        SemesterProcessor semester = new SemesterProcessor(siteURI);
        ScheduleProcessor schedule = new ScheduleProcessor(scheduleListURI);
        SchoolClassProcessor schoolClass = new SchoolClassProcessor(classScheduleURI);
        ExamProcessor exams = new ExamProcessor(examListURI);
        SectionProcessor section = new SectionProcessor(sectionURI);
        ItemProcessor item = new ItemProcessor(itemURI);
        
        SectionProcessor sectionAndItem = section.add(item);
        
        ExecutionCourseProcessor executionCourseProcessor = 
            executionCourse
                .add(year
                    .add(semester
                        .add(sectionAndItem))
                    .add(sectionAndItem))
                .add(semester
                    .add(sectionAndItem))
                .add(sectionAndItem);
        
        processors.add(
            degreeProcessor
                .add(executionCourses
                    .add(executionCourseProcessor)
                    .add(degreeCurricularPlan
                        .add(executionCourseProcessor)))
                .add(schedule
                    .add(schoolClass))
                .add(exams)
        );
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String uri = httpRequest.getRequestURI();
        
        String contextPath = httpRequest.getContextPath();
        if (uri.startsWith(contextPath)) {
            uri = uri.substring(contextPath.length() + 1);
        }
        
        ProcessingContext context = new ProcessingContext(contextPath, httpRequest, httpResponse);
        boolean processed = false;
        
        for (PathProcessor processor : this.processors) {
            if (processor.process(context, new PathElementsProvider(uri))) {
                processed = true;
                break;
            }
        }

        if (processed) {
            return;
        }
        
        if (!context.isChildAccepted()) {
            chain.doFilter(request, response);
        }
        else {
            httpResponse.sendRedirect(contextPath + this.notFoundURI);
        }
    }

    public void destroy() {
        // do nothing
    }

}
