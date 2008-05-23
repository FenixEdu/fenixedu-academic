<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page language="java" %>
<html:xhtml />

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	<h2><bean:message key="registration.curriculum" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

	<ul class="mtop2 list5">
		<li>
		<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="registration" paramProperty="idInternal">
			<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
		</li>
	</ul>
	
	<div style="float: right;">
		<bean:define id="personID" name="registration" property="student.person.idInternal"/>
		<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
	</div>
	
	<p class="mvert2">
		<span class="showpersonid">
		<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
			<fr:view name="registration" property="student" schema="student.show.personAndStudentInformation.short">
				<fr:layout name="flow">
					<fr:property name="labelExcluded" value="true"/>
				</fr:layout>
			</fr:view>
		</span>
	</p>

	<logic:present name="registration" property="ingression">
		<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>

		<fr:view name="registration" schema="student.registrationDetail" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
				<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
			</fr:layout>
		</fr:view>
	</logic:present>
	
	<logic:notPresent name="registration" property="ingression">
		<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
		<fr:view name="registration" schema="student.registrationsWithStartData" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
			</fr:layout>
		</fr:view>
	</logic:notPresent>

	<h3 class="mbottom05"><bean:message key="label.registrationAttends" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>

	<span class="pleft1">	
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
		<html:link page="/registration.do?method=prepareAddAttends" paramId="registrationId" paramName="registration" paramProperty="idInternal">
			<bean:message key="label.add.attends" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
	</span>

	<logic:present name="attendsMap">
		<table class="tstyle4 thlight mtop05">
			<logic:iterate id="attendsEntry" name="attendsMap">
				<bean:define id="executionPeriod" name="attendsEntry" property="key"/>
				<bean:define id="attendsSet" name="attendsEntry" property="value"/>
				<tr>
					<th colspan="3">
						<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.year"/>:
						<bean:write name="executionPeriod" property="executionYear.name"/>
						<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.semester"/>:
						<bean:write name="executionPeriod" property="semester"/>
					</th>
				</tr>
				<logic:iterate id="attends" name="attendsSet">
					<tr>
						<td>
							<bean:define id="executionCourse" name="attends" property="executionCourse"/>
							<bean:write name="executionCourse" property="nome"/>
						</td>
						<td>
							<logic:present name="attends" property="enrolment">
								<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.attends.with.enrolment"/>
							</logic:present>
							<logic:notPresent name="attends" property="enrolment">
								<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.attends.without.enrolment"/>
							</logic:notPresent>
						</td>
						<td>
							<logic:notPresent name="attends" property="enrolment">
								<bean:define id="url" type="java.lang.String">/registration.do?method=deleteAttends&amp;attendsId=<bean:write name="attends" property="idInternal"/></bean:define>
								<html:link page="<%= url %>" paramId="registrationId" paramName="registration" paramProperty="idInternal">
									<bean:message key="label.delete.attends" bundle="ACADEMIC_OFFICE_RESOURCES"/>
								</html:link>
							</logic:notPresent>
						</td>
					</tr>
				</logic:iterate>
			</logic:iterate>
		</table>
	</logic:present>

	<span class="pleft1">	
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
		<html:link page="/registration.do?method=prepareAddAttends" paramId="registrationId" paramName="registration" paramProperty="idInternal">
			<bean:message key="label.add.attends" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
	</span>
	
</logic:present>
