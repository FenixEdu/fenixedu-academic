<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="requests.historic"/></h2>

<html:messages id="messages" message="true">
	<p><span class="error0"><bean:write name="messages" bundle="ACADEMIC_OFFICE_RESOURCES"/></span></p>
</html:messages>

<div style="float: right;">
	<bean:define id="personID" name="registration" property="student.person.idInternal"/>
	<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
</div>

<p class="mvert2">
	<span style="background-color: #ecf3e1; border-bottom: 1px solid #ccdeb2; padding: 0.4em 0.6em;">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
		<fr:view name="registration" property="student" schema="student.show.personAndStudentInformation.short">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:view>
	</span>
</p>


<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="registration" schema="student.registrationDetail" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop0"/>
	</fr:layout>
</fr:view>





<h3><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="requests.historic"/></h3>
<bean:define id="historicalAcademicServiceRequests" name="registration" property="historicalAcademicServiceRequests"/>
<logic:notEmpty name="historicalAcademicServiceRequests">
	<fr:view name="historicalAcademicServiceRequests" schema="AcademicServiceRequest.view-for-given-registration">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright" />
			<fr:property name="linkFormat(view)" value="/academicServiceRequestsManagement.do?method=viewAcademicServiceRequest&academicServiceRequestId=${idInternal}&backAction=academicServiceRequestsManagement&backMethod=viewRegistrationAcademicServiceRequestsHistoric"/>
			<fr:property name="key(view)" value="view"/>
			<fr:property name="sortBy" value="creationDate=desc, activeSituation.creationDate=desc, urgentRequest=desc, description=asc"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="historicalAcademicServiceRequests">
	<p>
		<em>
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="no.historical.academic.service.requests"/>
		</em>
	</p>
</logic:empty>


<p class="mtop2">
	<html:form action="/student.do?method=visualizeRegistration">
		<bean:define id="registrationID" name="registration" property="idInternal"/>
		<html:hidden property="registrationID" value="<%=registrationID.toString()%>"/>
		<html:submit><bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
	</html:form>
</p>
