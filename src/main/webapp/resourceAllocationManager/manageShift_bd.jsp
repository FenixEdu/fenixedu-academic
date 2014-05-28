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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%>
<html:xhtml/>

<jsp:include page="/commons/contextShiftAndExecutionCourseAndExecutionDegreeAndCurricularYear.jsp" />

<h2><bean:message key="link.manage.turnos"/> <span class="small">${executionDegree.executionDegree.degreeCurricularPlan.name}</span></h2>

<h3><bean:message key="title.editTurno"/> <span class="small">${shift.nome}</span></h3>

<p>
	<span class="error"><!-- Error messages go here -->
		<html:errors/>
	</span>
</p>

<table>
	<tr>
		<td>
			<html:form action="/manageLesson">
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareCreate"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>

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

				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="label.lesson.create"/>
				</html:submit>			
			</html:form>
		</td>
		<td width="10">
		</td>
		<td>
			<html:form action="/addClasses">
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="listClasses"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>

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

				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="label.classes.add"/>
				</html:submit>			
			</html:form>
		</td>
		<td width="10">
		</td>
		<td>
			<html:form action="/manageShift">
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="viewStudentsEnroled"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>

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

				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="label.view.students.enroled.shift"/>
				</html:submit>			
			</html:form>
		</td>
		<td width="10">
		</td>
		<td>
			<html:form action="/manageShifts">
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="listShifts"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>

                <html:hidden alt="<%= PresentationConstants.ACADEMIC_INTERVAL %>" property="<%= PresentationConstants.ACADEMIC_INTERVAL %>"
                         value="<%= pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL).toString() %>"/>
				<html:hidden alt="<%= PresentationConstants.EXECUTION_DEGREE_OID %>" property="<%= PresentationConstants.EXECUTION_DEGREE_OID %>"
						 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
				<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEAR_OID %>" property="<%= PresentationConstants.CURRICULAR_YEAR_OID %>"
						 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>

				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="label.return"/>
				</html:submit>			
			</html:form>
		</td>
	</tr>
</table>

<html:form action="/manageShift" focus="nome">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editShift"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

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

	<table class="tstyle5 thlight thright mtop15">
		<tr>
			<th>
				<bean:message key="property.turno.name"/>:
			</th>
			<td>
				<bean:write name="createShiftForm" property="nome"/>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="property.turno.disciplina"/>:
			</th>
			<td>
				<bean:define id="executionCourseList" name="<%= PresentationConstants.EXECUTION_COURSE_LIST_KEY %>"/>
				<html:select bundle="HTMLALT_RESOURCES" property="courseInitials" size="1"
					onchange="this.form.method.value='listExecutionCourseCourseLoads';this.form.submit();">
					<html:option value=""><!-- w3c complient --></html:option>
					<html:options property="sigla" labelProperty="nome" collection="executionCourseList"/>
				</html:select>								
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="property.turno.types"/>:
			</th>
			<td>
				<logic:notEmpty name="tiposAula">									
					<logic:iterate id="tipoAula" name="tiposAula">
						<html:multibox property="shiftTiposAula">
							<bean:write name="tipoAula" property="value"/>
						</html:multibox>
						<bean:write name="tipoAula" property="label"/>
					</logic:iterate>				
				</logic:notEmpty>		
				<logic:empty name="tiposAula">
					--
				</logic:empty>	
			</td>
		</tr>
        <tr>
            <th>
                <bean:message key="property.turno.capacity"/>:
            </th>
            <td>
                <html:text bundle="HTMLALT_RESOURCES" altKey="text.lotacao" property="lotacao" size="11" maxlength="20"/>
            </td>
        </tr>			
        <tr>
            <th>
                <bean:message key="property.turno.comment"/>:
            </th>
            <td>
                <html:textarea bundle="HTMLALT_RESOURCES" altKey="text.comment" property="comment" cols="30"/>
            </td>
        </tr>			
	</table>
	<table>
		<tr>
			<td>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="label.change"/>
				</html:submit>
			</td>
			<td>
			</td>
			<td>
				<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
					<bean:message key="label.clear"/>
				</html:reset>
			</td>
		<tr/>
	</table>
</html:form>

<jsp:include page="shiftLessonList.jsp"/>

<jsp:include page="shiftClassesList.jsp"/>