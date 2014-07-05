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
import java.util.Locale;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.fenixframework.Atomic;

import com.google.common.base.Strings;

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
            String graduateTitleSuffix, String graduateTitleSuffixEn) {
        this();

        setExecutionYear(executionYear);
        setCycleCourseGroup(cycleCourseGroup);
        checkParameters(graduateTitleSuffix, graduateTitleSuffixEn);

        setGraduateTitleSuffix(new LocalizedString(Locale.getDefault(), graduateTitleSuffix).with(Locale.ENGLISH,
                graduateTitleSuffixEn));
    }

    @Atomic
    public void edit(ExecutionYear editExecutionYear, String graduateTitleSuffix, String graduateTitleSuffixEn) {
        setExecutionYear(editExecutionYear);
        checkParameters(graduateTitleSuffix, graduateTitleSuffixEn);

        setGraduateTitleSuffix(new LocalizedString(Locale.getDefault(), graduateTitleSuffix).with(Locale.ENGLISH,
                graduateTitleSuffixEn));
    }

    private void checkParameters(String graduatedTitleSuffix, String graduatedTitleSuffixEn) {
        if (getExecutionYear() == null) {
            throw new DomainException("cycle.course.group.information.execution.year.cannot.be.empty");
        }
        if (getCycleCourseGroup() == null) {
            throw new DomainException("cycle.course.group.information.course.group.cannot.be.empty");
        }
        if (Strings.isNullOrEmpty(graduatedTitleSuffix) || Strings.isNullOrEmpty(graduatedTitleSuffixEn)) {
            throw new DomainException("cycle.course.group.information.title.suffix.cannot.be.empty");
        }
    }

<<<<<<< HEAD
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

        this.setGraduatedTitle(mls.with(MultiLanguageString.pt, editGraduatedTitle).with(MultiLanguageString.en,
                editGraduatedTitleEn));
        checkParameters();
    }

=======
    public String getGraduateTitleSuffixDefault() {
        return getGraduateTitleSuffix() != null ? getGraduateTitleSuffix().getContent(Locale.getDefault()) : "";
    }

    public String getGraduateTitleSuffixEn() {
        return getGraduateTitleSuffix() != null ? getGraduateTitleSuffix().getContent(Locale.ENGLISH) : "";
    }
>>>>>>> New feature: removed unused slot from CycleCourseGroupInformation and
}
