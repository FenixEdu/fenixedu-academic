<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="documents" name="result" property="resultDocumentFiles"/>
	
	<bean:define id="resultId" name="result" property="idInternal" />
	<bean:define id="resultType" name="result" property="class.simpleName"/>
	<bean:define id="parameters">
		resultId=<bean:write name="resultId"/>&resultType=<bean:write name="result" property="class.simpleName"/>
	</bean:define>
	
	<bean:define id="actionPath">
		/result/resultDocumentFilesManagement.do?method=create&<bean:write name="parameters"/>
	</bean:define>
	<bean:define id="invalidPath">
		/result/resultDocumentFilesManagement.do?method=prepareEdit&<bean:write name="parameters"/>
	</bean:define>
	<bean:define id="cancelPath">
		/result/resultDocumentFilesManagement.do?method=backToResult&<bean:write name="parameters"/>
	</bean:define>
	<bean:define id="removePath">
		/result/resultDocumentFilesManagement.do?method=remove&<bean:write name="parameters"/>
	</bean:define>
	<bean:define id="backLink">
		/result/resultAssociationsManagement.do?method=backToResult&<bean:write name="parameters"/>		
	</bean:define>

	<%-- Use Case Titles --%>
	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.Result.superUseCase.title"/></em>
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultDocumentFiles.useCase.title"/>: <fr:view name="result" property="title"/></h2>
	
	<%-- Warning/Error messages --%>
	<logic:messagesPresent name="messages" message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error"><bean:write name="messages"/></span></p>
		</html:messages>
	</logic:messagesPresent>
	
	<%-- Documents List--%>
	<h4><bean:message bundle="RESEARCHER_RESOURCES" key="label.documents"/></h4>
	<logic:empty name="documents">
		<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultDocumentFiles.emptyList"/></em></p>
	</logic:empty>
	<logic:notEmpty name="documents">
		<fr:view name="documents" schema="resultDocumentFile.summary">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2"/>
				<fr:property name="columnClasses" value=",,,acenter"/>
				<fr:property name="sortBy" value="uploadTime=desc"/>
							
				<fr:property name="linkFormat(download)" value="${downloadUrl}"/>
				<fr:property name="key(download)" value="link.download"/>
				<fr:property name="contextRelative(download)" value="false"/>
				<fr:property name="bundle(download)" value="RESEARCHER_RESOURCES"/>
				<fr:property name="order(download)" value="1"/>
				
				<fr:property name="link(remove)" value="<%= removePath %>"/>
				<fr:property name="param(remove)" value="idInternal/documentFileId"/>
				<fr:property name="key(remove)" value="link.remove"/>
				<fr:property name="bundle(remove)" value="RESEARCHER_RESOURCES"/>
				<fr:property name="order(remove)" value="2"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>

	<%-- Document File Submission --%>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultDocumentFiles.selectFile"/></h3>
	<fr:edit id="bean" name="bean" action="<%= actionPath %>" schema="resultDocumentFile.submission.edit">
		<fr:layout name="tabular">
			<fr:property name="classes" value="style1" />
			<fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
		<fr:destination name="exception" path="<%= invalidPath %>"/>
		<fr:destination name="invalid" path="<%= invalidPath %>"/>
		<fr:destination name="cancel" path="<%= cancelPath %>"/>
	</fr:edit>
	<br/>
	
	<%-- Go to previous page --%>
	<html:link page="<%= backLink %>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.goBackToView"/></html:link>
</logic:present>
