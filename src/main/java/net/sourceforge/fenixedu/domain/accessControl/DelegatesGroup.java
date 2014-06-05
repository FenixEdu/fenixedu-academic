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

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

import com.google.common.base.Objects;

@GroupOperator("delegate")
public class DelegatesGroup extends FenixGroup {
    private static final long serialVersionUID = 3999030680050502895L;

    @GroupArgument
    private Degree degree;

    @GroupArgument
    private FunctionType function;

    private DelegatesGroup() {
        super();
    }

    private DelegatesGroup(Degree degree, FunctionType function) {
        this();
        this.degree = degree;
        this.function = function;
    }

    public static DelegatesGroup get(FunctionType function) {
        return new DelegatesGroup(null, function);
    }

    public static DelegatesGroup get(Degree degree) {
        return new DelegatesGroup(degree, null);
    }

    public static DelegatesGroup get(Degree degree, FunctionType function) {
        return new DelegatesGroup(degree, function);
    }

    @Override
    public String getPresentationName() {
        if (degree != null) {
            return BundleUtil.getString(getPresentationNameBundle(), "label." + getClass().getSimpleName()) + " "
                    + BundleUtil.getString(getPresentationNameBundle(), "label.of") + " " + degree.getSigla();
        }
        return BundleUtil
                .getString(getPresentationNameBundle(), "label." + getClass().getSimpleName() + "." + function.getName());
    }

    @Override
    public String getPresentationNameBundle() {
        return Bundle.DELEGATE;
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();
        if (degree == null) {
            for (Function f : Function.readAllActiveFunctionsByType(function)) {
                for (PersonFunction personFunction : f.getActivePersonFunctions()) {
                    User user = personFunction.getPerson().getUser();
                    if (user != null) {
                        users.add(user);
                    }
                }
            }
        } else {
            for (Student student : degree.getAllActiveDelegates()) {
                User user = student.getPerson().getUser();
                if (user != null) {
                    users.add(user);
                }
            }
        }
        return users;
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        if (user == null || user.getPerson().getStudent() == null) {
            return false;
        }
        if (degree != null) {
            return user.getPerson().getStudent().getLastActiveRegistration().getDegree().equals(degree)
                    && degree.hasAnyActiveDelegateFunctionForStudent(user.getPerson().getStudent());
        }
        return user.getPerson().getStudent().hasActiveDelegateFunction(function);
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentDelegatesGroup.getInstance(degree, function);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof DelegatesGroup) {
            DelegatesGroup other = (DelegatesGroup) object;
            return Objects.equal(degree, other.degree) && Objects.equal(function, other.function);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(degree, function);
    }
}
