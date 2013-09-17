<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<em><bean:message key="administrative.office.services" bundle="STUDENT_RESOURCES"/></em>
<h2><bean:message key="documents.requirement.consult" bundle="STUDENT_RESOURCES"/></h2>

<logic:messagesPresent message="true">
	<p>
	<span class="error0"><!-- Error messages go here -->
		<html:messages id="message" message="true" bundle="STUDENT_RESOURCES">
			<bean:write name="message"/>
		</html:messages>
	</span>
	</p>
</logic:messagesPresent>

<bean:define id="simpleClassName" name="documentRequest" property="class.simpleName" />
<fr:view name="documentRequest" schema="<%= simpleClassName  + ".view"%>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thlight thright" />
		<fr:property name="rowClasses" value=",,,,,,"/>
	</fr:layout>
</fr:view>

<logic:present name="documentRequest" property="activeSituation">
	<p class="mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="request.situation"/></strong></p>
	<fr:view name="documentRequest" property="activeSituation" schema="AcademicServiceRequestSituation.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thright thlight mtop025"/>
		<fr:property name="rowClasses" value=",,tdhl1,"/>
		</fr:layout>
	</fr:view>
</logic:present>

<html:form action="/documentRequest.do?method=viewDocumentRequests">
	<p>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.registrationId" property="registrationId"/>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.back" /></html:submit>
	</p>
</html:form>