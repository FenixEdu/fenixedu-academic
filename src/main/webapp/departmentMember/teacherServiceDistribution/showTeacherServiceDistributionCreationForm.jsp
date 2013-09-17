<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<em><bean:message key="link.teacherServiceDistribution"/></em>
<h2><bean:message key="link.teacherServiceDistribution.teacherEmptyServiceDistributionCreation"/></h2>

<p class="breadcumbs">
	<em>
		<html:link page='/tsdProcess.do?method=prepareTSDProcess'>
		<bean:message key="link.teacherServiceDistribution"/>
		</html:link>
		>
		<html:link page="/tsdProcess.do?method=prepareForTSDProcessCreation">
			<bean:message key="link.teacherServiceDistribution.tsdProcessCreation"/>
		</html:link>
		>
		<bean:message key="link.teacherServiceDistribution.teacherEmptyServiceDistributionCreation"/>
	</em>
</p>


<html:form action="/tsdProcess">
<html:hidden property="method" value="createTSDProcess"/>
<html:hidden property="page" value="0"/>

<p><strong><bean:write name="departmentName"/></strong></p>
<table class='tstyle5 thlight thright mtop05 mbottom05'>
<tr>
	<th>
		<bean:message key="label.teacherServiceDistribution.executionYear"/>:
	</th>
	<td>
		<html:select property="executionYear" onchange="this.form.method.value='prepareForEmptyTSDProcessCreation'; this.form.submit();">
			<html:options collection="executionYearList" property="externalId" labelProperty="year"/>
		</html:select>
	</td>
</tr>
<tr>
	<th>
		<bean:message key="label.teacherServiceDistribution.semester"/>
	</th>
	<td>
		<html:select property="executionPeriod">
			<html:option value="-1"><bean:message key="label.teacherServiceDistribution.both"/></html:option>
			<html:options collection="executionPeriodsList" property="externalId" labelProperty="semester"/>
		</html:select>
	</td>
</tr>
<tr>
	<th>
		<bean:message key="label.teacherServiceDistribution.name"/>:
	</th>
	<td>
		<html:text property="name" size="30" maxlength="240" />
	</td>
</tr>
</table>

<html:button property="" onclick="this.form.method.value='createTSDProcess'; this.form.page.value=1; this.form.submit()">
	<bean:message key="label.teacherServiceDistribution.create"/>
</html:button>

<br/>
<br/>
<span class="error"><html:errors property="name"/></span>

<br/>

<html:link page="/tsdProcess.do?method=prepareForTSDProcessCreation">
	<bean:message key="link.back"/>
</html:link>
</html:form>