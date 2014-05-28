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
 * Created on 2004/04/15
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;

/**
 * @author Luis Cruz
 * 
 */
public class InfoGroupStudent extends InfoObject {

    private final GroupStudent groupStudentDomainReference;

    public InfoGroupStudent(final GroupStudent groupStudent) {
        groupStudentDomainReference = groupStudent;
    }

    public static InfoGroupStudent newInfoFromDomain(GroupStudent groupStudent) {
        return groupStudent == null ? null : new InfoGroupStudent(groupStudent);
    }

    private GroupStudent getGroupStudent() {
        return groupStudentDomainReference;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof InfoGroupStudent && getGroupStudent() == ((InfoGroupStudent) obj).getGroupStudent();
    }

    @Override
    public int hashCode() {
        return getGroupStudent().hashCode();
    }

    @Override
    public String getExternalId() {
        return getGroupStudent().getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

    @Override
    public String toString() {
        return getGroupStudent().toString();
    }

    /**
     * @return Returns the finalDegreeDegreeWorkGroup.
     */
    public InfoGroup getFinalDegreeDegreeWorkGroup() {
        return InfoGroup.newInfoFromDomain(getGroupStudent().getFinalDegreeDegreeWorkGroup());
    }

    /**
     * @return Returns the finalDegreeWorkProposalConfirmation.
     */
    public InfoProposal getFinalDegreeWorkProposalConfirmation() {
        return InfoProposal.newInfoFromDomain(getGroupStudent().getFinalDegreeWorkProposalConfirmation());
    }

    /**
     * @return Returns the student.
     */
    public InfoStudent getStudent() {
        return InfoStudent.newInfoFromDomain(getGroupStudent().getRegistration());
    }

}
