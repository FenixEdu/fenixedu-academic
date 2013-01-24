package net.sourceforge.fenixedu.domain.accessControl.academicAdministration;

import java.util.Set;

import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType.Scope;

public class AcademicOperationTypeArray extends EnumArray<AcademicOperationType> {
    public AcademicOperationTypeArray(String serialized) {
	super(serialized);
    }

    public AcademicOperationTypeArray(Set<AcademicOperationType> elements) {
	super(elements);
    }

    @Override
    public AcademicOperationTypeArray with(AcademicOperationType element) {
	return (AcademicOperationTypeArray) super.with(element);
    }

    @Override
    public AcademicOperationTypeArray without(AcademicOperationType element) {
	return (AcademicOperationTypeArray) super.without(element);
    }

    @Override
    protected AcademicOperationType resolve(String serialize) {
	return AcademicOperationType.valueOf(serialize);
    }

    @Override
    protected EnumArray<AcademicOperationType> make(Set<AcademicOperationType> elements) {
	return new AcademicOperationTypeArray(elements);
    }

    public boolean hasOperationsOfScope(Scope scope) {
	for (AcademicOperationType operation : getValues()) {
	    if (operation.isOfScope(scope)) {
		return true;
	    }
	}
	return false;
    }
}