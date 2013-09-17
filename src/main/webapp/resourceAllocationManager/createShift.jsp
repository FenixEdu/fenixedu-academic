<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<html:xhtml/>

<h3 class="mbottom05"><bean:message key="title.create.shift"/></h3>

<html:form action="/manageShifts" focus="nome">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createShift"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<html:hidden alt="<%= PresentationConstants.EXECUTION_PERIOD_OID %>" property="<%= PresentationConstants.EXECUTION_PERIOD_OID %>"
			 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
	<html:hidden alt="<%= PresentationConstants.EXECUTION_DEGREE_OID %>" property="<%= PresentationConstants.EXECUTION_DEGREE_OID %>"
			 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
	<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEAR_OID %>" property="<%= PresentationConstants.CURRICULAR_YEAR_OID %>"
			 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>

	<table class="tstyle5 thlight thright mtop05">
		<tr>
			<th>
				<bean:message key="property.turno.disciplina"/>:
			</th>
			<td >
				<jsp:include page="selectExecutionCourseList.jsp"/>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="property.turno.type"/>:
			</th>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" property="tipoAula" size="1">
					<html:options collection="tiposAula" property="value" labelProperty="label"/>
				</html:select>
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

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="label.create"/>
	</html:submit>

	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
		<bean:message key="label.clear"/>
	</html:reset>

</html:form>

