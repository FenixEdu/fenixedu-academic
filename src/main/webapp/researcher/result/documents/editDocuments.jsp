<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="documents" name="result" property="resultDocumentFiles"/>
<bean:define id="resultId" name="result" property="externalId"/>
<bean:define id="resultType" name="result" property="class.simpleName"/>
<bean:define id="result" name="result"/>
<bean:define id="parameters" value="<%="resultId=" + resultId + "&resultType=" + resultType %>" toScope="request"/>
<logic:present name="unit">
	<bean:define id="unitID" name="unit" property="externalId"/>
	<bean:define id="parameters" value="<%=parameters + "&unitId=" + unitID %>"/>
</logic:present>

<bean:define id="prepareEdit" value="<%="/result/resultDocumentFilesManagement.do?method=prepareEdit&" + parameters + "&forwardTo=" + request.getParameter("forwardTo")%>"/>

<fr:edit id="editDocuments" name="documents" schema="resultDocumentFile.submited.edit" action="<%= prepareEdit %>">
	<fr:layout name="tabular-row">
		<fr:property name="classes" value="tstyle2 mtop05"/>
		<fr:property name="columnClasses" value=",acenter,acenter,acenter"/>
	</fr:layout>
</fr:edit>
