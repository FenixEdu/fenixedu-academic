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
	
	<!-- Action paths definitions -->
	<bean:define id="parameters" value="<%="resultId=" + resultId + "&resultType=" + resultType %>" toScope="request"/>



	<bean:define id="create" value="<%="/result/resultDocumentFilesManagement.do?forwardTo=editDocumentFiles&method=create&" + parameters%>" />
	<bean:define id="backLink" value="<%="/result/resultDocumentFilesManagement.do?forwardTo=editDocumentFiles&method=backToResult&" + parameters %>" />
	<bean:define id="prepareEdit" value="<%="/result/resultDocumentFilesManagement.do?forwardTo=editDocumentFiles&method=prepareEdit&" + parameters%>" />
	<bean:define id="prepareAlter" value="<%="/result/resultDocumentFilesManagement.do?forwardTo=editDocumentFiles&method=prepareAlter&" + parameters%>"/>	
	<bean:define id="cancel" value="<%="/result/resultDocumentFilesManagement.do?forwardTo=editDocumentFiles&method=backToResult&" + parameters%>"/>
	
	<%-- Use Case Titles --%>
	<logic:equal name="resultType" value="ResultPatent">
		<em><bean:message key="link.patentsManagement" bundle="RESEARCHER_RESOURCES"/></em> 
	</logic:equal>
	<logic:notEqual name="resultType" value="ResultPatent">
		<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.publications"/></em>
	</logic:notEqual>
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultDocumentFiles.useCase.title"/>: <fr:view name="result" property="title"/></h2>
	
	<%-- Go to previous page --%>
	<ul class="mvert2 list5">
		<li>

			<html:link page="<%= backLink %>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.goBackToView"/></html:link>


		</li>
	</ul>

	<%-- Warning/Error messages --%>
	<logic:messagesPresent name="messages" message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error0"><bean:write name="messages"/></span></p>
		</html:messages>
	</logic:messagesPresent>
	
	<%-- Documents List--%>
	<strong><bean:message bundle="RESEARCHER_RESOURCES" key="label.documents"/></strong>
	<logic:empty name="documents">
		<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultDocumentFiles.emptyList"/></em></p>
	</logic:empty>
	<logic:notEmpty name="documents">
		<logic:notPresent name="editExisting">
		    (<html:link page="<%=prepareAlter%>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.edit"/></html:link>)
			<jsp:include page="viewDocuments.jsp"/>
		</logic:notPresent>
		<logic:present name="editExisting">
		
			<jsp:include page="editDocuments.jsp"/>
		</logic:present>
	</logic:notEmpty>

	<%-- Document File Submission --%>
	<p class="mtop2 mbottom0"><b><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultDocumentFiles.selectFile"/>:</b></p>
	
	<fr:edit id="editBean" name="bean" schema="resultDocumentFile.submission.edit" action="<%= create %>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright thtop" />
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="exception" path="<%= prepareEdit %>"/>
		<fr:destination name="invalid" path="<%= prepareEdit %>"/>
		<fr:destination name="cancel" path="<%= cancel %>"/>
	</fr:edit>
	
</logic:present>
