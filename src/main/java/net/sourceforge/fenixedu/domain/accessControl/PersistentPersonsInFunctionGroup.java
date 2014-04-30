package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.organizationalStructure.Function;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

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
        PersistentPersonsInFunctionGroup instance = function.getPersonsInFunctionGroup();
        return instance != null ? instance : create(function);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentPersonsInFunctionGroup create(Function function) {
        PersistentPersonsInFunctionGroup instance = function.getPersonsInFunctionGroup();
        return instance != null ? instance : new PersistentPersonsInFunctionGroup(function);
    }

}
