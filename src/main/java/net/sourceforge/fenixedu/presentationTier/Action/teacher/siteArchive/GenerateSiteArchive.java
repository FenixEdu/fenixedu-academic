package net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.MetaDomainObject;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive.rules.ResourceRule;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive.rules.Rule;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive.rules.SimpleTransformRule;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "teacher", path = "/generateArchive", scope = "session", parameter = "method")
@Forwards(value = {
        @Forward(name = "fallback", path = "manage-execution-course-instructions", tileProperties = @Tile(title = "bolacha")),
        @Forward(name = "options", path = "execution-course-archive-options", tileProperties = @Tile(title = "bolacha2")) })
public class GenerateSiteArchive extends FenixDispatchAction {

    public ExecutionCourse getExecutionCourse(HttpServletRequest request) {
        String parameter = request.getParameter("executionCourseID");

        if (parameter == null) {
            return null;
        }

        try {
            return FenixFramework.getDomainObject(parameter);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ExecutionCourse executionCourse = getExecutionCourse(request);
        if (executionCourse != null) {
            request.setAttribute("executionCourse", executionCourse);
        }

        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("options", new ArchiveOptions());
        return mapping.findForward("options");
    }

    public ActionForward generate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ExecutionCourse executionCourse = getExecutionCourse(request);
        if (executionCourse == null) {
            return mapping.findForward("fallback");
        }

        ArchiveOptions options = getOptions(request);
        if (options == null) {
            return prepare(mapping, actionForm, request, response);
        }

        String name = getArchiveName(executionCourse);
        // NOTE: Using a DiskZipArchive instead of a ZipArchive because a
        // ZipArchive
        // writes directly to the response during the entire process and
        // prevents
        // the server from showing an error page when something goes wrong. This
        // leaves the user with a corrupt Zip file and no other information.
        Archive archive = new DiskZipArchive(response, name);
        Fetcher fetcher = new Fetcher(archive, request, response);

        queueResources(request, executionCourse, options, fetcher);

        List<Content> contents = new ArrayList<Content>();
        contents.add(MetaDomainObject.getMeta(ExecutionCourseSite.class).getAssociatedPortal());
        contents.add(executionCourse.getSite());
        FilterFunctionalityContext context = new FilterFunctionalityContext(request, contents);

        fetcher.process(context);
        archive.finish();

        return null;
    }

    private String getArchiveName(ExecutionCourse executionCourse) {
        String year = executionCourse.getExecutionYear().getYear().replaceAll("/", "-");
        return String.format("%s (%s)", executionCourse.getNome(), year);
    }

