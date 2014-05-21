package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Objects;

import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;

import org.fenixedu.bennu.core.groups.Group;

public class PersistentDelegateStudentsGroup extends PersistentDelegateStudentsGroup_Base {
    private PersistentDelegateStudentsGroup(PersonFunction delegateFunction, FunctionType type) {
        super();
        setDelegateFunction(delegateFunction);
        setType(type);
    }

    @Override
    public Group toGroup() {
        return DelegateStudentsGroup.get(getDelegateFunction(), getType());
    }

    @Override
    protected void gc() {
        setDelegateFunction(null);
        super.gc();
    }

    public static PersistentDelegateStudentsGroup getInstance(PersonFunction delegateFunction, FunctionType type) {
        return singleton(
                () -> delegateFunction.getDelegateStudentsGroupSet().stream()
                .filter(group -> Objects.equals(group.getType(), type)).findAny(),
                () -> new PersistentDelegateStudentsGroup(delegateFunction, type));
    }
}
