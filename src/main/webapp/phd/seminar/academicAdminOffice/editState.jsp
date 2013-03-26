<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.edit.state" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<bean:define id="processId" name="process" property="externalId" />

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<%--
<div class="breadcumbs">
	<span class="actual">Step 1: Step Name</span> > 
	<span>Step N: Step name </span>
</div>
--%>
<html:link action="<%= String.format("/publicPresentationSeminarProcess.do?method=manageStates&processId=%s", processId) %>" >
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>


<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=" />
<%--  ### End of Error Messages  ### --%>

<bean:define id="stateId" name="bean" property="state.externalId" />
<bean:define id="processId" name="bean" property="phdProcess.externalId" />

<fr:form action="<%= String.format("/publicPresentationSeminarProcess.do?method=editState&processId=%s&stateId=%s", processId, stateId) %>" >
	<fr:edit id="bean" name="bean" visible="false" />
	
	<fr:edit id="bean-edit" name="bean">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.phd.PhdProcessStateBean" bundle="PHD_RESOURCES" >
			<fr:slot name="stateDate" required="true">
				<fr:validator name="pt.ist.fenixWebFramework.rendererExtensions.validators.DateTimeValidator" />
			</fr:slot>
		</fr:schema>
		
		<fr:layout name="tabular" >
			<fr:property name="columnClasses" value=",,tderror1" />
		</fr:layout>
		
		<fr:destination name="invalid" path = "<%= String.format("/publicPresentationSeminarProcess.do?method=editStateInvalid&processId=%s&stateId=%s", processId, stateId) %>"/>
		<fr:destination name="cancel" path = "<%= String.format("/publicPresentationSeminarProcess.do?method=manageStates&processId=%s", processId) %>"/>
	</fr:edit>
	
	<p>
		<html:submit><bean:message key="button.submit" bundle="APPLICATION_RESOURCES" /></html:submit>
		<html:cancel><bean:message key="button.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</p>
</fr:form> 
