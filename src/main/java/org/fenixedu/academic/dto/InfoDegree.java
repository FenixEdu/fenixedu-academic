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
package org.fenixedu.academic.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.DegreeInfo;
import org.fenixedu.academic.domain.GradeScaleEnum;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.commons.i18n.I18N;

public class InfoDegree extends InfoObject implements Comparable {

    private final Degree degreeDomainReference;

    private final boolean showEnVersion = I18N.getLocale().equals(org.fenixedu.academic.util.LocaleUtils.EN);

    public InfoDegree(final Degree degree) {
        degreeDomainReference = degree;
    }

    public Degree getDegree() {
        return degreeDomainReference;
    }

    @Override
    public String toString() {
        return getDegree().toString();
    }

    public String getSigla() {
        return getDegree().getSigla();
    }

    public String getNome() {
        return showEnVersion && !StringUtils.isEmpty(getNameEn()) ? getNameEn() : getDegree().getNome();
    }

    public String getPresentationName() {
        return getDegree().getPresentationName();
    }

    public String getNameEn() {
        return getDegree().getNameEn();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof InfoDegree && getDegree().equals(((InfoDegree) obj).getDegree());
    }

    public boolean isBolonhaDegree() {
        return getDegree().isBolonhaDegree();
    }

    public DegreeType getDegreeType() {
        return getDegree().getDegreeType();
    }

    public DegreeType getTipoCurso() {
        return getDegree().getDegreeType();
    }

    public List<InfoDegreeCurricularPlan> getInfoDegreeCurricularPlans() {
        final List<InfoDegreeCurricularPlan> infoDegreeCurricularPlans = new ArrayList<InfoDegreeCurricularPlan>();
        for (final DegreeCurricularPlan degreeCurricularPlan : getDegree().getDegreeCurricularPlansSet()) {
            infoDegreeCurricularPlans.add(InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan));
        }
        return infoDegreeCurricularPlans;
    }

    @Override
    public int compareTo(Object arg0) {
        InfoDegree degree = (InfoDegree) arg0;
        return this.getNome().compareTo(degree.getNome());
    }

    public List<InfoDegreeInfo> getInfoDegreeInfos() {
        final List<InfoDegreeInfo> infoDegreeInfos = new ArrayList<InfoDegreeInfo>();
        for (final DegreeInfo degreeInfo : getDegree().getDegreeInfosSet()) {
            infoDegreeInfos.add(InfoDegreeInfo.newInfoFromDomain(degreeInfo));
        }
        return infoDegreeInfos;
    }

    public GradeScaleEnum getGradeScale() {
        return getDegree().getGradeScale();
    }

    public static InfoDegree newInfoFromDomain(final Degree degree) {
        return degree == null ? null : new InfoDegree(degree);
    }

    @Override
    public String getExternalId() {
        return getDegree().getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

}
