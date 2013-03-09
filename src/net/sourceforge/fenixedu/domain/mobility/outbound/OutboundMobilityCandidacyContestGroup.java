package net.sourceforge.fenixedu.domain.mobility.outbound;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.commons.Transformer;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculum;
import net.sourceforge.fenixedu.util.BundleUtil;
import net.sourceforge.fenixedu.util.StringUtils;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class OutboundMobilityCandidacyContestGroup extends OutboundMobilityCandidacyContestGroup_Base implements
        Comparable<OutboundMobilityCandidacyContestGroup> {

    public OutboundMobilityCandidacyContestGroup(final OutboundMobilityCandidacyContest contest,
            final Set<ExecutionDegree> executionDegrees) {
        if (contest == null) {
            throw new NullPointerException("error.OutboundMobilityCandidacyContestGroup.must.have.contest");
        }
        if (executionDegrees.isEmpty()) {
            throw new NullPointerException("error.OutboundMobilityCandidacyContestGroup.must.have.execution.degree");
        }
        setRootDomainObject(RootDomainObject.getInstance());
        addOutboundMobilityCandidacyContest(contest);
        getExecutionDegreeSet().addAll(executionDegrees);

        // TODO : This is a hack due to a bug in the consistency predicate or fenix-framework code.
        //        When the relation is initialized but never traversed, the consistency predicate always
        //        fails. Forcing a traversal will resolve this issue. The bug has already been solved in
        //        the framework, but the framework has not yet been updated on this project.
        getOutboundMobilityCandidacyContestCount();
        getExecutionDegreeCount();
    }

    public OutboundMobilityCandidacyContestGroup(final Set<ExecutionDegree> executionDegrees) {
        if (executionDegrees.isEmpty()) {
            throw new NullPointerException("error.OutboundMobilityCandidacyContestGroup.must.have.execution.degree");
        }
        setRootDomainObject(RootDomainObject.getInstance());
        getExecutionDegreeSet().addAll(executionDegrees);

        // TODO : This is a hack due to a bug in the consistency predicate or fenix-framework code.
        //        When the relation is initialized but never traversed, the consistency predicate always
        //        fails. Forcing a traversal will resolve this issue. The bug has already been solved in
        //        the framework, but the framework has not yet been updated on this project.
        getOutboundMobilityCandidacyContestCount();
        getExecutionDegreeCount();
    }

    @Override
    public int compareTo(final OutboundMobilityCandidacyContestGroup o) {
        final int dc = getDescription().compareTo(o.getDescription());
        return dc == 0 ? getExternalId().compareTo(o.getExternalId()) : dc;
    }

    public String getDescription() {
        return StringUtils.join(getSortedExecutionDegrees(), ", ", new Transformer<ExecutionDegree, String>() {
            @Override
            public String transform(final ExecutionDegree executionDegree) {
                return executionDegree.getDegree().getSigla();
            }
        });
    }

    public SortedSet<ExecutionDegree> getSortedExecutionDegrees() {
        final SortedSet<ExecutionDegree> result = new TreeSet<ExecutionDegree>(ExecutionDegree.COMPARATOR_BY_DEGREE_CODE);
        result.addAll(getExecutionDegreeSet());
        return result;
    }

    @Service
    public void addExecutionDegreeService(final ExecutionDegree executionDegree) {
        addExecutionDegree(executionDegree);
    }

    @Service
    public void removeExecutionDegreeService(final ExecutionDegree executionDegree) {
        removeExecutionDegree(executionDegree);
    }

    @Service
    public void addMobilityCoordinatorService(final Person person) {
        addMobilityCoordinator(person);
    }

    @Service
    public void removeMobilityCoordinatorService(final Person person) {
        removeMobilityCoordinator(person);
    }

    public static OutboundMobilityCandidacyContestGroup findOrCreateGroup(final ExecutionDegree executionDegree) {
        for (final OutboundMobilityCandidacyContestGroup mobilityGroup : executionDegree
                .getOutboundMobilityCandidacyContestGroupSet()) {
            if (mobilityGroup.getExecutionDegreeCount() == 1) {
                return mobilityGroup;
            }
        }
        return new OutboundMobilityCandidacyContestGroup(Collections.singleton(executionDegree));
    }

    public void delete() {
        getExecutionDegreeSet().clear();
        getMobilityCoordinatorSet().clear();
        for (final OutboundMobilityCandidacyContest contest : getOutboundMobilityCandidacyContestSet()) {
            contest.delete();
        }
        removeRootDomainObject();
        deleteDomainObject();

    }

    public boolean handles(final Degree degree) {
        for (final ExecutionDegree executionDegree : getExecutionDegreeSet()) {
            if (executionDegree.getDegree() == degree) {
                return true;
            }
        }
        return false;
    }

    public void fillCandidatesInformation(final Spreadsheet spreadsheet, final OutboundMobilityCandidacyPeriod period) {
        spreadsheet.setHeader(getString("label.username"));
        spreadsheet.setHeader(getString("label.name"));
        spreadsheet.setHeader(getString("label.degree"));
        spreadsheet.setHeader(getString("label.candidate.classification"));
        spreadsheet.setHeader(getString("label.curricular.year"));
        spreadsheet.setHeader(getString("label.ects.completed.degree"));
        spreadsheet.setHeader(getString("label.average.degree"));
        spreadsheet.setHeader(getString("label.ects.completed.cycle.first"));
        spreadsheet.setHeader(getString("label.average.cycle.first"));
        spreadsheet.setHeader(getString("label.ects.completed.cycle.second"));
        spreadsheet.setHeader(getString("label.average.cycle.second"));

        final Set<Registration> processed = new HashSet<Registration>();
        for (final OutboundMobilityCandidacyContest contest : getOutboundMobilityCandidacyContestSet()) {
            for (final OutboundMobilityCandidacy candidacy : contest.getOutboundMobilityCandidacySet()) {
                final OutboundMobilityCandidacySubmission submission = candidacy.getOutboundMobilityCandidacySubmission();
                final Registration registration = submission.getRegistration();

                if (!processed.contains(registration)) {
                    final Row row = spreadsheet.addRow();
                    final Person person = registration.getPerson();
                    final BigDecimal candidacyGrade = submission.getGrade(this);
                    final ICurriculum curriculum = registration.getCurriculum();
                    row.setCell(person.getUsername());
                    row.setCell(person.getName());
                    row.setCell(registration.getDegree().getSigla());
                    row.setCell(candidacyGrade == null ? "" : candidacyGrade.toString());
                    row.setCell(curriculum.getCurricularYear());
                    row.setCell(curriculum.getSumEctsCredits().toString());
                    row.setCell(curriculum.getAverage().toString());
                    fillCycleDetails(row, CycleType.FIRST_CYCLE, registration);
                    fillCycleDetails(row, CycleType.SECOND_CYCLE, registration);

                    processed.add(registration);
                }
            }
        }
    }

    private void fillCycleDetails(final Row row, final CycleType cycleType, final Registration registration) {
        if (isForCycle(cycleType, registration)) {
            ICurriculum curriculum = registration.getCurriculum(cycleType);
            row.setCell(curriculum.getSumEctsCredits().toString());
            row.setCell(curriculum.getAverage().toString());
        } else {
            row.setCell("");
            row.setCell("");
        }
    }

    private boolean isForCycle(final CycleType cycleType, final Registration registration) {
        for (final StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
            if (studentCurricularPlan.getCycle(cycleType) != null) {
                return true;
            }
        }
        return false;
    }

    private String getString(final String key) {
        return BundleUtil.getStringFromResourceBundle("resources.AcademicAdminOffice", key);
    }

    @Service
    public void setGrades(final OutboundMobilityCandidacyPeriod candidacyPeriod, final String contents) {
        final StringBuilder problems = new StringBuilder();

        int l = 0;
        for (final String line : contents.split("\n")) {
            l++;
            final String[] parts = line.split("\t");
            final Person person = Person.findByUsername(parts[0]);
            if (person == null) {
                problems.append(getMessage("error.username.not.valid.on.line", parts[0], Integer.toString(l)));
            }
            try {
                final BigDecimal grade = new BigDecimal(parts[1]);
                final OutboundMobilityCandidacySubmission submission = candidacyPeriod.findSubmissionFor(person);
                submission.setGrade(this, grade);
            } catch (final NumberFormatException ex) {
                problems.append(getMessage("error.invalid.grade.on.one", parts[1], Integer.toString(l)));
            }
        }

        if (problems.length() > 0) {
            throw new DomainException("error.unable.to.set.grades", problems.toString());
        }
    }

    private String getMessage(final String key, final String... args) {
        return BundleUtil.getStringFromResourceBundle("resources.AcademicAdminOffice", key, args);
    }

}
