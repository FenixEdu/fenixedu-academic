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
package net.sourceforge.fenixedu.presentationTier.Action.alumni;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Formation;
import net.sourceforge.fenixedu.domain.Job;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.MobilePhone;
import net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionUnit;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.StrutsApplication;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsApplication(bundle = "AlumniResources", path = "alumni-section", titleKey = "label.alumni.main.title", hint = "Alumni",
        accessGroup = "role(ALUMNI)")
@Mapping(module = "alumni", path = "/index")
@Forwards(value = { @Forward(name = "Success", path = "/alumni/index.jsp") })
public class AlumniHomeDA extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Person person = getLoggedPerson(request);
        double formationsPercentage = getFormationsPercentage(person.getFormations());
        double jobsPercentage = getJobsPercentage(person.getJobsSet());

        if (!areContactsUpToDate(person)) {
            request.setAttribute("showContactsMessage", true);
        }

        int finalFormationsResult = (int) Math.round(formationsPercentage / 10) * 10;
        request.setAttribute("educationStatus", Integer.toString(finalFormationsResult));
        int finalJobsResult = (int) Math.round(jobsPercentage / 10) * 10;
        request.setAttribute("professionalStatus", Integer.toString(finalJobsResult));

        if (finalFormationsResult < 50 || finalJobsResult < 50) {
            request.setAttribute("displayWarning", true);
        }
        if (finalFormationsResult < 50 && finalFormationsResult != 0) {
            request.setAttribute("educationInsufficientData", true);
        } else if (finalFormationsResult > 0) {
            request.setAttribute("educationSufficientData", true);
        } else {
            request.setAttribute("educationNoData", true);
        }

        if (finalJobsResult < 50 && finalJobsResult != 0) {
            request.setAttribute("professionalInsufficientData", true);
        } else if (finalJobsResult > 0) {
            request.setAttribute("professionalSufficientData", true);
        } else {
            request.setAttribute("professionalNoData", true);
        }

        if (finalJobsResult == 100) {
            request.setAttribute("dontShowJobComplete", true);
        }
        if (finalFormationsResult == 100) {
            request.setAttribute("dontShowFormationComplete", true);
        }
        return mapping.findForward("Success");
    }

    private boolean areContactsUpToDate(Person person) {
        return person.areContactsRecent(EmailAddress.class, 365) && person.areContactsRecent(MobilePhone.class, 365);
    }

    private double getFormationsPercentage(List<Formation> formations) {
        double completionPercentage = 0;
        for (Formation formation : formations) {
            int numberOfHits = 0;
            int totalNumber = 9;
            if (formation.getFormationType() != null) {
                numberOfHits++;
            }
            if (formation.getDegree() != null) {
                numberOfHits++;
            }
            if (formation.getEducationArea() != null) {
                numberOfHits++;
            }
            if (formation.getInstitutionType() != null) {
                numberOfHits++;
            }
            if (formation.getInstitution() != null) {
                numberOfHits++;
                AcademicalInstitutionType academicalInstitutionType =
                        ((AcademicalInstitutionUnit) formation.getInstitution()).getInstitutionType();
                if (academicalInstitutionType == null) {
                    academicalInstitutionType = formation.getInstitutionType();
                }
                if (AcademicalInstitutionType.FOREIGN_INSTITUTION.equals(academicalInstitutionType)) {
                    if (formation.getCountry() != null) {
                        numberOfHits++;
                    }
                    totalNumber++;
                }
                if (AcademicalInstitutionUnit.readOfficialParentUnitsByType(academicalInstitutionType).contains(
                        formation.getInstitution())) {
                    if (formation.getBaseInstitution() != null) {
                        numberOfHits++;
                    }
                    totalNumber++;
                }
            }
            if (formation.getBeginYear() != null) {
                numberOfHits++;
            }
            if (formation.getYear() != null) {
                numberOfHits++;
            }
            if (formation.getEctsCredits() != null) {
                numberOfHits++;
            }
            if (formation.getFormationHours() != null) {
                numberOfHits++;
            }
            completionPercentage += (double) numberOfHits / (double) totalNumber;
        }
        double totalCompletionPercentage = completionPercentage;
        if (!formations.isEmpty()) {
            totalCompletionPercentage = completionPercentage / formations.size();
        }
        totalCompletionPercentage *= 100;
        Math.ceil(totalCompletionPercentage);
        return totalCompletionPercentage;
    }

    private double getJobsPercentage(Collection<Job> jobs) {
        double completionPercentage = 0;
        int totalNumber = 11;
        for (Job job : jobs) {
            int numberOfHits = 0;
            if (!StringUtils.isEmpty(job.getEmployerName())) {
                numberOfHits++;
            }
            if (!StringUtils.isEmpty(job.getCity())) {
                numberOfHits++;
            }
            if (job.getCountry() != null) {
                numberOfHits++;
            }
            if (job.getBusinessArea() != null) {
                numberOfHits++;
            }
            if (job.getParentBusinessArea() != null) {
                numberOfHits++;
            }
            if (!StringUtils.isEmpty(job.getPosition())) {
                numberOfHits++;
            }
            if (job.getBeginDate() != null) {
                numberOfHits++;
            }
            if (job.getEndDate() != null) {
                numberOfHits++;
            }
            if (job.getJobApplicationType() != null) {
                numberOfHits++;
            }
            if (job.getContractType() != null) {
                numberOfHits++;
            }
            if (job.getSalaryType() != null) {
                numberOfHits++;
            }
            completionPercentage += (double) numberOfHits / (double) totalNumber;
        }
        double totalCompletionPercentage = completionPercentage;
        if (!jobs.isEmpty()) {
            totalCompletionPercentage = completionPercentage / jobs.size();
        }
        totalCompletionPercentage *= 100;
        Math.ceil(totalCompletionPercentage);
        return totalCompletionPercentage;
    }

}
