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
package org.fenixedu.academic.domain.credits.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.teacher.DegreeProjectTutorialService;

import org.apache.commons.lang.StringUtils;

public class ProjectTutorialServiceBean implements Serializable {

    protected Professorship professorship;
    protected Attends attend;
    protected Integer percentage;
    protected DegreeProjectTutorialService degreeProjectTutorialService;
    protected List<DegreeProjectTutorialService> othersDegreeProjectTutorialService =
            new ArrayList<DegreeProjectTutorialService>();

    public ProjectTutorialServiceBean(Professorship professorship, Attends attend) {
        this.professorship = professorship;
        this.attend = attend;
        for (DegreeProjectTutorialService degreeProjectTutorialService : attend.getDegreeProjectTutorialServicesSet()) {
            if (degreeProjectTutorialService.getProfessorship().equals(professorship)) {
                this.percentage = degreeProjectTutorialService.getPercentageValue();
                this.degreeProjectTutorialService = degreeProjectTutorialService;
            } else {
                othersDegreeProjectTutorialService.add(degreeProjectTutorialService);
            }
        }
    }

    public Professorship getProfessorship() {
        return professorship;
    }

    public void setProfessorship(Professorship professorship) {
        this.professorship = professorship;
    }

    public Attends getAttend() {
        return attend;
    }

    public void setAttend(Attends attend) {
        this.attend = attend;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }

    public List<DegreeProjectTutorialService> getOthersDegreeProjectTutorialService() {
        return othersDegreeProjectTutorialService;
    }

    public void setOthersDegreeProjectTutorialService(List<DegreeProjectTutorialService> othersDegreeProjectTutorialService) {
        this.othersDegreeProjectTutorialService = othersDegreeProjectTutorialService;
    }

    public DegreeProjectTutorialService getDegreeProjectTutorialService() {
        return degreeProjectTutorialService;
    }

    public void setDegreeProjectTutorialService(DegreeProjectTutorialService degreeProjectTutorialService) {
        this.degreeProjectTutorialService = degreeProjectTutorialService;
    }

    public String getOthersDegreeProjectTutorialServiceString() {
        List<String> result = new ArrayList<String>();
        for (DegreeProjectTutorialService degreeProjectTutorialService : getOthersDegreeProjectTutorialService()) {
            result.add(degreeProjectTutorialService.getProfessorship().getTeacher().getPerson().getPresentationName() + " - "
                    + degreeProjectTutorialService.getPercentageValue() + "%");
        }
        return StringUtils.join(result, "<br/>");
    }
}