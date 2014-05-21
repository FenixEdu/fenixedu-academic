package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Objects;

import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.fenixedu.bennu.core.groups.Group;

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
        final FunctionType relationFunctionType1 = relationFunctionType;
        return singleton(
                () -> unit
                        .getUnitGroupSet()
                        .stream()
                        .filter(group -> Objects.equals(group.getRelationType(), relationType)
                                && Objects.equals(group.getRelationFunctionType(), relationFunctionType1)
                                && Objects.equals(group.getIncludeSubUnits(), includeSubUnits)).findAny(),
                () -> new PersistentUnitGroup(unit, relationType, relationFunctionType, includeSubUnits));
    }
}
