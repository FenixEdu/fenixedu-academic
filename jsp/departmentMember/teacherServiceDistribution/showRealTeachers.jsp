<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<style>
table.vtsbc {
margin-bottom: 1em;
border: 2px solid #aaa;
text-align: center;
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

</style>

<h3>
	<html:link page='/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution'>
		<bean:message key="link.teacherServiceDistribution"/>
	</html:link>
	>
	<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
		<bean:write name="teacherServiceDistribution" property="name"/>&nbsp;
<%--		(<bean:write name="teacherServiceDistribution" property="executionPeriod.semester"/>º<bean:message key="label.common.courseSemester"/>--%>
		<bean:write name="teacherServiceDistribution" property="executionYear.year"/>
	</html:link>
	>
	<html:link page='<%= "/valuationTeachersGroup.do?method=prepareForValuationTeachersGroupServices&amp;valuationGroupingID=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getCurrentValuationPhase().getRootValuationGrouping().getIdInternal().toString() %>'>
		<bean:message key="link.teacherServiceDistribution.manageRootGrouping"/>
	</html:link>
	>
	<bean:message key="label.teacherServiceDistribution.addTeacher"/>
</h3>

<br/>

<html:form action="/valuationTeachersGroup">
<html:hidden property="method" value="showDepartmentTeachers"/>
<html:hidden property="valuationGrouping"/>
<html:hidden property="teacher"/>

	<b><bean:message key="label.teacherServiceDistribution.department"/>:</b>
	<html:select property="department" title="Departamento" onchange="this.form.submit();">
		<html:options collection="departmentList" property="idInternal" labelProperty="realName"/>
	</html:select>

<br/>
<br/>
<br/>

<logic:present name="teachersList">
	<b><bean:message key="label.teacherServiceDistribution.availableTeachers"/>:</b>
	<br/>
	
	<table class='vtsbc'>
	<tr>
		<th>
			<b><bean:message key="label.teacherService.teacher.name"/></b>
		</th>
		<th>
			<b><bean:message key="label.teacherServiceDistribution.category"/></b>
		</th>
		<th>
		</th>
	<logic:iterate id="teacher" name="teachersList">
		<tr>
			<td>
				<bean:write name="teacher" property="person.nome" />
    		</td>
    		<td>
    			<bean:write name="teacher" property="category.shortName" />
    		</td>
    		<td>
    			<bean:define id="teacherID" name="teacher" property="idInternal"/>
    			<html:link href='<%= "javascript:document.valuationTeachersGroupForm.method.value='addTeacher'; document.valuationTeachersGroupForm.teacher.value=" + teacherID.toString() + "; document.valuationTeachersGroupForm.submit()" %>'>
    				<bean:message key="button.add"/>
    			</html:link>
    		</td>
    	</tr>
	</logic:iterate>
	</table>
</logic:present>
<logic:notPresent name="teachersList">
	<span class="error">
		<bean:message key="label.teacherServiceDistribution.teachersAlreadyIncluded"/>
	</span>
</logic:notPresent>
</html:form>

<br/>
<html:link page='<%= "/valuationTeachersGroup.do?method=prepareForValuationTeachersGroupServices&amp;valuationGroupingID=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getCurrentValuationPhase().getRootValuationGrouping().getIdInternal().toString() %>'>
	<bean:message key="link.back"/>
</html:link>
