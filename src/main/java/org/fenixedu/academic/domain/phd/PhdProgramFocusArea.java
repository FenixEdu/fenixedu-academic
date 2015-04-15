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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.MultiLanguageString;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class PhdProgramFocusArea extends PhdProgramFocusArea_Base {
    static public Comparator<PhdProgramFocusArea> COMPARATOR_BY_NAME = new Comparator<PhdProgramFocusArea>() {
        @Override
        public int compare(PhdProgramFocusArea o1, PhdProgramFocusArea o2) {
            int result = o1.getName().compareTo(o2.getName());
            return (result != 0) ? result : DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2);
        }
    };

    private PhdProgramFocusArea() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setWhenCreated(new DateTime());
        setActive(Boolean.TRUE);
    }

    public PhdProgramFocusArea(final MultiLanguageString name) {
        this();
        String[] args = {};
        if (name == null) {
            throw new DomainException("error.PhdProgramFocusArea.invalid.name", args);
        }
        checkForEqualFocusArea(name);
        setName(name);
    }

    private void checkForEqualFocusArea(final MultiLanguageString name) {
        for (final PhdProgramFocusArea focusArea : Bennu.getInstance().getPhdProgramFocusAreasSet()) {
            if (focusArea != this && focusArea.getName().equalInAnyLanguage(name)) {
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
        for (final PhdProgram program : getPhdProgramsSet()) {
            if (program.getDegree() != null && program.getDegree().equals(degree)) {
                return true;
            }
        }
        return false;
    }

    public List<ExternalPhdProgram> getAssociatedExternalPhdProgramsForCollaborationType(
            final PhdIndividualProgramCollaborationType type) {
        List<ExternalPhdProgram> externalPhdProgramList = new ArrayList<ExternalPhdProgram>();

        CollectionUtils.select(getExternalPhdProgramsSet(), new Predicate() {

            @Override
            public boolean evaluate(Object object) {
                return ((ExternalPhdProgram) object).isForCollaborationType(type);
            }

        }, externalPhdProgramList);

        return externalPhdProgramList;
    }

    public static PhdProgramFocusArea readPhdProgramFocusAreaByName(final String name) {
        return (PhdProgramFocusArea) CollectionUtils.find(Bennu.getInstance().getPhdProgramFocusAreasSet(), new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                return name.equals(((PhdProgramFocusArea) arg0).getName().getContent());
            }

        });
    }

    public static Set<PhdProgramFocusArea> getActivePhdProgramFocusAreas() {
        final Set<PhdProgramFocusArea> result = new HashSet<PhdProgramFocusArea>();
        for (final PhdProgramFocusArea area : Bennu.getInstance().getPhdProgramFocusAreasSet()) {
            if (area.getActive() != null && area.getActive().booleanValue()) {
                result.add(area);
            }
        }
        return result;
    }

}
