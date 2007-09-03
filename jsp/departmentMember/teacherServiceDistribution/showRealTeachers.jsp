<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<em><bean:message key="link.teacherServiceDistribution"/></em>
<h2><bean:message key="label.teacherServiceDistribution.addTeacher"/></h2>

<p class="breadcumbs">
	<em>
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
		<bean:message key="label.teacherServiceDistribution.addTeacher"/>
	</em>
</p>


<html:form action="/valuationTeachersGroup">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="showDepartmentTeachers"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.valuationGrouping" property="valuationGrouping"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacher" property="teacher"/>

<p>
	<b><bean:message key="label.teacherServiceDistribution.department"/>:</b>
	<html:select property="department" title="Departamento" onchange="this.form.submit();">
		<html:options collection="departmentList" property="idInternal" labelProperty="realName"/>
	</html:select>
</p>


<logic:present name="teachersList">
	
<p class="mtop2 mbottom05">
	<b><bean:message key="label.teacherServiceDistribution.availableTeachers"/>:</b>
</p>

	
<table class='tstyle4 mtop05'>
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
				<bean:write name="teacher" property="person.name" />
    		</td>
    		<td>
    			<bean:write name="teacher" property="category.shortName" />
    		</td>
    		<td>
    			<bean:define id="teacherID" name="teacher" property="idInternal"/>
    			<html:link href='<%= "javascript:document.forms[0].method.value='addTeacher'; document.forms[0].teacher.value=" + teacherID.toString() + "; document.forms[0].submit()" %>'>
    				<bean:message key="button.add"/>
    			</html:link>
    		</td>
    	</tr>
	</logic:iterate>
</table>

</logic:present>


<logic:notPresent name="teachersList">
	<p>
		<em>
			<bean:message key="label.teacherServiceDistribution.teachersAlreadyIncluded"/>
		</em>
	</p>
</logic:notPresent>


</html:form>

<ul>
	<li>
		<html:link page='<%= "/valuationTeachersGroup.do?method=prepareForValuationTeachersGroupServices&amp;valuationGroupingID=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getCurrentValuationPhase().getRootValuationGrouping().getIdInternal().toString() %>'>
			<bean:message key="link.back"/>
		</html:link>
	</li>
</ul>
