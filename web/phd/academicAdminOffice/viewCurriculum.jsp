<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%-- ### Title #### --%>
<em><bean:message key="label.phd.coordinator.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.view.curriculum" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<jsp:include page="/phd/common/viewCurriculum.jsp" />

</logic:present>
