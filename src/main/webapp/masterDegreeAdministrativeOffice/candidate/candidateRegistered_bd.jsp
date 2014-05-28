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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>


<span class="error"><!-- Error messages go here --><html:errors /></span>


<br />


<logic:present name="infoCandidateRegistration">
	<table>
		<tr>
			<td>
				<strong><bean:message key="label.masterDegree.administrativeOffice.name" /></strong>
			</td>
			<td>
				<bean:write name="infoCandidateRegistration" property="infoStudentCurricularPlan.infoStudent.infoPerson.nome" />
			</td>			
		</tr>
		<tr>
			<td>
				<strong><bean:message key="label.masterDegree.administrativeOffice.number" /></strong>
			</td>
			<td>
				<bean:write name="infoCandidateRegistration" property="infoStudentCurricularPlan.infoStudent.number"/>
			</td>
		</tr>			
		<tr>
			<td>
				<strong><bean:message key="label.masterDegree.administrativeOffice.degree" /></strong>
			</td>
			<td>
				<bean:write name="infoCandidateRegistration" property="infoStudentCurricularPlan.infoDegreeCurricularPlan.infoDegree.nome"/>
			</td>
		</tr>			
		<tr>
			<td>
				<strong><bean:message key="label.specialization" /></strong>
			</td>
			<td>
				<bean:message name="infoCandidateRegistration" property="infoStudentCurricularPlan.specialization.name" bundle="ENUMERATION_RESOURCES"/>
			</td>
		</tr>			
		<tr>
			<td>
				<strong><bean:message key="property.curricularCourse.branch" /></strong>
			</td>
			<logic:notEmpty name="infoCandidateRegistration" property="infoStudentCurricularPlan.infoBranch">
				<td>
					<bean:write name="infoCandidateRegistration" property="infoStudentCurricularPlan.infoBranch.name"/>
				</td>
			</logic:notEmpty>
		</tr>			
	</table>
	
	<br />
	<br />

	<logic:present name="infoCandidateRegistration" property="enrolments" >
		<table>
			<tr>
           		<td align="center">
        			<strong><bean:message key="property.course"/></strong>
        		</td>
           		<td align="center">
        			<strong><bean:message key="property.curricularCourse.branch"/></strong>
        		</td>
			</tr>
           	<logic:iterate id="enrolment" name="infoCandidateRegistration" property="enrolments" >	
	           	<tr>
	           		<td>
            			<bean:write name="enrolment" property="infoCurricularCourse.name"/>
            		</td>
				</tr>
	       	</logic:iterate>
		</table>
	</logic:present>
	
	<br />
	<br />
	<br />

	<html:link page="/candidateRegistration.do?method=preparePrint" target="_blank">
		<h2><bean:message key="link.masterDegree.administrativeOffice.printCandidateRegistration" /></h2>
	</html:link>	
	
	
</logic:present>