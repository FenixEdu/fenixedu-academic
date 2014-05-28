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
<%@ page isELIgnored="true"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="link.tests"/></h2>


<logic:present name="registrationSelectExecutionYearBean">
	<div class="mvert15">
		<fr:form action="/studentTests.do?method=viewStudentExecutionCoursesWithTests">
			<fr:edit id="executionYear" name="registrationSelectExecutionYearBean" slot="executionYear">
				<fr:layout name="menu-select-postback">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.StudentExecutionYearsProvider" />
					<fr:property name="format" value="${year}" />
					<fr:property name="destination" value="postBack"/>
				</fr:layout>
			</fr:edit>
		</fr:form>
	</div>
</logic:present>


<logic:present name="studentExecutionCoursesList">
	<logic:empty name="studentExecutionCoursesList">
		<p class="mvert15"><em><bean:message key="message.noStudentTests"/></em></p>
	</logic:empty>
	
	<logic:notEmpty name="studentExecutionCoursesList" >
		<bean:message key="message.CoursesWithTests"/>:
		<table class="tstyle1 thlight tdcenter">
			<tr>
				<th><bean:message key="label.curricular.course.acronym"/></th>
				<th><bean:message key="label.curricular.course.name"/></th>
			</tr>
			<logic:iterate id="executionCourse" name="studentExecutionCoursesList" type="net.sourceforge.fenixedu.domain.ExecutionCourse">
			<tr>
				<td>
					<html:link page="/studentTests.do?method=testsFirstPage" paramId="objectCode" paramName="executionCourse" paramProperty="externalId">
						<bean:write name="executionCourse" property="sigla"/>
					</html:link>
				</td>
				<td>
					<html:link page="/studentTests.do?method=testsFirstPage" paramId="objectCode" paramName="executionCourse" paramProperty="externalId">
						<bean:write name="executionCourse" property="nome"/>
					</html:link>
				</td>
			</tr>
			</logic:iterate>
		</table>
	</logic:notEmpty>
</logic:present>

<logic:notPresent name="studentExecutionCoursesList">
	<p class="mvert15"><em><bean:message key="message.noStudentTests"/></em></p>
</logic:notPresent>
