<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<jsp:include page="/phd/candidacy/publicProgram/institution/commonBreadcumbs.jsp" />
	
	<bean:message key="label.phd.public.submit.candidacy" bundle="PHD_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.institution.public.candidacy" bundle="PHD_RESOURCES" /></h1>

<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<bean:define id="hash" name="process" property="candidacyHashCode.value" />

<p>
	<html:link action="/applications/phd/phdProgramApplicationProcess.do?method=viewApplication" paramId="hash" paramName="hash">
		« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
	</html:link>
</p>

<h2 style="margin-top: 1em;"><bean:message key="label.phd.public.submit.candidacy" bundle="PHD_RESOURCES"/> </h2>

<bean:define id="processId" name="process" property="externalId" />

<fr:form id="validateCandidacyForm" action="<%= String.format("/applications/phd/phdProgramApplicationProcess.do?processId=%s&amp;hash=%s", processId, hash) --%>">


<logic:equal name="canEditCandidacy" value="true">
	<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />
	<input type="hidden" id="methodForm" name="method" value="validateApplication" />

	<%--  ### Error Messages  ### --%>
	<jsp:include page="/phd/errorsAndMessages.jsp" />
	<%--  ### End of Error Messages  ### --%>
	

	<logic:messagesPresent message="true" property="validation">
		<div class="infoop2 mvert1">
			<p class="mvert05"><bean:message key="message.phd.institution.public.candidacy.validation.preconditions" bundle="PHD_RESOURCES" /></p>
			<ul class="mvert05">
				<html:messages id="messages" message="true" bundle="PHD_RESOURCES" property="validation">
					<li><bean:write name="messages" /></li>
				</html:messages>
			</ul>
		</div>
	</logic:messagesPresent>
	
	<logic:messagesNotPresent message="true" property="validation">
		
		<p><bean:message key="message.phd.institution.public.candidacy.validation.confirmation" bundle="PHD_RESOURCES" /></p>
	</logic:messagesNotPresent>

	<p>
		<logic:messagesPresent message="true" property="validation">
			<html:submit onclick="javascript:document.getElementById('methodForm').value='viewApplication';javascript:document.getElementById('validateCandidacyForm').submit();" bundle="HTMLALT_RESOURCES" altKey="submit.submit">« <bean:message bundle="PHD_RESOURCES" key="label.back"/></html:submit>
		</logic:messagesPresent>
		<logic:messagesNotPresent message="true" property="validation">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.phd.public.submit.candidacy"/></html:submit>
			<html:submit onclick="javascript:document.getElementById('methodForm').value='viewApplication';javascript:document.getElementById('validateCandidacyForm').submit();" bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.cancel"/></html:submit>
		</logic:messagesNotPresent>
	</p>
</logic:equal>
</fr:form>
	