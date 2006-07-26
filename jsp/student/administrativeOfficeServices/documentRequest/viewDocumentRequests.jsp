<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="administrative.office.services"/></em>
<h2><bean:message key="documents.requirement.consult"/></h2>

<logic:messagesPresent message="true">
	<span class="error">
		<html:messages id="message" message="true" bundle="STUDENT_RESOURCES">
			<bean:write name="message"/>
		</html:messages>
	</span>
</logic:messagesPresent>

<fr:view name="documentRequests" schema="DocumentRequest.summary-view-by-student">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thright" />
		
		<fr:property name="linkFormat(view)" value="/documentRequest.do?method=viewDocumentRequest&documentRequestId=${idInternal}"/>
		<fr:property name="key(view)" value="view"/>

		<fr:property name="sortBy" value="urgentRequest=desc,creationDate=desc"/>
	</fr:layout>
</fr:view>
