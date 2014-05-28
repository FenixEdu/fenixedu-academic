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
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>

<jsp:include page="/commons/contextShiftAndExecutionCourseAndExecutionDegreeAndCurricularYear.jsp" />

<h2><bean:message key="link.manage.turnos"/> <span class="small">${executionDegree.executionDegree.degreeCurricularPlan.name}</span></h2>

<bean:define id="shiftName" name="<%= PresentationConstants.SHIFT %>" property="nome"/>
<bean:define id="shiftId" name="<%= PresentationConstants.SHIFT %>" property="externalId"/>
<bean:define id="shiftType" name="<%= PresentationConstants.SHIFT %>" property="shiftTypesIntegerComparator"/>

<html:link action="/manageShift.do?method=prepareEditShift&page=0&shift_oid=${shift.externalId}&execution_course_oid=${executionCourseOID}&academicInterval=${academicInterval}&curricular_year_oid=${curricular_year_oid}&execution_degree_oid=${execution_degree_oid}">
	<bean:message key="button.back" />
</html:link>

<h3>Alunos Inscritos <span class="small">${shift.nome}</span></h3>

<p>
	<logic:present name="<%= PresentationConstants.EXECUTION_COURSE %>" scope="request">
		<bean:write name="<%= PresentationConstants.EXECUTION_COURSE %>" property="nome"/>
	</logic:present>
</p>

<logic:present name="<%= PresentationConstants.STUDENT_LIST %>" scope="request">
<html:form styleClass="col-lg-8" action="/manageShift">
	<table class="table">
		<thead>
			<th>
			</th>
			<th>
				<bean:message key="label.number"/>
			</th>
			<th>
				<bean:message key="label.name"/>
			</th>
			<th>
				<bean:message key="label.mail"/>
			</th>
			<th>
				<bean:message key="label.degree"/>
			</th>
			<th>
			</th>
		</thead>
		<logic:iterate id="shiftEnrolment" name="shift" property="shift.shiftEnrolmentsOrderedByDate">
			<bean:define id="student" name="shiftEnrolment" property="registration"/>
			<tr>
				<td>
					<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.studentIDs" property="studentIDs"><bean:write name="student" property="externalId"/></html:multibox>
				</td>
				<td>
					<bean:write name="student" property="number"/>
				</td>
				<td>
					<bean:write name="student" property="student.person.name"/>
				</td>
				<td>
					<bean:write name="student" property="student.person.email"/>
				</td>
				<td>
                    <logic:present name="student" property="activeStudentCurricularPlan">
    					<bean:write name="student" property="activeStudentCurricularPlan.degreeCurricularPlan.degree.sigla"/>
                    </logic:present>
				</td>
				<td>
					<dt:format pattern="dd/MM/yyyy HH:mm:ss">
						<bean:write name="shiftEnrolment" property="createdOn.millis"/>
					</dt:format>
				</td>
			</tr>
		</logic:iterate>
	</table>


<p class="mtop2"><strong><bean:message key="title.transfer.students.shif"/></strong></p>

<p>
	<span class="info"><bean:message key="message.transfer.students.shift.notice"/></span>
</p>

<logic:present name="<%= PresentationConstants.SHIFTS %>" scope="request">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="changeStudentsShift"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.oldShiftId" property="oldShiftId" value="<%= pageContext.findAttribute("shiftId").toString() %>"/>

        <html:hidden alt="<%= PresentationConstants.ACADEMIC_INTERVAL %>" property="<%= PresentationConstants.ACADEMIC_INTERVAL %>"
                     value="<%= pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL).toString() %>"/>
		<html:hidden alt="<%= PresentationConstants.EXECUTION_DEGREE_OID %>" property="<%= PresentationConstants.EXECUTION_DEGREE_OID %>"
					 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
		<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEAR_OID %>" property="<%= PresentationConstants.CURRICULAR_YEAR_OID %>"
					 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
		<html:hidden alt="<%= PresentationConstants.EXECUTION_COURSE_OID %>" property="<%= PresentationConstants.EXECUTION_COURSE_OID %>"
					 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
		<html:hidden alt="<%= PresentationConstants.SHIFT_OID %>" property="<%= PresentationConstants.SHIFT_OID %>"
					 value="<%= pageContext.findAttribute("shiftOID").toString() %>"/>

		<table>
			<tr>
				<th>
				</th>
				<th>
				</th>
			</tr>
			<logic:iterate id="otherShift" name="<%= PresentationConstants.SHIFTS %>">
				<logic:notEqual name="otherShift" property="nome" value="<%= pageContext.findAttribute("shiftName").toString() %>">
					<bean:define id="otherShiftId" name="otherShift" property="externalId"/>
					<bean:define id="otherShiftType" name="otherShift" property="shiftTypesIntegerComparator"/>
					<logic:equal name="shiftType" value="<%= pageContext.findAttribute("otherShiftType").toString() %>">
						<tr>
							<td>
								<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.newShiftId" property="newShiftId" value="<%= pageContext.findAttribute("otherShiftId").toString() %>"/>
							</td>
							<td>
								<bean:write name="otherShift" property="nome"/>
							</td>
						</tr>
					</logic:equal>
				</logic:notEqual>
			</logic:iterate>
		</table>
		<p>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.transfer"/></html:submit>
		</p>
</logic:present>

	</html:form> 
</logic:present>

<logic:notPresent name="<%= PresentationConstants.STUDENT_LIST %>" scope="request">
	<p>
		<span class="warning0"><!-- Error messages go here --><bean:message key="errors.students.none.in.shift"/></span>	
	</p>
</logic:notPresent>