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
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class RegistrationProtocol extends RegistrationProtocol_Base {

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
            return o1.getRegistrationAgreement().compareTo(o2.getRegistrationAgreement());
        }
    };

    public RegistrationProtocol() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public RegistrationProtocol(RegistrationAgreement registrationAgreement) {
        this();
        setRegistrationAgreement(registrationAgreement);
    }

    @Atomic
    public static RegistrationProtocol serveRegistrationProtocol(RegistrationAgreement registrationAgreement) {
        Set<RegistrationProtocol> dataset = Bennu.getInstance().getRegistrationProtocolsSet();
        for (RegistrationProtocol iter : dataset) {
            if (iter.getRegistrationAgreement().name().equals(registrationAgreement.name())) {
                return iter;
            }
        }
        RegistrationProtocol newProtocol = new RegistrationProtocol(registrationAgreement);
        return newProtocol;
    }

    @Atomic
    public void addSupervisor(Person supervisor) {
        this.addSupervisors(supervisor);
    }

    @Atomic
    public void removeSupervisor(Person supervisor) {
        this.removeSupervisors(supervisor);
    }

    public boolean equals(RegistrationProtocol myOtherSelf) {
        return (this == myOtherSelf);
    }

    public boolean equals(RegistrationAgreement myOtherSoul) {
        return this.getRegistrationAgreement().name().equals(myOtherSoul.name());
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Person> getSupervisors() {
        return getSupervisorsSet();
    }

    @Deprecated
    public boolean hasAnySupervisors() {
        return !getSupervisorsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.Registration> getRegistrations() {
        return getRegistrationsSet();
    }

    @Deprecated
    public boolean hasAnyRegistrations() {
        return !getRegistrationsSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasRegistrationAgreement() {
        return getRegistrationAgreement() != null;
    }

}
