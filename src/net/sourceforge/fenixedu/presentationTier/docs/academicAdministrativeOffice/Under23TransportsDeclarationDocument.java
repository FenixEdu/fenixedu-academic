package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import static pt.utl.ist.fenix.tools.util.DateFormatUtil.DEFAULT_DATE_FORMAT;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.AdministrativeOfficeUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.serviceRequests.Under23TransportsDeclarationRequest;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class Under23TransportsDeclarationDocument extends AdministrativeOfficeDocument {

    static private final long serialVersionUID = 1L;

    protected Under23TransportsDeclarationDocument(final Under23TransportsDeclarationRequest documentRequest) {
	super(documentRequest);
    }

    @Override
    protected Under23TransportsDeclarationRequest getDocumentRequest() {
	return (Under23TransportsDeclarationRequest) super.getDocumentRequest();
    }

    @Override
    protected void fillReport() {

	final Person person = getDocumentRequest().getPerson();

	addParameter("personName", person.getName());
	addParameter("documentIdNumber", person.getDocumentIdNumber());
	addParameter("emissionDate", person.getEmissionDateOfDocumentIdYearMonthDay() == null ? "" : person
		.getEmissionDateOfDocumentIdYearMonthDay().toString(DEFAULT_DATE_FORMAT));
	addParameter("birthDate", person.getDateOfBirthYearMonthDay().toString(DEFAULT_DATE_FORMAT));

	addParameter("executionYear", getExecutionYear().getQualifiedName());
	addParameter("institutionName", UnitUtils.readInstitutionUnit().getName());

	addAddressInformation("person", getDocumentRequest().getPerson());
	addAddressInformation("institution", getAdministrativeOfficeUnit());
	addParameter("institutionPhone", getAdministrativeOfficeUnit().getDefaultPhone().getNumber());

	addParameter("reportDate", new LocalDate().toString("dd 'de' MMMM 'de' yyyy", Language.getLocale()));
    }

    private Unit getAdministrativeOfficeUnit() {

	switch (getDocumentRequest().getAdministrativeOffice().getAdministrativeOfficeType()) {

	case DEGREE:
	    final Campus campus = AccessControl.getPerson().getEmployeeCampus();
	    final AdministrativeOfficeUnit officeUnit = getDocumentRequest().getAdministrativeOffice().getUnit();
	    for (final Unit unit : officeUnit.getSubUnits()) {
		if (unit.getCampus() == campus) {
		    return unit;
		}
	    }

	case MASTER_DEGREE:
	    return getDocumentRequest().getAdministrativeOffice().getUnit();
	}

	throw new DomainException("error.Under23TransportsDeclarationDocument.unexpected.administrative.office.type");
    }

    private void addAddressInformation(final String prefix, final Party party) {
	final PhysicalAddress address = party.getDefaultPhysicalAddress();
	addParameter(prefix + "Address", address.getAddress());
	addParameter(prefix + "Parish", address.getParishOfResidence());
	addParameter(prefix + "Municipality", address.getDistrictSubdivisionOfResidence());
	addParameter(prefix + "AreaCode", address.getAreaCode());
	addParameter(prefix + "AreaOfAreaCode", address.getAreaOfAreaCode());
    }

}
