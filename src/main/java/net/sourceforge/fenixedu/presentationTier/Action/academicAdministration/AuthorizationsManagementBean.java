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
package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PersistentAccessGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.PersistentAcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Splitter;

public class AuthorizationsManagementBean implements Serializable {
    private static final long serialVersionUID = 604369029723208403L;

    private Party party;

    private List<AuthorizationGroupBean> groups;

    public AuthorizationsManagementBean() {
        super();
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
        this.groups = new ArrayList<AuthorizationGroupBean>();
        if (party != null) {
            for (PersistentAccessGroup group : party.getPersistentAccessGroup()) {
                if (group instanceof PersistentAcademicAuthorizationGroup && !group.hasDeletedBennu()) {
                    AuthorizationGroupBean bean = new AuthorizationGroupBean((PersistentAcademicAuthorizationGroup) group);
                    groups.add(bean);
                }
            }
            Collections.sort(this.groups, AuthorizationGroupBean.COMPARATOR_BY_LOCALIZED_NAME);
        }
    }

    public boolean getHasNewObject() {
        for (AuthorizationGroupBean bean : groups) {
            if (bean.getNewObject()) {
                return true;
            }
        }
        return false;
    }

    public List<Degree> getDegrees() {

        List<Degree> degrees = new ArrayList<Degree>(Bennu.getInstance().getDegreesSet());

        Collections.sort(degrees);

        return degrees;
    }

    public List<PhdProgram> getPhdPrograms() {

        List<PhdProgram> programs = new ArrayList<PhdProgram>(Bennu.getInstance().getPhdProgramsSet());

        Collections.sort(programs, PhdProgram.COMPARATOR_BY_NAME);

        return programs;

    }

    public List<AuthorizationGroupBean> getGroups() {
        return this.groups;
    }

    public Set<DegreeType> getDegreeTypes() {
        return DegreeType.NOT_EMPTY_VALUES;
    }

    public Set<AdministrativeOffice> getAdministrativeOffices() {
        return Bennu.getInstance().getAdministrativeOfficesSet();
    }

    public void removeAuthorization(String parameter) {
        AuthorizationGroupBean bean = getBeanByOid(parameter);
        if (bean != null) {
            if (bean.getGroup() != null) {
                bean.delete(party);
            }
            getGroups().remove(bean);
        }
    }

    public void editAuthorization(String parameter) {
        AuthorizationGroupBean bean = getBeanByOid(parameter);
        if (bean != null) {
            bean.edit();
        }
    }

    public void addNewAuthorization() {
        groups.add(0, new AuthorizationGroupBean());
    }

    private AuthorizationGroupBean getBeanByOid(String parameter) {
        for (AuthorizationGroupBean bean : getGroups()) {
            if (bean.getId().equals(parameter)) {
                return bean;
            }
        }
        return null;
    }

    public void createAuthorization(String courses, String officesStr) {
        AuthorizationGroupBean bean = getBeanByOid("-1");
        if (bean != null) {

            Set<AcademicProgram> programs = new HashSet<AcademicProgram>();
            Set<AdministrativeOffice> offices = new HashSet<AdministrativeOffice>();

            extractPrograms(courses, programs);
            extractOffices(officesStr, offices);

            bean.create(party, programs, offices);
        }
    }

    private static final Splitter SPLITTER = Splitter.on(';');

    public void editAuthorizationPrograms(String oid, String courses, String officesStr) {
        AuthorizationGroupBean bean = getBeanByOid(oid);
        if (bean != null) {

            Set<AcademicProgram> programs = new HashSet<AcademicProgram>();
            Set<AdministrativeOffice> offices = new HashSet<AdministrativeOffice>();

            extractPrograms(courses, programs);
            extractOffices(officesStr, offices);

            bean.editAuthorizationPrograms(programs, offices);

        }
    }

    private void extractPrograms(String courses, Set<AcademicProgram> programs) {
        if (!courses.trim().isEmpty()) {
            for (String course : SPLITTER.split(courses)) {
                AcademicProgram program = FenixFramework.getDomainObject(course);
                programs.add(program);
            }
        }
    }

    private void extractOffices(String officesStr, Set<AdministrativeOffice> offices) {
        if (!officesStr.trim().isEmpty()) {
            for (String officeStr : SPLITTER.split(officesStr)) {
                AdministrativeOffice office = FenixFramework.getDomainObject(officeStr);
                offices.add(office);
            }
        }
    }

    public Collection<Party> getPeopleInUnit() {
        if (!party.isUnit()) {
            return Collections.emptySet();
        }

        Set<Party> people = new TreeSet<Party>(Party.COMPARATOR_BY_NAME);

        LinkedList<Party> units = new LinkedList<Party>();
        units.add(party);

        while (!units.isEmpty()) {
            Party unit = units.removeFirst();

            people.addAll(unit.getActiveChildParties(AccountabilityTypeEnum.WORKING_CONTRACT, Person.class));

            units.addAll(unit.getActiveChildParties(AccountabilityTypeEnum.ORGANIZATIONAL_STRUCTURE, Unit.class));
        }

        return people;
    }
}
