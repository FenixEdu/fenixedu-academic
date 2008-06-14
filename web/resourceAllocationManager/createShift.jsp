<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants" %>

<html:xhtml/>

<h3 class="mbottom05"><bean:message key="title.create.shift"/></h3>

<html:form action="/manageShifts" focus="nome">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createShift"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
			 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
	<html:hidden alt="<%= SessionConstants.EXECUTION_DEGREE_OID %>" property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
			 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
	<html:hidden alt="<%= SessionConstants.CURRICULAR_YEAR_OID %>" property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
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

