package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

abstract public class UnitRelatedAcademicServiceRequestSituation extends UnitRelatedAcademicServiceRequestSituation_Base {

    protected UnitRelatedAcademicServiceRequestSituation() {
	super();
    }

    @Override
    protected void init(final AcademicServiceRequest academicServiceRequest,
	    final AcademicServiceRequestBean academicServiceRequestBean) {
	
	super.init(academicServiceRequest, academicServiceRequestBean);
	checkParameters(academicServiceRequestBean.getSituationUnit());
	super.setUnit(academicServiceRequestBean.getSituationUnit());
    }

    private void checkParameters(final Unit unit) {
	if (unit == null) {
	    throw new DomainException("error.UnitRelatedAcademicServiceRequestSituation.unit.cannot.be.null");
	}
    }

    @Override
    public void setUnit(Unit unit) {
	throw new DomainException("error.UnitRelatedAcademicServiceRequestSituation.cannot.modify.unit");
    }

    @Override
    public void delete() {
	super.setUnit(null);
	super.delete();
    }
}
