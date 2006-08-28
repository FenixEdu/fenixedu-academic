<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<style>
table.vtsbc {
margin-bottom: 1em;
border: 2px solid #aaa;
text-align: left;
border-collapse: collapse;
}
table.vtsbc th {
padding: 0.2em 0.2em;
border: 1px solid #bbb;
border-bottom: 1px solid #aaa;
background-color: #cacaca;
font-weight: bold;
}
table.vtsbc td {
background-color: #eaeaea;
border: none;
border: 1px solid #ccc;
padding: 0.25em 0.5em;
}
table.vtsbc td.courses {
background-color: #f4f4f8;
padding: 0.25em 0.25em;
text-align: left;
}
</style>

<h3>
	<html:link page='/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution'>
		<bean:message key="link.teacherServiceDistribution"/>
	</html:link>
	>
	<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
		<bean:write name="teacherServiceDistribution" property="name"/>&nbsp;
		<bean:write name="teacherServiceDistribution" property="executionYear.year"/>
	</html:link>
	>
	<html:link page='<%= "/valuationTeachersGroup.do?method=prepareForValuationTeachersGroupServices&amp;valuationGroupingID=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getCurrentValuationPhase().getRootValuationGrouping().getIdInternal().toString() %>'>
		<bean:message key="link.teacherServiceDistribution.manageRootGrouping"/>
	</html:link>
	>
	<bean:message key="label.teacherService.createTeacher"/>
</h3>

<br/>
<br/>
<html:form action="/valuationTeachersGroup">
<html:hidden property="method" value="createValuationTeacher"/>
<html:hidden property="valuationGrouping"/>
<html:hidden property="page" value="2"/>

	<table class="vtsbc">
		<tr>
			<th colspan="2" align="center">			
				<bean:message key="label.teacherService.createTeacher"/>
			</th>
		</tr>
		<tr>
			<td>			
				<b><bean:message key="label.teacherServiceDistribution.name"/>:</b>
			</td>
			<td>
				<html:text property="name" size="25" maxlength="240"/>
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message key="label.teacherServiceDistribution.category"/>:</b>
			</td>
			<td>
				<html:select property="category">
					<html:options collection="categoriesList" property="idInternal" labelProperty="shortName"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td>			
				<b><bean:message key="label.teacherService.teacher.hours"/>:</b>
			</td>
			<td>
				<html:text property="hours" size="3" maxlength="4"/>
			</td>
		</tr>
	</table>
	<html:button property="" onclick="this.form.submit();"> 
		<bean:message key="label.teacherServiceDistribution.create"/> 
	</html:button>
</html:form> 

<br/>
<span class="error">
	<html:errors />
	<logic:present name="creationFailure">
		<bean:message key="label.teacherServiceDistribution.valuationTeacherCreationFailure"/>
	</logic:present>
</span>
<br/>
<br/>
<html:link page='<%= "/valuationTeachersGroup.do?method=prepareForValuationTeachersGroupServices&amp;valuationGroupingID=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getCurrentValuationPhase().getRootValuationGrouping().getIdInternal().toString() %>'>
	<bean:message key="link.back"/>
</html:link>
