<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>



<h2><bean:message key="label.documentRequestsManagement.viewDocumentRequest" /></h2>

<hr><br/>

<logic:messagesPresent message="true">
	<ul>
		<html:messages id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
	<br />
</logic:messagesPresent>


<strong><bean:message  key="label.documentRequestsManagement.documentRequestInformation"/></strong>
<bean:define id="simpleClassName" name="documentRequest" property="class.simpleName" />
<fr:view name="documentRequest" schema="<%= simpleClassName  + ".view"%>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4" />
		<fr:property name="columnClasses" value="listClasses,," />
	</fr:layout>
</fr:view>

<br/><br/>

<strong><bean:message  key="label.documentRequestsManagement.documentRequestSituation"/></strong>
<fr:view name="documentRequest" property="activeSituation" schema="AcademicServiceRequestSituation.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4" />
		<fr:property name="columnClasses" value="listClasses,," />
	</fr:layout>
</fr:view>

<bean:define id="url" name="url" />
<html:form action="<%="/documentRequestsManagement.do?method=search" + url%>">
	<bean:define id="documentRequestId" name="documentRequest" property="idInternal" />
	<html:hidden property="documentRequestId" value="<%= documentRequestId.toString() %>"/>
	
	<html:submit styleClass="inputbutton"><bean:message key="label.documentRequestsManagement.back" /></html:submit>
</html:form>
