<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="documents" name="result" property="resultDocumentFiles"/>
	<bean:define id="resultId" name="result" property="idInternal"/>
	<bean:define id="resultType" name="result" property="class.simpleName"/>
	<bean:define id="result" name="result"/>
	<bean:define id="parameters" value="<%="resultId=" + resultId + "&resultType=" + resultType %>" toScope="request"/>
	<bean:define id="prepareEdit" value="<%="/result/resultDocumentFilesManagement.do?method=prepareEdit&" + parameters%>"/>

	<fr:edit id="editDocuments" name="documents" schema="resultDocumentFile.submited.edit" action="<%= prepareEdit %>">
		<fr:layout name="tabular-row">
			<fr:property name="classes" value="tstyle2"/>
			<fr:property name="columnClasses" value=",acenter,acenter,acenter"/>
		</fr:layout>
	</fr:edit>
</logic:present>