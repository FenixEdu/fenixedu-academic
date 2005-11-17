/*
 * Created on Nov 9, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.coordinator.evaluation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IProject;
import net.sourceforge.fenixedu.presentationTier.jsf.components.util.CalendarLink;

import org.apache.commons.beanutils.BeanComparator;

public class CoordinatorProjectsInformationBackingBean extends
        CoordinatorEvaluationManagementBackingBean {

    private final ResourceBundle messages = getResourceBundle("ServidorApresentacao/ApplicationResources");
    private List<IExecutionCourse> executionCoursesWithProjects;
    private List<IExecutionCourse> executionCoursesWithoutProjects;
    private Map<Integer, List<IProject>> projects = new HashMap();
    private List<CalendarLink> projectsCalendarLink;

    private void filterExecutionCourses() {
        if (this.executionCoursesWithProjects == null || this.executionCoursesWithoutProjects == null) {
            this.executionCoursesWithProjects = new ArrayList();
            this.executionCoursesWithoutProjects = new ArrayList();
            Collections.sort(getExecutionCourses(), new BeanComparator("sigla"));
            projects.clear();
            for (final IExecutionCourse executionCourse : getExecutionCourses()) {
                final List<IProject> associatedProjects = executionCourse.getAssociatedProjects();
                if (!executionCourse.getAssociatedProjects().isEmpty()) {
                    Collections.sort(associatedProjects, new BeanComparator("begin"));
                    this.executionCoursesWithProjects.add(executionCourse);
                    this.projects.put(executionCourse.getIdInternal(), associatedProjects);
                } else {
                    this.executionCoursesWithoutProjects.add(executionCourse);
                }
            }
        }
    }
    
    public List<CalendarLink> getProjectsCalendarLink() {
        if (this.projectsCalendarLink == null) {
            this.projectsCalendarLink = new ArrayList();
            StringBuffer linkLabel;
            final DateFormat sdf = new SimpleDateFormat("HH:mm");            
            for (final IExecutionCourse executionCourse : this.getExecutionCoursesWithProjects()) {
                for (final IProject project : executionCourse.getAssociatedProjects()) {
                    final CalendarLink calendarLinkBegin = new CalendarLink();
                    calendarLinkBegin.setObjectOccurrence(project.getBegin());
                    linkLabel = new StringBuffer(20);
                    linkLabel.append(executionCourse.getSigla());
                    linkLabel.append(" (").append(sdf.format(project.getBegin())).append(")");
                    linkLabel.append("<br/>").append(messages.getString("label.coordinator.enrolmentBegin"));
                    calendarLinkBegin.setObjectLinkLabel(linkLabel.toString());
                    
                    final CalendarLink calendarLinkEnd = new CalendarLink();
                    calendarLinkEnd.setObjectOccurrence(project.getEnd());
                    linkLabel = new StringBuffer(20);
                    linkLabel.append(executionCourse.getSigla());
                    linkLabel.append(" (").append(sdf.format(project.getEnd())).append(")");
                    linkLabel.append("<br/>").append(messages.getString("label.delivery"));
                    calendarLinkEnd.setObjectLinkLabel(linkLabel.toString());

                    final Map<String, String> linkParameters = new HashMap<String, String>();
                    linkParameters.put("degreeCurricularPlanID", getDegreeCurricularPlanID().toString());
                    linkParameters.put("executionPeriodID", getExecutionPeriodID().toString());
                    linkParameters.put("executionCourseID", executionCourse.getIdInternal().toString());
                    linkParameters.put("curricularYearID", getCurricularYearID().toString());
                    linkParameters.put("evaluationID", project.getIdInternal().toString());
                    
                    calendarLinkBegin.setLinkParameters(linkParameters);
                    calendarLinkEnd.setLinkParameters(linkParameters);
                    projectsCalendarLink.add(calendarLinkBegin);
                    projectsCalendarLink.add(calendarLinkEnd);
                }
            }
        }
        return this.projectsCalendarLink;
    }

    public List<IExecutionCourse> getExecutionCoursesWithProjects() {
        filterExecutionCourses();
        return this.executionCoursesWithProjects;
    }

    public List<IExecutionCourse> getExecutionCoursesWithoutProjects() {
        filterExecutionCourses();
        return this.executionCoursesWithoutProjects;
    }

    public Map<Integer, List<IProject>> getProjects() {
        return this.projects;
    }

    protected void clearAttributes() {
        super.clearAttributes();
        this.executionCoursesWithProjects = null;
        this.executionCoursesWithoutProjects = null;
    }
}
