<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<h2><bean:message key="label.studentPage" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>


<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>


<h3 class="mtop15 mbottom025"><bean:message key="label.studentDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<table class="mtop025">
	<tr>
		<td>
			<fr:view name="student" schema="student.show.personAndStudentInformation">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thright thlight mtop0 mbottom0"/>
		      		<fr:property name="rowClasses" value="tdhl1,,,,"/>
				</fr:layout>
			</fr:view>
		</td>
		<td>
			<bean:define id="personID" name="student" property="person.externalId"/>
			<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES"/>
		</td>
	</tr>
</table>


<p class="mvert05">
	<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
	<html:link page="/student.do?method=viewPersonalData" paramId="studentID" paramName="student" paramProperty="externalId">
		<bean:message key="link.student.viewPersonalData" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</html:link>

	
</p>

<h3 class="mtop15 mbottom025"><bean:message key="label.studentRegistrations" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="student" property="registrations" schema="student.registrationsToList" >
	<fr:layout name="tabular">
		<fr:property name="sortBy" value="startDate=desc"/>
		<fr:property name="classes" value="tstyle1 thlight mtop025 boldlink1"/>
		<fr:property name="columnClasses" value=",,tdhl1,,"/>
		<fr:property name="linkFormat(view)" value="/student.do?method=visualizeRegistration&registrationID=${externalId}" />
		<fr:property name="key(view)" value="link.student.visualizeRegistration"/>
		<fr:property name="bundle(view)" value="ACADEMIC_OFFICE_RESOURCES"/>
		<fr:property name="contextRelative(view)" value="true"/>
	</fr:layout>
</fr:view>


