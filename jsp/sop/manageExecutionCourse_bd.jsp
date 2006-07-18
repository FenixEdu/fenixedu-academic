<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
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

<h2>Gest�o de Disciplinas</h2>
<br />
<span class="error"><html:errors /></span>
<br />
Aviso: A altera��o das cargas hor�rias pode tornar os dados incoerentes.
Sempre que se altere as cargas � necess�rio confirmar que os turnos da disciplina
obdecem � nova carga definida.
<br />
<html:form action="/manageExecutionCourse" focus="theoreticalHours">

	<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
				 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
	<html:hidden alt="<%= SessionConstants.EXECUTION_COURSE_OID %>" property="<%= SessionConstants.EXECUTION_COURSE_OID %>"
				 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>

	<table>
			<tr>
				<th class="listClasses-header">
					<bean:message key="label.hours.load.theoretical"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="label.hours.load.theoretical_practical"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="label.hours.load.practical"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="label.hours.load.laboratorial"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="label.hours.load.seminary"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="label.hours.load.problems"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="label.hours.load.fieldWork"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="label.hours.load.trainingPeriod"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="label.hours.load.tutorialOrientation"/>
				</th>
			</tr>
			<tr>
				<td class="listClasses">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.theoreticalHours" name="executionCourse" property="theoreticalHours" size="4"/>
				</td>
				<td class="listClasses">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.theoPratHours" name="executionCourse" property="theoPratHours" size="4"/>
				</td>
				<td class="listClasses">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.praticalHours" name="executionCourse" property="praticalHours" size="4"/>
				</td>
				<td class="listClasses">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.labHours" name="executionCourse" property="labHours" size="4"/>
				</td>
				<td class="listClasses">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.seminaryHours" name="executionCourse" property="seminaryHours" size="4"/>
				</td>
				<td class="listClasses">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.problemsHours" name="executionCourse" property="problemsHours" size="4"/>
				</td>
				<td class="listClasses">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.fieldWorkHours" name="executionCourse" property="fieldWorkHours" size="4"/>
				</td>
				<td class="listClasses">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.trainingPeriodHours" name="executionCourse" property="trainingPeriodHours" size="4"/>
				</td>
				<td class="listClasses">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.tutorialOrientationHours" name="executionCourse" property="tutorialOrientationHours" size="4"/>
				</td>
				
			</tr>
	</table>
	<br />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="edit"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
	<bean:message key="label.change"/>
	</html:submit>
</html:form>


<br />
<logic:present name="<%= SessionConstants.LIST_INFOCLASS %>" scope="request">
	<table>
		<tr>
			<th class="listClasses-header">
				<bean:message key="label.degree.name"/>
			</th>
			<th class="listClasses-header">
				<bean:message key="label.name"/>
			</th>
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