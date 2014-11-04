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
package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Accountability;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;

import com.google.common.base.Objects;

@GroupOperator("unit")
public class UnitGroup extends FenixGroup {
    private static final long serialVersionUID = 3393643895062911436L;

    @GroupArgument
    private Unit unit;

    @GroupArgument
    private AccountabilityTypeEnum relationType;

    @GroupArgument
    private FunctionType relationFunctionType;

    @GroupArgument
    private Boolean includeSubUnits;

    private UnitGroup() {
        super();
    }

    private UnitGroup(Unit unit, AccountabilityTypeEnum relationType, FunctionType relationFunctionType, Boolean includeSubUnits) {
        this();
        this.unit = unit;
        this.relationType = relationType;
        this.relationFunctionType = relationFunctionType;
        this.includeSubUnits = includeSubUnits;
    }

    public static UnitGroup recursiveWorkers(Unit unit) {
        return new UnitGroup(unit, AccountabilityTypeEnum.WORKING_CONTRACT, null, true);
    }

    public static UnitGroup workers(Unit unit) {
        return new UnitGroup(unit, AccountabilityTypeEnum.WORKING_CONTRACT, null, false);
    }

    public static UnitGroup get(Unit unit, AccountabilityTypeEnum relationType, Boolean includeSubUnits) {
        return new UnitGroup(unit, relationType, null, includeSubUnits);
    }

    public static UnitGroup get(Unit unit, FunctionType relationFunctionType, Boolean includeSubUnits) {
        return new UnitGroup(unit, null, relationFunctionType, includeSubUnits);
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        if (relationType != null) {
            return new String[] { unit.getNameI18n().getContent(), relationType.getLocalizedName() };
        }
        return new String[] { unit.getNameI18n().getContent(), relationFunctionType.getName() };
    }

    @Override
    public Set<User> getMembers() {
        return getMembers(new DateTime());
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        Set<User> users = new HashSet<>();
        collect(users, unit, when);
        return users;
    }

    private void collect(Set<User> users, Unit unit, DateTime when) {
        Collection<? extends Accountability> accs;
        if (relationType != null) {
            accs = unit.getChildAccountabilities(relationType);
        } else {
            accs = unit.getChildsSet();
        }
        for (Accountability accountability : accs) {
            if (accountability.isActive(when.toYearMonthDay())) {
                if (relationFunctionType != null) {
                    if (accountability.getAccountabilityType() instanceof Function) {
                        Function function = (Function) accountability.getAccountabilityType();
                        if (!function.getFunctionType().equals(relationFunctionType)) {
                            continue;
                        }
                    } else {
                        continue;
                    }
                }
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
        for (Accountability accountability : user.getPerson().getParentAccountabilities(relationType)) {
            if (accountability.isActive(when.toYearMonthDay())) {
                if (includeSubUnits && isAncestor(unit, accountability.getParentParty(), relationType, when)) {
                    return true;
                } else if (accountability.getParentParty().equals(unit)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isAncestor(Party possibleAncestor, Party possibleChild, AccountabilityTypeEnum subUnitRecursionType,
            DateTime when) {
        if (possibleChild == null) {
            return false;
        }
        if (possibleChild.equals(possibleAncestor)) {
            return true;
        }
        for (Party parent : possibleChild.getParentPartiesByDates(subUnitRecursionType, Unit.class, when)) {
            if (isAncestor(possibleAncestor, parent, subUnitRecursionType, when)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentUnitGroup.getInstance(unit, relationType, relationFunctionType, includeSubUnits);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof UnitGroup) {
            UnitGroup other = (UnitGroup) object;
            return Objects.equal(unit, other.unit) && Objects.equal(relationType, other.relationType)
                    && Objects.equal(relationFunctionType, other.relationFunctionType)
                    && Objects.equal(includeSubUnits, other.includeSubUnits);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(unit, relationType, relationFunctionType, includeSubUnits);
    }

}