    private void queueResources(HttpServletRequest request, ExecutionCourse executionCourse, ArchiveOptions options,
            Fetcher fetcher) {
        fetcher.queue(new Resource("CSS/main.css", "/CSS/main.css"));
        fetcher.queue(new Resource("images/icon_email.gif", "/images/icon_email.gif"));

        String announcementsName = options.isAnnouncements() ? "announcements.html" : "#";
        String planningName = options.isPlanning() ? "planning.html" : "#";
        String scheduleName = options.isSchedule() ? "schedule.html" : "#";
        String shiftsName = options.isShifts() ? "shifts.html" : "#";
        String groupingsName = options.isGroupings() ? "groupings.html" : "#";
        String evaluationName = options.isEvaluations() ? "evaluation.html" : "#";

        String contextPath = request.getContextPath();

        List<Rule> globalRules = new ArrayList<Rule>();
        globalRules.add(new ResourceRule(contextPath + "/CSS/(.*)", "CSS/$1"));
        globalRules.add(new ResourceRule(contextPath + "/images/(.*)", "images/$1"));
        globalRules.add(new SimpleTransformRule(
                contextPath + "/publico/showCourseSite.do\\?.*?method=showCurricularCourseSite.*", "#"));
        globalRules.add(new SimpleTransformRule(contextPath
                + "/publico/executionCourse.do\\?(executionCourseID=[0-9]+|&amp;|method=firstPage)+", "index.html"));
        globalRules.add(new SimpleTransformRule(contextPath
                + "/publico/announcementManagement.do\\?(executionCourseID=[0-9]+|&amp;|method=start)+", announcementsName));
        globalRules.add(new SimpleTransformRule(contextPath
                + "/publico/executionCourse.do\\?(executionCourseID=[0-9]+|&amp;|method=lessonPlannings)+", planningName));
        globalRules.add(new SimpleTransformRule(contextPath
                + "/publico/executionCourse.do\\?(executionCourseID=[0-9]+|&amp;|method=summaries)+", "summaries.html"));
        globalRules.add(new SimpleTransformRule(contextPath
                + "/publico/executionCourse.do\\?(executionCourseID=[0-9]+|&amp;|method=objectives)+", "objectives.html"));
        globalRules.add(new SimpleTransformRule(contextPath
                + "/publico/executionCourse.do\\?(executionCourseID=[0-9]+|&amp;|method=program)+", "program.html"));
        globalRules.add(new SimpleTransformRule(contextPath
                + "/publico/executionCourse.do\\?(executionCourseID=[0-9]+|&amp;|method=evaluationMethod)+",
                "evaluation-method.html"));
        globalRules.add(new SimpleTransformRule(contextPath
                + "/publico/executionCourse.do\\?(executionCourseID=[0-9]+|&amp;|method=bibliographicReference)+",
                "bibliography.html"));
        globalRules.add(new SimpleTransformRule(contextPath
                + "/publico/executionCourse.do\\?(executionCourseID=[0-9]+|&amp;|method=schedule)+", scheduleName));
        globalRules.add(new SimpleTransformRule(contextPath
                + "/publico/executionCourse.do\\?(executionCourseID=[0-9]+|&amp;|method=shifts)+", shiftsName));
        globalRules.add(new SimpleTransformRule(contextPath
                + "/publico/executionCourse.do\\?(executionCourseID=[0-9]+|&amp;|method=groupings)+", groupingsName));
        globalRules.add(new SimpleTransformRule(contextPath
                + "/publico/executionCourse.do\\?(executionCourseID=[0-9]+|&amp;|method=evaluations)+", evaluationName));
        globalRules.add(new SimpleTransformRule(contextPath
                + "/publico/searchScormContent.do\\?(executionCourseID=[0-9]+|&amp;|method=prepareSearchForExecutionCourse)+",
                "#"));
        globalRules.add(new SimpleTransformRule(contextPath + "/publico/executionCourse.do\\?.*?sectionID=([0-9]+).*",
                "section-$1.html"));
        globalRules.add(new SimpleTransformRule(contextPath + "/publico/executionCourse.do\\?.*?method=rss.*", "#"));

        Resource resource =
                new Resource("index.html", "/publico/executionCourse.do?method=firstPage&executionCourseID="
                        + executionCourse.getExternalId());
        resource.addAllRules(globalRules);
        resource.addRule(new SimpleTransformRule(contextPath
                + "/publico/announcementManagement.do\\?.*?announcementId=([0-9]+).*",
                announcementsName.equals("#") ? "#" : announcementsName + "#ID_$1"));
        fetcher.queue(resource);

        if (options.isAnnouncements()) {
            resource =
                    new Resource("announcements.html",
                            "/publico/announcementManagement.do?method=start&ommitArchive=true&executionCourseID="
                                    + executionCourse.getExternalId());
            resource.addAllRules(globalRules);
            resource.addRule(new SimpleTransformRule(contextPath
                    + "/publico/announcementManagement.do.*?announcementId=([0-9]+).*", "announcements.html#ID_$1"));
            resource.addRule(new SimpleTransformRule(contextPath
                    + "/publico/announcementManagement.do.*?announcementBoardId=([0-9]+).*", "#"));
            fetcher.queue(resource);
        }

        if (options.isPlanning()) {
            resource =
                    new Resource("planning.html", "/publico/executionCourse.do?method=lessonPlannings&executionCourseID="
                            + executionCourse.getExternalId());
            resource.addAllRules(globalRules);
            fetcher.queue(resource);
        }

        resource =
                new Resource("summaries.html", "/publico/executionCourse.do?method=summaries&ommitFilter=true&executionCourseID="
                        + executionCourse.getExternalId());
        resource.addAllRules(globalRules);
        fetcher.queue(resource);

        resource =
                new Resource("objectives.html", "/publico/executionCourse.do?method=objectives&executionCourseID="
                        + executionCourse.getExternalId());
        resource.addAllRules(globalRules);
        fetcher.queue(resource);

        resource =
                new Resource("program.html", "/publico/executionCourse.do?method=program&executionCourseID="
                        + executionCourse.getExternalId());
        resource.addAllRules(globalRules);
        fetcher.queue(resource);

        resource =
                new Resource("evaluation-method.html", "/publico/executionCourse.do?method=evaluationMethod&executionCourseID="
                        + executionCourse.getExternalId());
        resource.addAllRules(globalRules);
        fetcher.queue(resource);

        resource =
                new Resource("bibliography.html", "/publico/executionCourse.do?method=bibliographicReference&executionCourseID="
                        + executionCourse.getExternalId());
        resource.addAllRules(globalRules);
        fetcher.queue(resource);

        if (options.isSchedule()) {
            resource =
                    new Resource("schedule.html", "/publico/executionCourse.do?method=schedule&executionCourseID="
                            + executionCourse.getExternalId());
            resource.addAllRules(globalRules);
            resource.addRule(new SimpleTransformRule("(" + contextPath + "/publico/)?siteViewer.do.*", "#"));
            fetcher.queue(resource);
        }

        if (options.isShifts()) {
            resource =
                    new Resource("shifts.html", "/publico/executionCourse.do?method=shifts&executionCourseID="
                            + executionCourse.getExternalId());
            resource.addAllRules(globalRules);
            resource.addRule(new SimpleTransformRule("(" + contextPath + "/publico/)?siteViewer.do.*", "#"));
            resource.addRule(new SimpleTransformRule("(" + contextPath
                    + "/publico/)?viewClassTimeTableWithClassNameAndDegreeInitialsAction.do.*", "#"));
            fetcher.queue(resource);
        }

        if (options.isGroupings()) {
            resource =
                    new Resource("groupings.html", "/publico/executionCourse.do?method=groupings&executionCourseID="
                            + executionCourse.getExternalId());
            resource.addAllRules(globalRules);
            ResourceRule groupRule =
                    new ResourceRule(contextPath + "/publico/executionCourse.do\\?method=grouping.*?groupingID=([0-9]+).*",
                            "group-$1.html");
            groupRule.addAllRules(globalRules);
            resource.addRule(groupRule);
            fetcher.queue(resource);
        }

        if (options.isEvaluations()) {
            resource =
                    new Resource("evaluation.html", "/publico/executionCourse.do?method=evaluations&executionCourseID="
                            + executionCourse.getExternalId());
            resource.addAllRules(globalRules);
            ResourceRule marksRule =
                    new ResourceRule(contextPath + "/publico/executionCourse.do\\?.*method=marks.*", "marks.html");
            marksRule.addAllRules(globalRules);
            resource.addRule(marksRule);
            fetcher.queue(resource);
        }

        for (Section section : executionCourse.getSite().getAssociatedSections()) {
            String name = String.format("section-%s.html", section.getExternalId());
            String url =
                    String.format("/publico/executionCourse.do?method=section&executionCourseID=%s&sectionID=%s",
                            executionCourse.getExternalId(), section.getExternalId());
            resource = new Resource(name, url);
            resource.addAllRules(globalRules);

            if (options.isFiles()) {
                ResourceRule fileRule = new ResourceRule(".*?/bitstream/([0-9]+/[0-9]+/[0-9]+)/(.*)", "files/$2");
                resource.addRule(fileRule);
            }

            fetcher.queue(resource);
        }
    }

    private ArchiveOptions getOptions(HttpServletRequest request) {
        return getRenderedObject("options");
    }

}
