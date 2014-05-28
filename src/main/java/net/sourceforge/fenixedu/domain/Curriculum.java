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
/*
 * Curriculum.java
 *
 * Created on 6 de Janeiro de 2003, 20:29
 */
package net.sourceforge.fenixedu.domain;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * 
 * @author EP 15 - fjgc
 * @author Jo�o Mota
 */
public class Curriculum extends Curriculum_Base {

    public Curriculum() {
        super();
        setRootDomainObject(Bennu.getInstance());
        final User userView = Authenticate.getUser();
        if (userView != null) {
            this.setPersonWhoAltered(userView.getPerson());
        }
    }

    public void edit(String generalObjectives, String operacionalObjectives, String program, String generalObjectivesEn,
            String operacionalObjectivesEn, String programEn) {

        this.setGeneralObjectives(generalObjectives);
        this.setOperacionalObjectives(operacionalObjectives);
        this.setProgram(program);
        this.setGeneralObjectivesEn(generalObjectivesEn);
        this.setOperacionalObjectivesEn(operacionalObjectivesEn);
        this.setProgramEn(programEn);

        final User userView = Authenticate.getUser();
        this.setPersonWhoAltered(userView.getPerson());
        this.setLastModificationDateDateTime(new DateTime());
    }

    public void delete() {
        setCurricularCourse(null);
        setPersonWhoAltered(null);
        setRootDomainObject(null);

        deleteDomainObject();
    }

    public MultiLanguageString getGeneralObjectivesI18N() {
        MultiLanguageString multiLanguageString = new MultiLanguageString();
        if (getGeneralObjectives() != null && getGeneralObjectives().length() > 0) {
            multiLanguageString = multiLanguageString.with(MultiLanguageString.pt, getGeneralObjectives());
        }
        if (getGeneralObjectivesEn() != null && getGeneralObjectivesEn().length() > 0) {
            multiLanguageString = multiLanguageString.with(MultiLanguageString.en, getGeneralObjectivesEn());
        }
        return multiLanguageString;
    }

    public MultiLanguageString getOperacionalObjectivesI18N() {
        MultiLanguageString multiLanguageString = new MultiLanguageString();
        if (getOperacionalObjectives() != null && getOperacionalObjectives().length() > 0) {
            multiLanguageString = multiLanguageString.with(MultiLanguageString.pt, getOperacionalObjectives());
        }
        if (getOperacionalObjectivesEn() != null && getOperacionalObjectivesEn().length() > 0) {
            multiLanguageString = multiLanguageString.with(MultiLanguageString.en, getOperacionalObjectivesEn());
        }
        return multiLanguageString;
    }

    public MultiLanguageString getProgramI18N() {
        MultiLanguageString multiLanguageString = new MultiLanguageString();
        if (getProgram() != null && getProgram().length() > 0) {
            multiLanguageString = multiLanguageString.with(MultiLanguageString.pt, getProgram());
        }
        if (getProgramEn() != null && getProgramEn().length() > 0) {
            multiLanguageString = multiLanguageString.with(MultiLanguageString.en, getProgramEn());
        }
        return multiLanguageString;
    }

    @Deprecated
    public java.util.Date getLastModificationDate() {
        org.joda.time.DateTime dt = getLastModificationDateDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setLastModificationDate(java.util.Date date) {
        if (date == null) {
            setLastModificationDateDateTime(null);
        } else {
            setLastModificationDateDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Deprecated
    public boolean hasLastModificationDateDateTime() {
        return getLastModificationDateDateTime() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasGeneralObjectives() {
        return getGeneralObjectives() != null;
    }

    @Deprecated
    public boolean hasGeneralObjectivesEn() {
        return getGeneralObjectivesEn() != null;
    }

    @Deprecated
    public boolean hasOperacionalObjectives() {
        return getOperacionalObjectives() != null;
    }

    @Deprecated
    public boolean hasOperacionalObjectivesEn() {
        return getOperacionalObjectivesEn() != null;
    }

    @Deprecated
    public boolean hasPersonWhoAltered() {
        return getPersonWhoAltered() != null;
    }

    @Deprecated
    public boolean hasProgramEn() {
        return getProgramEn() != null;
    }

    @Deprecated
    public boolean hasProgram() {
        return getProgram() != null;
    }

    @Deprecated
    public boolean hasCurricularCourse() {
        return getCurricularCourse() != null;
    }

}
