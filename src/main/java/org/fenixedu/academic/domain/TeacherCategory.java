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
package org.fenixedu.academic.domain;

import java.util.Optional;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;

import com.google.common.base.Strings;

public class TeacherCategory extends TeacherCategory_Base implements Comparable<TeacherCategory> {

    protected TeacherCategory() {
        super();
        setRoot(Bennu.getInstance());
    }

    public TeacherCategory(String code, LocalizedString name, Integer weight) {
        this();
        setCode(code);
        setName(name);
        setWeight(weight);
    }

    public static Optional<TeacherCategory> findByCode(String code) {
        if (Strings.isNullOrEmpty(code)) {
            return Optional.empty();
        }
        return Bennu.getInstance().getTeacherCategorySet().stream().filter(c -> code.equals(c.getCode())).findAny();
    }

    @Override
    public int compareTo(TeacherCategory o) {
        int weigth = getWeight().compareTo(o.getWeight());
        if (weigth != 0) {
            return -weigth;
        }
        int byName = getName().compareTo(o.getName());
        if (byName != 0) {
            return byName;
        }
        return getExternalId().compareTo(o.getExternalId());
    }

    @Override
    public void setCode(String code) {
        if (Strings.isNullOrEmpty(code)) {
            throw new DomainException("teacher.category.code.empty", code);
        }

        Optional<TeacherCategory> category = findByCode(code);
        if (category.isPresent() && !this.equals(category.get())) {
            throw new DomainException("teacher.category.code.already.exists", code);
        }
        super.setCode(code);
    }
}
