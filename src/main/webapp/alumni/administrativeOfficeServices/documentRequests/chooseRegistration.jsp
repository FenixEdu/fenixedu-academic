<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<logic:present role="ALUMNI">

	<em><bean:message key="administrative.office.services" bundle="STUDENT_RESOURCES"/></em>
	<h2><bean:message key="documents.requirement" bundle="STUDENT_RESOURCES"/></h2>

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
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareCreateDocumentRequest" />

		<p class="mtop2">
			<bean:message key="label.registration" bundle="STUDENT_RESOURCES"/>: <html:select property="registrationId">
			<html:options collection="registrations" property="externalId"
				labelProperty="degreeNameWithDegreeCurricularPlanName" />
			</html:select>
		</p>

		<p class="mtop2">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
				<bean:message key="button.continue" />
			</html:submit>
		</p>
	</html:form>
</logic:present>

