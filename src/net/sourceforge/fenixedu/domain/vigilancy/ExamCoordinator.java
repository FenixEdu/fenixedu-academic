package net.sourceforge.fenixedu.domain.vigilancy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.joda.time.DateTime;

public class ExamCoordinator extends ExamCoordinator_Base {

    public ExamCoordinator() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    private ExamCoordinator(Person person) {
        this();
        this.setPerson(person);
    }

    public ExamCoordinator(Person person, ExecutionYear executionYear, Unit unit) {
        this(person);
        this.setExecutionYear(executionYear);
        this.setUnit(unit);
        this.setAllowedToCreateGroups(true);
    }

    public String getEmail() {
        return this.getPerson().getEmail();
    }

    public List<Convoke> getConvokesThatCanManage() {
        List<VigilantGroup> groups = this.getVigilantGroups();
        Set<Convoke> convokes = new HashSet<Convoke>();

        for (VigilantGroup group : groups) {
            convokes.addAll(group.getConvokes());
        }

        return new ArrayList<Convoke>(convokes);
    }

    public List<UnavailablePeriod> getUnavailablePeriodsThatCanManage() {
        Set<UnavailablePeriod> unavailablePeriods = new HashSet<UnavailablePeriod>();
        for (VigilantGroup group : this.getVigilantGroups()) {
            unavailablePeriods.addAll(group.getUnavailablePeriodsOfVigilantsInGroup());
        }
        return new ArrayList<UnavailablePeriod>(unavailablePeriods);
    }

    public List<Vigilant> getVigilantsThatCanManage() {
        Set<Vigilant> vigilants = new HashSet<Vigilant>();
        List<VigilantGroup> groups = this.getVigilantGroups();

        for (VigilantGroup group : groups) {
            vigilants.addAll(group.getVigilants());
        }

        return new ArrayList<Vigilant>(vigilants);
    }

    public Boolean isAllowedToCreateGroups() {
        return this.getAllowedToCreateGroups();
    }

    public boolean managesGivenVigilantGroup(VigilantGroup group) {
        List<VigilantGroup> groups = this.getVigilantGroups();
        return groups.contains(group);
    }

    /*
     * public List<Convoke> getConvokesThatCanManage(ExecutionCourse course) {
     * List<Convoke> convokesForExecutionCourse = new ArrayList<Convoke> ();
     * for(Convoke convoke : this.getConvokesThatCanManage()) {
     * if(convoke.getAssociatedExecutionCourses().contains(course)) {
     * convokesForExecutionCourse.add(convoke); } } return
     * convokesForExecutionCourse; }
     * 
     * public List<Convoke>
     * getConvokesThatCanManageAndAreBeforeCurrentDate(ExecutionCourse course) {
     * List<Convoke> convokes = this.getConvokesThatCanManage(course); List<Convoke>
     * convokesToReturn = new ArrayList<Convoke> (); DateTime currentDate = new
     * DateTime(); for(Convoke convoke : convokes) {
     * if(convoke.getBeginDateTime().isBefore(currentDate)) {
     * convokesToReturn.add(convoke); }
     *  } return convokesToReturn; }
     */
    public List<ExecutionCourse> getAssociatedExecutionCourses() {
        List<ExecutionCourse> courses = new ArrayList<ExecutionCourse>();
        for (VigilantGroup group : this.getVigilantGroups()) {
            courses.addAll(group.getExecutionCourses());
        }
        return courses;
    }

    /*
     * public List<Convoke>
     * getConvokesThatCanManageAndAreAfterCurrentDate(ExecutionCourse course) {
     * List<Convoke> convokes = this.getConvokesThatCanManage(course); List<Convoke>
     * convokesToReturn = new ArrayList<Convoke> (); DateTime currentDate = new
     * DateTime(); for(Convoke convoke : convokes) {
     * if(convoke.getBeginDateTime().isAfter(currentDate)) {
     * convokesToReturn.add(convoke); }
     *  } return convokesToReturn; }
     */

    public List<WrittenEvaluation> getAssociatedWrittenEvaluations() {
        List<VigilantGroup> groups = this.getVigilantGroups();
        List<WrittenEvaluation> evaluations = new ArrayList<WrittenEvaluation>();
        for (VigilantGroup group : groups) {
            evaluations.addAll(group.getWrittenEvaluations());
        }
        return evaluations;
    }

    public List<WrittenEvaluation> getAssociatedWrittenEvaluationsAfterDate(DateTime date) {
        List<VigilantGroup> groups = this.getVigilantGroups();
        Set<WrittenEvaluation> evaluations = new HashSet<WrittenEvaluation>();
        for (VigilantGroup group : groups) {
            evaluations.addAll(group.getWrittenEvaluationsAfterDate(date));
        }
        return new ArrayList<WrittenEvaluation>(evaluations);
    }

    public List<WrittenEvaluation> getAssociatedWrittenEvaluationsBeforeDate(DateTime date) {
        List<VigilantGroup> groups = this.getVigilantGroups();
        Set<WrittenEvaluation> evaluations = new HashSet<WrittenEvaluation>();
        for (VigilantGroup group : groups) {
            evaluations.addAll(group.getWrittenEvaluationsBeforeDate(date));
        }
        return new ArrayList<WrittenEvaluation>(evaluations);
    }

    public void delete() {
        this.getUnit().removeExamCoordinators(this);
        removeRootDomainObject();
        removePerson();
        removeExecutionYear();
        super.removeRootDomainObject();
    }
}
