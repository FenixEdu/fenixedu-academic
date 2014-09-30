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
package net.sourceforge.fenixedu.domain.student;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.DomainObjectUtil;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class RegistrationProtocol extends RegistrationProtocol_Base implements Comparable<RegistrationProtocol> {

    static {
        Person.getRelationRegistrationProtocolPerson().addListener(new RelationAdapter<Person, RegistrationProtocol>() {

            @Override
            public void afterAdd(Person o1, RegistrationProtocol o2) {
                if (o1 != null && o2 != null) {
                    if (!o1.hasRole(RoleType.EXTERNAL_SUPERVISOR)) {
                        o1.addPersonRoleByRoleType(RoleType.EXTERNAL_SUPERVISOR);
                    }
                }
            }

            @Override
            public void afterRemove(Person o1, RegistrationProtocol o2) {
                if (o1 != null && o2 != null) {
                    if (o1.getRegistrationProtocolsSet().size() == 0) {
                        o1.removeRoleByType(RoleType.EXTERNAL_SUPERVISOR);
                    }
                }
                super.afterRemove(o1, o2);
            }
        });
    }

    static final public Comparator<RegistrationProtocol> AGREEMENT_COMPARATOR = new Comparator<RegistrationProtocol>() {
        @Override
        public int compare(RegistrationProtocol o1, RegistrationProtocol o2) {
            return o1.compareTo(o2);
        }
    };

    @Override
    public int compareTo(final RegistrationProtocol rp) {
        final int c = getCode().compareTo(rp.getCode());
        return c == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(this, rp) : c;
    }

    public RegistrationProtocol(String code, LocalizedString description, Boolean enrolmentByStudentAllowed, Boolean payGratuity,
            Boolean allowsIDCard, Boolean onlyAllowedDegreeEnrolment, Boolean isAlien, Boolean exempted, Boolean mobility,
            Boolean military, Boolean allowDissertationCandidacyWithoutChecks, Boolean forOfficialMobilityReporting,
            Boolean attemptAlmaMatterFromPrecedent) {
        setRootDomainObject(Bennu.getInstance());
        setCode(code);
        setDescription(description);
        setEnrolmentByStudentAllowed(enrolmentByStudentAllowed);
        setPayGratuity(payGratuity);
        setAllowsIDCard(allowsIDCard);
        setOnlyAllowedDegreeEnrolment(onlyAllowedDegreeEnrolment);
        setAlien(isAlien);
        setExempted(exempted);
        setMobility(mobility);
        setMilitary(military);
        setAllowDissertationCandidacyWithoutChecks(allowDissertationCandidacyWithoutChecks);
        setForOfficialMobilityReporting(forOfficialMobilityReporting);
        setAttemptAlmaMatterFromPrecedent(attemptAlmaMatterFromPrecedent);
    }

    @Atomic
    public void addSupervisor(Person supervisor) {
        this.addSupervisors(supervisor);
    }

    @Atomic
    public void removeSupervisor(Person supervisor) {
        this.removeSupervisors(supervisor);
    }

    public static RegistrationProtocol getDefault() {
        for (final RegistrationProtocol protocol : Bennu.getInstance().getRegistrationProtocolsSet()) {
            if (protocol.isEnrolmentByStudentAllowed() && !protocol.isAlien()) {
                return protocol;
            }
        }
        return null;
    }

    public boolean isAlien() {
        return getAlien() != null && getAlien().booleanValue();
    }

    public boolean isEnrolmentByStudentAllowed() {
        return getEnrolmentByStudentAllowed() != null && getEnrolmentByStudentAllowed().booleanValue();
    }

    public boolean isMilitaryAgreement() {
        return getMilitary() != null && getMilitary().booleanValue();
    }

    public boolean isToPayGratuity() {
        return getPayGratuity() != null && getPayGratuity().booleanValue();
    }

    public boolean allowDissertationCandidacyWithoutChecks() {
        return getAllowDissertationCandidacyWithoutChecks() != null
                && getAllowDissertationCandidacyWithoutChecks().booleanValue();
    }

    public boolean isMobilityAgreement() {
        return getMobility() != null && getMobility().booleanValue();
    }

    public boolean isOnlyAllowedDegreeEnrolment() {
        return getOnlyAllowedDegreeEnrolment() != null && getOnlyAllowedDegreeEnrolment().booleanValue();
    }

    public boolean isExempted() {
        return getExempted() != null && getExempted().booleanValue();
    }

    public boolean isForOfficialMobilityReporting() {
        return getForOfficialMobilityReporting() != null && getForOfficialMobilityReporting().booleanValue();
    }

    public boolean allowsIDCard() {
        return getAllowsIDCard() != null && getAllowsIDCard().booleanValue();
    }

    public boolean attemptAlmaMatterFromPrecedent() {
        return getAttemptAlmaMatterFromPrecedent() != null && getAttemptAlmaMatterFromPrecedent().booleanValue();
    }

}
