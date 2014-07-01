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
package net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;

public class SummaryTeacherBean implements Serializable {

    private Professorship professorshipReference;

    private Boolean others;

    public SummaryTeacherBean(Professorship professorship) {
        if (professorship == null) {
            throw new RuntimeException();
        }
        setProfessorship(professorship);
    }

    public SummaryTeacherBean(Boolean ohters) {
        if (ohters == null) {
            throw new RuntimeException();
        }
        setOthers(ohters);
    }

    public Boolean getOthers() {
        return others;
    }

    public void setOthers(Boolean others) {
        this.others = others;
    }

    public Professorship getProfessorship() {
        return this.professorshipReference;
    }

    public void setProfessorship(Professorship professorship) {
        this.professorshipReference = professorship;
    }

    public String getLabel() {
        if (getProfessorship() != null) {
            return getProfessorship().getPerson().getName();
        } else if (getOthers()) {
            return BundleUtil.getString(Bundle.APPLICATION, "label.others");
        }
        return "";
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof SummaryTeacherBean
                && (getProfessorship() == null || getProfessorship().equals(((SummaryTeacherBean) obj).getProfessorship())) && (getOthers() == null || getOthers()
                .equals(((SummaryTeacherBean) obj).getOthers())));
    }

    @Override
    public int hashCode() {
        return 37 * (((getProfessorship() != null) ? getProfessorship().hashCode() : 37) + ((getOthers() != null) ? getOthers()
                .hashCode() : 37));
    }
}
