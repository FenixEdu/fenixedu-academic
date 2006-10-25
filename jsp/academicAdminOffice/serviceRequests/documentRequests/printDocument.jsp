<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="document.print" /></h2>

<hr/><br/>

<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.documentRequestsManagement.documentRequestInformation"/></strong>
<bean:define id="simpleClassName" name="documentRequest" property="class.simpleName" />
<fr:view name="documentRequest" schema="<%= simpleClassName  + ".view"%>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4" />
		<fr:property name="columnClasses" value="listClasses,," />
	</fr:layout>
</fr:view>

<logic:messagesPresent message="true">
	<html:messages id="messages" message="true">
		<p><span class="error0"><bean:write name="messages" bundle="ACADEMIC_OFFICE_RESOURCES"/></span></p>
	</html:messages>
</logic:messagesPresent>

<logic:messagesNotPresent message="true">
	<p>
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="documentRequest.confirmDocumentSuccessfulPrinting"/>
		<html:form action="/documentRequestsManagement.do">	
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="concludeDocumentRequest"/>
			<bean:define id="documentRequestId" type="java.lang.Integer" name="documentRequest" property="idInternal"/>
		</html:form>
	</p>
</logic:messagesNotPresent>

<p>
	<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="documentRequest" paramProperty="registration.idInternal">
		<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</html:link>
</p>
