<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>en/education/fct-phd-programmes/">FCT Doctoral Programmes</a> &gt;
	<bean:message key="title.view.candidacy.process" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.epfl.public.candidacy" bundle="PHD_RESOURCES" /></h1>

<%-- ### End of Title ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Operation Area ### --%>
<fr:form id="editCandidacyForm" action="/applications/epfl/phdProgramCandidacyProcess.do">

<logic:equal name="canEditCandidacy" value="true">

		<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />
		<input type="hidden" id="methodForm" name="method" />
		
		<a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareEditPersonalInformation';document.getElementById('editCandidacyForm').submit();"><bean:message key="label.phd.public.candidacy.createCandidacy.fillPersonalInformation.edit" bundle="PHD_RESOURCES"/></a> | 
		<a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareEditQualifications';document.getElementById('editCandidacyForm').submit();"><bean:message key="label.phd.public.candidacy.createCandidacy.edit.qualifications" bundle="PHD_RESOURCES"/></a> |
		<a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareEditPhdIndividualProgramProcessInformation';document.getElementById('editCandidacyForm').submit();"><bean:message key="label.phd.public.candidacy.createCandidacy.fillCandidacyInformation.edit" bundle="PHD_RESOURCES"/></a> |
		<a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareEditCandidacyGuidings';document.getElementById('editCandidacyForm').submit();"><bean:message key="label.phd.public.candidacy.createCandidacy.edit.guidings" bundle="PHD_RESOURCES"/></a> | 
		<a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareEditCandidacyReferees';document.getElementById('editCandidacyForm').submit();"><bean:message key="label.phd.public.candidacy.createCandidacy.manage.referees" bundle="PHD_RESOURCES"/></a> | 
		<a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareUploadDocuments';document.getElementById('editCandidacyForm').submit();"><bean:message key="label.phd.public.candidacy.createCandidacy.updloadDocuments" bundle="PHD_RESOURCES"/></a> |   
		<a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareValidateCandidacy';document.getElementById('editCandidacyForm').submit();"><bean:message key="label.phd.public.candidacy.validate" bundle="PHD_RESOURCES"/></a>

</logic:equal>

<%--  ### Candidacy Period ### 
<bean:define id="startDate" name="candidacyPeriod" property="start" type="org.joda.time.DateTime" />
<bean:define id="endDate" name="candidacyPeriod" property="end" type="org.joda.time.DateTime" />
<p class="mtop15"><b><bean:message key="message.candidacy.period" bundle="PHD_RESOURCES"/>: </b><%= startDate.toString("dd/MM/yyyy") %> <bean:message key="label.until" bundle="PHD_RESOURCES"/> <%= endDate.toString("dd/MM/yyyy") %></p>
--%>

<style>
.warning0 {
background-color: #fbf8cc;
/*color: #805500;*/
padding: 0.5em 1em;
}
</style>
<%--  ### Validation messages ### --%>
<logic:messagesPresent message="true" property="validation">
	<div class="warning0 mvert1">
		<p class="mvert05">For the application to be ready you must:</p>
		<ul class="mvert05">
			<html:messages id="messages" message="true" bundle="PHD_RESOURCES" property="validation">
				<li><bean:write name="messages" /></li>
			</html:messages>
		</ul>
	</div>
</logic:messagesPresent>
<logic:messagesNotPresent message="true" property="validation">
	<logic:equal name="canEditCandidacy" value="true">
		<div class="warning0 mvert1"><p class="mvert05">All required information was added. The application is now ready to be validated. To proceed press to following link: <strong><a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareValidateCandidacy';document.getElementById('editCandidacyForm').submit();"><bean:message key="label.phd.public.candidacy.validate" bundle="PHD_RESOURCES"/> »</a></strong></p></div>
	</logic:equal>
	<logic:equal name="canEditCandidacy" value="false">
		<div class="warning0 mvert1"><p class="mvert05"><b>Thank you!</b> Your application was submitted successfuly.</p></div>
	</logic:equal>
</logic:messagesNotPresent>

<p style="margin-bottom: 0.5em;">
	<b><bean:message key="label.process.id" bundle="CANDIDATE_RESOURCES"/></b>: <bean:write name="individualProgramProcess" property="processNumber"/>
</p>

