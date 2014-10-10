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

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;

import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.FluentIterable;

@GroupOperator("vigilancy")
public class VigilancyGroup extends FenixGroup {
    private static final long serialVersionUID = 2156751049164394092L;

    @GroupArgument
    private Vigilancy vigilancy;

    private VigilancyGroup() {
        super();
    }

    private VigilancyGroup(Vigilancy vigilancy) {
        this();
        this.vigilancy = vigilancy;
    }

    public static VigilancyGroup get(Vigilancy vigilancy) {
        return new VigilancyGroup(vigilancy);
    }

    @Override
    public String getPresentationName() {
        return Joiner.on('\n').join(FluentIterable.from(getMembers()).transform(new Function<User, String>() {
            @Override
            public String apply(User user) {
                return user.getPresentationName();
            }
        }));
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();
        for (Person person : vigilancy.getTeachers()) {
            User user = person.getUser();
            if (user != null) {
                users.add(user);
            }
        }
        User user = vigilancy.getVigilantWrapper().getPerson().getUser();
        if (user != null) {
            users.add(user);
        }
        return users;
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        return user != null && getMembers().contains(user);
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentVigilancyGroup.getInstance(vigilancy);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof VigilancyGroup) {
            return Objects.equal(vigilancy, ((VigilancyGroup) object).vigilancy);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(vigilancy);
    }
}
