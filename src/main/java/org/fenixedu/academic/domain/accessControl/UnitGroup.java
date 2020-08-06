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
package org.fenixedu.academic.domain.accessControl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.organizationalStructure.Accountability;
import org.fenixedu.academic.domain.organizationalStructure.AccountabilityType;
import org.fenixedu.academic.domain.organizationalStructure.AccountabilityTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.google.common.base.Objects;

@GroupOperator("unit")
public class UnitGroup extends FenixGroup {
    private static final long serialVersionUID = 3393643895062911436L;

    @GroupArgument
    private Unit unit;

    @Deprecated
    @GroupArgument
    private AccountabilityTypeEnum relationType;

    @GroupArgument
    private AccountabilityType accountabilityType;

    @GroupArgument
    private Boolean includeSubUnits;

    private UnitGroup() {
        super();
    }

    private UnitGroup(Unit unit, AccountabilityTypeEnum relationType, AccountabilityType accountabilityType,
            Boolean includeSubUnits) {
        this();
        this.unit = unit;
        this.relationType = relationType;
        this.accountabilityType = accountabilityType;
        this.includeSubUnits = includeSubUnits;
    }

    @Deprecated
    public static UnitGroup recursiveWorkers(Unit unit) {
        return new UnitGroup(unit, AccountabilityTypeEnum.WORKING_CONTRACT, null, true);
    }

    @Deprecated
    public static UnitGroup workers(Unit unit) {
        return new UnitGroup(unit, AccountabilityTypeEnum.WORKING_CONTRACT, null, false);
    }

    public static UnitGroup get(Unit unit, AccountabilityType accountabilityType, Boolean includeSubUnits) {
        return new UnitGroup(unit, null, accountabilityType, includeSubUnits);
    }

    public Unit getUnit() {
        return unit;
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        ArrayList<String> args = new ArrayList<String>();
        args.add(unit.getNameI18n().getContent());
        String type = "";
        if (accountabilityType != null) {
            type = BundleUtil.getString(Bundle.GROUP, "label.name.unit.connector.relation") + accountabilityType.getName();
        } else if (relationType != null) {
            type = BundleUtil.getString(Bundle.GROUP, "label.name.unit.connector.relation") + relationType.getLocalizedName();
        }
        args.add(type);
        String subunits = "";
        if (includeSubUnits) {
            subunits = BundleUtil.getString(Bundle.GROUP, "label.name.unit.subunits");
        }
        args.add(subunits);
        return args.toArray(new String[3]);
    }

    @Override
    public Stream<User> getMembers() {
        return getMembers(new DateTime());
    }

    @Override
    public Stream<User> getMembers(DateTime when) {
        Set<User> users = new HashSet<>();
        collect(users, unit, when);
        return users.stream();
    }

    private void collect(Set<User> users, Unit unit, DateTime when) {
        Collection<? extends Accountability> accs;
        if (accountabilityType != null) {
            accs = unit.getChildsSet().stream().filter(a -> a.getAccountabilityType() == accountabilityType)
                    .collect(Collectors.toSet());
        } else if (relationType != null) {
            accs = unit.getChildAccountabilities(relationType);
        } else {
            accs = unit.getChildsSet();
        }
        for (Accountability accountability : accs) {
            if (accountability.isActive(when.toYearMonthDay())) {
                Party party = accountability.getChildParty();
                if (party instanceof Person) {
                    User user = ((Person) party).getUser();
                    if (user != null) {
                        users.add(user);
                    }
                }
            }
        }
        if (includeSubUnits) {
            for (Unit subUnit : unit.getSubUnits()) {
                collect(users, subUnit, when);
            }
        }
    }

    @Override
    public boolean isMember(User user) {
        return isMember(user, new DateTime());
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        if (user == null) {
            return false;
        }
        YearMonthDay whenYMD = when.toYearMonthDay();
        for (Accountability accountability : user.getPerson().getParentsSet()) {

            if ((relationType != null && accountability.getAccountabilityType().getType() == relationType)
                    || (accountabilityType != null && accountability.getAccountabilityType() == accountabilityType)) {

                if (accountability.isActive(whenYMD)) {
                    if (accountability.getParentParty().equals(unit)) {
                        return true;
                    } else if (includeSubUnits && isAncestor(unit, accountability.getParentParty(), whenYMD)) {
                        return true;
                    }
                }

            }

        }
        return false;
    }

    private boolean isAncestor(Party possibleAncestor, Party possibleChild, YearMonthDay when) {
        if (possibleChild == null) {
            return false;
        }
        if (possibleChild.equals(possibleAncestor)) {
            return true;
        }
        for (Accountability acc : possibleChild.getParentsSet()) {
            if (acc.getParentParty() instanceof Unit && acc.isActive(when)
                    && isAncestor(possibleAncestor, acc.getParentParty(), when)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        final AccountabilityType type = relationType != null ? AccountabilityType.readByType(relationType) : accountabilityType;
        return PersistentUnitGroup.getInstance(unit, type, includeSubUnits);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof UnitGroup) {
            UnitGroup other = (UnitGroup) object;
            return Objects.equal(unit, other.unit) && Objects.equal(relationType, other.relationType)
                    && Objects.equal(accountabilityType, other.accountabilityType)
                    && Objects.equal(includeSubUnits, other.includeSubUnits);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(unit, relationType, includeSubUnits);
    }
}