<%--  ### Personal Information ### --%>
<h2 style="margin-top: 1em;"><bean:message key="title.public.phd.personal.data" bundle="PHD_RESOURCES"/></h2>
<logic:equal name="canEditPersonalInformation" value="true">
	<fr:view name="pendingPartyContactBean">
		<fr:schema type="net.sourceforge.fenixedu.dataTransferObject.contacts.PendingPartyContactBean" bundle="PHD_RESOURCES">
			<fr:slot name="person.name" key="label.net.sourceforge.fenixedu.domain.Person.name" >
				<property name="classes" value="bold nowrap"/>
			</fr:slot>
			<fr:slot name="person.gender" key="label.net.sourceforge.fenixedu.domain.Person.gender" />
			<fr:slot name="person.idDocumentType" key="label.net.sourceforge.fenixedu.domain.Person.idDocumentType" />
			<fr:slot name="person.documentIdNumber" key="label.net.sourceforge.fenixedu.domain.Person.documentIdNumber" />
			<fr:slot name="person.emissionLocationOfDocumentId" key="label.net.sourceforge.fenixedu.domain.Person.emissionLocationOfDocumentId" />
			<fr:slot name="person.socialSecurityNumber" key="label.net.sourceforge.fenixedu.domain.Person.socialSecurityNumber" />
		   	<fr:slot name="person.dateOfBirth" key="label.net.sourceforge.fenixedu.domain.Person.dateOfBirth" />
			<fr:slot name="person.districtSubdivisionOfBirth" key="label.net.sourceforge.fenixedu.domain.Person.districtSubdivisionOfBirth" />
			<fr:slot name="person.nationality.countryNationality" key="label.net.sourceforge.fenixedu.domain.Person.nationality" />
			<fr:slot name="defaultPhysicalAddress.address" key="label.net.sourceforge.fenixedu.domain.Person.address" />
			<fr:slot name="defaultPhysicalAddress.area" key="label.net.sourceforge.fenixedu.domain.Person.area" />
			<fr:slot name="defaultPhysicalAddress.areaCode" key="label.net.sourceforge.fenixedu.domain.Person.areaCode" />
		    <fr:slot name="defaultPhysicalAddress.countryOfResidence.localizedName" key="label.net.sourceforge.fenixedu.domain.Person.countryOfResidence.localizedName" />
			<fr:slot name="defaultPhone.number" key="label.net.sourceforge.fenixedu.domain.Person.phone" />
			<fr:slot name="defaultMobilePhone.number" key="label.net.sourceforge.fenixedu.domain.Person.mobile" />
			<fr:slot name="defaultEmailAddress.value" key="label.net.sourceforge.fenixedu.domain.Person.email" />
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="thlight thleft"/>
	        <fr:property name="columnClasses" value="width175px,,,,"/>
		</fr:layout>
	</fr:view>
	<logic:equal name="canEditCandidacy" value="true">
		<p class="mvert05"><a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareEditPersonalInformation';document.getElementById('editCandidacyForm').submit();"><bean:message key="label.phd.public.candidacy.createCandidacy.fillPersonalInformation.edit" bundle="PHD_RESOURCES"/></a></p>
	</logic:equal>
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

<%--  ### Photo ### --%>
<h2 style="margin-top: 1em;"><bean:message key="label.photo" bundle="PHD_RESOURCES"/></h2>
<logic:equal name="canEditCandidacy" value="true">
	<logic:empty name="individualProgramProcess" property="person.personalPhotoEvenIfPending">
		<p class="mvert05"><em><bean:message key="label.not.defined" bundle="PHD_RESOURCES"/></em></p>
	</logic:empty>
	<a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareUploadPhoto';document.getElementById('editCandidacyForm').submit();"><bean:message key="label.edit.photo" bundle="PHD_RESOURCES"/></a>
	<br/><br/>
</logic:equal>
<logic:notEmpty name="individualProgramProcess" property="person.personalPhotoEvenIfPending">
	<div>
		<bean:define id="photoCode" name="individualProgramProcess" property="person.personalPhotoEvenIfPending.externalId" />
		<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrievePendingByID&amp;photoCode=" + photoCode.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
	</div>
</logic:notEmpty>

<%--  ### Candidacy Information ### --%>
<h2 style="margin-top: 1em;"><bean:message key="label.phd.public.candidacy.createCandidacy.fillCandidacyInformation" bundle="PHD_RESOURCES"/></h2>
<fr:view name="individualProgramProcess" schema="Public.PhdIndividualProgramProcess.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="thlight thleft"/>
        <fr:property name="columnClasses" value="width175px,,,,"/>
	</fr:layout>
</fr:view>
<logic:equal name="canEditCandidacy" value="true">
	<p class="mvert05"><a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareEditPhdIndividualProgramProcessInformation';document.getElementById('editCandidacyForm').submit();"><bean:message key="label.phd.public.candidacy.createCandidacy.fillCandidacyInformation.edit" bundle="PHD_RESOURCES"/></a></p>
</logic:equal>

