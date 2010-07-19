package net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Contract;
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

@Mapping(path = "/professionalInformation", module = "manager")
@Forwards( { @Forward(name = "showProfessionalInformation", path = "/manager/personManagement/contracts/showProfessionalInformation.jsp") })
public class ProfessionalInformationDA extends FenixDispatchAction {

    public ActionForward showProfessioanlData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Person person = DomainObject.fromExternalId((String) getFromRequest(request, "personId"));

	if (person.getPersonProfessionalData() != null) {
	    request.setAttribute("professionalData", person.getPersonProfessionalData());
	}
	request.setAttribute("person", person);
	return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showSituations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Person person = DomainObject.fromExternalId((String) getFromRequest(request, "personId"));

	List<PersonContractSituation> situations = new ArrayList<PersonContractSituation>();
	if (person.getPersonProfessionalData() != null) {
	    for (PersonContractSituation personContractSituation : person.getPersonProfessionalData()
		    .getPersonContractSituations()) {
		if (personContractSituation.getAnulationDate() == null) {
		    situations.add(personContractSituation);
		}
	    }
	}
	request.setAttribute("situations", situations);
	request.setAttribute("person", person);
	return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showCategories(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Person person = DomainObject.fromExternalId((String) getFromRequest(request, "personId"));

	List<PersonProfessionalCategory> categories = new ArrayList<PersonProfessionalCategory>();
	if (person.getPersonProfessionalData() != null) {
	    for (PersonProfessionalCategory personProfessionalCategory : person.getPersonProfessionalData()
		    .getPersonProfessionalCategories()) {
		if (personProfessionalCategory.getAnulationDate() == null) {
		    categories.add(personProfessionalCategory);
		}
	    }
	}
	request.setAttribute("categories", categories);
	request.setAttribute("person", person);
	return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showRegimes(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Person person = DomainObject.fromExternalId((String) getFromRequest(request, "personId"));

	List<PersonProfessionalRegime> regimes = new ArrayList<PersonProfessionalRegime>();
	if (person.getPersonProfessionalData() != null) {
	    for (PersonProfessionalRegime personProfessionalRegime : person.getPersonProfessionalData()
		    .getPersonProfessionalRegimes()) {
		if (personProfessionalRegime.getAnulationDate() == null) {
		    regimes.add(personProfessionalRegime);
		}
	    }
	}
	request.setAttribute("regimes", regimes);
	request.setAttribute("person", person);
	return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showRelations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Person person = DomainObject.fromExternalId((String) getFromRequest(request, "personId"));

	List<PersonProfessionalRelation> relations = new ArrayList<PersonProfessionalRelation>();
	if (person.getPersonProfessionalData() != null) {
	    for (PersonProfessionalRelation personProfessionalRelation : person.getPersonProfessionalData()
		    .getPersonProfessionalRelations()) {
		if (personProfessionalRelation.getAnulationDate() == null) {
		    relations.add(personProfessionalRelation);
		}
	    }
	}
	request.setAttribute("relations", relations);
	request.setAttribute("person", person);
	return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showContracts(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Person person = DomainObject.fromExternalId((String) getFromRequest(request, "personId"));

	List<PersonProfessionalContract> contracts = new ArrayList<PersonProfessionalContract>();
	if (person.getPersonProfessionalData() != null) {
	    for (PersonProfessionalContract personProfessionalContract : person.getPersonProfessionalData()
		    .getPersonProfessionalContracts()) {
		if (personProfessionalContract.getAnulationDate() == null) {
		    contracts.add(personProfessionalContract);
		}
	    }
	}
	request.setAttribute("contracts", contracts);
	request.setAttribute("person", person);
	return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showFunctionsAccumulations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Person person = DomainObject.fromExternalId((String) getFromRequest(request, "personId"));

	List<PersonFunctionsAccumulation> functionsAccumulations = new ArrayList<PersonFunctionsAccumulation>();
	if (person.getPersonProfessionalData() != null) {
	    for (PersonFunctionsAccumulation employeeFunctionsAccumulation : person.getPersonProfessionalData()
		    .getPersonFunctionsAccumulations()) {
		if (employeeFunctionsAccumulation.getAnulationDate() == null) {
		    functionsAccumulations.add(employeeFunctionsAccumulation);
		}
	    }
	}
	request.setAttribute("functionsAccumulations", functionsAccumulations);
	request.setAttribute("person", person);
	return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showSabbaticals(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Person person = DomainObject.fromExternalId((String) getFromRequest(request, "personId"));

	List<PersonProfessionalExemption> sabbaticals = new ArrayList<PersonProfessionalExemption>();
	if (person.getPersonProfessionalData() != null) {
	    for (PersonProfessionalExemption personProfessionalExemption : person.getPersonProfessionalData()
		    .getPersonProfessionalExemptions()) {
		if (personProfessionalExemption instanceof PersonSabbatical
			&& personProfessionalExemption.getAnulationDate() == null) {
		    sabbaticals.add(personProfessionalExemption);
		}
	    }
	}
	request.setAttribute("sabbaticals", sabbaticals);
	request.setAttribute("person", person);
	return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showServiceExemptions(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Person person = DomainObject.fromExternalId((String) getFromRequest(request, "personId"));

	List<PersonProfessionalExemption> serviceExemptions = new ArrayList<PersonProfessionalExemption>();
	if (person.getPersonProfessionalData() != null) {
	    for (PersonProfessionalExemption personProfessionalExemption : person.getPersonProfessionalData()
		    .getPersonProfessionalExemptions()) {
		if (personProfessionalExemption instanceof PersonServiceExemption
			&& personProfessionalExemption.getAnulationDate() == null) {
		    serviceExemptions.add(personProfessionalExemption);
		}
	    }
	}
	request.setAttribute("serviceExemptions", serviceExemptions);
	request.setAttribute("person", person);
	return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showGrantOwnerEquivalences(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Person person = DomainObject.fromExternalId((String) getFromRequest(request, "personId"));

	List<PersonProfessionalExemption> grantOwnerEquivalences = new ArrayList<PersonProfessionalExemption>();
	if (person.getPersonProfessionalData() != null) {
	    for (PersonProfessionalExemption personProfessionalExemption : person.getPersonProfessionalData()
		    .getPersonProfessionalExemptions()) {
		if (personProfessionalExemption instanceof PersonGrantOwnerEquivalent
			&& personProfessionalExemption.getAnulationDate() == null) {
		    grantOwnerEquivalences.add(personProfessionalExemption);
		}
	    }
	}
	request.setAttribute("grantOwnerEquivalences", grantOwnerEquivalences);
	request.setAttribute("person", person);
	return mapping.findForward("showProfessionalInformation");
    }

    public ActionForward showEmployeeWorkingUnits(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Person person = DomainObject.fromExternalId((String) getFromRequest(request, "personId"));
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