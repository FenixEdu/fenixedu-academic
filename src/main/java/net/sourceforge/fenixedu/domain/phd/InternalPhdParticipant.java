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
package net.sourceforge.fenixedu.domain.phd;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.apache.commons.lang.StringUtils.isEmpty;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitAcronym;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class InternalPhdParticipant extends InternalPhdParticipant_Base {

    static {
        getRelationInternalPhdParticipantPerson().addListener(new RelationAdapter<Person, InternalPhdParticipant>() {

            @Override
            public void beforeAdd(Person person, InternalPhdParticipant participant) {
                if (participant != null && person != null) {
                    for (final PhdParticipant each : participant.getIndividualProcess().getParticipants()) {
                        if (each.isInternal() && ((InternalPhdParticipant) each).isFor(person)) {
                            throw new DomainException("phd.InternalPhdParticipant.process.already.has.participant.for.person");
                        }
                    }
                }
            }

        });
    }

    private InternalPhdParticipant() {
        super();
    }

    InternalPhdParticipant(PhdIndividualProgramProcess process, PhdParticipantBean bean) {
        this();
        checkPerson(process, bean.getPerson());
        init(process);
        setPerson(bean.getPerson());
        setTitle(bean.getTitle());
        setInstitution(bean.getInstitution());
        setWorkLocation(bean.getWorkLocation());
    }

    private void checkPerson(PhdIndividualProgramProcess process, final Person person) {
        String[] args = {};
        if (process == null) {
            throw new DomainException("error.InternalPhdParticipant.process.cannot.be.null", args);
        }
        String[] args1 = {};
        if (person == null) {
            throw new DomainException("error.InternalPhdParticipant.person.cannot.be.null", args1);
        }

        for (final PhdParticipant participant : process.getParticipantsSet()) {
            if (participant.isFor(person)) {
                throw new DomainException("error.InternalPhdParticipant.person.already.is.participant");
            }
        }
    }

    @Override
    public String getName() {
        return getPerson().getName();
    }

    @Override
    public String getQualification() {
        final Qualification qualification = getPerson().getLastQualification();
        return qualification != null ? qualification.getType().getLocalizedName() : EMPTY;
    }

    @Override
    public String getCategory() {
        if (!isEmpty(super.getCategory())) {
            return super.getCategory();
        }
        return getTeacher() != null && getTeacher().getCategory() != null ? getTeacher().getCategory().getName().getContent() : EMPTY;
    }

    public Teacher getTeacher() {
        return getPerson().getTeacher();
    }

    public Department getDepartment() {
        if (isTeacher()) {
            return getTeacher().getCurrentWorkingDepartment();
        }

        return null;
    }

    @Override
    public String getWorkLocation() {
        return getRootDomainObject().getInstitutionUnit().getName();
    }

    @Override
    public String getInstitution() {
        return UnitAcronym.readUnitAcronymByAcronym("utl").getUnits().iterator().next().getName();
    }

    @Override
    public String getAddress() {
        final PhysicalAddress address = getPerson().getDefaultPhysicalAddress();
        return address != null ? writeAddress(address) : EMPTY;
    }

    private String writeAddress(final PhysicalAddress address) {
        final StringBuilder buffer = new StringBuilder();

        buffer.append(isEmpty(address.getAddress()) ? EMPTY : address.getAddress());

        if (!isEmpty(address.getAreaCode())) {
            buffer.append(", ").append(address.getAreaCode());
        }

        if (!isEmpty(address.getAreaOfAreaCode())) {
            buffer.append(", ").append(address.getAreaOfAreaCode());
        }

        return buffer.toString();
    }

    @Override
    public String getEmail() {
        return getPerson().getEmailForSendingEmails();
    }

    @Override
    public String getPhone() {
        final String phone = getPerson().getDefaultPhoneNumber();
        return !isEmpty(phone) ? phone : getPerson().getDefaultMobilePhoneNumber();
    }

    @Override
    protected boolean canBeDeleted() {
        return super.canBeDeleted() && !getIndividualProcess().isCoordinatorForPhdProgram(getPerson());
    }

    @Override
    protected void disconnect() {
        setPerson(null);
        super.disconnect();
    }

    @Override
    public boolean isFor(Person person) {
        return getPerson() == person;
    }

    @Override
    public boolean isInternal() {
        return true;
    }

    @Override
    public boolean isTeacher() {
        return getTeacher() != null;
    }

    public String getRoleOnProcess() {
        if (getIndividualProcess().isGuider(getPerson())) {
            return BundleUtil.getString(Bundle.PHD, "label.phd.guiding");
        }
        if (getIndividualProcess().isAssistantGuider(getPerson())) {
            return BundleUtil.getString(Bundle.PHD, "label.phd.assistant.guiding");
        }
        return null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
