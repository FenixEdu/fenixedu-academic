<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page isELIgnored="true"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ page import="org.fenixedu.bennu.core.security.Authenticate"%>
<%@ page import="org.fenixedu.bennu.core.util.CoreConfiguration"%>

<html:xhtml/>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<jsp:include page="/phd/candidacy/publicProgram/institution/commonBreadcumbs.jsp" />
	
	<bean:message key="title.view.candidacy.process" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.institution.public.candidacy" bundle="PHD_RESOURCES" /></h1>

<%-- ### End of Title ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Operation Area ### --%>
<logic:equal name="canEditCandidacy" value="true">
		
		<html:link action="/applications/phd/phdProgramApplicationProcess.do?method=prepareEditPersonalData" paramId="processId" paramName="process" paramProperty="externalId">
			<bean:message key="label.phd.public.candidacy.createCandidacy.fillPersonalInformation.edit" bundle="PHD_RESOURCES"/>
		</html:link> |
		
		<html:link action="/applications/phd/phdProgramApplicationProcess.do?method=prepareUploadPhoto" paramId="processId" paramName="process" paramProperty="externalId">
			<bean:message key="label.edit.photo" bundle="PHD_RESOURCES"/>
		</html:link> |
		
		<html:link action="/applications/phd/phdProgramApplicationProcess.do?method=prepareEditPhdInformationData" paramId="processId" paramName="process" paramProperty="externalId">
			<bean:message key="label.phd.public.candidacy.createCandidacy.fillCandidacyInformation.edit" bundle="PHD_RESOURCES"/>
		</html:link> |

		<html:link action="/applications/phd/phdProgramApplicationProcess.do?method=prepareEditCandidacyGuidings" paramId="processId" paramName="process" paramProperty="externalId">
			<bean:message key="label.phd.public.candidacy.createCandidacy.edit.guidings" bundle="PHD_RESOURCES"/>
		</html:link> |

		<html:link action="/applications/phd/phdProgramApplicationProcess.do?method=prepareEditQualifications" paramId="processId" paramName="process" paramProperty="externalId">
			<bean:message key="label.phd.public.candidacy.createCandidacy.edit.qualifications" bundle="PHD_RESOURCES"/>
		</html:link> |

		<html:link action="/applications/phd/phdProgramApplicationProcess.do?method=prepareEditReferees" paramId="processId" paramName="process" paramProperty="externalId">
			<bean:message key="label.phd.public.candidacy.createCandidacy.manage.referees" bundle="PHD_RESOURCES"/>
		</html:link> |
		
		<html:link action="/applications/phd/phdProgramApplicationProcess.do?method=prepareUploadDocuments" paramId="processId" paramName="process" paramProperty="externalId">
			<bean:message key="label.phd.public.candidacy.createCandidacy.updloadDocuments" bundle="PHD_RESOURCES"/>
		</html:link> |

		<html:link action="/applications/phd/phdProgramApplicationProcess.do?method=prepareValidateApplication" paramId="processId" paramName="process" paramProperty="externalId">
			<b><bean:message key="label.phd.public.candidacy.validate" bundle="PHD_RESOURCES"/></b>
		</html:link>
</logic:equal>

<%--  ### Candidacy Period ### --%>
<bean:define id="candidacyPeriod" name="process" property="publicPhdCandidacyPeriod" />
<bean:define id="startDate" name="candidacyPeriod" property="start" type="org.joda.time.DateTime" />
<bean:define id="endDate" name="candidacyPeriod" property="end" type="org.joda.time.DateTime" />


<p class="mtop15">
	<b><bean:message key="label.process.id" bundle="CANDIDATE_RESOURCES"/></b>: <bean:write name="process" property="processNumber"/>
</p>

<p class="mbottom15">
	<b><bean:message key="message.candidacy.period" bundle="PHD_RESOURCES"/>: </b><%= startDate.toString("dd/MM/yyyy") %> <bean:message key="label.until" bundle="PHD_RESOURCES"/> <%= endDate.toString("dd/MM/yyyy") %>
</p>



<style>

#aviso {
display: block;
width: 100%;
color: #393939;
position: relative;
}

.aviso-header {
	background-color: #f5ecbc;
	border: 1px solid #f2e5a6;
	border-radius-top-left:3px;
	-webkit-box-shadow:inset 0px 1px 0px #faf5da;
	
	border-top-right-radius:4px;
	border-top-left-radius:4px;
	
	-moz-border-top-right-radius:4px;
	-moz-border-top-left-radius:4px;
}

.aviso-header h2 {
	font-size: 13px;
	text-shadow: 0px 1px 0px #faf6e1;
	padding-left: 20px;
	color: #333;
	margin: 10px 0;
}

