<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>



<h2><bean:message key="label.documentRequestsManagement.editDocumentRequest" /></h2>

<hr><br/>

<logic:messagesPresent message="true">
	<ul>
		<html:messages id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
	<br />
</logic:messagesPresent>

<strong><bean:message  key="label.documentRequestManagement.documentRequestInformation"/></strong>
<bean:define id="documentRequest" name="documentRequestEditBean" property="documentRequest"/>
<bean:define id="simpleClassName" name="documentRequest" property="class.simpleName" />
<fr:view name="documentRequestEditBean" property="documentRequest" schema="<%= simpleClassName  + ".view"%>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4" />
		<fr:property name="columnClasses" value="listClasses,," />
	</fr:layout>
</fr:view>

<br/><br/>
<bean:define id="url" name="url" />
<logic:equal name="documentRequest" property="editable" value="true">
<fr:edit id="documentRequestEdit" name="documentRequestEditBean" 
		schema="DocumentRequestEditBean"
		action="/documentRequestsManagement.do?method=editDocumentRequest">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4" />
		<fr:property name="columnClasses" value="listClasses,," />
	</fr:layout>
	<fr:destination name="cancel" path="<%="/documentRequestsManagement.do?method=search" + url%>"/>
</fr:edit>
</logic:equal>
<logic:notEqual name="documentRequest" property="editable" value="true">
	<strong><bean:message  key="label.documentRequestManagement.documentRequestSituation"/></strong>
	<fr:view name="documentRequest" property="activeSituation" schema="AcademicServiceRequestSituation.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
			<fr:property name="columnClasses" value="listClasses,," />
		</fr:layout>
	</fr:view>
	<br/><br/>
	<span class="success0"><bean:message key="label.documentRequestsManagement.documentRequestNotModifiable"/></span>
	
	<br/><br/>
	<html:form action="<%="/documentRequestsManagement.do?method=search" + url%>">
		<bean:define id="documentRequestId" name="documentRequest" property="idInternal" />
		<html:hidden property="documentRequestId" value="<%= documentRequestId.toString() %>"/>
		
		<html:submit styleClass="inputbutton"><bean:message key="label.documentRequestsManagement.back" /></html:submit>
	</html:form>
</logic:notEqual>