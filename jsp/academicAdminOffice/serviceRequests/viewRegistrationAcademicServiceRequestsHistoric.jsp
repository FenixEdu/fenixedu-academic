<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="requests.historic"/></h2>

<hr style="margin-bottom: 2em;"/>

<html:messages id="messages" message="true">
	<p><span class="error0"><bean:write name="messages" bundle="ACADEMIC_OFFICE_RESOURCES"/></span></p>
</html:messages>

<strong><bean:message key="label.studentDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong>
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

<strong><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong>
<fr:view name="registration" schema="student.registrationDetail" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4"/>
      	<fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>

<p>
	<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="requests.historic"/></strong>
	<bean:define id="historicalAcademicServiceRequests" name="registration" property="historicalAcademicServiceRequests"/>
	<logic:notEmpty name="historicalAcademicServiceRequests">
			<fr:view name="historicalAcademicServiceRequests" schema="AcademicServiceRequest.view-for-given-registration">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4 thlight thright" />
					
					<fr:property name="linkFormat(view)" value="/academicServiceRequestsManagement.do?method=viewAcademicServiceRequest&academicServiceRequestId=${idInternal}&backAction=academicServiceRequestsManagement&backMethod=viewRegistrationAcademicServiceRequestsHistoric"/>
					<fr:property name="key(view)" value="view"/>
			
					<fr:property name="sortBy" value="creationDate=desc, urgentRequest=desc, description=asc"/>
				</fr:layout>
			</fr:view>
	</logic:notEmpty>
	<logic:empty name="historicalAcademicServiceRequests">
		<p>
			<span class="warning0">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="no.historical.academic.service.requests"/>
			</span>
		</p>
	</logic:empty>
</p>

<p style="margin-top: 4em;">
	<html:form action="/student.do?method=visualizeRegistration">
		<bean:define id="registrationID" name="registration" property="idInternal"/>
		<html:hidden property="registrationID" value="<%=registrationID.toString()%>"/>
		<html:submit styleClass="inputbutton"><bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
	</html:form>
</p>
