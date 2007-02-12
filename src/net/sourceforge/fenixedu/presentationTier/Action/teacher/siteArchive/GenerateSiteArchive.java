package net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive.rules.ResourceRule;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive.rules.Rule;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive.rules.SimpleTransformRule;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class GenerateSiteArchive extends FenixDispatchAction {
    
    public ExecutionCourse getExecutionCourse(HttpServletRequest request) {
        String parameter = request.getParameter("executionCourseID");
        
        if (parameter == null) {
            return null;
        }
        
        try {
            Integer oid = new Integer(parameter);
            return RootDomainObject.getInstance().readExecutionCourseByOID(oid);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ExecutionCourse executionCourse = getExecutionCourse(request);
        if (executionCourse != null) {
            request.setAttribute("executionCourse", executionCourse);
        }
        
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("options", new ArchiveOptions());
        return mapping.findForward("options");
    }
    
    public ActionForward generate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ExecutionCourse executionCourse = getExecutionCourse(request);
        if (executionCourse == null) {
            return mapping.findForward("fallback");
        }

        ArchiveOptions options = getOptions(request);
        if (options == null) {
            return prepare(mapping, actionForm, request, response);
        }

        String name = getArchiveName(executionCourse);
        ZipArchive archive = new ZipArchive(response, name);
        Fetcher fetcher = new Fetcher(archive, request, response);
        
        queueResources(request, executionCourse, options, fetcher);
        
        fetcher.process();
        archive.finish();
        
        return null;
    }

    private String getArchiveName(ExecutionCourse executionCourse) {
        String year = executionCourse.getExecutionYear().getYear().replaceAll("/", "-");
        return String.format("%s (%s)", executionCourse.getNome(), year);
    }

    private void queueResources(HttpServletRequest request, ExecutionCourse executionCourse, ArchiveOptions options, Fetcher fetcher) {
        fetcher.queue(new Resource("CSS/main.css", "/CSS/main.css"));
        fetcher.queue(new Resource("images/icon_email.gif", "/images/icon_email.gif"));
        
        String announcementsName = options.isAnnouncements() ? "announcements.html" : "#";
        String planningName      = options.isPlanning() ? "planning.html" : "#";
        String scheduleName      = options.isSchedule() ? "schedule.html" : "#";
        String shiftsName        = options.isShifts() ? "shifts.html" : "#";
        String groupingsName     = options.isGroupings() ? "groupings.html" : "#";
        String evaluationName    = options.isEvaluations() ? "evaluation.html" : "#";
        
        String contextPath = request.getContextPath();
        
        List<Rule> globalRules = new ArrayList<Rule>();
        globalRules.add(new ResourceRule(contextPath + "/CSS/(.*)", "CSS/$1"));
        globalRules.add(new ResourceRule(contextPath + "/images/(.*)", "images/$1"));
        globalRules.add(new SimpleTransformRule(contextPath + "/publico/changeLocaleTo.do", "#"));
        globalRules.add(new SimpleTransformRule(contextPath + "/publico/showCourseSite.do\\?.*?method=showCurricularCourseSite.*", "#"));
        globalRules.add(new SimpleTransformRule(contextPath + "/publico/executionCourse.do\\?(executionCourseID=[0-9]+|&amp;|method=firstPage)+", "index.html"));
        globalRules.add(new SimpleTransformRule(contextPath + "/publico/announcementManagement.do\\?(executionCourseID=[0-9]+|&amp;|method=start)+", announcementsName));
        globalRules.add(new SimpleTransformRule(contextPath + "/publico/executionCourse.do\\?(executionCourseID=[0-9]+|&amp;|method=lessonPlannings)+", planningName));
        globalRules.add(new SimpleTransformRule(contextPath + "/publico/executionCourse.do\\?(executionCourseID=[0-9]+|&amp;|method=summaries)+", "summaries.html"));
        globalRules.add(new SimpleTransformRule(contextPath + "/publico/executionCourse.do\\?(executionCourseID=[0-9]+|&amp;|method=objectives)+", "objectives.html"));
        globalRules.add(new SimpleTransformRule(contextPath + "/publico/executionCourse.do\\?(executionCourseID=[0-9]+|&amp;|method=program)+", "program.html"));
        globalRules.add(new SimpleTransformRule(contextPath + "/publico/executionCourse.do\\?(executionCourseID=[0-9]+|&amp;|method=evaluationMethod)+", "evaluation-method.html"));
        globalRules.add(new SimpleTransformRule(contextPath + "/publico/executionCourse.do\\?(executionCourseID=[0-9]+|&amp;|method=bibliographicReference)+", "bibliography.html"));
        globalRules.add(new SimpleTransformRule(contextPath + "/publico/executionCourse.do\\?(executionCourseID=[0-9]+|&amp;|method=schedule)+", scheduleName));
        globalRules.add(new SimpleTransformRule(contextPath + "/publico/executionCourse.do\\?(executionCourseID=[0-9]+|&amp;|method=shifts)+", shiftsName));
        globalRules.add(new SimpleTransformRule(contextPath + "/publico/executionCourse.do\\?(executionCourseID=[0-9]+|&amp;|method=groupings)+", groupingsName));
        globalRules.add(new SimpleTransformRule(contextPath + "/publico/executionCourse.do\\?(executionCourseID=[0-9]+|&amp;|method=evaluations)+", evaluationName));
        globalRules.add(new SimpleTransformRule(contextPath + "/publico/searchScormContent.do\\?(executionCourseID=[0-9]+|&amp;|method=prepareSearchForExecutionCourse)+", "#"));
        globalRules.add(new SimpleTransformRule(contextPath + "/publico/executionCourse.do\\?.*?sectionID=([0-9]+).*", "section-$1.html"));
        globalRules.add(new SimpleTransformRule(contextPath + "/publico/executionCourse.do\\?.*?method=rss.*", "#"));
        
        Resource resource = new Resource("index.html", "/publico/executionCourse.do?method=firstPage&executionCourseID=" + executionCourse.getIdInternal());
        resource.addAllRules(globalRules);
        resource.addRule(new SimpleTransformRule(contextPath + "/publico/announcementManagement.do\\?.*?announcementId=([0-9]+).*", announcementsName.equals("#") ? "#" : announcementsName + "#ID_$1"));
        fetcher.queue(resource);

        if (options.isAnnouncements()) {
            resource = new Resource("announcements.html", "/publico/announcementManagement.do?method=start&ommitArchive=true&executionCourseID=" + executionCourse.getIdInternal());
            resource.addAllRules(globalRules);
            resource.addRule(new SimpleTransformRule(contextPath + "/publico/announcementManagement.do.*?announcementId=([0-9]+).*", "announcements.html#ID_$1"));
            resource.addRule(new SimpleTransformRule(contextPath + "/publico/announcementManagement.do.*?announcementBoardId=([0-9]+).*", "#"));
            fetcher.queue(resource);
        }
        
        if (options.isPlanning()) {
            resource = new Resource("planning.html", "/publico/executionCourse.do?method=lessonPlannings&executionCourseID=" + executionCourse.getIdInternal());
            resource.addAllRules(globalRules);
            fetcher.queue(resource);
        }
        
        resource = new Resource("summaries.html", "/publico/executionCourse.do?method=summaries&ommitFilter=true&executionCourseID=" + executionCourse.getIdInternal());
        resource.addAllRules(globalRules);
        fetcher.queue(resource);

        resource = new Resource("objectives.html", "/publico/executionCourse.do?method=objectives&executionCourseID=" + executionCourse.getIdInternal());
        resource.addAllRules(globalRules);
        fetcher.queue(resource);

        resource = new Resource("program.html", "/publico/executionCourse.do?method=program&executionCourseID=" + executionCourse.getIdInternal());
        resource.addAllRules(globalRules);
        fetcher.queue(resource);
        
        resource = new Resource("evaluation-method.html", "/publico/executionCourse.do?method=evaluationMethod&executionCourseID=" + executionCourse.getIdInternal());
        resource.addAllRules(globalRules);
        fetcher.queue(resource);
        
        resource = new Resource("bibliography.html", "/publico/executionCourse.do?method=bibliographicReference&executionCourseID=" + executionCourse.getIdInternal());
        resource.addAllRules(globalRules);
        fetcher.queue(resource);
        
        if (options.isSchedule()) {
            resource = new Resource("schedule.html", "/publico/executionCourse.do?method=schedule&executionCourseID=" + executionCourse.getIdInternal());
            resource.addAllRules(globalRules);
            resource.addRule(new SimpleTransformRule("(" + contextPath + "/publico/)?siteViewer.do.*", "#"));
            fetcher.queue(resource);
        }
        
        if (options.isShifts()) {
            resource = new Resource("shifts.html", "/publico/executionCourse.do?method=shifts&executionCourseID=" + executionCourse.getIdInternal());
            resource.addAllRules(globalRules);
            resource.addRule(new SimpleTransformRule("(" + contextPath + "/publico/)?siteViewer.do.*", "#"));
            resource.addRule(new SimpleTransformRule("(" + contextPath + "/publico/)?viewClassTimeTableWithClassNameAndDegreeInitialsAction.do.*", "#"));
            fetcher.queue(resource);
        }
        
        if (options.isGroupings()) {
            resource = new Resource("groupings.html", "/publico/executionCourse.do?method=groupings&executionCourseID=" + executionCourse.getIdInternal());
            resource.addAllRules(globalRules);
            ResourceRule groupRule = new ResourceRule(contextPath + "/publico/executionCourse.do\\?method=grouping.*?groupingID=([0-9]+).*", "group-$1.html");
            groupRule.addAllRules(globalRules);
            resource.addRule(groupRule);
            fetcher.queue(resource);
        }
        
        if (options.isEvaluations()) {
            resource = new Resource("evaluation.html", "/publico/executionCourse.do?method=evaluations&executionCourseID=" + executionCourse.getIdInternal());
            resource.addAllRules(globalRules);
            ResourceRule marksRule = new ResourceRule(contextPath + "/publico/executionCourse.do\\?.*method=marks.*", "marks.html");
            marksRule.addAllRules(globalRules);
            resource.addRule(marksRule);
            fetcher.queue(resource);
        }
        
        for (Section section : executionCourse.getSite().getAssociatedSections()) {
            String name = String.format("section-%s.html", section.getIdInternal());
            String url = String.format("/publico/executionCourse.do?method=section&executionCourseID=%s&sectionID=%s", executionCourse.getIdInternal(), section.getIdInternal());
            resource = new Resource(name, url);
            resource.addAllRules(globalRules);
            fetcher.queue(resource);
        }
    }

    private ArchiveOptions getOptions(HttpServletRequest request) {
        return (ArchiveOptions) getRenderedObject("options");
    }

}
