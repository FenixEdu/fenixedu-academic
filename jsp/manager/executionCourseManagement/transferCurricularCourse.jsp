<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<h2><bean:message key="label.manager.executionCourseManagement.edit.executionCourse"/></h2>

<span class="error"><html:errors/></span>

<bean:write name="infoExecutionCourse" property="nome"/>
 - <bean:write name="infoExecutionCourse" property="sigla"/>
<br />
<bean:write name="infoCurricularCourse" property="name"/>
 - <bean:write name="infoCurricularCourse" property="code"/>
 - <bean:write name="infoCurricularCourse" property="infoDegreeCurricularPlan.name"/>

<br />
<br />
<html:form action="/editExecutionCourseTransferCurricularCourses" focus="name">
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="transferCurricularCourse"/>

	<bean:define id="executionCourseId" name="infoExecutionCourse" property="idInternal"/>
	<bean:define id="curricularCourseId" name="infoCurricularCourse" property="idInternal"/>

	<bean:write name="executionCourseId"/><br />
	<bean:write name="curricularCourseId"/><br />

	<html:hidden property="executionCourseId" value="<%= pageContext.findAttribute("executionCourseId").toString() %>"/>
	<html:hidden property="curricularCourseId" value="<%= pageContext.findAttribute("curricularCourseId").toString() %>"/>
	<html:hidden property="executionPeriodId" value="<%= pageContext.findAttribute("executionPeriodId").toString() %>"/>

	<html:select property="destinationExecutionDegreeId" size="1"
			onchange="this.form.method.value='selectExecutionDegree';document.executionCourseTransferCurricularCourseForm.submit();">
		<html:options collection="executionDegrees" labelProperty="label" property="value" />
	</html:select>

	<html:select property="curricularYear" size="1"
			onchange="this.form.method.value='selectExecutionDegree';document.executionCourseTransferCurricularCourseForm.submit();">
		<html:options collection="curricularYears" labelProperty="label" property="value" />
	</html:select>

	<br />
	<br />
	<logic:present name="executionCourses">
		<table>
			<tr>
				<td class="listClasses-header">&nbsp;
				</td>
				<td class="listClasses-header">&nbsp;
				</td>
				<td class="listClasses-header">&nbsp;
				</td>
			</tr>
			<logic:iterate id="executionCourse" name="executionCourses">
				<bean:define id="destinationExecutionCourseId" name="executionCourse" property="idInternal"/>
				<tr>
					<td class="listClasses">
						<bean:write name="executionCourse" property="idInternal"/>
						<html:radio property="destinationExecutionCourseId"
								value="<%= pageContext.findAttribute("destinationExecutionCourseId").toString() %>" />
					</td>
					<td class="listClasses">
						<bean:write name="executionCourse" property="nome"/>
					</td>
					<td class="listClasses">
						<bean:write name="executionCourse" property="sigla"/>
					</td>
				</tr>
			</logic:iterate>		
		</table>
	</logic:present>

	<br />
	<br />
	<html:submit styleClass="inputbutton"><bean:message key="button.transfer"/></html:submit>
</html:form>