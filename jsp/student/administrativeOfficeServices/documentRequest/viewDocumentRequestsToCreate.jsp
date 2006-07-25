<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="administrative.office.services"/></em>
<h2><bean:message key="documents.requirement"/></h2>

<logic:messagesPresent message="true">
	<span class="error">
		<html:messages id="message" message="true" bundle="STUDENT_RESOURCES">
			<bean:write name="message"/>
		</html:messages>
	</span>
</logic:messagesPresent>

<fr:edit schema="DocumentRequestCreateBean.viewToConfirmCreation" name="documentRequestCreateBeans" id="documentRequestCreateBeans" action="/documentRequest.do?method=create">
	<fr:layout name="tabular-editable">
		<fr:property name="classes" value="tstyle4 thlight thright" />
	</fr:layout>
</fr:edit>
