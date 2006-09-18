<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<html:xhtml/>

<h2><bean:message key="title.create.shift"/></h2>

<br/>
<html:form action="/manageShifts" focus="nome">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createShift"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
			 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
	<html:hidden alt="<%= SessionConstants.EXECUTION_DEGREE_OID %>" property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
			 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
	<html:hidden alt="<%= SessionConstants.CURRICULAR_YEAR_OID %>" property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
			 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>

	<table cellpadding="0" cellspacing="0">
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
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.tipoAula" property="tipoAula" size="1">
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
                <html:text bundle="HTMLALT_RESOURCES" altKey="text.lotacao" property="lotacao" size="11" maxlength="20"/>
            </td>
        </tr>		
	</table>

	<br />
	<table align='left' cellpadding="0" cellspacing="0">
		<tr align="center">
			<td>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="label.create"/>
				</html:submit>
			</td>
			<td width="10">
			</td>
			<td>
				<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
					<bean:message key="label.clear"/>
				</html:reset>
			</td>
		</tr>
	</table>

</html:form>

