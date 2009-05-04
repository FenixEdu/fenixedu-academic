<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.manageProcesses.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.manageProcesses" bundle="PHD_RESOURCES" /></h2>

<%-- ### End of Title ### --%>



<%--  ###  Return Links  ### --%>

<%--  ### Return Links  ### --%>




<%--  ### Error Messages  ### --%>
<logic:messagesPresent message="true">
	<ul class="nobullet list6">
		<html:messages id="messages" message="true" bundle="messages_bundle">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<%--
<fr:hasMessages type="conversion" for="##FR_EDIT_ID##">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message show="label"/>:<fr:message /></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>
--%>
<%--  ### End of Error Messages  ### --%>



<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>

<%--  ### End Of Context Information  ### --%>



<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>
<br/>
<html:link action="/phdProgramCandidacyProcess.do?method=prepareCreateCandidacy">
	<bean:message bundle="PHD_RESOURCES" key="label.phd.candidacy.academicAdminOffice.createCandidacy"/>
</html:link>
<br/>
<br/>
<strong><bean:message  key="label.phd.processes" bundle="PHD_RESOURCES"/></strong>
<logic:notEmpty name="processes">
	<fr:view schema="PhdIndividualProgramProcess.view" name="processes">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 mtop15" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="processes">
	<br/>
	<em><bean:message key="label.phd.no.processes" bundle="PHD_RESOURCES" /></em>
	<br/>
</logic:empty>
 

<%--  ### End of Operation Area  ### --%>



<%--  ### Buttons (e.g. Submit)  ### --%>

<%--  ### End of Buttons (e.g. Submit)  ### --%>


</logic:present>