.aviso-corpo {
	background-color: #fdfcf9;
	border-bottom: 1px solid #f2e5a6;
	border-left: 1px solid #f2e5a6;
	border-right: 1px solid #f2e5a6;
	
	border-bottom-right-radius:4px;
	border-bottom-left-radius:4px;
	
	-moz-border-bottom-right-radius:4px;
	-moz-border-bottom-left-radius:4px;
}
.aviso-padding {
	padding: 30px;
}

.aviso-corpo ul {
	margin: 0px 0px 0px 20px;
	padding: 0px;
	line-height: 1.5em;
}

.aviso-progress {
	float: right;
	position: absolute;
	top: -55px;
	right: 30px;
	width: 236px;
	height: 35px;
	display: block;
	position: relative;
	background: url("<%= request.getContextPath() + "/images/candidacy/barra.base.png" %>");
}

.aviso-barra {
max-width: 231px;
	height: 27px;
	background: url("<%= request.getContextPath() + "/images/candidacy/barra.progress.png" %>");
	position: relative;
	top: 4px;
	left: 3px;
}
.aviso-barra span {
	font-size: 13px;
	font-weight: bold;
	color: #a28912;
	text-shadow: 0px 1px 0px #faf6e1;
	float: right;
	padding: 5px 4px 0 0;
}

.aviso-done {
	color: #c6c5c3;
	text-decoration: line-through;
}

</style>


<bean:define id="individualProgramProcess" name="process" property="individualProgramProcess" />

<%--  ### Validation messages ### --%>

<bean:define id="documentsSubmittedPercentage" name="documentsSubmittedPercentage" />
<bean:define id="numberOfDocumentsToSubmit" name="numberOfDocumentsToSubmit" />
<bean:define id="numberOfDocumentsSubmitted" name="numberOfDocumentsSubmitted" />

<logic:messagesPresent message="true" property="validation">
	<div id="aviso">
		<div class="aviso-header">
			<h2><bean:message key="message.phd.public.candidacy.requirements" bundle="PHD_RESOURCES" /></h2>
		</div>
		<div class="aviso-corpo">
			<div class="aviso-padding">
				<ul>
					<html:messages id="messages" message="true" bundle="PHD_RESOURCES" property="validation">
						<li><bean:write name="messages" /></li>
					</html:messages>
				</ul>
				<div class="aviso-progress">
					<div class="aviso-barra" style="width:<%= documentsSubmittedPercentage %>%"><span><%= numberOfDocumentsSubmitted + "/" + numberOfDocumentsToSubmit %></span></div>
				</div>
			</div>
		</div>
	</div>
</logic:messagesPresent>




<bean:define id="processId" name="process" property="externalId" />



<logic:messagesNotPresent message="true" property="validation">
	<logic:equal name="canEditCandidacy" value="true">
		<div class="infoop2 mvert1">
			<p style="margin-bottom: 10px;"><bean:message key="message.phd.public.candidacy.ready.to.validate" bundle="PHD_RESOURCES" /></p>
			<p>
				<strong>
					<input type=button onClick="parent.location='<%= request.getContextPath() + "/publico/applications/phd/phdProgramApplicationProcess.do?method=prepareValidateApplication&processId=" + processId %>'" value='<bean:message key="label.phd.public.candidacy.validate" bundle="PHD_RESOURCES"/> »'>
				</strong>
			</p>
		</div>
	</logic:equal>
	<logic:equal name="canEditCandidacy" value="false">
		<div class="infoop2 mvert1">
			<p class="mvert05"><bean:message key="message.phd.public.candidacy.already.submitted" bundle="PHD_RESOURCES" /></p>
		</div>
	</logic:equal>
</logic:messagesNotPresent>
<div class="simpleblock1">
	<p><bean:message key="message.phd.institution.application.need.original.documents" bundle="PHD_RESOURCES" /></p>
</div>


<%--  ### Personal Information ### --%>
<h2 style="margin-top: 1.5em;"><bean:message key="title.public.phd.personal.data" bundle="PHD_RESOURCES"/></h2>
<logic:equal name="canEditPersonalInformation" value="true">
	<fr:view name="personBean" schema="Public.PhdIndividualProgramProcess.view.person.bean">
		<fr:layout name="tabular">
			<fr:property name="classes" value="thlight thleft"/>
	        <fr:property name="columnClasses" value="width175px,,,,"/>
		</fr:layout>
	</fr:view>
	<logic:equal name="canEditCandidacy" value="true">
		<p class="mvert05">
			<html:link action="/applications/phd/phdProgramApplicationProcess.do?method=prepareEditPersonalData" paramId="processId" paramName="process" paramProperty="externalId">
				<bean:message key="label.phd.public.candidacy.createCandidacy.fillPersonalInformation.edit" bundle="PHD_RESOURCES"/>
			</html:link>
		</p>
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
<h2 style="margin-top: 1.5em;"><bean:message key="label.photo" bundle="PHD_RESOURCES"/></h2>
<logic:equal name="canEditCandidacy" value="true">
	<logic:empty name="individualProgramProcess" property="person.personalPhotoEvenIfPending">
		<p class="mvert05"><em><bean:message key="label.not.defined" bundle="PHD_RESOURCES"/>.</em></p>
	</logic:empty>
	<html:link action="/applications/phd/phdProgramApplicationProcess.do?method=prepareUploadPhoto" paramId="processId" paramName="process" paramProperty="externalId">
		<bean:message key="label.edit.photo" bundle="PHD_RESOURCES"/>
	</html:link>
