<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<span class="error"><html:errors/></span>

<h2><bean:message key="label.professorships"/></h2>

<table border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td nowrap="nowrap">
			<bean:message key="property.executionPeriod"/>:
    	</td>
		<td nowrap="nowrap">
			<html:form action="/manageExecutionCourses" >
				<html:hidden property="method" value="perform"/>
				<html:hidden property="page" value="1"/>
				<html:select property="selectedExecutionPeriodId" size="1" onchange="this.form.submit();">
					<html:options labelProperty="label" property="value" collection="executionPeriodLabelValueBeans" />
				</html:select>
			</html:form>
    	</td>
    </tr>
</table>

<br />
<br />

<table width="90%"cellpadding="5" border="0">
	<tr>
		<td class="listClasses-header" style="text-align:left">
			<bean:message key="label.professorships.acronym"/>
		</td>
		<td class="listClasses-header" style="text-align:left">
			<bean:message key="label.professorships.name"/>
		</td>
		<td class="listClasses-header" style="text-align:left">
			<bean:message key="label.professorships.degrees"/>
		</td>
		<td class="listClasses-header" style="text-align:left">
			<bean:message key="label.semestre"/>
		</td>
	</tr>
	<logic:iterate id="executionCourse" name="executionCourses">
		<tr>
			<td class="listClasses" style="text-align:left">
				<bean:write name="executionCourse" property="sigla"/>
			</td>
			<td class="listClasses" style="text-align:left">
				<bean:write name="executionCourse" property="nome"/>
			</td>
			<td class="listClasses" style="text-align:left">
			</td>
			<td class="listClasses" style="text-align:left">
				<bean:write name="executionCourse" property="executionPeriod.qualifiedName"/>
			</td>
		</tr>		
	</logic:iterate>
</table>
