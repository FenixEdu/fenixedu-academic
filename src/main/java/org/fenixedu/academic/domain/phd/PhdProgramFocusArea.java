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
package org.fenixedu.academic.domain.phd;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.LocaleUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.DateTime;

public class PhdProgramFocusArea extends PhdProgramFocusArea_Base {
    static public Comparator<PhdProgramFocusArea> COMPARATOR_BY_NAME = (o1, o2) -> {
        int result = o1.getName().compareTo(o2.getName());
        return (result != 0) ? result : DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2);
    };

    private PhdProgramFocusArea() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setWhenCreated(new DateTime());
        setActive(Boolean.TRUE);
    }

    public PhdProgramFocusArea(final LocalizedString name) {
        this();
        String[] args = {};
        if (name == null) {
            throw new DomainException("error.PhdProgramFocusArea.invalid.name", args);
        }
        checkForEqualFocusArea(name);
        setName(name);
    }

    private void checkForEqualFocusArea(final LocalizedString name) {
        for (final PhdProgramFocusArea focusArea : Bennu.getInstance().getPhdProgramFocusAreasSet()) {
            if (focusArea != this && LocaleUtils.equalInAnyLanguage(focusArea.getName(), name)) {
                throw new DomainException("error.PhdProgramFocusArea.found.area.with.same.name");
            }
        }
    }

    public void delete() {
        getPhdProgramsSet().clear();
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public boolean hasPhdProgramFor(final Degree degree) {
        return getPhdProgramsSet().stream().anyMatch(program -> program.getDegree() != null && program.getDegree().equals(degree));
    }

    public List<ExternalPhdProgram> getAssociatedExternalPhdProgramsForCollaborationType(
            final PhdIndividualProgramCollaborationType type) {
        return getExternalPhdProgramsSet().stream()
                .filter(externalPhdProgram -> externalPhdProgram.isForCollaborationType(type))
                .collect(Collectors.toList());
    }

    public static PhdProgramFocusArea readPhdProgramFocusAreaByName(final String name) {
        return Bennu.getInstance().getPhdProgramFocusAreasSet().stream()
                .filter(phdProgramFocusArea -> name.equals(phdProgramFocusArea.getName().getContent()))
                .findFirst().orElse(null);
    }

    public static Set<PhdProgramFocusArea> getActivePhdProgramFocusAreas() {
        return Bennu.getInstance().getPhdProgramFocusAreasSet().stream()
                .filter(area -> area.getActive() != null)
                .filter(PhdProgramFocusArea::getActive)
                .collect(Collectors.toSet());
    }

}
