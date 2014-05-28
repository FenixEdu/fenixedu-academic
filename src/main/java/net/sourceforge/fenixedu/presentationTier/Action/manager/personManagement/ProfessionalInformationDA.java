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
package net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Contract;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonAbsence;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonContractSituation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonFunctionsAccumulation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonGrantOwnerEquivalent;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalCategory;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalContract;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalExemption;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalRegime;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalRelation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonSabbatical;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonServiceExemption;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/professionalInformation", module = "manager", functionality = FindPersonAction.class)
@Forwards({ @Forward(name = "showProfessionalInformation",
        path = "/manager/personManagement/contracts/showProfessionalInformation.jsp") })
public class ProfessionalInformationDA extends FenixDispatchAction {

    public ActionForward showProfessioanlData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = FenixFramework.getDomainObject((String) getFromRequest(request, "personId"));

        if (person.getPersonProfessionalData() != null) {
            request.setAttribute("professionalData", person.getPersonProfessionalData());
        }
        request.setAttribute("person", person);
        return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showSituations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = FenixFramework.getDomainObject((String) getFromRequest(request, "personId"));

        List<PersonContractSituation> situations = new ArrayList<PersonContractSituation>();
        if (person.getPersonProfessionalData() != null) {
            for (GiafProfessionalData giafProfessionalData : person.getPersonProfessionalData().getGiafProfessionalDatasSet()) {
                for (PersonContractSituation personContractSituation : giafProfessionalData.getPersonContractSituations()) {
                    if (personContractSituation.getAnulationDate() == null) {
                        situations.add(personContractSituation);
                    }
                }
            }
        }
        request.setAttribute("situations", situations);
        request.setAttribute("person", person);
        return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showCategories(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = FenixFramework.getDomainObject((String) getFromRequest(request, "personId"));

        List<PersonProfessionalCategory> categories = new ArrayList<PersonProfessionalCategory>();
        if (person.getPersonProfessionalData() != null) {
            for (GiafProfessionalData giafProfessionalData : person.getPersonProfessionalData().getGiafProfessionalDatasSet()) {
                for (PersonProfessionalCategory personProfessionalCategory : giafProfessionalData
                        .getPersonProfessionalCategories()) {
                    if (personProfessionalCategory.getAnulationDate() == null) {
                        categories.add(personProfessionalCategory);
                    }
                }
            }
        }
        request.setAttribute("categories", categories);
        request.setAttribute("person", person);
        return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showRegimes(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = FenixFramework.getDomainObject((String) getFromRequest(request, "personId"));

        List<PersonProfessionalRegime> regimes = new ArrayList<PersonProfessionalRegime>();
        if (person.getPersonProfessionalData() != null) {
            for (GiafProfessionalData giafProfessionalData : person.getPersonProfessionalData().getGiafProfessionalDatasSet()) {
                for (PersonProfessionalRegime personProfessionalRegime : giafProfessionalData.getPersonProfessionalRegimes()) {
                    if (personProfessionalRegime.getAnulationDate() == null) {
                        regimes.add(personProfessionalRegime);
                    }
                }
            }
        }
        request.setAttribute("regimes", regimes);
        request.setAttribute("person", person);
        return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showRelations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = FenixFramework.getDomainObject((String) getFromRequest(request, "personId"));

        List<PersonProfessionalRelation> relations = new ArrayList<PersonProfessionalRelation>();
        if (person.getPersonProfessionalData() != null) {
            for (GiafProfessionalData giafProfessionalData : person.getPersonProfessionalData().getGiafProfessionalDatasSet()) {
                for (PersonProfessionalRelation personProfessionalRelation : giafProfessionalData
                        .getPersonProfessionalRelations()) {
                    if (personProfessionalRelation.getAnulationDate() == null) {
                        relations.add(personProfessionalRelation);
                    }
                }
            }
        }
        request.setAttribute("relations", relations);
        request.setAttribute("person", person);
        return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showContracts(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = FenixFramework.getDomainObject((String) getFromRequest(request, "personId"));

        List<PersonProfessionalContract> contracts = new ArrayList<PersonProfessionalContract>();
        if (person.getPersonProfessionalData() != null) {
            for (GiafProfessionalData giafProfessionalData : person.getPersonProfessionalData().getGiafProfessionalDatasSet()) {
                for (PersonProfessionalContract personProfessionalContract : giafProfessionalData
                        .getPersonProfessionalContracts()) {
                    if (personProfessionalContract.getAnulationDate() == null) {
                        contracts.add(personProfessionalContract);
                    }
                }
            }
        }
        request.setAttribute("contracts", contracts);
        request.setAttribute("person", person);
        return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showFunctionsAccumulations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = FenixFramework.getDomainObject((String) getFromRequest(request, "personId"));

        List<PersonFunctionsAccumulation> functionsAccumulations = new ArrayList<PersonFunctionsAccumulation>();
        if (person.getPersonProfessionalData() != null) {
            for (GiafProfessionalData giafProfessionalData : person.getPersonProfessionalData().getGiafProfessionalDatasSet()) {
                for (PersonFunctionsAccumulation employeeFunctionsAccumulation : giafProfessionalData
                        .getPersonFunctionsAccumulations()) {
                    if (employeeFunctionsAccumulation.getAnulationDate() == null) {
                        functionsAccumulations.add(employeeFunctionsAccumulation);
                    }
                }
            }
        }
        request.setAttribute("functionsAccumulations", functionsAccumulations);
        request.setAttribute("person", person);
        return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showSabbaticals(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = FenixFramework.getDomainObject((String) getFromRequest(request, "personId"));

        List<PersonProfessionalExemption> sabbaticals = new ArrayList<PersonProfessionalExemption>();
        if (person.getPersonProfessionalData() != null) {
            for (GiafProfessionalData giafProfessionalData : person.getPersonProfessionalData().getGiafProfessionalDatasSet()) {
                for (PersonProfessionalExemption personProfessionalExemption : giafProfessionalData
                        .getPersonProfessionalExemptions()) {
                    if (personProfessionalExemption instanceof PersonSabbatical
                            && personProfessionalExemption.getAnulationDate() == null) {
                        sabbaticals.add(personProfessionalExemption);
                    }
                }
            }
        }
        request.setAttribute("sabbaticals", sabbaticals);
        request.setAttribute("person", person);
        return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showServiceExemptions(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = FenixFramework.getDomainObject((String) getFromRequest(request, "personId"));

        List<PersonProfessionalExemption> serviceExemptions = new ArrayList<PersonProfessionalExemption>();
        if (person.getPersonProfessionalData() != null) {
            for (GiafProfessionalData giafProfessionalData : person.getPersonProfessionalData().getGiafProfessionalDatasSet()) {
                for (PersonProfessionalExemption personProfessionalExemption : giafProfessionalData
                        .getPersonProfessionalExemptions()) {
                    if (personProfessionalExemption instanceof PersonServiceExemption
                            && personProfessionalExemption.getAnulationDate() == null) {
                        serviceExemptions.add(personProfessionalExemption);
                    }
                }
            }
        }
        request.setAttribute("serviceExemptions", serviceExemptions);
        request.setAttribute("person", person);
        return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showGrantOwnerEquivalences(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = FenixFramework.getDomainObject((String) getFromRequest(request, "personId"));

        List<PersonProfessionalExemption> grantOwnerEquivalences = new ArrayList<PersonProfessionalExemption>();
        if (person.getPersonProfessionalData() != null) {
            for (GiafProfessionalData giafProfessionalData : person.getPersonProfessionalData().getGiafProfessionalDatasSet()) {
                for (PersonProfessionalExemption personProfessionalExemption : giafProfessionalData
                        .getPersonProfessionalExemptions()) {
                    if (personProfessionalExemption instanceof PersonGrantOwnerEquivalent
                            && personProfessionalExemption.getAnulationDate() == null) {
                        grantOwnerEquivalences.add(personProfessionalExemption);
                    }
                }
            }
        }
        request.setAttribute("grantOwnerEquivalences", grantOwnerEquivalences);
        request.setAttribute("person", person);
        return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showAbsences(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = FenixFramework.getDomainObject((String) getFromRequest(request, "personId"));

        List<PersonProfessionalExemption> absences = new ArrayList<PersonProfessionalExemption>();
        if (person.getPersonProfessionalData() != null) {
            for (GiafProfessionalData giafProfessionalData : person.getPersonProfessionalData().getGiafProfessionalDatasSet()) {
                for (PersonProfessionalExemption personProfessionalExemption : giafProfessionalData
                        .getPersonProfessionalExemptions()) {
                    if (personProfessionalExemption instanceof PersonAbsence
                            && personProfessionalExemption.getAnulationDate() == null) {
                        absences.add(personProfessionalExemption);
                    }
                }
            }
        }
        request.setAttribute("absences", absences);
        request.setAttribute("person", person);
        return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showEmployeeWorkingUnits(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = FenixFramework.getDomainObject((String) getFromRequest(request, "personId"));
        List<Contract> workingUnits = new ArrayList<Contract>();
        Employee employee = person.getEmployee();
        if (employee != null) {
            workingUnits.addAll(employee.getWorkingContracts());
        }
        request.setAttribute("workingUnits", workingUnits);
        request.setAttribute("person", person);
        return mapping.findForward("showProfessionalInformation");
    }

}