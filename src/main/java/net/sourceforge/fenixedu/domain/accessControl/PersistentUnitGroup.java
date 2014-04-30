package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class PersistentUnitGroup extends PersistentUnitGroup_Base {
    protected PersistentUnitGroup(Unit unit, AccountabilityTypeEnum relationType, FunctionType relationFunctionType,
            boolean includeSubUnits) {
        super();
        setUnit(unit);
        setRelationType(relationType);
        setRelationFunctionType(relationFunctionType);
        setIncludeSubUnits(includeSubUnits);
    }

    @Override
    public Group toGroup() {
        if (getRelationType() != null) {
            return UnitGroup.get(getUnit(), getRelationType(), getIncludeSubUnits());
        } else {
            return UnitGroup.get(getUnit(), getRelationFunctionType(), getIncludeSubUnits());
        }
    }

    @Override
    protected void gc() {
        setUnit(null);
        super.gc();
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
