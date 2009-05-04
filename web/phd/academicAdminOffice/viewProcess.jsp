<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.viewProcess.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.academicAdminOffice.viewProcess" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links  ### --%>
<html:link action="/phdIndividualProgramProcess.do?method=manageProcesses">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<%--  ### Return Links  ### --%>


<%--  ### Error Messages  ### --%>
<logic:messagesPresent message="true">
	<ul class="nobullet list6">
		<html:messages id="messages" message="true" bundle="PHD_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>
<%--  ### End of Error Messages  ### --%>



<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>

<%--  ### End Of Context Information  ### --%>



<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>

Very nice page!!!!

<%--  ### End of Operation Area  ### --%>



<%--  ### Buttons (e.g. Submit)  ### --%>

<%--  ### End of Buttons (e.g. Submit)  ### --%>


</logic:present>