<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="STUDENT">

	<em><bean:message key="administrative.office.services" /></em>
	<h2><bean:message key="documents.requirement" /></h2>

	<logic:messagesPresent message="true">
		<p>
			<span class="error0"><!-- Error messages go here -->
			<html:messages id="message" message="true"
				bundle="STUDENT_RESOURCES">
				<bean:write name="message" />
			</html:messages>
			</span>
		</p>
	</logic:messagesPresent>



	<html:form action="/documentRequest.do">
		<html:hidden property="method" value="prepareCreateDocumentRequest" />

		<p class="mtop2">
			<bean:message key="label.registration" />: <html:select property="registrationId">
			<html:options collection="registrations" property="idInternal"
				labelProperty="lastStudentCurricularPlan.degreeCurricularPlan.presentationName" />
			</html:select>
		</p>

		<p class="mtop2">
			<html:submit styleClass="inputbutton">
				<bean:message key="button.continue" />
			</html:submit>
		</p>
	</html:form>
</logic:present>

