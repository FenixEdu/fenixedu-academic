/*
 * Created on Nov 7, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.GroupEnrolment;
import net.sourceforge.fenixedu.domain.util.icalendar.EvaluationEventBean;
import net.sourceforge.fenixedu.util.BundleUtil;
import net.sourceforge.fenixedu.util.EvaluationType;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class Project extends Project_Base {

    private static final Logger logger = LoggerFactory.getLogger(Project.class);

    static {
        getRelationProjectGrouping().addListener(new RelationAdapter<Project, Grouping>() {
            @Override
            public void afterAdd(Project project, Grouping grouping) {
                if (project != null && grouping != null) {
                    if (grouping.getAutomaticEnrolment() && grouping.getStudentGroupsSet().isEmpty()) {
                        int groupCount = 0;
                        for (final ExecutionCourse executionCourse : project.getAssociatedExecutionCoursesSet()) {
                            for (Attends attend : executionCourse.getAttendsSet()) {
                                try {
                                    GroupEnrolment.run(grouping.getExternalId(), null, ++groupCount, new ArrayList<String>(),
                                            attend.getRegistration().getStudent().getPerson().getUsername());
                                } catch (FenixServiceException e) {
                                    // TODO Auto-generated catch block
                                    logger.error(e.getMessage(), e);
                                    groupCount--;
                                }
                            }
                        }
                        grouping.setGroupMaximumNumber(groupCount);
                    }
                }
            }
        });
    }

    private Project() {
        super();
    }

    public Project(String name, Date begin, Date end, String description, Boolean onlineSubmissionsAllowed,
            Integer maxSubmissionsToKeep, Grouping grouping, ExecutionCourse executionCourse) {
        this();
        if (name == null || begin == null || end == null || executionCourse == null) {
            throw new NullPointerException();
        }
        if (begin.after(end)) {
            throw new DomainException("error.evaluation.begin.sooner.end");
        }

        this.setName(name);
        this.setBegin(begin);
        this.setEnd(end);
        this.setDescription(description != null ? description : "");
        this.addAssociatedExecutionCourses(executionCourse);
        this.setGradeScale(GradeScale.TYPE20);

        setOnlineSubmissionProperties(onlineSubmissionsAllowed, maxSubmissionsToKeep, grouping);
    }

    public Project(String name, Date begin, Date end, String description, Boolean onlineSubmissionsAllowed,
            Integer maxSubmissionsToKeep, Grouping grouping, ExecutionCourse executionCourse, GradeScale gradeScale) {

        this(name, begin, end, description, onlineSubmissionsAllowed, maxSubmissionsToKeep, grouping, executionCourse);

        if (gradeScale != null) {
            this.setGradeScale(gradeScale);
        }

        logCreate();
    }

    public void edit(String name, Date begin, Date end, String description, Boolean onlineSubmissionsAllowed,
            Integer maxSubmissionsToKeep, Grouping grouping, GradeScale gradeScale, List<Department> departments) {
        if (name == null || begin == null || end == null) {
            throw new NullPointerException();
        }
        if (begin.after(end)) {
            throw new DomainException("error.evaluation.begin.sooner.end");
        }
        setName(name);
        setBegin(begin);
        setEnd(end);
        setGradeScale(gradeScale);
        setDescription((description != null) ? description : "");

        if (!getProjectSubmissions().isEmpty()) {
            if (!getGrouping().equals(grouping) || !getOnlineSubmissionsAllowed().equals(onlineSubmissionsAllowed)
                    || !getMaxSubmissionsToKeep().equals(maxSubmissionsToKeep)) {
                throw new DomainException("error.project.onlineSubmissionOptionsCannotBeChangedBecauseSubmissionsAlreadyExist");
            }

        }

        setOnlineSubmissionProperties(onlineSubmissionsAllowed, maxSubmissionsToKeep, grouping);
        final Collection<Department> departmentsList = getDeparments();
        departmentsList.clear();
        departmentsList.addAll(departments);

        logEdit();
    }

    private void setOnlineSubmissionProperties(Boolean onlineSubmissionsAllowed, Integer maxSubmissionsToKeep, Grouping grouping) {

        setOnlineSubmissionsAllowed(onlineSubmissionsAllowed);

        if (onlineSubmissionsAllowed == true) {
            if ((maxSubmissionsToKeep == null) || (maxSubmissionsToKeep == 0) || (grouping == null)) {
                throw new DomainException("error.project.maxSubmissionsAndGroupingRequiredForOnlineSubmissions");
            } else {
                setMaxSubmissionsToKeep(maxSubmissionsToKeep);
                setGrouping(grouping);
            }
        } else {
            setMaxSubmissionsToKeep(null);
            setGrouping(null);

        }
    }

    @Override
    public EvaluationType getEvaluationType() {
        return EvaluationType.PROJECT_TYPE;
    }

    @Deprecated
    public java.util.Date getBegin() {
        org.joda.time.DateTime dt = getProjectBeginDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setBegin(java.util.Date date) {
        setProjectBeginDateTime(new org.joda.time.DateTime(date.getTime()));
    }

    @Deprecated
    public java.util.Date getEnd() {
        org.joda.time.DateTime dt = getProjectEndDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setEnd(java.util.Date date) {
        setProjectEndDateTime(new org.joda.time.DateTime(date.getTime()));
    }

    @Override
    public void setMaxSubmissionsToKeep(Integer maxSubmissionsToKeep) {
        if (maxSubmissionsToKeep != null && maxSubmissionsToKeep > 99) {
            throw new DomainException("error.project.maxSubmissionsToKeepMustBeLessThan", "99");
        }

        super.setMaxSubmissionsToKeep(maxSubmissionsToKeep);
    }

    public boolean canAddNewSubmissionWithoutExceedLimit(StudentGroup studentGroup) {
        return (countProjectSubmissionsForStudentGroup(studentGroup) + 1) <= getMaxSubmissionsToKeep()
                && !(studentGroup.wasDeleted());
    }

    public int countProjectSubmissionsForStudentGroup(StudentGroup studentGroup) {
        int count = 0;

        for (ProjectSubmission projectSubmission : getProjectSubmissions()) {
            if (projectSubmission.getStudentGroup() == studentGroup) {
                count++;
            }
        }

        return count;
    }

    public boolean isSubmissionPeriodOpen() {
        DateTime currentDateTime = new DateTime();

        if ((currentDateTime.compareTo(getProjectBeginDateTime()) < 0)
                || (currentDateTime.compareTo(getProjectEndDateTime()) > 0)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isCanComment() {
        for (ExecutionCourse executionCourse : getAssociatedExecutionCourses()) {
            final Professorship professorship = executionCourse.getProfessorshipForCurrentUser();
            if (professorship != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void delete() {
        if (!getProjectSubmissions().isEmpty()) {
            throw new DomainException("error.project.cannotDeleteBecauseHasSubmissionsAssociated");
        }
        if (getProjectDepartmentGroup() != null) {
            throw new DomainException("error.project.cannotDeleteProjectUsedInAccessControl");
        }

        logRemove();
        setGrouping(null);
        getDeparmentsSet().clear();
        super.delete();
    }

    public List<ProjectSubmission> getProjectSubmissionsByStudentGroup(StudentGroup studentGroup) {
        List<ProjectSubmission> result = new ArrayList<ProjectSubmission>();

        for (ProjectSubmission projectSubmission : getProjectSubmissions()) {
            if (projectSubmission.getStudentGroup() == studentGroup) {
                result.add(projectSubmission);
            }
        }

        return result;
    }

    public ProjectSubmission getOldestProjectSubmissionForStudentGroup(StudentGroup studentGroup) {
        ProjectSubmission oldestProjectSubmission = null;
        for (ProjectSubmission projectSubmission : getProjectSubmissionsByStudentGroup(studentGroup)) {
            if (oldestProjectSubmission == null) {
                oldestProjectSubmission = projectSubmission;
            } else if (projectSubmission.getSubmissionDateTime().compareTo(oldestProjectSubmission.getSubmissionDateTime()) < 0) {
                oldestProjectSubmission = projectSubmission;
            }
        }

        return oldestProjectSubmission;
    }

    public Collection<ProjectSubmission> getLastProjectSubmissionForEachStudentGroup() {
        final Map<StudentGroup, ProjectSubmission> lastProjectSubmissionByStudentGroup =
                new HashMap<StudentGroup, ProjectSubmission>();

        for (final ProjectSubmission projectSubmission : getProjectSubmissions()) {
            final StudentGroup studentGroup = projectSubmission.getStudentGroup();

            if (studentGroup.wasDeleted()) {
                continue;
            }
            final ProjectSubmission lastProjectSubmission = lastProjectSubmissionByStudentGroup.get(studentGroup);

            if (lastProjectSubmission == null) {
                lastProjectSubmissionByStudentGroup.put(studentGroup, projectSubmission);
            } else if (projectSubmission.getSubmissionDateTime().compareTo(lastProjectSubmission.getSubmissionDateTime()) > 0) {
                lastProjectSubmissionByStudentGroup.put(studentGroup, projectSubmission);
            }
        }

        return lastProjectSubmissionByStudentGroup.values();
    }

    public Collection<ProjectSubmission> getLastProjectSubmissionForEachDeletedStudentGroup() {
        final Map<StudentGroup, ProjectSubmission> lastProjectSubmissionByStudentGroup =
                new HashMap<StudentGroup, ProjectSubmission>();

        for (final ProjectSubmission projectSubmission : getProjectSubmissions()) {
            final StudentGroup studentGroup = projectSubmission.getStudentGroup();

            if (!studentGroup.wasDeleted()) {
                continue;
            }

            final ProjectSubmission lastProjectSubmission = lastProjectSubmissionByStudentGroup.get(studentGroup);

            if (lastProjectSubmission == null) {
                lastProjectSubmissionByStudentGroup.put(studentGroup, projectSubmission);
            } else if (projectSubmission.getSubmissionDateTime().compareTo(lastProjectSubmission.getSubmissionDateTime()) > 0) {
                lastProjectSubmissionByStudentGroup.put(studentGroup, projectSubmission);
            }
        }

        return lastProjectSubmissionByStudentGroup.values();
    }

    public ProjectSubmission getLastProjectSubmissionForStudentGroup(StudentGroup group) {
        for (ProjectSubmission projectSubmission : getLastProjectSubmissionForEachStudentGroup()) {
            if (projectSubmission.getStudentGroup().equals(group)) {
                return projectSubmission;
            }
        }
        return null;
    }

    public List<ProjectSubmissionLog> getProjectSubmissionLogsByStudentGroup(StudentGroup studentGroup) {
        List<ProjectSubmissionLog> result = new ArrayList<ProjectSubmissionLog>();

        for (ProjectSubmissionLog projectSubmissionLog : getProjectSubmissionLogs()) {
            if (projectSubmissionLog.getStudentGroup() == studentGroup) {
                result.add(projectSubmissionLog);
            }
        }

        return result;
    }

    public List<EvaluationEventBean> getAllEvents(ExecutionCourse executionCourse) {
        List<EvaluationEventBean> result = new ArrayList<EvaluationEventBean>();
        result.add(new EvaluationEventBean("Inicio " + this.getName() + " : " + executionCourse.getNome(), this
                .getProjectBeginDateTime(), this.getProjectBeginDateTime().plusHours(1), false, null, null,
                this.getDescription(), Collections.singleton(executionCourse)));
        if (this.getOnlineSubmissionsAllowed()) {
            String url = FenixConfigurationManager.getFenixUrl() + "/privado";
            result.add(new EvaluationEventBean("Fim " + this.getName() + " : " + executionCourse.getNome(), this
                    .getProjectEndDateTime().minusHours(1), this.getProjectEndDateTime(), false, null, url,
                    this.getDescription(), Collections.singleton(executionCourse)));
        } else {
            result.add(new EvaluationEventBean("Fim " + this.getName() + " : " + executionCourse.getNome(), this
                    .getProjectEndDateTime().minusHours(1), this.getProjectEndDateTime(), false, null, null, this
                    .getDescription(), Collections.singleton(executionCourse)));
        }
        return result;
    }

    @Deprecated
    public java.util.Date getProjectBegin() {
        org.joda.time.DateTime dt = getProjectBeginDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setProjectBegin(java.util.Date date) {
        if (date == null) {
            setProjectBeginDateTime(null);
        } else {
            setProjectBeginDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Deprecated
    public java.util.Date getProjectEnd() {
        org.joda.time.DateTime dt = getProjectEndDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setProjectEnd(java.util.Date date) {
        if (date == null) {
            setProjectEndDateTime(null);
        } else {
            setProjectEndDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Override
    public String getPresentationName() {
        return BundleUtil.getStringFromResourceBundle("resources.ApplicationResources", "label.project") + " " + getName();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Department> getDeparments() {
        return getDeparmentsSet();
    }

    @Deprecated
    public boolean hasAnyDeparments() {
        return !getDeparmentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ProjectSubmission> getProjectSubmissions() {
        return getProjectSubmissionsSet();
    }

    @Deprecated
    public boolean hasAnyProjectSubmissions() {
        return !getProjectSubmissionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ProjectSubmissionLog> getProjectSubmissionLogs() {
        return getProjectSubmissionLogsSet();
    }

    @Deprecated
    public boolean hasAnyProjectSubmissionLogs() {
        return !getProjectSubmissionLogsSet().isEmpty();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasOnlineSubmissionsAllowed() {
        return getOnlineSubmissionsAllowed() != null;
    }

    @Deprecated
    public boolean hasDescription() {
        return getDescription() != null;
    }

    @Deprecated
    public boolean hasProjectBeginDateTime() {
        return getProjectBeginDateTime() != null;
    }

    @Deprecated
    public boolean hasMaxSubmissionsToKeep() {
        return getMaxSubmissionsToKeep() != null;
    }

    @Deprecated
    public boolean hasProjectEndDateTime() {
        return getProjectEndDateTime() != null;
    }

    @Deprecated
    public boolean hasGrouping() {
        return getGrouping() != null;
    }

}
