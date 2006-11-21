package net.sourceforge.fenixedu.domain.util.workflow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;

public abstract class Operation implements Serializable,Comparable<Operation> {

    private Set<RoleType> authorizedRoleTypes;

    private List<Form> forms = new ArrayList<Form>();

    private int currentFormIndex = -1;

    protected Operation(Set<RoleType> roleTypes) {
	this.authorizedRoleTypes = roleTypes;
    }

    public Set<RoleType> getAuthorizedRoleTypes() {
	return Collections.unmodifiableSet(authorizedRoleTypes);
    }

    public List<Form> getForms() {
	return Collections.unmodifiableList(forms);
    }

    protected final void addForm(Form form) {
	this.forms.add(form);
    }

    public boolean isAuthorized(Person person) {
	for (final RoleType roleType : getAuthorizedRoleTypes()) {
	    if (person.hasRole(roleType)) {
		return true;
	    }
	}
	return false;
    }

    public final int getCurrentFormPosition() {
	return this.currentFormIndex + 1;
    }

    public final boolean hasMoreForms() {
	return (this.currentFormIndex + 1 <= this.forms.size() - 1);
    }

    public final Form moveToNextForm() {
	return this.forms.get(++this.currentFormIndex);
    }

    public final void execute(Person person) {
	if (!isAuthorized(person)) {
	    throw new WorkflowException("error.workflow.operaton.cannot.be.executed.by.person");
	}
	internalExecute();
	getState().onOperationFinished(this, person);
    }

    public int getTotalForms() {
	return this.forms.size();
    }

    public boolean isVisible() {
	return true;
    }

    protected abstract void internalExecute();

    public abstract boolean isInput();

    public abstract IStateWithOperations getState();

}