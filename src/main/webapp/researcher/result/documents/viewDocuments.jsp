<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="documents" name="result" property="resultDocumentFiles"/>
<bean:define id="resultId" name="result" property="externalId"/>
<bean:define id="result" name="result"/>
<bean:define id="parameters" value="<%="resultId=" + resultId + "&resultType=" + result.getClass().getSimpleName()%>"/>
<logic:present name="unit">
	<bean:define id="unitID" name="unit" property="externalId"/>
	<bean:define id="parameters" value="<%=parameters + "&unitId=" + unitID %>"/>
</logic:present>

<bean:define id="remove" value="<%="/result/resultDocumentFilesManagement.do?method=remove&" + parameters + "&forwardTo=" + request.getParameter("forwardTo")%>"/>

<fr:view name="documents" schema="resultDocumentFile.submited.edit">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 mtop05"/>
		<fr:property name="columnClasses" value=",acenter,acenter,acenter"/>
					
		<fr:property name="linkFormat(download)" value="${downloadUrl}"/>
		<fr:property name="key(download)" value="link.download"/>
		<fr:property name="contextRelative(download)" value="false"/>
		<fr:property name="bundle(download)" value="RESEARCHER_RESOURCES"/>
		<fr:property name="order(download)" value="1"/>
		
		<fr:property name="link(remove)" value="<%= remove %>"/>
		<fr:property name="param(remove)" value="externalId/documentFileId"/>
		<fr:property name="key(remove)" value="link.remove"/>
		<fr:property name="bundle(remove)" value="RESEARCHER_RESOURCES"/>
		<fr:property name="order(remove)" value="2"/>
	</fr:layout>
</fr:view>
