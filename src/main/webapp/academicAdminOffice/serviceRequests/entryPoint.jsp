<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<h3 class="text-center"><bean:message key="label.academic.service.requests" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>

<div class="container">
	<div class="row text-center">

		<div class="col-lg-4">
			<html:link styleClass="btn btn-primary" page="/academicServiceRequestsManagement.do?method=search&academicSituationType=NEW"><bean:message key="new.requests" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link>
		</div>

		<div class="col-lg-4">
			<html:link styleClass="btn btn-info" page="/academicServiceRequestsManagement.do?method=search&academicSituationType=PROCESSING"><bean:message key="processing.requests" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link>
		</div>

		<div class="col-lg-4">
			<html:link styleClass="btn btn-success" page="/academicServiceRequestsManagement.do?method=search&academicSituationType=CONCLUDED"><bean:message key="concluded.requests" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link>
		</div>

	</div>
</div>