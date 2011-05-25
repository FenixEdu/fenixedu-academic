<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<jsp:include page="/phd/candidacy/publicProgram/institution/commonBreadcumbs.jsp" />

	<bean:message key="title.phd.candidacy.referee.letter" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.institution.public.candidacy" bundle="PHD_RESOURCES" /></h1>

<h2><bean:message key="title.phd.candidacy.referee.letter" bundle="PHD_RESOURCES" /></h2>

<%-- ### End of Title ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=createRefereeLetterBean.overall.promise" />
<%--  ### End of Error Messages  ### --%>

<logic:present name="createRefereeLetterBean">

<p class="mbottom05"><strong><bean:message key="label.phd.institution.public.candidacy.applicant.name" bundle="PHD_RESOURCES" />: </strong><bean:write name="createRefereeLetterBean" property="person.name" /></p>

<bean:define id="doctoralProgramme" name="refereeLetterHash" property="phdProgramCandidacyProcess.individualProgramProcess.phdProgram.name.content" type="String"/>

<bean:message key="message.phd.institution.public.candidacy.fill.referee.letter" bundle="PHD_RESOURCES" arg0="<%= doctoralProgramme %>"/>

<bean:define id="hash" name="refereeLetterHash" property="value" />
<div class="fs_form">	

	<fr:form id="refereeForm" action="<%=  "/applications/phd/phdProgramApplicationProcess.do?method=createRefereeLetter&hash=" + hash %>" encoding="multipart/form-data">
		<fr:edit id="createRefereeLetterBean" name="createRefereeLetterBean" visible="false" />

		<fieldset style="display: block;">
			<legend><bean:message key="label.phd.institution.public.candidacy.referee.form" bundle="PHD_RESOURCES" /></legend>
			<p class="mtop05"><span><bean:message key="message.mandatory.fields" bundle="PHD_RESOURCES"/></span></p>
		
			<fr:edit id="Public.PhdCandidacyRefereeLetterBean.applicant.information" name="createRefereeLetterBean" 
				schema="Public.PhdCandidacyRefereeLetterBean.applicant.information">
				<fr:layout name="tabular">
						<fr:property name="classes" value="thlight thleft thtop mtop05"/>
				        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
						<fr:property name="requiredMarkShown" value="true" />
				</fr:layout>
				<fr:destination name="invalid" path="<%= "/applications/phd/phdProgramApplicationProcess.do?method=createRefereeLetterInvalid&hash=" + hash %>"/>
			</fr:edit>
			
			<br/>
			<p class="mvert05"><bean:message key="message.phd.institution.public.candidacy.fill.referee.overall.promise" bundle="PHD_RESOURCES" /></p>
			<fr:edit id="createRefereeLetterBean.overall.promise" name="createRefereeLetterBean" 
				schema="Public.PhdCandidacyRefereeLetterBean.overall.promise">
				<fr:layout name="tabular">
						<fr:property name="classes" value="thlight thleft thtop mtop05"/>
				        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
						<fr:property name="requiredMarkShown" value="true" />
				</fr:layout>
				<fr:destination name="invalid" path="<%= "/applications/phd/phdProgramApplicationProcess.do?method=createRefereeLetterInvalid&hash=" + hash %>" />
			</fr:edit>
			
			<br/>
			<fr:edit id="createRefereeLetterBean.comments" name="createRefereeLetterBean" 
				schema="Public.PhdCandidacyRefereeLetterBean.comments">
				<fr:layout name="tabular">
						<fr:property name="classes" value="thlight thleft thtop mtop05"/>
				        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
						<fr:property name="requiredMarkShown" value="true" />
				</fr:layout>
			
				<fr:destination name="invalid" path="<%= "/applications/phd/phdProgramApplicationProcess.do?method=createRefereeLetterInvalid&hash=" + hash %>" />
			</fr:edit>
		
			<br/>
			<p class="mvert05"><bean:message key="message.phd.institution.public.candidacy.fill.referee.personal.data" bundle="PHD_RESOURCES" />: </p>
			<fr:edit id="createRefereeLetterBean.referee.information" name="createRefereeLetterBean" 
				schema="Public.PhdCandidacyRefereeLetterBean.referee.information">
				<fr:layout name="tabular">
						<fr:property name="classes" value="thlight thleft thtop mtop05"/>
				        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
						<fr:property name="requiredMarkShown" value="true" />
				</fr:layout>
			
				<fr:destination name="invalid" path="<%= "/applications/phd/phdProgramApplicationProcess.do?method=createRefereeLetterInvalid&hash=" + hash %>" />
			</fr:edit>
		</fieldset>
		
		<p>
			<bean:message key="message.phd.institution.public.candidacy.fill.referee.submit.only.once" bundle="PHD_RESOURCES" />
		</p>
		
		<html:submit ><bean:message bundle="PHD_RESOURCES" key="label.submit"/></html:submit>
	</fr:form>
</div>

</logic:present>
