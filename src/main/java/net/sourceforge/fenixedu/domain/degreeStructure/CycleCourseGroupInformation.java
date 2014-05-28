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
package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CycleCourseGroupInformation extends CycleCourseGroupInformation_Base {

    public static final Comparator<CycleCourseGroupInformation> COMPARATOR_BY_EXECUTION_YEAR =
            new Comparator<CycleCourseGroupInformation>() {

                @Override
                public int compare(CycleCourseGroupInformation arg0, CycleCourseGroupInformation arg1) {
                    return arg0.getExecutionYear().isBefore(arg1.getExecutionYear()) ? 1 : -1;
                }

            };

    public CycleCourseGroupInformation() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public CycleCourseGroupInformation(final CycleCourseGroup cycleCourseGroup, final ExecutionYear executionYear,
            String graduatedTitle, String graduatedTitleEn) {
        this();

        setExecutionYear(executionYear);
        setCycleCourseGroup(cycleCourseGroup);
        setGraduatedTitle(new MultiLanguageString(MultiLanguageString.pt, graduatedTitle).with(MultiLanguageString.en, graduatedTitleEn));
        checkParameters();
    }

    private void checkParameters() {
        if (getExecutionYear() == null) {
            throw new DomainException("cycle.course.group.information.execution.year.cannot.be.empty");
        }

        if (getCycleCourseGroup() == null) {
            throw new DomainException("cycle.course.group.information.course.group.cannot.be.empty");
        }
    }

    public String getGraduatedTitlePt() {
        return getGraduatedTitle().getContent(MultiLanguageString.pt);
    }

    public String getGraduatedTitleEn() {
        return getGraduatedTitle().getContent(MultiLanguageString.en);
    }

    @Atomic
    public void edit(ExecutionYear editExecutionYear, String editGraduatedTitle, String editGraduatedTitleEn) {
        this.setExecutionYear(editExecutionYear);
        MultiLanguageString mls = this.getGraduatedTitle();

        this.setGraduatedTitle(mls.with(MultiLanguageString.pt, editGraduatedTitle).with(MultiLanguageString.en, editGraduatedTitleEn));
        checkParameters();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCycleCourseGroup() {
        return getCycleCourseGroup() != null;
    }

    @Deprecated
    public boolean hasGraduatedTitle() {
        return getGraduatedTitle() != null;
    }

    @Deprecated
    public boolean hasExecutionYear() {
        return getExecutionYear() != null;
    }

}
