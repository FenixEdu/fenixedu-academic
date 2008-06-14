<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants"%>
<html:xhtml/>

<em><bean:message key="title.resourceAllocationManager.management"/></em>
<h2><bean:message key="link.manage.turnos"/></h2>

<p class="mbottom05">O curso seleccionado &eacute;:</p>
<jsp:include page="context.jsp"/>

<h3><bean:message key="title.editTurno"/></h3>

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

				<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
						 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
				<html:hidden alt="<%= SessionConstants.EXECUTION_DEGREE_OID %>" property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
						 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
				<html:hidden alt="<%= SessionConstants.CURRICULAR_YEAR_OID %>" property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
						 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
				<html:hidden alt="<%= SessionConstants.EXECUTION_COURSE_OID %>" property="<%= SessionConstants.EXECUTION_COURSE_OID %>"
						 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
				<html:hidden alt="<%= SessionConstants.SHIFT_OID %>" property="<%= SessionConstants.SHIFT_OID %>"
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

				<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
						 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
				<html:hidden alt="<%= SessionConstants.EXECUTION_DEGREE_OID %>" property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
						 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
				<html:hidden alt="<%= SessionConstants.CURRICULAR_YEAR_OID %>" property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
						 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
				<html:hidden alt="<%= SessionConstants.EXECUTION_COURSE_OID %>" property="<%= SessionConstants.EXECUTION_COURSE_OID %>"
						 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
				<html:hidden alt="<%= SessionConstants.SHIFT_OID %>" property="<%= SessionConstants.SHIFT_OID %>"
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

				<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
						 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
				<html:hidden alt="<%= SessionConstants.EXECUTION_DEGREE_OID %>" property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
						 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
				<html:hidden alt="<%= SessionConstants.CURRICULAR_YEAR_OID %>" property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
						 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
				<html:hidden alt="<%= SessionConstants.EXECUTION_COURSE_OID %>" property="<%= SessionConstants.EXECUTION_COURSE_OID %>"
						 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
				<html:hidden alt="<%= SessionConstants.SHIFT_OID %>" property="<%= SessionConstants.SHIFT_OID %>"
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

				<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
						 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
				<html:hidden alt="<%= SessionConstants.EXECUTION_DEGREE_OID %>" property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
						 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
				<html:hidden alt="<%= SessionConstants.CURRICULAR_YEAR_OID %>" property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
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

	<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
			 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
	<html:hidden alt="<%= SessionConstants.EXECUTION_DEGREE_OID %>" property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
			 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
	<html:hidden alt="<%= SessionConstants.CURRICULAR_YEAR_OID %>" property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
			 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
	<html:hidden alt="<%= SessionConstants.EXECUTION_COURSE_OID %>" property="<%= SessionConstants.EXECUTION_COURSE_OID %>"
			 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
	<html:hidden alt="<%= SessionConstants.SHIFT_OID %>" property="<%= SessionConstants.SHIFT_OID %>"
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
				<bean:define id="executionCourseList" name="<%= SessionConstants.EXECUTION_COURSE_LIST_KEY %>"/>
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