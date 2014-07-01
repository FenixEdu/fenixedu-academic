/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.finalDegreeWork;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Accountability;
import net.sourceforge.fenixedu.domain.organizationalStructure.CompetenceCourseGroupUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.ProposalStatusType;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.ProposalsFilterBean.WithCandidatesFilter;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.FinalDegreeWorkProposalStatus;

import org.apache.commons.collections.Predicate;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class Proposal extends Proposal_Base {

    public final static Comparator<Proposal> COMPARATOR_BY_PROPOSAL_NUMBER = new Comparator<Proposal>() {

        @Override
        public int compare(Proposal arg0, Proposal arg1) {
            return arg0.getProposalNumber().compareTo(arg1.getProposalNumber());
        }

    };

    public final static Comparator<Proposal> COMPARATOR_BY_STATUS = new Comparator<Proposal>() {

        @Override
        public int compare(Proposal arg0, Proposal arg1) {
            return arg0.getProposalStatus().compareTo(arg1.getProposalStatus());
        }

    };

    public final static Comparator<Proposal> COMPARATOR_BY_NUMBER_OF_CANDIDATES = new Comparator<Proposal>() {

        @Override
        public int compare(Proposal arg0, Proposal arg1) {
            if (arg1.getNumberOfCandidates() > arg0.getNumberOfCandidates()) {
                return -1;
            } else {
                return 1;
            }
        }

    };

    public static class StatusPredicate implements Predicate {
        private final ProposalStatusType status;

        public StatusPredicate(ProposalStatusType status) {
            this.status = status;
        }

        @Override
        public boolean evaluate(Object object) {
            if (object instanceof Proposal) {
                Proposal proposal = (Proposal) object;
                return proposal.getProposalStatus().equals(this.status);
            }
            return false;
        }
    }

    public static class CandidacyAttributionPredicate implements Predicate {
        private final Set<CandidacyAttributionType> attributionTypes;

        public CandidacyAttributionPredicate(Set<CandidacyAttributionType> types) {
            attributionTypes = types;
        }

        @Override
        public boolean evaluate(Object object) {
            if (object instanceof Proposal) {
                if (attributionTypes == null || attributionTypes.isEmpty()) {
                    return true;
                }
                final CandidacyAttributionType status = ((Proposal) object).getAttributionStatus();
                if (status == null) {
                    return false;
                }
                for (CandidacyAttributionType type : attributionTypes) {
                    if (type.equals(status)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public static class HasCandidatesPredicate implements Predicate {
        private final WithCandidatesFilter withCandidates;

        public HasCandidatesPredicate(WithCandidatesFilter withCandidatesFilter) {
            this.withCandidates = withCandidatesFilter;
        }

        @Override
        public boolean evaluate(Object object) {
            if (object instanceof Proposal) {
                Proposal proposal = (Proposal) object;
                switch (withCandidates) {
                case ALL:
                    return true;
                case WITH_CANDIDATES:
                    return proposal.getNumberOfCandidates() > 0;
                case WITHOUT_CANDIDATES:
                    return proposal.getNumberOfCandidates() == 0;

                }
            }
            return false;
        }
    }

    public Proposal() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void delete() {
        getAssociatedGroupStudentsSet().clear();
        getBranchesSet().clear();
        setCoorientator(null);
        setGroupAttributed(null);
        setGroupAttributedByTeacher(null);
        for (final GroupProposal groupProposal : getGroupProposalsSet()) {
            groupProposal.delete();
        }
        setOrientator(null);
        setScheduleing(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public boolean isProposalConfirmedByTeacherAndStudents(final FinalDegreeWorkGroup group) {
        return getGroupAttributedByTeacher() == group && group.isConfirmedByStudents(this);
    }

    public boolean isForExecutionYear(final ExecutionYear executionYear) {
        final Scheduleing scheduleing = getScheduleing();
        for (final ExecutionDegree executionDegree : scheduleing.getExecutionDegreesSet()) {
            if (executionDegree.getExecutionYear() == executionYear) {
                return true;
            }
        }
        return false;
    }

    public boolean canBeReadBy(final User userView) {
        if (getStatus() != null && getStatus().equals(FinalDegreeWorkProposalStatus.PUBLISHED_STATUS)) {
            return true;
        }
        if (getOrientator() != null && getOrientator() != null && userView != null
                && getOrientator().getUsername().equals(userView.getUsername())) {
            return true;
        }
        if (getCoorientator() != null && getCoorientator() != null && userView != null
                && getCoorientator().getUsername().equals(userView.getUsername())) {
            return true;
        }
        if (userView != null) {
            final Person person = userView.getPerson();
            for (final ExecutionDegree executionDegree : getScheduleing().getExecutionDegrees()) {
                for (final Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
                    if (coordinator.getPerson() == person) {
                        return true;
                    }
                }

                if (person.getEmployee() != null && person.hasRole(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE)) {
                    final Employee employee = person.getEmployee();
                    final Department department = employee.getCurrentDepartmentWorkingPlace();
                    final Set<CompetenceCourse> competenceCourses = department.getCompetenceCoursesSet();
                    if (hasDissertationCompetenceCourseForDepartment(executionDegree, competenceCourses)
                            || hasDissertationCompetenceCourseForDepartment(executionDegree, department.getDepartmentUnit())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected boolean hasDissertationCompetenceCourseForDepartment(final ExecutionDegree executionDegree,
            final Set<CompetenceCourse> competenceCourses) {
        for (final CompetenceCourse competenceCourse : competenceCourses) {
            for (final CurricularCourse curricularCourse : competenceCourse.getAssociatedCurricularCoursesSet()) {
                if (curricularCourse.getType() == CurricularCourseType.TFC_COURSE || competenceCourse.isDissertation()) {
                    final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
                    if (degreeCurricularPlan.getExecutionDegreesSet().contains(executionDegree)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean hasDissertationCompetenceCourseForDepartment(final ExecutionDegree executionDegree, final Unit unit) {
        if (unit.isCompetenceCourseGroupUnit()) {
            final CompetenceCourseGroupUnit competenceCourseGroupUnit = (CompetenceCourseGroupUnit) unit;
            if (hasDissertationCompetenceCourseForDepartment(executionDegree, competenceCourseGroupUnit.getCompetenceCoursesSet())) {
                return true;
            }
        }
        for (final Accountability accountability : unit.getChildsSet()) {
            final Party party = accountability.getChildParty();
            if (party.isUnit() && hasDissertationCompetenceCourseForDepartment(executionDegree, (Unit) party)) {
                return true;
            }
        }
        return false;
    }

    public int getNumberOfCandidates() {
        return getGroupProposalsSet().size();
    }

    public List<Person> getOrientators() {
        List<Person> persons = new ArrayList<Person>();
        persons.add(getOrientator());
        persons.add(getCoorientator());
        return persons;
    }

    public String getOrientatorsAsString() {
        final StringBuilder builder = new StringBuilder();
        if (hasOrientator()) {
            builder.append(getOrientator().getName());
        }
        if (hasCoorientator()) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(getCoorientator().getName());
        }
        if (getCompanionName() != null && !getCompanionName().isEmpty()) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(getCompanionName());
        }
        return builder.toString();
    }

    public CandidacyAttributionType getAttributionStatus() {
        if (hasGroupAttributed()) {
            return CandidacyAttributionType.ATTRIBUTED_BY_CORDINATOR;
        }
        if (hasGroupAttributedByTeacher()) {
            FinalDegreeWorkGroup group = getGroupAttributedByTeacher();
            for (GroupStudent groupStudent : group.getGroupStudents()) {
                if (groupStudent.getFinalDegreeWorkProposalConfirmation() != this) {
                    return CandidacyAttributionType.ATTRIBUTED_NOT_CONFIRMED;
                }
            }
            return CandidacyAttributionType.ATTRIBUTED;
        }
        return CandidacyAttributionType.NOT_ATTRIBUTED;
    }

    public Collection<GroupStudent> getAttributionGroup() {
        if (hasGroupAttributed()) {
            return getGroupAttributed().getGroupStudents();
        }
        if (hasGroupAttributedByTeacher()) {
            FinalDegreeWorkGroup group = getGroupAttributedByTeacher();
            for (GroupStudent groupStudent : group.getGroupStudents()) {
                if (groupStudent.getFinalDegreeWorkProposalConfirmation() != this) {
                    return Collections.emptyList();
                }
            }
            return group.getGroupStudents();
        }
        return Collections.emptyList();
    }

    public String getAttributionStatusLabel() {
        String key = getAttributionStatus().getClass().getSimpleName() + "." + getAttributionStatus();
        return BundleUtil.getString(Bundle.ENUMERATION, key);
    }

    public boolean getForPublish() {
        return getForApproval() || getStatus().equals(FinalDegreeWorkProposalStatus.APPROVED_STATUS);
    }

    public boolean getForApproval() {
        return getStatus() == null;
    }

    public ProposalStatusType getProposalStatus() {
        FinalDegreeWorkProposalStatus status = getStatus();
        if (status == null) {
            return ProposalStatusType.FOR_APPROVAL;
        }
        if (FinalDegreeWorkProposalStatus.APPROVED_STATUS.equals(status)) {
            return ProposalStatusType.APPROVED;
        }
        if (FinalDegreeWorkProposalStatus.PUBLISHED_STATUS.equals(status)) {
            return ProposalStatusType.PUBLISHED;
        }
        return null;
    }

    public String getPresentationName() {
        return String.format("%s - %s", getProposalNumber(), getTitle());
    }

    public Set<FinalDegreeWorkGroup> getCandidacies() {
        final Set<FinalDegreeWorkGroup> candidacies = new HashSet<FinalDegreeWorkGroup>();
        for (GroupProposal groupProposal : getGroupProposals()) {
            candidacies.add(groupProposal.getFinalDegreeDegreeWorkGroup());
        }
        return candidacies;
    }

    @Override
    public void setCoorientator(Person coorientator) {
        if (coorientator != getCoorientator()) {
            checkPersonProposals(coorientator);
        }
        super.setCoorientator(coorientator);
    }

    @Override
    public void setOrientator(Person orientator) {
        if (orientator != getOrientator()) {
            checkPersonProposals(orientator);
        }
        super.setOrientator(orientator);
    }

    protected void checkPersonProposals(Person person) {
        if (person != null && getScheduleing() != null) {
            int count = 0;

            for (Proposal p : person.getAssociatedProposalsByOrientator()) {
                if (p.getScheduleing().equals(getScheduleing())) {
                    count++;
                }
            }

            for (Proposal p : person.getAssociatedProposalsByCoorientator()) {
                if (p.getScheduleing().equals(getScheduleing())) {
                    count++;
                }
            }

            Integer maximumNumberOfProposalsPerPerson = getScheduleing().getMaximumNumberOfProposalsPerPerson();

            if (maximumNumberOfProposalsPerPerson != null && maximumNumberOfProposalsPerPerson > 0) {
                if (count >= maximumNumberOfProposalsPerPerson) {
                    throw new DomainException("error.Scheduleing.maximumNumberOfProposalsPerPerson");
                }
            }
        }
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent> getAssociatedGroupStudents() {
        return getAssociatedGroupStudentsSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedGroupStudents() {
        return !getAssociatedGroupStudentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Branch> getBranches() {
        return getBranchesSet();
    }

    @Deprecated
    public boolean hasAnyBranches() {
        return !getBranchesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal> getGroupProposals() {
        return getGroupProposalsSet();
    }

    @Deprecated
    public boolean hasAnyGroupProposals() {
        return !getGroupProposalsSet().isEmpty();
    }

    @Deprecated
    public boolean hasDescription() {
        return getDescription() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasProposalNumber() {
        return getProposalNumber() != null;
    }

    @Deprecated
    public boolean hasCompanionMail() {
        return getCompanionMail() != null;
    }

    @Deprecated
    public boolean hasCompanyAdress() {
        return getCompanyAdress() != null;
    }

    @Deprecated
    public boolean hasGroupAttributed() {
        return getGroupAttributed() != null;
    }

    @Deprecated
    public boolean hasObservations() {
        return getObservations() != null;
    }

    @Deprecated
    public boolean hasLocation() {
        return getLocation() != null;
    }

    @Deprecated
    public boolean hasDeliverable() {
        return getDeliverable() != null;
    }

    @Deprecated
    public boolean hasCompanyName() {
        return getCompanyName() != null;
    }

    @Deprecated
    public boolean hasRequirements() {
        return getRequirements() != null;
    }

    @Deprecated
    public boolean hasTitle() {
        return getTitle() != null;
    }

    @Deprecated
    public boolean hasFraming() {
        return getFraming() != null;
    }

    @Deprecated
    public boolean hasOrientatorsCreditsPercentage() {
        return getOrientatorsCreditsPercentage() != null;
    }

    @Deprecated
    public boolean hasStatus() {
        return getStatus() != null;
    }

    @Deprecated
    public boolean hasMinimumNumberOfGroupElements() {
        return getMinimumNumberOfGroupElements() != null;
    }

    @Deprecated
    public boolean hasCompanionName() {
        return getCompanionName() != null;
    }

    @Deprecated
    public boolean hasUrl() {
        return getUrl() != null;
    }

    @Deprecated
    public boolean hasCoorientatorsCreditsPercentage() {
        return getCoorientatorsCreditsPercentage() != null;
    }

    @Deprecated
    public boolean hasMaximumNumberOfGroupElements() {
        return getMaximumNumberOfGroupElements() != null;
    }

    @Deprecated
    public boolean hasObjectives() {
        return getObjectives() != null;
    }

    @Deprecated
    public boolean hasOrientator() {
        return getOrientator() != null;
    }

    @Deprecated
    public boolean hasGroupAttributedByTeacher() {
        return getGroupAttributedByTeacher() != null;
    }

    @Deprecated
    public boolean hasCoorientator() {
        return getCoorientator() != null;
    }

    @Deprecated
    public boolean hasScheduleing() {
        return getScheduleing() != null;
    }

    @Deprecated
    public boolean hasCompanionPhone() {
        return getCompanionPhone() != null;
    }

    @Deprecated
    public boolean hasDegreeType() {
        return getDegreeType() != null;
    }

}
