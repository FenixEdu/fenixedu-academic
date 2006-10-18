<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<h2><strong><bean:message key="label.studentDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<span class="error"><!-- Error messages go here --><bean:write name="message" /></span>
	<br/>
</html:messages>

<table >
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

<html:link page="/viewCurriculum.do?method=getCurriculum" paramId="studentNumber" paramName="registration" paramProperty="number">
	<bean:message key="link.student.viewCurriculum" bundle="ACADEMIC_OFFICE_RESOURCES"/>
</html:link>

<br/><br/>
<h2><strong><bean:message key="label.studentCurricularPlans" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>
<fr:view name="registration" property="studentCurricularPlans" schema="student.studentCurricularPlans" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4"/>
      	<fr:property name="columnClasses" value="listClasses,,"/>
		<fr:property name="linkFormat(enrol)" value="/studentEnrolments.do?method=prepare&scpID=${idInternal}" />
		<fr:property name="key(enrol)" value="link.student.enrolInCourses"/>
		<fr:property name="bundle(enrol)" value="ACADEMIC_OFFICE_RESOURCES"/>
		<fr:property name="visibleIf(enrol)" value="active"/>
		<fr:property name="contextRelative(enrol)" value="true"/>      	
	</fr:layout>
</fr:view>
<br/>
<h2><strong><bean:message key="label.person.title.precedenceDegreeInfo" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>
<fr:view name="registration" property="studentCandidacy.precedentDegreeInformation" schema="student.precedentDegreeInformation" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4"/>
      	<fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>

<%-- 
<br/>
<h2><strong><bean:message key="documentRequests" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>
<bean:define id="documentRequests" name="registration" property="documentRequests"/>
<logic:empty name="documentRequests">
	<p class="warning0"><bean:message key="no.document.requests"/><bean:write name="registration" property="person.username"/></p>
</logic:empty>
<fr:view name="documentRequests" schema="DocumentRequest.summary-view-by-student">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thright" />
		
		<fr:property name="linkFormat(view)" value="/documentRequest.do?method=viewDocumentRequest&documentRequestId=${idInternal}"/>
		<fr:property name="key(view)" value="view"/>

		<fr:property name="linkFormat(process)" value="/documentRequest.do?method=viewDocumentRequest&documentRequestId=${idInternal}"/>
		<fr:property name="key(process)" value="process"/>

		<fr:property name="sortBy" value="creationDate=desc, documentRequestType=asc, urgentRequest=desc"/>
	</fr:layout>
</fr:view>
--%>

<html:link page="/student.do?method=visualizeStudent" paramId="studentID" paramName="registration" paramProperty="student.idInternal">
	<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
</html:link>