<%--  ### Phd Supervisors ### --%>
<h2 style="margin-top: 1em;"><bean:message key="title.public.phd.guidings" bundle="PHD_RESOURCES"/> <span style="font-weight: normal; font-size: 13px; color: #777;">(<bean:message key="title.public.phd.if.applicable" bundle="PHD_RESOURCES"/>)</span></h2>
<logic:notEmpty name="individualProgramProcess" property="guidings">
	<logic:iterate id="guiding" name="individualProgramProcess" property="guidings" indexId="index" >
		<p class="mtop2 mbottom05"><strong><%= index.intValue() + 1 %>.</strong></p>
		<fr:view name="guiding" schema="Public.PhdProgramGuiding.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="thlight thleft"/>
		        <fr:property name="columnClasses" value="width175px,,,,"/>
			</fr:layout>
		</fr:view>
	</logic:iterate>
</logic:notEmpty>
<logic:empty name="individualProgramProcess" property="guidings">
	<p class="mvert05"><em><bean:message key="label.not.defined" bundle="PHD_RESOURCES"/></em></p>
</logic:empty>
<logic:equal name="canEditCandidacy" value="true">
	<p class="mvert05"><a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareEditCandidacyGuidings';document.getElementById('editCandidacyForm').submit();"><bean:message key="label.phd.public.candidacy.createCandidacy.edit.guidings" bundle="PHD_RESOURCES"/></a></p>
</logic:equal>

<%--  ### Academic Degrees ### --%>
<h2 style="margin-top: 1em;"><bean:message key="title.public.phd.qualifications" bundle="PHD_RESOURCES"/></h2>
<logic:notEmpty name="individualProgramProcess" property="qualifications">
	<logic:iterate id="qualification" name="individualProgramProcess" property="qualificationsSortedByAttendedEndDate" indexId="index" >
		<p class="mtop2 mbottom05"><strong><%= index.intValue() + 1 %>.</strong></p>
		<fr:view name="qualification" schema="Phd.Qualification.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="thlight thleft"/>
		        <fr:property name="columnClasses" value="width175px,,,,"/>
			</fr:layout>
		</fr:view>
	</logic:iterate>
</logic:notEmpty>
<logic:empty name="individualProgramProcess" property="qualifications">
	<p class="mvert05"><em><bean:message key="label.not.defined" bundle="PHD_RESOURCES"/></em></p>
</logic:empty>
<logic:equal name="canEditCandidacy" value="true">
	<p class="mvert05"><a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareEditQualifications';document.getElementById('editCandidacyForm').submit();"><bean:message key="label.phd.public.candidacy.createCandidacy.edit.qualifications" bundle="PHD_RESOURCES"/></a></p>
</logic:equal>

<%--  ### Referees ### --%>
<h2 style="margin-top: 1em;"><bean:message key="title.public.phd.reference.letters.authors" bundle="PHD_RESOURCES"/> <span style="font-weight: normal; font-size: 13px; color: #777;">(<bean:message key="title.public.phd.referees" bundle="PHD_RESOURCES"/>)</span></h2>
<logic:notEmpty name="individualProgramProcess" property="phdCandidacyReferees">
	<logic:iterate id="candidacyReferee" name="individualProgramProcess" property="phdCandidacyReferees" indexId="index" >
		<p class="mtop2 mbottom05"><strong><%= index.intValue() + 1 %>.</strong></p>
		<fr:view name="candidacyReferee" schema="PhdCandidacyReferee.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="thlight thleft"/>
		        <fr:property name="columnClasses" value="width175px,,,,"/>
			</fr:layout>
		</fr:view>
	</logic:iterate>
</logic:notEmpty>
<logic:empty name="individualProgramProcess" property="phdCandidacyReferees">
	<p class="mvert05"><em><bean:message key="label.not.defined" bundle="PHD_RESOURCES"/></em></p>
</logic:empty>
<logic:equal name="canEditCandidacy" value="true">
	<p class="mvert05"><a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareEditCandidacyReferees';document.getElementById('editCandidacyForm').submit();"><bean:message key="label.phd.public.candidacy.createCandidacy.manage.referees" bundle="PHD_RESOURCES"/></a></p>
</logic:equal>

<%--  ### Documents ### --%>
<h2 style="margin-top: 1em;"><bean:message key="title.public.phd.documents" bundle="PHD_RESOURCES"/></h2>
<logic:equal name="canEditCandidacy" value="true">
	<p class="mvert05"><a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareUploadDocuments';document.getElementById('editCandidacyForm').submit();"><bean:message key="label.phd.public.candidacy.createCandidacy.updloadDocuments" bundle="PHD_RESOURCES"/></a></p>
</logic:equal>
<logic:notEmpty name="individualProgramProcess" property="candidacyProcessDocuments">
	<fr:view name="individualProgramProcess" property="candidacyProcessDocuments" schema="Public.PhdProgramProcessDocument.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thcenter"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="individualProgramProcess" property="candidacyProcessDocuments">
	<p class="mvert05"><em><bean:message key="label.not.defined" bundle="PHD_RESOURCES"/></em></p>
</logic:empty>

</fr:form>
