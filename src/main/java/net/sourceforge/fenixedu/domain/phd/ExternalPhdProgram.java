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
package net.sourceforge.fenixedu.domain.phd;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ExternalPhdProgram extends ExternalPhdProgram_Base {

    public ExternalPhdProgram() {
        super();

        setRootDomainObject(Bennu.getInstance());
        setWhenCreated(new DateTime());
    }

    private ExternalPhdProgram(String name, String nameEn, String acronym,
            PhdIndividualProgramCollaborationType forCollaborationType) {
        this();

        check(name, nameEn, acronym, forCollaborationType);

        MultiLanguageString nameI18N = new MultiLanguageString(MultiLanguageString.pt, name).with(MultiLanguageString.en, nameEn);

        setName(nameI18N);

        setAcronym(acronym);
        setForCollaborationType(forCollaborationType);
    }

    private void check(String name, String nameEn, String acronym, PhdIndividualProgramCollaborationType forCollaborationType) {
        if (StringUtils.isEmpty(name)) {
            throw new DomainException("error.external.phd.program.name.is.empty");
        }

        if (StringUtils.isEmpty(nameEn)) {
            throw new DomainException("error.external.phd.program.name.en.is.empty");
        }

        if (StringUtils.isEmpty(acronym)) {
            throw new DomainException("error.external.phd.program.acronym.is.empty");
        }

        if (forCollaborationType == null) {
            throw new DomainException("error.external.phd.program.for.collaboration.type.is.null");
        }
    }

    public boolean isForCollaborationType(PhdIndividualProgramCollaborationType type) {
        return getForCollaborationType() == type;
    }

    public static ExternalPhdProgram createExternalPhdProgram(String name, String nameEn, String acronym,
            PhdIndividualProgramCollaborationType forCollaborationType) {
        if (readExternalPhdProgramByName(name) != null) {
            throw new DomainException("error.external.phd.program.with.same.name.exists");
        }

        if (readExternalPhdProgramByAcronym(acronym) != null) {
            throw new DomainException("error.external.phd.program.with.same.acronym.exists");
        }

        return new ExternalPhdProgram(name, nameEn, acronym, forCollaborationType);
    }

    public static List<ExternalPhdProgram> readExternalPhdProgramsForCollaborationType(
            final PhdIndividualProgramCollaborationType type) {
        List<ExternalPhdProgram> phdProgramList = new ArrayList<ExternalPhdProgram>();

        CollectionUtils.select(Bennu.getInstance().getExternalPhdProgramsSet(), new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                return ((ExternalPhdProgram) arg0).isForCollaborationType(type);
            }

        }, phdProgramList);

        return phdProgramList;
    }

    public static ExternalPhdProgram readExternalPhdProgramByName(final String name) {
        return (ExternalPhdProgram) CollectionUtils.find(Bennu.getInstance().getExternalPhdProgramsSet(), new Predicate() {

            @Override
            public boolean evaluate(Object object) {
                return name.equals(((ExternalPhdProgram) object).getName().getContent());
            }

        });
    }

    public static ExternalPhdProgram readExternalPhdProgramByAcronym(final String acronym) {
        return (ExternalPhdProgram) CollectionUtils.find(Bennu.getInstance().getExternalPhdProgramsSet(), new Predicate() {

            @Override
            public boolean evaluate(Object object) {
                return acronym.equals(((ExternalPhdProgram) object).getAcronym());
            }

        });
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess> getIndividualProgramProcesses() {
        return getIndividualProgramProcessesSet();
    }

    @Deprecated
    public boolean hasAnyIndividualProgramProcesses() {
        return !getIndividualProgramProcessesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.PhdProgramFocusArea> getPhdProgramFocusAreas() {
        return getPhdProgramFocusAreasSet();
    }

    @Deprecated
    public boolean hasAnyPhdProgramFocusAreas() {
        return !getPhdProgramFocusAreasSet().isEmpty();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasWhenCreated() {
        return getWhenCreated() != null;
    }

    @Deprecated
    public boolean hasForCollaborationType() {
        return getForCollaborationType() != null;
    }

    @Deprecated
    public boolean hasAcronym() {
        return getAcronym() != null;
    }

}