</logic:equal>
<logic:notEmpty name="individualProgramProcess" property="person.personalPhotoEvenIfPending">
	<div>
		<bean:define id="photoCode" name="individualProgramProcess" property="person.personalPhotoEvenIfPending.externalId" />
		<html:img align="middle" src="${fr:checksum('/person/retrievePersonalPhoto.do?method=retrievePendingByID&photoCode='.concat(photoCode.toString()))}" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
	</div>
</logic:notEmpty>

<%--  ### Candidacy Information ### --%>
<h2 style="margin-top: 1.5em;"><bean:message key="label.phd.public.candidacy.createCandidacy.fillCandidacyInformation" bundle="PHD_RESOURCES"/></h2>
<fr:view name="individualProgramProcess">
	<fr:schema bundle="PHD_RESOURCES" type="org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess">
		<fr:slot name="candidacyDate">
			<fr:property name="classes" value="bold nowrap"/>
		</fr:slot>
		<fr:slot name="phdProgram" layout="null-as-label">
			<fr:property name="subLayout" value="values" />
			<fr:property name="subSchema" value="PhdProgram.name" />
		</fr:slot>
		<fr:slot name="thesisTitle" key="label.org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess.thesis.title.proposed" />
		<fr:slot name="executionYear" layout="format">
			<fr:property name="format" value="${year}" />
		</fr:slot>	
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="thlight thleft"/>
        <fr:property name="columnClasses" value="width175px,,,,"/>
	</fr:layout>
	
</fr:view>
<logic:equal name="canEditCandidacy" value="true">
	<p class="mvert05">
	<html:link action="/applications/phd/phdProgramApplicationProcess.do?method=prepareEditPhdInformationData" paramId="processId" paramName="process" paramProperty="externalId">
		<bean:message key="label.phd.public.candidacy.createCandidacy.fillCandidacyInformation.edit" bundle="PHD_RESOURCES"/>
	</html:link>
	</p>
</logic:equal>

<%--  ### Payment details ### --%>
<logic:equal value="true" name="hasPaymentFees">
	<h2 style="margin-top: 1.5em;"><bean:message key="title.phd.public.candidacy.payment.details" bundle="PHD_RESOURCES"/> </h2>

	<bean:define id="event" name="process" property="event" type="org.fenixedu.academic.domain.accounting.Event" />

	<p>
	<ul>
		<li>
			<strong><bean:message key="label.application.fee.amount" bundle="PHD_RESOURCES"/>:</strong>
			<fr:view name="event" property="originalAmountToPay"/> &euro;
		</li>
<% if (Authenticate.getUser() != null) { %>
		<li>
			<a href="<%= CoreConfiguration.getConfiguration().applicationUrl()
			+ "/owner-accounting-events/" + event.getExternalId() + "/details" %>"><bean:message key="label.pay" bundle="PHD_RESOURCES"/></a>
		</li>
<% } %>
	</ul>
	</p>

</logic:equal>

<%--  ### Phd Supervisors ### --%>
<h2 style="margin-top: 1.5em;"><bean:message key="title.public.phd.guidings" bundle="PHD_RESOURCES"/> <span style="font-weight: normal; font-size: 13px; color: #777;">(<bean:message key="title.public.phd.if.applicable" bundle="PHD_RESOURCES"/>)</span></h2>
<logic:notEmpty name="individualProgramProcess" property="guidings">
	<logic:iterate id="guiding" name="individualProgramProcess" property="guidings" indexId="index" >
		<p class="mtop1 mbottom05"><strong><bean:message bundle="PHD_RESOURCES" key="label.author"/> <%= index.intValue() + 1 %></strong></p>
		<fr:view name="guiding" schema="Public.PhdProgramGuiding.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="thlight thleft"/>
		        <fr:property name="columnClasses" value="width175px,,,,"/>
			</fr:layout>
		</fr:view>
	</logic:iterate>
