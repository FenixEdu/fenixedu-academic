<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ page import="java.util.List"%>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
    	<td class="infoselected">
    		<strong><jsp:include page="contextExecutionCourse.jsp"/></strong>
		</td>
	</tr>
</table>

<h2>Gestão de Disciplinas</h2>
<br />
<span class="error"><html:errors /></span>
<br />
Aviso: A alteração das cargas horárias pode tornar os dados incoerentes.
Sempre que se altere as cargas é necessário confirmar que os turnos da disciplina
obdecem à nova carga definida.
<br />
<html:form action="/manageExecutionCourse" focus="theoreticalHours">

	<html:hidden property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
				 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
	<html:hidden property="<%= SessionConstants.EXECUTION_COURSE_OID %>"
				 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>

	<table>
			<tr>
				<td class="listClasses-header">
					<bean:message key="label.hours.load.theoretical"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="label.hours.load.theoretical_practical"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="label.hours.load.practical"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="label.hours.load.laboratorial"/>
				</td>
			</tr>
			<tr>
				<td class="listClasses">
					<html:text name="executionCourse" property="theoreticalHours" size="4"/>
				</td>
				<td class="listClasses">
					<html:text name="executionCourse" property="theoPratHours" size="4"/>
				</td>
				<td class="listClasses">
					<html:text name="executionCourse" property="praticalHours" size="4"/>
				</td>
				<td class="listClasses">
					<html:text name="executionCourse" property="labHours" size="4"/>
				</td>
			</tr>
	</table>
	<br />
	<html:hidden property="method" value="edit"/>
	<html:hidden property="page" value="1"/>
	<html:submit styleClass="inputbutton">
	<bean:message key="label.change"/>
	</html:submit>
</html:form>


<br />
<logic:present name="<%= SessionConstants.LIST_INFOCLASS %>" scope="request">
	<table>
		<tr>
			<td class="listClasses-header">
				<bean:message key="label.degree.name"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="label.name"/>
			</td>
		</tr>
		<logic:iterate id="infoClass" name="<%= SessionConstants.LIST_INFOCLASS %>" scope="request">
			<bean:define id="classOID" name="infoClass" property="idInternal"/>
			<bean:define id="curricularYearOID" name="infoClass" property="anoCurricular"/>
			<bean:define id="executionDegreeOID" name="infoClass" property="infoExecutionDegree.idInternal"/>
			<tr>
				<td class="listClasses">
					<bean:write name="infoClass" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.sigla"/>
				</td>
				<td class="listClasses">
					<html:link page="<%= "/manageClass.do?method=prepare&amp;"
							+ SessionConstants.CLASS_VIEW_OID
							+ "="
							+ pageContext.findAttribute("classOID")
							+ "&amp;"
							+ SessionConstants.EXECUTION_PERIOD_OID
							+ "="
							+ pageContext.findAttribute("executionPeriodOID")
							+ "&amp;"
							+ SessionConstants.CURRICULAR_YEAR_OID
							+ "="
							+ pageContext.findAttribute("curricularYearOID")
							+ "&amp;"
							+ SessionConstants.EXECUTION_DEGREE_OID
							+ "="
							+ pageContext.findAttribute("executionDegreeOID") %>">
						<bean:write name="infoClass" property="nome"/>
					</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:present>

<logic:notPresent name="<%= SessionConstants.LIST_INFOCLASS %>" scope="request">
	<span class="error"><bean:message key="message.executionCourse.classes.none"/></span>
</logic:notPresent>