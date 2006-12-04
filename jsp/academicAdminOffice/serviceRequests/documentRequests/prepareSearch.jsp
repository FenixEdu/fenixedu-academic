<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<html:xhtml/>

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.documentRequestsManagement.searchDocumentRequests" /></h2>

<hr/><br/>

<logic:messagesPresent message="true">
		<ul>
			<html:messages id="messages" message="true">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
		<br />
</logic:messagesPresent>

<fr:form action="/documentRequestsManagement.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="search"/>
	
	<br/>
	<table class="tstyle4 thlight thright">
									  
<!--   <fr:edit id="documentRequestSearch" name="documentRequestSearchBean" type="net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest.DocumentRequestSearchBean"
   		schema="DocumentRequestSearchBean.view">
	 	<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	
			<fr:property name="linkFormat(view)" value="<%="/documentRequestsManagement.do?method=viewDocumentRequest&documentRequestId=${idInternal}" %>"/>
			<fr:property name="key(view)" value="label.documentRequestsManagement.viewRequest"/>
	
	
	<html:submit styleClass="inputbutton"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.documentRequestsManagement.search" /></html:submit>
	-->
	</table> 
	
	<logic:notEmpty  name="documentRequestsResult">
	<bean:define id="newRequestUrl">
	/documentRequestsManagement.do?method=processNewAcademicServiceRequest&academicSituationType=<bean:write name="academicSituationType" property="name"/>
	</bean:define>
	<bean:define id="processRequestUrl">
	/academicServiceRequestsManagement.do?method=prepareConcludeAcademicServiceRequest
	</bean:define>
	<fr:view name="documentRequestsResult" schema="DocumentRequest.view-documentPurposeTypeInformation" >			
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright" />
			
		
			<fr:property name="linkFormat(processing)" value="<%= newRequestUrl + "&academicServiceRequestId=${idInternal}" %>"/>
			<fr:property name="key(processing)" value="processing"/>
			<fr:property name="visibleIf(processing)" value="newRequest"/>
			
			<fr:property name="linkFormat(concluded)" value="<%= processRequestUrl + "&academicServiceRequestId=${idInternal}" %>"/>
			<fr:property name="key(concluded)" value="concluded"/>
			<fr:property name="visibleIf(concluded)" value="processing"/>
		
		
			<fr:property name="sortBy" value="urgentRequest=desc,creationDate=asc"/>
		</fr:layout>
	</fr:view>
	</logic:notEmpty> 
	
	<logic:empty name="documentRequestsResult">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.documentRequestsManagement.noDocumentRequests" />
		<br/>
		<br/>
	</logic:empty>
</fr:form>
