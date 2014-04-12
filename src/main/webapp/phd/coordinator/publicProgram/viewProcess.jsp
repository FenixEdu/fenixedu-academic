<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

<html:xhtml/>

<logic:present role="role(COORDINATOR)">

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.viewProcess" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>


<html:link action="/candidacies/phdProgramCandidacyProcess.do?method=listProcesses">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>

<br/><br/>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<logic:empty name="hashCode" property="phdProgramCandidacyProcess">
	<bean:message key="label.not.defined" bundle="PHD_RESOURCES" />
</logic:empty>


<logic:notEmpty name="hashCode" property="phdProgramCandidacyProcess">

	<%--  ### Personal Information ### --%>
	<h2 style="margin-top: 1em;"><bean:message key="title.public.phd.personal.data" bundle="PHD_RESOURCES"/></h2>
	
	<fr:view name="hashCode" property="person" schema="Public.PhdIndividualProgramProcess.view.person">
		<fr:layout name="tabular">
			<fr:property name="classes" value="thlight thleft"/>
	        <fr:property name="columnClasses" value="width175px,,,,"/>
		</fr:layout>
	</fr:view>
	
	<%--  ### Photo ### --%>
	<logic:notEmpty name="hashCode" property="person.personalPhotoEvenIfPending">
		<h2 style="margin-top: 1em;"><bean:message key="label.photo" bundle="PHD_RESOURCES"/></h2>
		
		<div>
			<bean:define id="photoCode" name="hashCode" property="person.personalPhotoEvenIfPending.externalId" />
			<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrievePendingByID&amp;photoCode=" + photoCode.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
		</div>
	</logic:notEmpty>
	
	<bean:define id="individualProgramProcess" name="hashCode" property="individualProgramProcess" />
	<bean:define id="hashCodeId" name="hashCode" property="externalId" />

	<%--  ### Candidacy Information ### --%>
	<h2 style="margin-top: 1em;"><bean:message key="label.phd.public.candidacy.createCandidacy.fillCandidacyInformation" bundle="PHD_RESOURCES"/></h2>
	
	<fr:view name="individualProgramProcess" schema="Public.PhdIndividualProgramProcess.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="thlight thleft"/>
		       <fr:property name="columnClasses" value="width175px,,,,"/>
		</fr:layout>
	</fr:view>

	<%--  ### Thesis Subjects ### --%>
	<h2 style="margin-top: 1em;"><bean:message key="label.phd.thesisSubjects" bundle="PHD_RESOURCES"/></h2>
	
	<logic:notEmpty name="individualProgramProcess" property="thesisSubjectOrdersSorted">
		<fr:view name="individualProgramProcess" property="thesisSubjectOrdersSorted">
			<fr:schema type="net.sourceforge.fenixedu.domain.phd.ThesisSubjectOrder" bundle="PHD_RESOURCES">
				<fr:slot name="subjectOrder" key="label.order"/>
				<fr:slot name="thesisSubject.name" key="label.phd.name"/>
				<fr:slot name="thesisSubject.description" key="label.phd.description"/>
				<fr:slot name="thesisSubject.teacher.person.name" key="label.phd.guiding"/>
				<fr:slot name="thesisSubject.externalAdvisorName" key="label.phd.guiding.external"/>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="thlight thleft"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
	<logic:empty name="individualProgramProcess" property="thesisSubjectOrdersSorted">
		<p class="mvert05"><em><bean:message key="label.not.defined" bundle="PHD_RESOURCES"/></em></p>
	</logic:empty>
	
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
		
			<logic:equal name="candidacyReferee" property="letterAvailable" value="true">
				<html:link action="/candidacies/phdProgramCandidacyProcess.do?method=viewCandidacyRefereeLetter" paramId="candidacyRefereeId" paramName="candidacyReferee" paramProperty="externalId">
					<bean:message bundle="PHD_RESOURCES" key="label.view"/>
				</html:link>
			</logic:equal>
			<logic:equal name="candidacyReferee" property="letterAvailable" value="false">
				<html:link action="<%= "/candidacies/phdProgramCandidacyProcess.do?method=sendCandidacyRefereeEmail&hashCodeId=" + hashCodeId %>" paramId="candidacyRefereeId" paramName="candidacyReferee" paramProperty="externalId">
					<bean:message key="label.resend.email" bundle="PHD_RESOURCES"/>
				</html:link>
			</logic:equal>		
		</logic:iterate>
		
	</logic:notEmpty>
	
	<logic:empty name="individualProgramProcess" property="phdCandidacyReferees">
		<p class="mvert05"><em><bean:message key="label.not.defined" bundle="PHD_RESOURCES"/></em></p>
	</logic:empty>

	<%--  ### Documents ### --%>
	<h2 style="margin-top: 1em;"><bean:message key="title.public.phd.documents" bundle="PHD_RESOURCES"/></h2>
	<html:link action="/candidacies//phdProgramCandidacyProcess.do?method=downloadCandidacyDocuments" paramId="processId" paramName="hashCode" paramProperty="phdProgramCandidacyProcess.externalId">
		<bean:message key="label.phd.documents.download.all" bundle="PHD_RESOURCES" />
	</html:link>
	<logic:notEmpty name="individualProgramProcess" property="candidacyProcessDocuments">
		<fr:view name="individualProgramProcess" property="candidacyProcessDocuments" schema="Public.PhdProgramProcessDocument.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight thcenter"/>

				<fr:property name="linkFormat(view)" value="${downloadUrl}"/>
				<fr:property name="key(view)" value="label.view"/>
				<fr:property name="bundle(view)" value="PHD_RESOURCES"/>
				<fr:property name="order(view)" value="0" />
				<fr:property name="hasContext(view)" value="false" />
				<fr:property name="contextRelative(view)" value="false" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="individualProgramProcess" property="candidacyProcessDocuments">
		<p class="mvert05"><em><bean:message key="label.not.defined" bundle="PHD_RESOURCES"/></em></p>
	</logic:empty>

</logic:notEmpty>

</logic:present>