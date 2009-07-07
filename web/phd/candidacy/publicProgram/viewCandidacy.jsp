<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<a href="http://www.ist.utl.pt">IST</a> &gt;
	<a href="http://www.ist.utl.pt/en/html/ist-epfl/">IST-EPFL</a> &gt;
	<bean:message key="title.view.candidacy.process" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.public.candidacy" bundle="PHD_RESOURCES" /></h1>

<%-- ### End of Title ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Operation Area ### --%>

<logic:equal name="canEditCandidacy" value="true">

	<fr:form id="editCandidacyForm" action="/candidacies/phdProgramCandidacyProcess.do">
		<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />
		<input type="hidden" id="methodForm" name="method" />
		
		<a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareEditPersonalInformation';document.getElementById('editCandidacyForm').submit();"><bean:message key="label.phd.public.candidacy.createCandidacy.fillPersonalInformation.edit" bundle="PHD_RESOURCES"/></a> | 
		<a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareEditQualifications';document.getElementById('editCandidacyForm').submit();"><bean:message key="label.phd.public.candidacy.createCandidacy.edit.qualifications" bundle="PHD_RESOURCES"/></a> |
		<a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareEditPhdIndividualProgramProcessInformation';document.getElementById('editCandidacyForm').submit();"><bean:message key="label.phd.public.candidacy.createCandidacy.fillCandidacyInformation.edit" bundle="PHD_RESOURCES"/></a> |
		<a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareEditCandidacyGuidings';document.getElementById('editCandidacyForm').submit();"><bean:message key="label.phd.public.candidacy.createCandidacy.edit.guidings" bundle="PHD_RESOURCES"/></a> | 
		<a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareEditCandidacyReferees';document.getElementById('editCandidacyForm').submit();"><bean:message key="label.phd.public.candidacy.createCandidacy.manage.referees" bundle="PHD_RESOURCES"/></a> |  
		<a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareUploadDocuments';document.getElementById('editCandidacyForm').submit();"><bean:message key="label.phd.public.candidacy.createCandidacy.updloadDocuments" bundle="PHD_RESOURCES"/></a>
	</fr:form>

</logic:equal>

<p style="margin-bottom: 0.5em;">
	<b><bean:message key="label.process.id" bundle="CANDIDATE_RESOURCES"/></b>: <bean:write name="individualProgramProcess" property="processNumber"/>
</p>

<h2 style="margin-top: 1em;"><bean:message key="title.personal.data" bundle="CANDIDATE_RESOURCES"/></h2>

<logic:equal name="canEditPersonalInformation" value="true">
	<fr:view name="individualProgramProcess" property="person" schema="Public.PhdIndividualProgramProcess.view.person">
		<fr:layout name="tabular">
			<fr:property name="classes" value="thlight thleft"/>
	        <fr:property name="columnClasses" value="width175px,,,,"/>
		</fr:layout>
	</fr:view>
</logic:equal>
<logic:equal name="canEditPersonalInformation" value="false">
	<fr:view name="individualProgramProcess" property="person" schema="Public.PhdIndividualProgramProcess.view.person.simple">
		<fr:layout name="tabular">
			<fr:property name="classes" value="thlight thleft"/>
	        <fr:property name="columnClasses" value="width175px,,,,"/>
		</fr:layout>
	</fr:view>
	<em><bean:message key="message.check.personal.information.in.intranet" bundle="PHD_RESOURCES" /></em>
</logic:equal>

<logic:equal name="canEditCandidacy" value="true">
	<logic:empty name="individualProgramProcess" property="person.personalPhotoEvenIfPending">
		<h2 style="margin-top: 1em;"><bean:message key="label.photo" bundle="PHD_RESOURCES"/></h2>
		<a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareUploadPhoto';document.getElementById('editCandidacyForm').submit();"><bean:message key="label.add" bundle="PHD_RESOURCES"/></a>
	</logic:empty>
</logic:equal>
<logic:notEmpty name="individualProgramProcess" property="person.personalPhotoEvenIfPending">
	<h2 style="margin-top: 1em;"><bean:message key="label.photo" bundle="PHD_RESOURCES"/></h2>
	<div>
		<bean:define id="photoCode" name="individualProgramProcess" property="person.personalPhotoEvenIfPending.idInternal" />
		<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrievePendingByID&amp;photoCode=" + photoCode.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
	</div>
</logic:notEmpty>

<h2 style="margin-top: 1em;"><bean:message key="label.phd.public.candidacy.createCandidacy.fillCandidacyInformation" bundle="PHD_RESOURCES"/></h2>

<fr:view name="individualProgramProcess" schema="Public.PhdIndividualProgramProcess.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="thlight thleft"/>
        <fr:property name="columnClasses" value="width175px,,,,"/>
	</fr:layout>
</fr:view>

<logic:notEmpty name="individualProgramProcess" property="guidings">
	<h2 style="margin-top: 1em;"><bean:message key="title.public.phd.guidings" bundle="PHD_RESOURCES"/></h2>
	
	<logic:iterate id="guiding" name="individualProgramProcess" property="guidings" indexId="index" >
		<strong><%= index.intValue() + 1 %>.</strong>
		<fr:view name="guiding" schema="Public.PhdProgramGuiding.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="thlight thleft"/>
		        <fr:property name="columnClasses" value="width175px,,,,"/>
			</fr:layout>
		</fr:view>
	</logic:iterate>
</logic:notEmpty>

<logic:notEmpty name="individualProgramProcess" property="qualifications">
	<h2 style="margin-top: 1em;"><bean:message key="title.public.phd.qualifications" bundle="PHD_RESOURCES"/></h2>
	
	<logic:iterate id="qualification" name="individualProgramProcess" property="qualifications" indexId="index" >
		<strong><%= index.intValue() + 1 %>.</strong>
		<fr:view name="qualification" schema="Phd.Qualification.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="thlight thleft"/>
		        <fr:property name="columnClasses" value="width175px,,,,"/>
			</fr:layout>
		</fr:view>
	</logic:iterate>
</logic:notEmpty>

<h2 style="margin-top: 1em;"><bean:message key="title.public.phd.reference.letters.authors" bundle="PHD_RESOURCES"/></h2>
<logic:iterate id="candidacyReferee" name="individualProgramProcess" property="phdCandidacyReferees" indexId="index" >
	<strong><%= index.intValue() + 1 %>.</strong>
	<fr:view name="candidacyReferee" schema="PhdCandidacyReferee.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="thlight thleft"/>
	        <fr:property name="columnClasses" value="width175px,,,,"/>
		</fr:layout>
	</fr:view>
</logic:iterate>


<h2 style="margin-top: 1em;"><bean:message key="label.phd.public.candidacy.createCandidacy.updloadDocuments" bundle="PHD_RESOURCES"/></h2>
<fr:view name="individualProgramProcess" property="candidacyProcessDocuments" schema="Public.PhdProgramCandidacyProcessDocument.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thcenter"/>
	</fr:layout>
</fr:view>
