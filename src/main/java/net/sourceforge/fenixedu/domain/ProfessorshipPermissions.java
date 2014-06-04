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
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.domain.Bennu;

public class ProfessorshipPermissions extends ProfessorshipPermissions_Base {

    public ProfessorshipPermissions(final Professorship professorship) {
        super();
        setRootDomainObject(Bennu.getInstance());
        if (professorship == null) {
            throw new NullPointerException();
        }
        setProfessorship(professorship);

        setPersonalization(true);
        setSiteArchive(true);
        setAnnouncements(true);
        setSections(true);
        setSummaries(true);
        setStudents(true);
        setPlanning(true);
        setEvaluationSpecific(true);
        setEvaluationWorksheets(true);
        setEvaluationProject(true);
        setEvaluationTests(true);
        setEvaluationExams(true);
        setEvaluationFinal(true);
        setWorksheets(true);
        setGroups(true);
        setShift(true);

        setEvaluationMethod(true);
        setBibliografy(true);
    }

    public ProfessorshipPermissions copyPremissions(Professorship professorship) {
        ProfessorshipPermissions p = professorship.getPermissions();
        p.setPersonalization(getPersonalization());
        p.setSiteArchive(getSiteArchive());
        p.setAnnouncements(getAnnouncements());
        p.setSections(getSections());
        p.setSummaries(getSummaries());
        p.setStudents(getStudents());
        p.setPlanning(getPlanning());
        p.setEvaluationSpecific(getEvaluationSpecific());
        p.setEvaluationWorksheets(getEvaluationWorksheets());
        p.setEvaluationProject(getEvaluationProject());
        p.setEvaluationTests(getEvaluationTests());
        p.setEvaluationExams(getEvaluationExams());
        p.setWorksheets(getWorksheets());
        p.setGroups(getGroups());
        p.setShift(getShift());

        p.setEvaluationMethod(getEvaluationMethod());
        p.setBibliografy(getBibliografy());
        return p;
    }

    public void delete() {
        setRootDomainObject(null);
        setProfessorship(null);
        deleteDomainObject();
    }

    public void logEditProfessorship() {
        ExecutionCourse ec = getProfessorship().getExecutionCourse();
        ProfessorshipManagementLog.createLog(ec, Bundle.MESSAGING, "log.executionCourse.professorship.edited",
                getProfessorship().getPerson().getPresentationName(), ec.getNome(), ec.getDegreePresentationString());
    }

}
