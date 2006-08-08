<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="resultId" name="result" property="idInternal" />
	<bean:define id="resultType" name="result" property="class.simpleName"/>
	<bean:define id="actionPath">
		/result/resultDocumentFilesManagement.do?method=createResultDocumentFile&resultId=<bean:write name="resultId"/>&resultType=<bean:write name="result" property="class.simpleName"/>
	</bean:define>
	<bean:define id="invalidPath">
		/result/resultDocumentFilesManagement.do?method=prepareManageDocumentFiles&resultId=<bean:write name="resultId"/>&resultType=<bean:write name="result" property="class.simpleName"/>
	</bean:define>
	<bean:define id="cancelPath">
		/result/resultDocumentFilesManagement.do?method=backToResult&resultId=<bean:write name="resultId"/>&resultType=<bean:write name="result" property="class.simpleName"/>
	</bean:define>

	<%-- Use Case Super Title --%>
	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.superUseCaseTitle"/></em>
	
	<%-- Use Case Title --%>		
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultDocumentFiles.useCaseTitle"/></h2>
	
	<%-- Warning/Error messages --%>
	<logic:messagesPresent name="messages" message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error"><bean:write name="messages"/></span></p>
		</html:messages>
		<br/>
	</logic:messagesPresent>
	
	<%-- Document File Submission --%>
	<p><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultDocumentFiles.selectFile"/></p>
	<fr:edit 	id="documentFileBean" name="bean" schema="resultDocumentFile.submission.edit"
				type="net.sourceforge.fenixedu.dataTransferObject.research.result.ResultDocumentFileSubmissionBean"
				action="<%= actionPath %>">
		<fr:destination name="invalid" path="<%= invalidPath %>"/>
		<fr:destination name="cancel" path="<%= cancelPath %>"/>
	</fr:edit>
</logic:present>
