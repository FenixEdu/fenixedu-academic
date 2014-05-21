package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Optional;

import net.sourceforge.fenixedu.domain.organizationalStructure.Function;

import org.fenixedu.bennu.core.groups.Group;

public class PersistentPersonsInFunctionGroup extends PersistentPersonsInFunctionGroup_Base {
    protected PersistentPersonsInFunctionGroup(Function function) {
        super();
        setFunction(function);
    }

    @Override
    public Group toGroup() {
        return PersonsInFunctionGroup.get(getFunction());
    }

    @Override
    protected void gc() {
        setFunction(null);
        super.gc();
    }

    public static PersistentPersonsInFunctionGroup getInstance(Function function) {
        return singleton(() -> Optional.ofNullable(function.getPersonsInFunctionGroup()),
                () -> new PersistentPersonsInFunctionGroup(function));
    }
}