</logic:notEmpty>
<logic:empty name="individualProgramProcess" property="guidings">
	<p class="mvert05"><em><bean:message key="label.not.defined" bundle="PHD_RESOURCES"/>.</em></p>
</logic:empty>
<logic:equal name="canEditCandidacy" value="true">
	<p class="mvert05">
		<html:link action="/applications/phd/phdProgramApplicationProcess.do?method=prepareEditCandidacyGuidings" paramId="processId" paramName="process" paramProperty="externalId">
			<bean:message key="label.phd.public.candidacy.createCandidacy.edit.guidings" bundle="PHD_RESOURCES"/>
		</html:link>
	</p>
</logic:equal>

<%--  ### Academic Degrees ### --%>
<h2 style="margin-top: 1.5em;"><bean:message key="title.public.phd.qualifications" bundle="PHD_RESOURCES"/></h2>
<logic:notEmpty name="individualProgramProcess" property="qualifications">
	<logic:iterate id="qualification" name="individualProgramProcess" property="qualificationsSortedByAttendedEndDate" indexId="index" >
		<p class="mtop1 mbottom05"><strong><%= index.intValue() + 1 %>.</strong></p>
		<fr:view name="qualification" schema="Phd.Qualification.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="thlight thleft"/>
		        <fr:property name="columnClasses" value="width175px,,,,"/>
			</fr:layout>
		</fr:view>
	</logic:iterate>
</logic:notEmpty>
<logic:empty name="individualProgramProcess" property="qualifications">
	<p class="mvert05"><em><bean:message key="label.not.defined" bundle="PHD_RESOURCES"/>.</em></p>
</logic:empty>
<logic:equal name="canEditCandidacy" value="true">
	<p class="mvert05">
		<html:link action="/applications/phd/phdProgramApplicationProcess.do?method=prepareEditQualifications" paramId="processId" paramName="process" paramProperty="externalId">
			<bean:message key="label.phd.public.candidacy.createCandidacy.edit.qualifications" bundle="PHD_RESOURCES"/>
		</html:link>
	</p>
</logic:equal>

<%--  ### Referees ### --%>
<h2 style="margin-top: 1.5em;"><bean:message key="title.public.phd.reference.letters.authors" bundle="PHD_RESOURCES"/> <span style="font-weight: normal; font-size: 13px; color: #777;">(<bean:message key="title.public.phd.referees" bundle="PHD_RESOURCES"/>)</span></h2>
<logic:notEmpty name="individualProgramProcess" property="phdCandidacyReferees">
	<logic:iterate id="candidacyReferee" name="individualProgramProcess" property="phdCandidacyReferees" indexId="index" >
		<p class="mtop1 mbottom05"><strong><%= index.intValue() + 1 %>.</strong></p>
		<fr:view name="candidacyReferee" schema="PhdCandidacyReferee.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="thlight thleft"/>
		        <fr:property name="columnClasses" value="width175px,,,,"/>
			</fr:layout>
		</fr:view>
	</logic:iterate>
</logic:notEmpty>
<logic:empty name="individualProgramProcess" property="phdCandidacyReferees">
	<p class="mvert05"><em><bean:message key="label.not.defined" bundle="PHD_RESOURCES"/>.</em></p>
</logic:empty>
<logic:equal name="canEditCandidacy" value="true">
	<p class="mvert05">
		<html:link action="/applications/phd/phdProgramApplicationProcess.do?method=prepareEditReferees" paramId="processId" paramName="process" paramProperty="externalId">
			<bean:message key="label.phd.public.candidacy.createCandidacy.manage.referees" bundle="PHD_RESOURCES"/>
		</html:link>
	</p>
</logic:equal>

<%--  ### Documents ### --%>
<h2 style="margin-top: 1.5em;"><bean:message key="title.public.phd.documents" bundle="PHD_RESOURCES"/></h2>
<logic:notEmpty name="individualProgramProcess" property="candidacyProcessDocuments">
	<fr:view name="individualProgramProcess" property="candidacyProcess.latestDocumentVersions" schema="Public.PhdProgramProcessDocument.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thcenter mbottom0"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="individualProgramProcess" property="candidacyProcessDocuments">
	<p class="mvert05"><em><bean:message key="label.not.defined" bundle="PHD_RESOURCES"/>.</em></p>
</logic:empty>
<logic:equal name="canEditCandidacy" value="true">
	<p class="mvert05">
		<html:link action="/applications/phd/phdProgramApplicationProcess.do?method=prepareUploadDocuments" paramId="processId" paramName="process" paramProperty="externalId">
			<bean:message key="label.phd.public.candidacy.createCandidacy.updloadDocuments" bundle="PHD_RESOURCES"/>
		</html:link>
	</p>
</logic:equal>
