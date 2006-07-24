package net.sourceforge.fenixedu.domain.administrativeOffice;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;

public class AdministrativeOffice extends AdministrativeOffice_Base {

	private AdministrativeOffice() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());

	}

	public AdministrativeOffice(AdministrativeOfficeType administrativeOfficeType, Unit unit) {
		this();
		init(administrativeOfficeType, unit);
	}

	private void checkParameters(AdministrativeOfficeType administrativeOfficeType, Unit unit) {
		if (administrativeOfficeType == null) {
			throw new DomainException(
					"error.administrativeOffice.AdministrativeOffice.administrativeOfficeType.cannot.be.null");
		}
		if (unit == null) {
			throw new DomainException(
					"error.administrativeOffice.AdministrativeOffice.unit.cannot.be.null");
		}

		checkIfExistsAdministrativeOfficeForType(administrativeOfficeType);

	}

	private void checkIfExistsAdministrativeOfficeForType(
			AdministrativeOfficeType administrativeOfficeType) {

		for (final AdministrativeOffice administrativeOffice : RootDomainObject.getInstance()
				.getAdministrativeOffices()) {
			if (administrativeOffice.getAdministrativeOfficeType() == administrativeOfficeType) {
				throw new DomainException(
						"error.administrativeOffice.AdministrativeOffice.already.exists.with.administrativeOfficeType");
			}
		}
	}

	protected void init(AdministrativeOfficeType administrativeOfficeType, Unit unit) {
		checkParameters(administrativeOfficeType, unit);
		super.setAdministrativeOfficeType(administrativeOfficeType);
		super.setUnit(unit);

	}

	@Override
	public void setAdministrativeOfficeType(AdministrativeOfficeType administrativeOfficeType) {
		throw new DomainException(
				"error.administrativeOffice.AdministrativeOffice.cannot.modify.administrativeOfficeType");
	}

	@Override
	public void setUnit(Unit unit) {
		throw new DomainException("error.administrativeOffice.AdministrativeOffice.cannot.modify.unit");
	}

	// static methods
	public static AdministrativeOffice readByAdministrativeOfficeType(
			AdministrativeOfficeType administrativeOfficeType) {

		for (final AdministrativeOffice administrativeOffice : RootDomainObject.getInstance()
				.getAdministrativeOffices()) {

			if (administrativeOffice.getAdministrativeOfficeType() == administrativeOfficeType) {
				return administrativeOffice;
			}

		}
		return null;

	}

	public List<DocumentRequest> searchDocumentsBy(DocumentRequestType documentRequestType,
			AcademicServiceRequestSituationType requestSituationType, Boolean isUrgent, Student student) {

		final List<DocumentRequest> result = new ArrayList<DocumentRequest>();

		for (final AcademicServiceRequest serviceRequest : getAcademicServiceRequestsSet()) {
			
			if (serviceRequest.isNewRequest()) {
				continue;
			}

			if (serviceRequest instanceof DocumentRequest) {
				
				final DocumentRequest documentRequest = (DocumentRequest) serviceRequest;

				if (documentRequestType != null
						&& documentRequest.getDocumentRequestType() != documentRequestType) {
					continue;
				}

				if (requestSituationType != null
						&& documentRequest.getAcademicServiceRequestSituationType() != requestSituationType) {
					continue;
				}

				if (isUrgent != null && documentRequest.isUrgentRequest() != isUrgent.booleanValue()) {
					continue;
				}

				if (student != null && documentRequest.getStudent() != student) {
					continue;
				}

				result.add(documentRequest);
			}
		}

		return result;
	}

}
