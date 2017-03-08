/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.accessControl;

import java.util.stream.Stream;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ScientificCommission;
import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;

import com.google.common.base.Objects;

@GroupOperator("scientificCommission")
public class ScientificCommissionGroup extends FenixGroup {
    private static final long serialVersionUID = 8999642876831933207L;

    @GroupArgument
    private Degree degree;

    private ScientificCommissionGroup() {
        super();
    }

    private ScientificCommissionGroup(Degree degree) {
        this();
        this.degree = degree;
    }

    public static ScientificCommissionGroup get(Degree degree) {
        return new ScientificCommissionGroup(degree);
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { degree.getPresentationName() };
    }

    @Override
    public Stream<User> getMembers() {
        return degree.getCurrentScientificCommissionMembers().stream().map(member -> member.getPerson().getUser())
                .filter(user -> user != null);
    }

    @Override
    public Stream<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        if (user == null) {
            return false;
        }
        for (ScientificCommission member : user.getPerson().getScientificCommissionsSet()) {
            if (member.getExecutionDegree().getDegree().equals(degree)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentScientificCommissionGroup.getInstance(degree);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ScientificCommissionGroup) {
            return Objects.equal(degree, ((ScientificCommissionGroup) object).degree);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(degree);
    }
}
