<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ page language="java" %>
<html:xhtml />

<bean:define id="confirmNonReversableOp">return confirm('<bean:message key="message.confirm.non.reversable.operation"/>')</bean:define>

<h2><bean:message key="registration.curriculum" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>


<p>
	<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="registration" paramProperty="externalId">
		<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</html:link>
</p>


<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<div style="float: right;">
	<bean:define id="personID" name="registration" property="student.person.externalId"/>
	<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
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
	<html:link page="/registration.do?method=prepareAddAttends" paramId="registrationId" paramName="registration" paramProperty="externalId">
		<bean:message key="label.add.attends" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</html:link>
</span>

<logic:present name="attendsMap">
	<table class="tstyle4 thlight mtop05">
		<logic:iterate id="attendsEntry" name="attendsMap">
			<bean:define id="executionPeriod" name="attendsEntry" property="key"/>
			<bean:define id="attendsSet" name="attendsEntry" property="value"/>
			<tr>
				<th colspan="5">
					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.year"/>:
					<bean:write name="executionPeriod" property="executionYear.name"/>
					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.semester"/>:
					<bean:write name="executionPeriod" property="semester"/>
				</th>
			</tr>
			<logic:iterate id="attends" name="attendsSet" type="net.sourceforge.fenixedu.domain.Attends">
				<% boolean hasAnyShiftEnrolments = attends.hasAnyShiftEnrolments(); %>
				<tr>
					<td>
						<bean:define id="executionCourse" name="attends" property="executionCourse"/>
						<bean:write name="executionCourse" property="nome"/>
					</td>
					<td>
						<% if (hasAnyShiftEnrolments) { %>
							<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.attends.with.shift.enrolment"/>
						<% } else { %>
							<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.attends.without.shift.enrolment"/>
						<% } %>
					</td>
					<td>
						<bean:define id="url" type="java.lang.String">/registration.do?method=deleteShiftEnrolments&amp;attendsId=<bean:write name="attends" property="externalId"/></bean:define>
						<% if (hasAnyShiftEnrolments) { %>
							<html:link page="<%= url %>" onclick="<%= confirmNonReversableOp.toString() %>" paramId="registrationId" paramName="registration" paramProperty="externalId">
								<bean:message key="label.delete.shift.enrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/>
							</html:link>
						<% } %>
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
						<% if (!hasAnyShiftEnrolments) { %>
						<logic:notPresent name="attends" property="enrolment">
							<bean:define id="url" type="java.lang.String">/registration.do?method=deleteAttends&amp;attendsId=<bean:write name="attends" property="externalId"/></bean:define>
							<html:link page="<%= url %>" onclick="<%= confirmNonReversableOp.toString() %>" paramId="registrationId" paramName="registration" paramProperty="externalId">
								<bean:message key="label.delete.attends" bundle="ACADEMIC_OFFICE_RESOURCES"/>
							</html:link>
						</logic:notPresent>
						<% } %>
					</td>
				</tr>
			</logic:iterate>
		</logic:iterate>
	</table>
</logic:present>

<span class="pleft1">	
	<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
	<html:link page="/registration.do?method=prepareAddAttends" paramId="registrationId" paramName="registration" paramProperty="externalId">
		<bean:message key="label.add.attends" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</html:link>
</span>
