<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<%-- ### Title #### --%>
<em><bean:message key="label.phd.coordinator.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.view.curriculum" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<logic:present role="COORDINATOR">

	<jsp:include page="/phd/common/viewCurriculum.jsp" />

</logic:present>
