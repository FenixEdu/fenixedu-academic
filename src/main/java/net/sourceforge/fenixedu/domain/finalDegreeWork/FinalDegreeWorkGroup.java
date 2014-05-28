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

import java.util.Collections;
import java.util.Comparator;
import java.util.SortedSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;

import org.apache.commons.collections.Predicate;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.utl.ist.fenix.tools.util.CollectionUtils;

public class FinalDegreeWorkGroup extends FinalDegreeWorkGroup_Base {

    public static class IsValidGroupPredicate implements Predicate {

        @Override
        public boolean evaluate(Object arg0) {
            if (arg0 instanceof FinalDegreeWorkGroup) {
                return ((FinalDegreeWorkGroup) arg0).isValid();
            }
            return false;
        }

    }

    public static class AttributionStatusPredicate implements Predicate {

        final CandidacyAttributionType attributionType;

        public AttributionStatusPredicate(CandidacyAttributionType attributionType) {
            super();
            this.attributionType = attributionType;
        }

        @Override
        public boolean evaluate(Object object) {
            if (object instanceof FinalDegreeWorkGroup) {
                FinalDegreeWorkGroup group = (FinalDegreeWorkGroup) object;
                if (CandidacyAttributionType.TOTAL.equals(attributionType)
                        || group.getCandidacyAttributionStatus().equals(attributionType)) {
                    return true;
                }
            }
            return false;
        }

    }

    public final static Predicate WITHOUT_DISSERTATION_PREDICATE = new Predicate() {

        @Override
        public boolean evaluate(Object object) {
            if (object instanceof FinalDegreeWorkGroup) {
                FinalDegreeWorkGroup group = (FinalDegreeWorkGroup) object;
                if (group.isAttributed() && group.hasAnyGroupStudents()) {
                    final Student student = group.getGroupStudentsSet().iterator().next().getRegistration().getStudent();
                    final Degree degree = group.getExecutionDegree().getDegree();
                    ExecutionYear nextExecutionYear = group.getExecutionDegree().getExecutionYear().getNextExecutionYear();
                    for (final Registration registration : student.getRegistrationsSet()) {
                        if (degree == registration.getDegree()
                                && (registration.getStudentCurricularPlan(nextExecutionYear) != null)
                                && !registration.getStudentCurricularPlan(nextExecutionYear).getDissertationEnrolments()
                                        .isEmpty()) {
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            }
            return false;
        }
    };
    public static final Comparator<FinalDegreeWorkGroup> COMPARATOR_BY_STUDENT_NUMBERS = new Comparator<FinalDegreeWorkGroup>() {
        @Override
        public int compare(final FinalDegreeWorkGroup group1, final FinalDegreeWorkGroup group2) {
            if (group1.getGroupStudentsSet().isEmpty()) {
                return -1;
            }
            if (group2.getGroupStudentsSet().isEmpty()) {
                return 1;
            }
            final GroupStudent groupStudent1 =
                    Collections.min(group1.getGroupStudentsSet(), GroupStudent.COMPARATOR_BY_STUDENT_NUMBER);
            final GroupStudent groupStudent2 =
                    Collections.min(group2.getGroupStudentsSet(), GroupStudent.COMPARATOR_BY_STUDENT_NUMBER);
            return groupStudent1.getRegistration().getNumber().compareTo(groupStudent2.getRegistration().getNumber());
        }
    };

    public FinalDegreeWorkGroup() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public SortedSet<net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal> getGroupProposalsSortedByPreferenceOrder() {
        return CollectionUtils.constructSortedSet(getGroupProposalsSet(),
                net.sourceforge.fenixedu.domain.finalDegreeWork.GroupProposal.COMPARATOR_BY_PREFERENCE_ORDER);
    }

    public boolean isConfirmedByStudents(final Proposal proposal) {
        for (GroupStudent groupStudent : getGroupStudentsSet()) {
            if (groupStudent.getFinalDegreeWorkProposalConfirmation() != proposal) {
                return false;
            }
        }

        return true;
    }

    public void delete() {
        setExecutionDegree(null);
        for (final GroupProposal groupProposal : getGroupProposalsSet()) {
            groupProposal.delete();
        }
        for (final GroupStudent groupStudent : getGroupStudentsSet()) {
            groupStudent.delete();
        }
        setProposalAttributed(null);
        setProposalAttributedByTeacher(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public CandidacyAttributionType getCandidacyAttributionStatus() {
        if (hasProposalAttributed()) {
            return CandidacyAttributionType.ATTRIBUTED_BY_CORDINATOR;
        }
        Proposal proposalAttributedByTeacher = getProposalAttributedByTeacher();
        if (proposalAttributedByTeacher != null) {
            for (GroupStudent groupStudent : getGroupStudents()) {
                if (groupStudent.getFinalDegreeWorkProposalConfirmation() != proposalAttributedByTeacher) {
                    return CandidacyAttributionType.ATTRIBUTED_NOT_CONFIRMED;
                }
            }
            return CandidacyAttributionType.ATTRIBUTED;
        }
        return CandidacyAttributionType.NOT_ATTRIBUTED;
    }

    public boolean getAttributed() {
        return getCandidacyAttributionStatus().isFinalAttribution();
    }

    public boolean isAttributed() {
        return getAttributed();
    }

    public Proposal getAttributedProposal() {
        if (hasProposalAttributed()) {
            return getProposalAttributed();
        }
        Proposal proposalAttributedByTeacher = getProposalAttributedByTeacher();
        if (proposalAttributedByTeacher != null) {
            for (GroupStudent groupStudent : getGroupStudents()) {
                if (groupStudent.getFinalDegreeWorkProposalConfirmation() != proposalAttributedByTeacher) {
                    return null;
                }
            }
            return proposalAttributedByTeacher;
        }
        return null;
    }

    public boolean isValid() {
        return hasAnyGroupProposals() || hasProposalAttributed() || hasProposalAttributedByTeacher();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent> getGroupStudents() {
        return getGroupStudentsSet();
    }

    @Deprecated
    public boolean hasAnyGroupStudents() {
        return !getGroupStudentsSet().isEmpty();
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
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasProposalAttributed() {
        return getProposalAttributed() != null;
    }

    @Deprecated
    public boolean hasExecutionDegree() {
        return getExecutionDegree() != null;
    }

    @Deprecated
    public boolean hasProposalAttributedByTeacher() {
        return getProposalAttributedByTeacher() != null;
    }

}
