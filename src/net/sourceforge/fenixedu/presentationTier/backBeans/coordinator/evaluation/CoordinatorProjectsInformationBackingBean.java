/*
 * Created on Nov 9, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.coordinator.evaluation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IProject;

import org.apache.commons.beanutils.BeanComparator;

public class CoordinatorProjectsInformationBackingBean extends
        CoordinatorEvaluationManagementBackingBean {

    private List<IExecutionCourse> executionCoursesWithProjects;
    private List<IExecutionCourse> executionCoursesWithoutProjects;
    private Map<Integer, List<IProject>> projects = new HashMap();

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
