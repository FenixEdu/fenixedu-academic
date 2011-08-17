<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<logic:present role="GEP">

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.registeredDegreeCandidacies.first.time.list" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<jsp:include page="/academicAdminOffice/student/candidacies/registration/viewRegisteredDegreeCandidacies_bd.jsp" />

</logic:present>
