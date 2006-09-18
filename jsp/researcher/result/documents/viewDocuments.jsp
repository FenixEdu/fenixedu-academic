<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="documents" name="result" property="resultDocumentFiles"/>
	<bean:define id="resultId" name="result" property="idInternal"/>
	<bean:define id="result" name="result"/>
	<bean:define id="parameters" value="<%="resultId=" + resultId + "&resultType=" + result.getClass().getSimpleName()%>"/>
	<bean:define id="remove" value="<%="/result/resultDocumentFilesManagement.do?method=remove&" + parameters%>"/>

	<fr:view name="documents" schema="resultDocumentFile.submited.edit">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2"/>
			<fr:property name="columnClasses" value=",acenter,acenter,acenter"/>
						
			<fr:property name="linkFormat(download)" value="${downloadUrl}"/>
			<fr:property name="key(download)" value="link.download"/>
			<fr:property name="contextRelative(download)" value="false"/>
			<fr:property name="bundle(download)" value="RESEARCHER_RESOURCES"/>
			<fr:property name="order(download)" value="1"/>
			
			<fr:property name="link(remove)" value="<%= remove %>"/>
			<fr:property name="param(remove)" value="idInternal/documentFileId"/>
			<fr:property name="key(remove)" value="link.remove"/>
			<fr:property name="bundle(remove)" value="RESEARCHER_RESOURCES"/>
			<fr:property name="order(remove)" value="2"/>
		</fr:layout>
	</fr:view>
</logic:present>