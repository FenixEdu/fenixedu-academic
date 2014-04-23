package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Accountability;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.fenixedu.bennu.core.annotation.CustomGroupArgument;
import org.fenixedu.bennu.core.annotation.CustomGroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.Group;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;

@CustomGroupOperator("unit")
public class PersistentUnitGroup extends PersistentUnitGroup_Base {
    protected PersistentUnitGroup(Unit unit, AccountabilityTypeEnum relationType, FunctionType relationFunctionType,
            boolean includeSubUnits) {
        super();
        setUnit(unit);
        setRelationType(relationType);
        setRelationFunctionType(relationFunctionType);
        setIncludeSubUnits(includeSubUnits);
    }

    @CustomGroupArgument(index = 1)
    public static Argument<Unit> unitArgument() {
        return new SimpleArgument<Unit, PersistentUnitGroup>() {
            @Override
            public Unit parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : FenixFramework.<Unit> getDomainObject(argument);
            }

            @Override
            public Class<? extends Unit> getType() {
                return Unit.class;
            }

            @Override
            public String extract(PersistentUnitGroup group) {
                return group.getUnit() != null ? group.getUnit().getExternalId() : "";
            }
        };
    }

    @CustomGroupArgument(index = 2)
    public static Argument<AccountabilityTypeEnum> relationTypeArgument() {
        return new SimpleArgument<AccountabilityTypeEnum, PersistentUnitGroup>() {
            @Override
            public AccountabilityTypeEnum parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : AccountabilityTypeEnum.valueOf(argument);
            }

            @Override
            public Class<? extends AccountabilityTypeEnum> getType() {
                return AccountabilityTypeEnum.class;
            }

            @Override
            public String extract(PersistentUnitGroup group) {
                return group.getRelationType() != null ? group.getRelationType().name() : "";
            }
        };
    }

    @CustomGroupArgument(index = 3)
    public static Argument<FunctionType> relationFunctionTypeArgument() {
        return new SimpleArgument<FunctionType, PersistentUnitGroup>() {
            @Override
            public FunctionType parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : FunctionType.valueOf(argument);
            }

            @Override
            public Class<? extends FunctionType> getType() {
                return FunctionType.class;
            }

            @Override
            public String extract(PersistentUnitGroup group) {
                return group.getRelationFunctionType() != null ? group.getRelationFunctionType().name() : "";
            }
        };
    }

    @CustomGroupArgument(index = 4)
    public static Argument<Boolean> includeSubUnitsArgument() {
        return new SimpleArgument<Boolean, PersistentUnitGroup>() {
            @Override
            public Boolean parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? Boolean.FALSE : Boolean.valueOf(argument);
            }

            @Override
            public Class<? extends Boolean> getType() {
                return Boolean.class;
            }

            @Override
            public String extract(PersistentUnitGroup group) {
                return group.getIncludeSubUnits().toString();
            }
        };
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        if (getRelationType() != null) {
            return new String[] { getUnit().getName(), getRelationType().getFullyQualifiedName() };
        }
        return new String[] { getUnit().getName(), getRelationFunctionType().getName() };
    }

    @Override
    public Set<User> getMembers() {
        return getMembers(new DateTime());
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        Set<User> users = new HashSet<>();
        collect(users, getUnit(), when);
        return users;
    }

    private void collect(Set<User> users, Unit unit, DateTime when) {
        Collection<? extends Accountability> accs;
        if (getRelationType() != null) {
            accs = unit.getChildAccountabilities(getRelationType());
        } else {
            accs = unit.getChildsSet();
        }
        for (Accountability accountability : accs) {
            if (accountability.isActive(when.toYearMonthDay())) {
                if (getRelationFunctionType() != null) {
                    if (accountability.getAccountabilityType() instanceof Function) {
                        Function function = (Function) accountability.getAccountabilityType();
                        if (!function.getFunctionType().equals(getRelationFunctionType())) {
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
        if (getIncludeSubUnits()) {
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
        for (Accountability accountability : user.getPerson().getParentAccountabilities(getRelationType())) {
            if (accountability.isActive(when.toYearMonthDay())) {
                if (getIncludeSubUnits() && isAncestor(getUnit(), accountability.getParentParty(), getRelationType(), when)) {
                    return true;
                } else if (accountability.getParentParty().equals(getUnit())) {
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

    public static Set<Group> groupsForUser(User user) {
        return Collections.emptySet();
    }

    public static PersistentUnitGroup getInstance(final Unit unit, final AccountabilityTypeEnum relationType,
            FunctionType relationFunctionType, final Boolean includeSubUnits) {
        PersistentUnitGroup instance = select(unit, relationType, relationFunctionType, includeSubUnits);
        return instance != null ? instance : create(unit, relationType, relationFunctionType, includeSubUnits);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentUnitGroup create(final Unit unit, final AccountabilityTypeEnum relationType,
            FunctionType relationFunctionType, final Boolean includeSubUnits) {
        PersistentUnitGroup instance = select(unit, relationType, relationFunctionType, includeSubUnits);
        return instance != null ? instance : new PersistentUnitGroup(unit, relationType, relationFunctionType, includeSubUnits);
    }

    private static PersistentUnitGroup select(final Unit unit, final AccountabilityTypeEnum relationType,
            final FunctionType relationFunctionType, final Boolean includeSubUnits) {
        return Iterables.tryFind(unit.getUnitGroupSet(), new Predicate<PersistentUnitGroup>() {
            @Override
            public boolean apply(PersistentUnitGroup group) {
                return Objects.equal(group.getRelationType(), relationType)
                        && Objects.equal(group.getRelationFunctionType(), relationFunctionType)
                        && Objects.equal(group.getIncludeSubUnits(), includeSubUnits);
            }

        }).orNull();
    }
}
