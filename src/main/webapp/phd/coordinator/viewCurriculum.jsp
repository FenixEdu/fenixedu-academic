<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<%-- ### Title #### --%>
<em><bean:message key="label.phd.coordinator.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.view.curriculum" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<logic:present role="COORDINATOR">

	<jsp:include page="/phd/common/viewCurriculum.jsp" />

</logic:present>
