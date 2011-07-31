<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<jsp:include page="/phd/candidacy/publicProgram/institution/commonBreadcumbs.jsp" />
	<bean:message key="title.edit.candidacy.referees" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.institution.public.candidacy" bundle="PHD_RESOURCES" /></h1>

<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<bean:define id="processId" name="process" property="externalId" />
<bean:define id="hash" name="process" property="candidacyHashCode.value" />	

<p>
	<html:link action="/applications/phd/phdProgramApplicationProcess.do?method=viewApplication" paramId="hash" paramName="hash">
		« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
	</html:link>	
</p>

<h2 style="margin-top: 1em;"><bean:message key="title.public.phd.reference.letters.authors" bundle="PHD_RESOURCES"/> <span style="font-weight: normal; font-size: 13px; color: #777;">(<bean:message key="title.public.phd.referees" bundle="PHD_RESOURCES"/>)</span></h2>

<fr:form id="editCandidacyRefereeForm" action="<%= "/applications/phd/phdProgramApplicationProcess.do?method=addReferee&processId=" + processId %>">
	<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />
	
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>


<logic:equal name="canEditCandidacy" value="true">

	<%--  ### Error Messages  ### --%>
	<jsp:include page="/phd/errorsAndMessages.jsp" />
	<%--  ### End of Error Messages  ### --%>
	
	<logic:notPresent name="candidacyBean">
		<p><em><bean:message key="label.php.public.candidacy.hash.not.found" bundle="PHD_RESOURCES"/></em></p>
	</logic:notPresent>
	
	<logic:present name="candidacyBean">
	
		<p><bean:message key="message.phd.candidacy.add.referee.email.will.be.sent" bundle="PHD_RESOURCES" /></p>
	
		<logic:notEmpty name="refereeBean">
			<div class="fs_form">
				<fieldset style="display: block;">
					<legend><bean:message key="label.public.phd.add.referee" bundle="PHD_RESOURCES"/></legend>
								
					<fr:edit id="refereeBean" name="refereeBean" schema="Public.PhdProgramCandidacyProcess.referee">
						<fr:layout name="tabular-editable">
							<fr:property name="classes" value="thlight thleft"/>
							<fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
							<fr:property name="optionalMarkShown" value="true" />
						</fr:layout>
						
						<fr:destination name="invalid" path="<%= "/applications/phd/phdProgramApplicationProcess.do?method=editRefereesInvalid&processId=" + processId %>" />
					</fr:edit>
				</fieldset>
			</div>
			<p><html:submit><bean:message bundle="PHD_RESOURCES" key="label.public.phd.add.referee"/></html:submit></p>
		</logic:notEmpty>
		
		<logic:notEmpty name="process" property="individualProgramProcess.phdCandidacyReferees">
			<logic:iterate id="candidacyReferee" name="process" property="individualProgramProcess.phdCandidacyReferees" indexId="index" >
				<p class="mtop2 mbottom1"><strong><bean:message bundle="PHD_RESOURCES" key="label.author"/> <%= index.intValue() + 1 %></strong></p>
				<bean:define id="candidacyRefereeId" name="candidacyReferee" property="externalId" />
				<fr:view name="candidacyReferee">
					<fr:schema type="net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyReferee" bundle="PHD_RESOURCES">
						<fr:slot name="name" />
						<fr:slot name="email" />
						<fr:slot name="institution" />
						<fr:slot name="letterAvailable" />
						<%-- 
						<fr:slot name="refereeSubmissionFormLinkPt" layout="text-link"/>
						<fr:slot name="refereeSubmissionFormLinkEn" layout="text-link"/>
						--%>
					</fr:schema>				
					<fr:layout name="tabular">
						<fr:property name="classes" value="thlight thleft"/>
				        <fr:property name="columnClasses" value=",,,,"/>
					</fr:layout>
				</fr:view>
				<logic:equal name="candidacyReferee" property="letterAvailable" value="false">
					<p class="mtop05">
						<html:link action="<%= "/applications/phd/phdProgramApplicationProcess.do?method=sendCandidacyRefereeEmail&processId=" + processId  + "&candidacyRefereeId=" + candidacyRefereeId %>" >
							<bean:message key="label.resend.email" bundle="PHD_RESOURCES"/>
						</html:link> | 
						<html:link action="<%= "/applications/phd/phdProgramApplicationProcess.do?method=removeReferee&processId=" + processId + "&candidacyRefereeId=" + candidacyRefereeId %>" >
							<bean:message key="label.remove" bundle="PHD_RESOURCES"/>
						</html:link>
					</p>
				</logic:equal>
			</logic:iterate>
		</logic:notEmpty>
		 
	</logic:present>
</logic:equal>
</fr:form>
