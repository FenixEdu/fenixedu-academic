<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<em>
	<bean:message key="link.teacherServiceDistribution"/>
</em>

<h2><bean:message key="link.teacherServiceDistribution.teacherEmptyServiceDistributionCreation"/></h2>

<p class="breadcumbs">
	<em>
		<html:link page='/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution'>
			<bean:message key="link.teacherServiceDistribution.start"/>
		</html:link>
		>
		<html:link page="/teacherServiceDistribution.do?method=prepareForTeacherServiceDistributionCreation">
			<bean:message key="link.teacherServiceDistribution.teacherServiceDistributionCreation"/>
		</html:link>
		>
		<bean:message key="link.teacherServiceDistribution.teacherEmptyServiceDistributionCreation"/>
	</em>
</p>

<html:form action="/teacherServiceDistribution">
<html:hidden property="method" value="createTeacherServiceDistribution"/>
<html:hidden property="page" value="0"/>


<p class="mbottom15">
	<span class="error"><html:errors property="name"/></span>
</p>


<p><strong><bean:write name="departmentName"/></strong></p>
<table class='tstyle5 thlight'>
<tr>
	<td>
		<bean:message key="label.teacherServiceDistribution.executionYear"/>:
	</td>
	<td>
		<html:select property="executionYear" onchange="this.form.method.value='prepareForEmptyTeacherServiceDistributionCreation'; this.form.submit();">
			<html:options collection="executionYearList" property="idInternal" labelProperty="year"/>
		</html:select>
	</td>
</tr>
<tr>
	<td>
		<bean:message key="label.teacherServiceDistribution.semester"/>:
	</td>
	<td>
		<html:select property="executionPeriod">
			<html:option value="-1"><bean:message key="label.teacherServiceDistribution.both"/></html:option>
			<html:options collection="executionPeriodsList" property="idInternal" labelProperty="semester"/>
		</html:select>
	</td>
</tr>
<tr>
	<td>
		<bean:message key="label.teacherServiceDistribution.name"/>:
	</td>
	<td>
		<html:text property="name" size="40" maxlength="240" />
	</td>
</tr>
</table>

<p>
<html:button property="" onclick="this.form.method.value='createTeacherServiceDistribution'; this.form.page.value=1; this.form.submit()">
	<bean:message key="label.teacherServiceDistribution.create"/>
</html:button>
</p>


</html:form>