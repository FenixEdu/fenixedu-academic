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
/*
 * Created on 2004/04/19
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.apache.commons.collections.Predicate;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz
 * 
 */
public class RemoveStudentFromFinalDegreeWorkStudentGroup {

    public RemoveStudentFromFinalDegreeWorkStudentGroup() {
        super();
    }

    @Atomic
    public static Boolean run(String username, String groupOID, String studentToRemoveID) throws FenixServiceException {
        check(RolePredicates.STUDENT_PREDICATE);
        FinalDegreeWorkGroup group = FenixFramework.getDomainObject(groupOID);
        Registration registration = Registration.readByUsername(username);

        if (group == null || registration == null || group.getGroupStudents() == null
                || registration.getExternalId().equals(studentToRemoveID)) {
            return false;
        }

        if (!group.getGroupProposals().isEmpty()) {
            throw new GroupProposalCandidaciesExistException();
        }

        PREDICATE_FILTER_STUDENT_ID predicate = new PREDICATE_FILTER_STUDENT_ID(studentToRemoveID);
        for (GroupStudent groupStudent : group.getGroupStudents()) {
            if (!predicate.evaluate(groupStudent)) {
                groupStudent.delete();
            }
        }
        return true;
    }

    private static class PREDICATE_FILTER_STUDENT_ID implements Predicate {
        String studentID;

        @Override
        public boolean evaluate(Object arg0) {
            GroupStudent groupStudent = (GroupStudent) arg0;
            if (groupStudent != null && groupStudent.getRegistration() != null && studentID != null
                    && !studentID.equals(groupStudent.getRegistration().getExternalId())) {
                return true;
            }
            return false;

        }

        public PREDICATE_FILTER_STUDENT_ID(String studentID) {
            super();
            this.studentID = studentID;
        }
    }

    public static class GroupProposalCandidaciesExistException extends FenixServiceException {

        public GroupProposalCandidaciesExistException() {
            super();
        }

        public GroupProposalCandidaciesExistException(int errorType) {
            super(errorType);
        }

        public GroupProposalCandidaciesExistException(String s) {
            super(s);
        }

        public GroupProposalCandidaciesExistException(Throwable cause) {
            super(cause);
        }

        public GroupProposalCandidaciesExistException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}