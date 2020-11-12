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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.Coordinator;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;

@GroupOperator("coordinator")
public class CoordinatorGroup extends FenixGroup {
    private static final long serialVersionUID = 2016819143184891118L;

    @GroupArgument
    private DegreeType degreeType;

    @GroupArgument
    private Degree degree;

    @GroupArgument
    private Boolean responsible;

    private CoordinatorGroup() {
        super();
    }

    private CoordinatorGroup(DegreeType degreeType, Degree degree, Boolean isResponsible) {
        this();
        this.degreeType = degreeType;
        this.degree = degree;
        this.responsible = isResponsible;
    }

    public static CoordinatorGroup get() {
        return new CoordinatorGroup(null, null, null);
    }

    public static CoordinatorGroup get(DegreeType degreeType) {
        return new CoordinatorGroup(degreeType, null, null);
    }

    public static CoordinatorGroup get(Degree degree) {
        return new CoordinatorGroup(null, degree, null);
    }

    public static CoordinatorGroup get(DegreeType degreeType, Degree degree) {
        return new CoordinatorGroup(degreeType, degree, null);
    }

    public static CoordinatorGroup get(DegreeType degreeType, Degree degree, Boolean isResponsible) {
        return new CoordinatorGroup(degreeType, degree, isResponsible);
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        List<String> parts = new ArrayList<>();
        String connector = "";
        if (degreeType != null) {
            parts.add(degreeType.getName().getContent());
        }
        if (degree != null) {
            parts.add(degree.getPresentationName());
        }
        if (responsible != null) {
            parts.add(responsible.toString());
        }
        if (!parts.isEmpty()) {
            connector = BundleUtil.getString(Bundle.GROUP, "label.name.connector.default");
        }
        return new String[] { connector, Joiner.on(", ").join(parts) };
    }

    @Override
    public Stream<User> getMembers() {
        Set<User> users = new HashSet<>();
        if (degreeType != null) {
            for (ExecutionYear year : ExecutionYear.findCurrents()) {
                for (final ExecutionDegree executionDegree : year.getExecutionDegreesSet()) {
                    if (degreeType.equals(executionDegree.getDegreeType())) {
                        for (final Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
                            if (responsible == null || responsible.equals(coordinator.isResponsible())) {
                                User user = coordinator.getPerson().getUser();
                                if (user != null) {
                                    users.add(user);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (degree != null) {
            users.addAll(getCoordinators(degree, responsible));
        }

        if (degree == null && degreeType == null) {
            for (ExecutionYear executionYear : ExecutionYear.findCurrents()) {
                for (final ExecutionDegree executionDegree : executionYear.getExecutionDegreesSet()) {
                    for (final Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
                        if (responsible == null || responsible.equals(coordinator.isResponsible())) {
                            User user = coordinator.getPerson().getUser();
                            if (user != null) {
                                users.add(user);
                            }
                        }
                    }
                }
            }
        }
        return users.stream();
    }

    @Override
    public Stream<User> getMembers(DateTime when) {
        return getMembers();
    }

    private static Set<User> getCoordinators(Degree degree, Boolean isResponsible) {
        Set<User> users = new HashSet<>();
        Coordinator.findLastCoordinators(degree, false).forEach(coordinator -> {
            if (isResponsible == null || isResponsible.equals(coordinator.isResponsible())) {
                User user = coordinator.getPerson().getUser();
                if (user != null) {
                    users.add(user);
                }
            }
        });
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
                if (responsible != null && !responsible.equals(coordinator.isResponsible())) {
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
        return PersistentCoordinatorGroup.getInstance(degreeType, degree, responsible);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof CoordinatorGroup) {
            CoordinatorGroup other = (CoordinatorGroup) object;
            return Objects.equal(degreeType, other.degreeType) && Objects.equal(degree, other.degree)
                    && Objects.equal(responsible, other.responsible);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(degreeType, degree, responsible);
    }

}
