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
 * InfoExecutionCourse.java
 * 
 * Created on 28 de Novembro de 2002, 3:41
 */

package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Calendar;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.CurricularCourseScope;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.commons.i18n.I18N;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * @author tfc130
 */
public class InfoCurricularCourseScope extends InfoObject {

    public static final Comparator<InfoCurricularCourseScope> COMPARATOR_BY_YEAR_SEMESTER_BRANCH_AND_NAME =
            new Comparator<InfoCurricularCourseScope>() {

                @Override
                public int compare(InfoCurricularCourseScope o1, InfoCurricularCourseScope o2) {
                    final ComparatorChain comparatorChain = new ComparatorChain();

                    comparatorChain.addComparator(new Comparator<InfoCurricularCourseScope>() {

                        @Override
                        public int compare(InfoCurricularCourseScope o1, InfoCurricularCourseScope o2) {
                            return o1.getInfoCurricularSemester().getInfoCurricularYear().getYear()
                                    .compareTo(o2.getInfoCurricularSemester().getInfoCurricularYear().getYear());
                        }

                    });

                    comparatorChain.addComparator(new Comparator<InfoCurricularCourseScope>() {

                        @Override
                        public int compare(InfoCurricularCourseScope o1, InfoCurricularCourseScope o2) {
                            return o1.getInfoCurricularSemester().getSemester()
                                    .compareTo(o2.getInfoCurricularSemester().getSemester());
                        }

                    });

                    comparatorChain.addComparator(new Comparator<InfoCurricularCourseScope>() {

                        @Override
                        public int compare(InfoCurricularCourseScope o1, InfoCurricularCourseScope o2) {
                            final String acronym1 = o1.getInfoBranch() == null ? null : o1.getInfoBranch().getAcronym();
                            final String acronym2 = o2.getInfoBranch() == null ? null : o2.getInfoBranch().getAcronym();
                            if (StringUtils.isEmpty(acronym1) && StringUtils.isEmpty(acronym2)) {
                                return 0;
                            } else if (!StringUtils.isEmpty(acronym1) && StringUtils.isEmpty(acronym2)) {
                                return 1;
                            } else if (StringUtils.isEmpty(acronym1) && !StringUtils.isEmpty(acronym2)) {
                                return -1;
                            }

                            return acronym1.compareTo(acronym2);
                        }
                    });

                    comparatorChain.addComparator(new Comparator<InfoCurricularCourseScope>() {

                        @Override
                        public int compare(InfoCurricularCourseScope o1, InfoCurricularCourseScope o2) {
                            return InfoCurricularCourse.COMPARATOR_BY_NAME_AND_ID.compare(o1.getInfoCurricularCourse(),
                                    o2.getInfoCurricularCourse());
                        }

                    });

                    return comparatorChain.compare(o1, o2);
                }

            };

    private final CurricularCourseScope curricularCourseScope;

    private final boolean showEnVersion = I18N.getLocale().equals(MultiLanguageString.en);

    public InfoCurricularCourseScope(final CurricularCourseScope curricularCourseScope) {
        this.curricularCourseScope = curricularCourseScope;
    }

    public CurricularCourseScope getCurricularCourseScope() {
        return curricularCourseScope;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof InfoCurricularCourseScope
                && getCurricularCourseScope() == ((InfoCurricularCourseScope) obj).getCurricularCourseScope();
    }

    @Override
    public String toString() {
        return getCurricularCourseScope().toString();
    }

    public Boolean isActive() {
        return getCurricularCourseScope().isActive();
    }

    public Calendar getBeginDate() {
        return getCurricularCourseScope().getBeginDate();
    }

    public Calendar getEndDate() {
        return getCurricularCourseScope().getEndDate();
    }

    public InfoBranch getInfoBranch() {
        return InfoBranch.newInfoFromDomain(getCurricularCourseScope().getBranch());
    }

    public InfoCurricularCourse getInfoCurricularCourse() {
        return InfoCurricularCourse.newInfoFromDomain(getCurricularCourseScope().getCurricularCourse());
    }

    public InfoCurricularSemester getInfoCurricularSemester() {
        return InfoCurricularSemester.newInfoFromDomain(getCurricularCourseScope().getCurricularSemester());
    }

    public static InfoCurricularCourseScope newInfoFromDomain(final CurricularCourseScope curricularCourseScope) {
        return curricularCourseScope == null ? null : new InfoCurricularCourseScope(curricularCourseScope);
    }

    public String getAnotation() {
        return getCurricularCourseScope().getAnotation();
    }

    @Override
    public String getExternalId() {
        return getCurricularCourseScope().getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

}
