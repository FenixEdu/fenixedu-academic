<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<h2><strong><bean:message key="label.studentDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>

<html:messages id="messages" message="true">
	<p><span class="error0"><bean:write name="messages" bundle="ACADEMIC_OFFICE_RESOURCES"/></span></p>
</html:messages>

<table>
	<tr>
		<td>
			<fr:view name="registration" property="student" schema="student.show.personAndStudentInformation">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4"/>
			      	<fr:property name="columnClasses" value="listClasses,,"/>
				</fr:layout>
			</fr:view>
		</td>
		<td>
			<bean:define id="personID" name="registration" property="student.person.idInternal"/>
			<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES"/>
		</td>
	</tr>
</table>
<br/>
<h2><strong><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>
<fr:view name="registration" schema="student.registrationDetail" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4"/>
      	<fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>

<p>
	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="document.requests.historic"/></h2>
	<bean:define id="historicalDocumentRequests" name="registration" property="historicalDocumentRequests"/>
	<logic:notEmpty name="historicalDocumentRequests">
		<fr:view name="historicalDocumentRequests" schema="DocumentRequest.summary-view-by-student">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight thright" />
				
				<fr:property name="linkFormat(edit)" value="/documentRequestsManagement.do?method=prepareEditDocumentRequest&documentRequestId=${idInternal}"/>
				<fr:property name="key(edit)" value="edit"/>
		
				<fr:property name="sortBy" value="creationDate=desc, documentRequestType=asc, urgentRequest=desc"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="historicalDocumentRequests">
		<p>
			<span class="warning0">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.documentRequestsManagement.noHistoricalDocumentRequest"/>
			</span>
		</p>
	</logic:empty>
</p>

<p style="margin-top: 4em;">
	<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="registration" paramProperty="idInternal">
		<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</html:link>
</p>
