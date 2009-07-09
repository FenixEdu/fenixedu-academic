<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<a href="http://www.ist.utl.pt">IST</a> &gt;
	<a href="http://www.ist.utl.pt/en/about-IST/global-cooperation/IST-EPFL/">IST-EPFL</a> &gt;
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.public.candidacy" bundle="PHD_RESOURCES" /></h1>
<h2><bean:message key="label.phd.public.submit.candidacy" bundle="PHD_RESOURCES" /></h2>

<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<fr:form id="validateCandidacyForm" action="/candidacies/phdProgramCandidacyProcess.do">
	<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />
	<input type="hidden" id="methodForm" name="method" value="validateCandidacy" />
	
	<a href="#" onclick="javascript:document.getElementById('methodForm').value='backToViewCandidacy';javascript:document.getElementById('validateCandidacyForm').submit();">« <bean:message bundle="PHD_RESOURCES" key="label.back"/></a>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<logic:equal name="canEditCandidacy" value="true">

	<%--  ### Error Messages  ### --%>
	<jsp:include page="/phd/errorsAndMessages.jsp" />
	<%--  ### End of Error Messages  ### --%>
	
	<logic:messagesPresent message="true" property="validation">
		<div class="warning1 mbottom05" style="width: 700px;">
			<html:messages id="messages" message="true" bundle="PHD_RESOURCES" property="validation">
				<p class="mvert025"><bean:write name="messages" /></p>
			</html:messages>
		</div>
	</logic:messagesPresent>
	
	<p>
		After you validate your application you can not change information.
		Do you want to submit?
	</p>
	
	<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.phd.public.submit.candidacy"/></html:submit></p>
</logic:equal>
</fr:form>
