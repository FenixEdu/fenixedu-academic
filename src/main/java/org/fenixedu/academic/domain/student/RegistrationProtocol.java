/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.student;

import java.util.Comparator;

import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.fenixframework.Atomic;

public class RegistrationProtocol extends RegistrationProtocol_Base implements Comparable<RegistrationProtocol> {

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
