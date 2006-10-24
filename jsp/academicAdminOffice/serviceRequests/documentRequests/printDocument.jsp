<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="document.printed" /></h2>

<hr/><br/>

<logic:messagesPresent message="true">
	<ul>
		<html:messages id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
	<br />
</logic:messagesPresent>

<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.documentRequestsManagement.documentRequestInformation"/></strong>
<bean:define id="simpleClassName" name="documentRequest" property="class.simpleName" />
<fr:view name="documentRequest" schema="<%= simpleClassName  + ".view"%>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4" />
		<fr:property name="columnClasses" value="listClasses,," />
	</fr:layout>
</fr:view>

<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="documentRequest.confirmDocumentSuccessfulPrinting"/>
<html:form action="/documentRequestsManagement.do">	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="concludeDocumentRequest"/>
	<bean:define id="documentRequestId" type="java.lang.Integer" name="documentRequest" property="idInternal"/>

	
</html:form>
