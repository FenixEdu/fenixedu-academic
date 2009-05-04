<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.candidacy.academicAdminOffice.createCandidacy.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.candidacy.academicAdminOffice.createCandidacy" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>



<%--  ###  Return Links  ### --%>

<%--  ### Return Links  ### --%>




<%--  ### Error Messages  ### --%>
<logic:messagesPresent message="true">
	<ul class="nobullet list6">
		<html:messages id="messages" message="true" bundle="PHD_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<fr:hasMessages type="conversion" for="createCandidacyBean">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message show="label"/>:<fr:message /></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<%--  ### End of Error Messages  ### --%>


<fr:form action="/phdProgramCandidacyProcess.do?method=createCandidacy">

<strong><bean:message  key="label.phd.candidacy.academicAdminOffice.createCandidacy.personalInformation" bundle="PHD_RESOURCES"/></strong>
<fr:edit id="createCandidacyBean.editPersonalInformation"
	name="createCandidacyBean"
	property="personBean"
	schema="PhdProgramCandidacyProcessBean.editPersonalInformation">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thcenter mtop05" />
		<fr:destination name="invalid" path="/phdProgramCandidacyProcess.do?method=prepareCreateCandidacyInvalid" />
	</fr:layout>
	
</fr:edit>

<strong><bean:message  key="label.phd.candidacy.academicAdminOffice.createCandidacy.programInformation" bundle="PHD_RESOURCES"/></strong>
<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<fr:edit id="createCandidacyBean.editProgramInformation"
	name="createCandidacyBean"
	schema="PhdProgramCandidacyProcessBean.editProgramInformation">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thcenter mtop05" />
		<fr:destination name="invalid" path="/phdProgramCandidacyProcess.do?method=prepareCreateCandidacyInvalid" />
	</fr:layout>
	
</fr:edit>



</fr:form>

<%--  ### End Of Context Information  ### --%>



<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>

<%--  ### End of Operation Area  ### --%>



<%--  ### Buttons (e.g. Submit)  ### --%>

<%--  ### End of Buttons (e.g. Submit)  ### --%>


</logic:present>
