<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<span class="error">
	<html:errors/>
</span>

<table cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td>
			<html:form action="/manageLesson">
				<html:hidden property="method" value="prepareCreate"/>
				<html:hidden property="page" value="0"/>

				<html:hidden property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
						 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
				<html:hidden property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
						 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
				<html:hidden property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
						 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
				<html:hidden property="<%= SessionConstants.EXECUTION_COURSE_OID %>"
						 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
				<html:hidden property="<%= SessionConstants.SHIFT_OID %>"
						 value="<%= pageContext.findAttribute("shiftOID").toString() %>"/>

				<html:submit styleClass="inputbutton">
					<bean:message key="label.lesson.create"/>
				</html:submit>			
			</html:form>
		</td>
		<td width="10">
		</td>
		<td>
			<html:form action="/addClasses">
				<html:hidden property="method" value="listClasses"/>
				<html:hidden property="page" value="0"/>

				<html:hidden property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
						 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
				<html:hidden property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
						 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
				<html:hidden property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
						 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
				<html:hidden property="<%= SessionConstants.EXECUTION_COURSE_OID %>"
						 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
				<html:hidden property="<%= SessionConstants.SHIFT_OID %>"
						 value="<%= pageContext.findAttribute("shiftOID").toString() %>"/>

				<html:submit styleClass="inputbutton">
					<bean:message key="label.classes.add"/>
				</html:submit>			
			</html:form>
		</td>
		<td width="10">
		</td>
		<td>
			<html:form action="/manageShift">
				<html:hidden property="method" value="viewStudentsEnroled"/>
				<html:hidden property="page" value="0"/>

				<html:hidden property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
						 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
				<html:hidden property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
						 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
				<html:hidden property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
						 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
				<html:hidden property="<%= SessionConstants.EXECUTION_COURSE_OID %>"
						 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
				<html:hidden property="<%= SessionConstants.SHIFT_OID %>"
						 value="<%= pageContext.findAttribute("shiftOID").toString() %>"/>

				<html:submit styleClass="inputbutton">
					<bean:message key="label.view.students.enroled.shift"/>
				</html:submit>			
			</html:form>
		</td>
		<td width="10">
		</td>
		<td>
			<html:form action="/manageShifts">
				<html:hidden property="method" value="listShifts"/>
				<html:hidden property="page" value="0"/>

				<html:hidden property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
						 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
				<html:hidden property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
						 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
				<html:hidden property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
						 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>

				<html:submit styleClass="inputbutton">
					<bean:message key="label.return"/>
				</html:submit>			
			</html:form>
		</td>
	</tr>
</table>

<br />
<html:form action="/manageShift" focus="nome">

	<html:hidden property="method" value="editShift"/>
	<html:hidden property="page" value="1"/>

	<html:hidden property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
			 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
	<html:hidden property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
			 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
	<html:hidden property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
			 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
	<html:hidden property="<%= SessionConstants.EXECUTION_COURSE_OID %>"
			 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
	<html:hidden property="<%= SessionConstants.SHIFT_OID %>"
			 value="<%= pageContext.findAttribute("shiftOID").toString() %>"/>

	<table cellpadding="0" cellspacing="0">
		<tr>
			<td class="formTD">
				<bean:message key="property.turno.name"/>
				:
			</td>
			<td class="formTD">
				<html:text property="nome" size="11" maxlength="20"/>
			</td>
		</tr>
		<tr>
			<td class="formTD">
				<bean:message key="property.turno.disciplina"/>
				:
			</td>
			<td  class="formTD">
				<jsp:include page="selectExecutionCourseList.jsp"/>
			</td>
		</tr>
		<tr>
			<td class="formTD">
				<bean:message key="property.turno.type"/>
				:
			</td>
			<td class="formTD">
				<html:select property="tipoAula" size="1">
					<html:options collection="tiposAula" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>
        <tr>
            <td class="formTD">
                <bean:message key="property.turno.capacity"/>
                :
            </td>
            <td class="formTD">
                <html:text property="lotacao" size="11" maxlength="20"/>
            </td>
        </tr>			
	</table>

	<br />
	<table align='left' cellpadding="0" cellspacing="0">
		<tr align="center">
			<td>
				<html:submit styleClass="inputbutton">
					<bean:message key="label.change"/>
				</html:submit>
			</td>
			<td width="10">
			</td>
			<td>
				<html:reset styleClass="inputbutton">
					<bean:message key="label.clear"/>
				</html:reset>
			</td>
		<tr />
	</table>
</html:form>