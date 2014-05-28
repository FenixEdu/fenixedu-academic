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

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;

import com.google.common.base.Objects;

@GroupOperator("coordinator")
public class CoordinatorGroup extends FenixGroup {
    private static final long serialVersionUID = 2016819143184891118L;

    @GroupArgument
    private DegreeType degreeType;

    @GroupArgument
    private Degree degree;

    private CoordinatorGroup() {
        super();
    }

    private CoordinatorGroup(DegreeType degreeType, Degree degree) {
        this();
        this.degreeType = degreeType;
        this.degree = degree;
    }

    public static CoordinatorGroup get() {
        return new CoordinatorGroup(null, null);
    }

    public static CoordinatorGroup get(DegreeType degreeType) {
        return new CoordinatorGroup(degreeType, null);
    }

    public static CoordinatorGroup get(Degree degree) {
        return new CoordinatorGroup(null, degree);
    }

    public static CoordinatorGroup get(DegreeType degreeType, Degree degree) {
        return new CoordinatorGroup(degreeType, degree);
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        if (degreeType != null) {
            return new String[] { degreeType.getFilteredName() };
        } else if (degree != null) {
            return new String[] { degree.getPresentationName() };
        }
        return new String[0];
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();
        if (degreeType != null) {
            ExecutionYear year = ExecutionYear.readCurrentExecutionYear();
            for (final ExecutionDegree executionDegree : year.getExecutionDegreesSet()) {
                final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
                final Degree degree = degreeCurricularPlan.getDegree();
                if (degree.getDegreeType().equals(degreeType)) {
                    for (final Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
                        User user = coordinator.getPerson().getUser();
                        if (user != null) {
                            users.add(user);
                        }
                    }
                }
            }
//            for (Degree degree : Degree.readAllByDegreeType(degreeType)) {
//                users.addAll(getCoordinators(degree));
//            }
        }
        if (degree != null) {
            users.addAll(getCoordinators(degree));
        }
        if (degree == null && degreeType == null) {
            final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
            for (final ExecutionDegree executionDegree : executionYear.getExecutionDegreesSet()) {
                for (final Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
                    User user = coordinator.getPerson().getUser();
                    if (user != null) {
                        users.add(user);
                    }
                }
            }
        }
        return users;
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    private static Set<User> getCoordinators(Degree degree) {
        Set<User> users = new HashSet<>();
        for (Coordinator coordinator : degree.getCurrentCoordinators()) {
            User user = coordinator.getPerson().getUser();
            if (user != null) {
                users.add(user);
            }
        }
        return users;
    }

    @Override
    public boolean isMember(User user) {
        if (user == null || user.getPerson().getCoordinatorsSet().isEmpty()) {
            return false;
        }
        for (Coordinator coordinator : user.getPerson().getCoordinatorsSet()) {
            ExecutionDegree executionDegree = coordinator.getExecutionDegree();
            if (executionDegree.getExecutionYear().isCurrent()) {
                if (degreeType != null && degreeType != executionDegree.getDegree().getDegreeType()) {
                    continue;
                }
                if (degree != null && !executionDegree.getDegree().equals(degree)) {
                    continue;
                }
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
        return PersistentCoordinatorGroup.getInstance(degreeType, degree);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof CoordinatorGroup) {
            CoordinatorGroup other = (CoordinatorGroup) object;
            return Objects.equal(degreeType, other.degreeType) && Objects.equal(degree, other.degree);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(degreeType, degree);
    }

}
