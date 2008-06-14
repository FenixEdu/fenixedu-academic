<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<em><bean:message key="link.teacherServiceDistribution"/></em>
<h2><bean:message key="link.teacherServiceDistribution.tsdProcessCopy"/></h2>

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
		<bean:message key="link.teacherServiceDistribution.tsdProcessCopy"/>
	</em>
</p>


<html:form action="/tsdProcessCopy">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="copyTSDProcess"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>

<p><strong><bean:write name="departmentName"/></strong></p>
<table class='tstyle5 thlight thright  mtop05'>
<tr>
	<th>
		<bean:message key="label.teacherServiceDistribution.executionYear"/>:
	</th>
	<td>
		<html:select property="executionYear" onchange="this.form.method.value='prepareForTSDProcessCopy'; this.form.submit();">
			<html:option value="-1"><bean:message key="label.teacherServiceDistribution.all"/></html:option>
			<html:options collection="executionYearList" property="idInternal" labelProperty="year"/>
		</html:select>
	</td>
</tr>
<tr>
	<th>
		<bean:message key="label.teacherServiceDistribution.semester"/>:
	</th>
	<td>
		<logic:notEmpty name="executionPeriodsList">
			<html:select property="executionPeriod" onchange="this.form.method.value='prepareForTSDProcessCopy'; this.form.submit();">
				<html:option value="-1"><bean:message key="label.teacherServiceDistribution.both"/></html:option>
				<html:options collection="executionPeriodsList" property="idInternal" labelProperty="semester"/>
			</html:select>
		</logic:notEmpty>
	</td>
</tr>
</table>
<br/>

<logic:empty name="tsdProcessList">
	<p>
		<em>
			<bean:message key="label.teacherServiceDistribution.noPublishedTSDProcesssForExecutionPeriod"/>
		</em>
	</p>
</logic:empty>

<logic:notEmpty name="tsdProcessList">
	<b><bean:message key="label.teacherServiceDistribution.availableTSDProcesss"/>:</b>
	<br/>
	<table class='tstyle4 thlight mtop05'>
		<tr>
			<th/>
			<th>
				<bean:message key="label.teacherServiceDistribution.name"/>
			</th>
			<th>
				<bean:message key="label.teacherServiceDistribution.executionYear"/>
			</th>
			<th>
				<bean:message key="label.teacherServiceDistribution.semesters"/>
			</th>
		</tr>
	<logic:iterate name="tsdProcessList" id="tsdProcess"> 
		<tr>
			<td>
				<bean:define id="tsdProcessId" name="tsdProcess" property="idInternal" />
				<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.tsdProcess" property="tsdProcess" value="<%= ((Integer)tsdProcessId).toString() %>"/>
			</td>
			<td class="highlight7" align="left" width="250">
				<bean:write name="tsdProcess" property="name"/>
			</td>
			<td align="right">
				<bean:write name="tsdProcess" property="executionYear.year"/>
			</td>
			<td align="right">
				<logic:iterate id="executionPeriod" name="tsdProcess" property="orderedExecutionPeriods">
					<bean:write name="executionPeriod" property="semester"/>&#186;&nbsp;
				</logic:iterate>
			</td>
		</tr>
	</logic:iterate>
	</table>

	<br/>
	<br/>


	<b><bean:message key="label.teacherServiceDistribution.newDistributionData"/>:</b>
	<br/>
	<table class='tstyle5 thlight thright mtop05 mbottom05 thmiddle'>
	<tr>
		<th>
			<bean:message key="label.teacherServiceDistribution.executionYear"/>:
		</th>
		<td>
			<html:select property="executionYearForCopy">
				<html:options collection="executionYearList" property="idInternal" labelProperty="year"/>
			</html:select>
		</td>
	</tr>
	<tr>
		<th>
			<bean:message key="label.teacherServiceDistribution.semester"/>:
		</th>
		<td>
			<html:select property="executionPeriodForCopy">
				<html:option value="-1"><bean:message key="label.teacherServiceDistribution.both"/></html:option>
				<html:options collection="executionPeriodsListForCopy" property="idInternal" labelProperty="semester"/>
			</html:select>
		</td>
	</tr>
	<tr>
		<th>
			<bean:message key="label.teacherServiceDistribution.name"/>:
		</th>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name" size="30" maxlength="240" />
		</td>
	</tr>
	</table>

	<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='copyTSDProcess'; this.form.page.value=1; this.form.submit()">
		<bean:message key="label.teacherServiceDistribution.create"/>
	</html:button>
</logic:notEmpty>

<br/>
<span class="error"><html:errors property="name"/></span>
<br/>
<br/>

<html:link page="/tsdProcess.do?method=prepareForTSDProcessCreation">
	<bean:message key="link.back"/>
</html:link>
</html:form>