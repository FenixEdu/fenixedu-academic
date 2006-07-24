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

<bean:define id="simpleClassName" name="documentRequest" property="class.simpleName" />
<fr:view name="documentRequest" schema="<%= simpleClassName  + ".view"%>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4" />
		<fr:property name="columnClasses" value="listClasses,," />
	</fr:layout>
</fr:view>

<html:form action="/documentRequestsManagement.do">
	<html:hidden property="method"/>
	<html:hidden property="documentRequestType" />
	<html:hidden property="requestSituationType"  />
	<html:hidden property="isUrgent" />
	<html:hidden property="studentNumber"  />
	<bean:define id="documentRequestId" name="documentRequest" property="idInternal" />
	<html:hidden property="documentRequestId" value="<%= documentRequestId.toString() %>"/>
	
	<html:submit onclick="this.form.method.value='editDocumentRequest';"><bean:message key="label.documentRequestsManagement.back" /></html:submit>
	<html:submit onclick="this.form.method.value='search';"><bean:message key="label.documentRequestsManagement.back" /></html:submit>
</html:form>
