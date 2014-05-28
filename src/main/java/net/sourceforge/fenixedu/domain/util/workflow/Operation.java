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
package net.sourceforge.fenixedu.domain.util.workflow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;

public abstract class Operation implements Serializable, Comparable<Operation> {

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