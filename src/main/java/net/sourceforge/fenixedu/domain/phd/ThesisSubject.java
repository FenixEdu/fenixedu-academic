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

import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.exceptions.PhdDomainOperationException;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ThesisSubject extends ThesisSubject_Base {

    protected ThesisSubject() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    protected ThesisSubject(PhdProgramFocusArea focusArea, MultiLanguageString name, MultiLanguageString description,
            Teacher teacher, String externalAdvisor) {
        this();

        checkParameters(focusArea, name, description, teacher);

        setPhdProgramFocusArea(focusArea);
        setName(name);
        setDescription(description);
        setTeacher(teacher);
        setExternalAdvisorName(externalAdvisor);

        for (PhdIndividualProgramProcess process : focusArea.getIndividualProgramProcesses()) {
            if (isCandidacyPeriodOpen(process)) {
                new ThesisSubjectOrder(this, process, process.getHighestThesisSubjectOrder() + 1);
            }
        }
    }

    private void checkParameters(PhdProgramFocusArea focusArea, MultiLanguageString name, MultiLanguageString description,
            Teacher teacher) {
        String[] args = {};
        if (focusArea == null) {
            throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.ThesisSubject.focusArea.required", args);
        }

        if (name == null) {
            String[] args1 = {};
            if (name == null) {
                throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.ThesisSubject.name.required", args1);
            }
        }

        if (!name.hasContent(MultiLanguageString.en)) {
            throw new PhdDomainOperationException(
                    "error.net.sourceforge.fenixedu.domain.phd.ThesisSubject.name.in.english.required");
        }

        if (teacher == null) {
            throw new PhdDomainOperationException("error.net.sourceforge.fenixedu.domain.phd.ThesisSubject.teacher.required");
        }
    }

    @Atomic
    public void edit(MultiLanguageString name, MultiLanguageString description, Teacher teacher, String externalAdvisor) {
        checkParameters(getPhdProgramFocusArea(), name, description, teacher);

        setName(name);
        setDescription(description);
        setTeacher(teacher);
        setExternalAdvisorName(externalAdvisor);
    }

    @Atomic
    public void delete() {
        for (ThesisSubjectOrder order : getThesisSubjectOrders()) {
            if (isCandidacyPeriodOpen(order.getPhdIndividualProgramProcess())) {
                order.delete();
            }
        }
        setPhdProgramFocusArea(null);

        if (!hasAnyThesisSubjectOrders()) {
            setTeacher(null);

            setRootDomainObject(null);
            deleteDomainObject();
        }
    }

    @Atomic
    public static ThesisSubject createThesisSubject(PhdProgramFocusArea focusArea, MultiLanguageString name,
            MultiLanguageString description, Teacher teacher, String externalAdvisor) {
        return new ThesisSubject(focusArea, name, description, teacher, externalAdvisor);
    }

    private boolean isCandidacyPeriodOpen(PhdIndividualProgramProcess process) {
        return process.getCandidacyProcess().getPublicPhdCandidacyPeriod() != null
                && process.getCandidacyProcess().getPublicPhdCandidacyPeriod().isOpen();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.ThesisSubjectOrder> getThesisSubjectOrders() {
        return getThesisSubjectOrdersSet();
    }

    @Deprecated
    public boolean hasAnyThesisSubjectOrders() {
        return !getThesisSubjectOrdersSet().isEmpty();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasTeacher() {
        return getTeacher() != null;
    }

    @Deprecated
    public boolean hasExternalAdvisorName() {
        return getExternalAdvisorName() != null;
    }

    @Deprecated
    public boolean hasDescription() {
        return getDescription() != null;
    }

    @Deprecated
    public boolean hasPhdProgramFocusArea() {
        return getPhdProgramFocusArea() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

}
