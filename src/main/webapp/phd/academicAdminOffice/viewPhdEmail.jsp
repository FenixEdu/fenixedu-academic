<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.manage.emails.view" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<bean:define id="emailBean" name="emailBean" />
<bean:define id="process" name="emailBean" property="process" />
<bean:define id="processId" name="process" property="externalId" />

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<p>
	<html:link action="<%= "/phdIndividualProgramProcess.do?method=preparePhdEmailsManagement&amp;processId=" + processId.toString() %>">
		« <bean:message key="label.back" bundle="PHD_RESOURCES" />
	</html:link>
</p>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<%-- ### Operational Area ### --%>
<fr:view name="emailBean" schema="PhdIndividualProgramProcessEmailBean.phdEmail.view">

	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop1" />
	</fr:layout>
	
</fr:view>

