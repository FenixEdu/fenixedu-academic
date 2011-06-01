


<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="pt.utl.ist.fenix.tools.util.i18n.Language"%>
<%@ page import="java.util.Locale"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>

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
.warning0 {
background-color: #fbf8cc;
padding: 0.5em 1em;
}
</style>

<bean:define id="individualProgramProcess" name="process" property="individualProgramProcess" />

<%--  ### Validation messages ### --%>
<logic:messagesPresent message="true" property="validation">
	<div class="infoop2 mtop1" style="margin-bottom: 25px;">
		<p class="mvert05"><bean:message key="message.phd.public.candidacy.requirements" bundle="PHD_RESOURCES" />:</p>
		<ul class="mvert05">
			<html:messages id="messages" message="true" bundle="PHD_RESOURCES" property="validation">
				<li><bean:write name="messages" /></li>
			</html:messages>
		</ul>
	</div>
</logic:messagesPresent>


<logic:messagesNotPresent message="true" property="validation">
	<logic:equal name="canEditCandidacy" value="true">
		<div class="warning0 mvert1">
			<p class="mvert05">
				<bean:message key="message.phd.public.candidacy.ready.to.validate" bundle="PHD_RESOURCES" />
				<p><strong>
					<html:link action="/applications/phd/phdProgramApplicationProcess.do?method=prepareValidateApplication" paramId="processId" paramName="process" paramProperty="externalId">
						<bean:message key="label.phd.public.candidacy.validate" bundle="PHD_RESOURCES"/> »
					</html:link>
				</strong></p>
			</p>
		</div>
	</logic:equal>
	<logic:equal name="canEditCandidacy" value="false">
		<div class="warning0 mvert1"><p class="mvert05"><bean:message key="message.phd.public.candidacy.already.submitted" bundle="PHD_RESOURCES" /></p></div>
	</logic:equal>
</logic:messagesNotPresent>





<%--  ### Personal Information ### --%>
<h2 style="margin-top: 1.5em;"><bean:message key="title.public.phd.personal.data" bundle="PHD_RESOURCES"/></h2>
<logic:equal name="canEditPersonalInformation" value="true">
	<fr:view name="individualProgramProcess" property="person" schema="Public.PhdIndividualProgramProcess.view.person">
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
		<bean:define id="photoCode" name="individualProgramProcess" property="person.personalPhotoEvenIfPending.idInternal" />
		<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrievePendingByID&amp;photoCode=" + photoCode.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
	</div>
</logic:notEmpty>

<%--  ### Candidacy Information ### --%>
<h2 style="margin-top: 1.5em;"><bean:message key="label.phd.public.candidacy.createCandidacy.fillCandidacyInformation" bundle="PHD_RESOURCES"/></h2>
<fr:view name="individualProgramProcess">
	<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess">
		<fr:slot name="candidacyDate">
			<fr:property name="classes" value="bold nowrap"/>
		</fr:slot>
		<fr:slot name="phdProgram" layout="null-as-label">
			<fr:property name="subLayout" value="values" />
			<fr:property name="subSchema" value="PhdProgram.name" />
		</fr:slot>
		<fr:slot name="thesisTitle" key="label.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.thesis.title.proposed" />
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
<h2 style="margin-top: 1.5em;"><bean:message key="title.phd.public.candidacy.payment.details" bundle="PHD_RESOURCES"/> </h2>

<logic:notEmpty name="process" property="associatedPaymentCode">
<p> <bean:message key="message.phd.institution.application.sibs.payment.details" bundle="PHD_RESOURCES" /></p>
<table>
	<tr>
		<td><strong><bean:message key="label.sibs.entity.code" bundle="CANDIDATE_RESOURCES"/>:</strong></td>
		<td><bean:write name="sibsEntityCode"/></td>
	</tr>
	<tr>
		<td><strong><bean:message key="label.sibs.payment.code" bundle="CANDIDATE_RESOURCES"/>:</strong></td>
		<td><fr:view name="process" property="associatedPaymentCode.formattedCode"/></td>
	</tr>
	<tr>
		<td><strong><bean:message key="label.sibs.amount" bundle="CANDIDATE_RESOURCES"/>:</strong></td>
		<td><fr:view name="process" property="associatedPaymentCode.minAmount"/> &euro;</td>
	</tr>
</table>
</logic:notEmpty>

<%--  ### Phd Supervisors ### --%>
<h2 style="margin-top: 1.5em;"><bean:message key="title.public.phd.guidings" bundle="PHD_RESOURCES"/> <span style="font-weight: normal; font-size: 13px; color: #777;">(<bean:message key="title.public.phd.if.applicable" bundle="PHD_RESOURCES"/>)</span></h2>
<logic:notEmpty name="individualProgramProcess" property="guidings">
	<logic:iterate id="guiding" name="individualProgramProcess" property="guidings" indexId="index" >
		<p class="mtop1 mbottom1"><strong><bean:message bundle="PHD_RESOURCES" key="label.author"/> <%= index.intValue() + 1 %></strong></p>
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
	<fr:view name="individualProgramProcess" property="candidacyProcessDocuments" schema="Public.PhdProgramProcessDocument.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thcenter"/>
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
