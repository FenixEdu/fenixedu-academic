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
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

import com.google.common.base.Objects;

@GroupOperator("alumni")
public class AlumniGroup extends FenixGroup {
    private static final long serialVersionUID = 5431112068108722868L;

    @GroupArgument("")
    private Degree degree;

    private AlumniGroup() {
        super();
    }

    private AlumniGroup(Degree degree) {
        this();
        this.degree = degree;
    }

    public static AlumniGroup get() {
        return new AlumniGroup();
    }

    public static AlumniGroup get(Degree degree) {
        return new AlumniGroup(degree);
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { degree == null ? "" : BundleUtil.getString(Bundle.GROUP, "label.name.connector.default")
                + degree.getPresentationName() };
    }

    @Override
    public Stream<User> getMembers() {
        return getMembers(DateTime.now());
    }

    @Override
    public Stream<User> getMembers(DateTime when) {
        return Bennu.getInstance().getAlumnisSet().stream().map(alumni -> alumni.getStudent().getPerson().getUser())
                .filter(u -> u != null).filter(u -> degree == null || isMember(u, when));
    }

    @Override
    public boolean isMember(User user) {
        return isMember(user, DateTime.now());
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        if (user == null || user.getPerson().getStudent() == null || user.getPerson().getStudent().getAlumni() == null) {
            return false;
        }
        if (degree != null) {
            for (Registration registration : user.getPerson().getStudent().getRegistrationsFor(degree)) {
                if (registration.isRegistrationConclusionProcessed()) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentAlumniGroup.getInstance(degree);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof AlumniGroup) {
            return Objects.equal(degree, ((AlumniGroup) object).degree);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(degree);
    }
}
