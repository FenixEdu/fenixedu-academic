<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<logic:present role="role(STUDENT)">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.student.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.alertMessages" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess" %>">
	« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<jsp:include page="/phd/common/viewAlertMessageArchive.jsp"/>

</logic:present>
