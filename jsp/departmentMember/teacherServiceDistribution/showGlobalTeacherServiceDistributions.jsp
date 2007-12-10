<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.formbeans.teacherServiceDistribution.GlobalTSDProcessValuationForm" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.TeacherServiceDistributionDTOEntry" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<em><bean:message key="link.teacherServiceDistribution"/></em>
<h2><bean:message key="link.teacherServiceDistribution.tsdProcessVisualization"/></h2>

<p class="breadcumbs">
	<em>
		<html:link page='/tsdProcess.do?method=prepareTSDProcess'>
			<bean:message key="link.teacherServiceDistribution"/>
		</html:link>
		>
		<bean:message key="link.teacherServiceDistribution.tsdProcessVisualization"/>
	</em>
</p>

<html:form action="/globalTSDProcessValuation">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareForGlobalTSDProcessValuation"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.viewType" property="viewType"/>

<p><strong><bean:write name="departmentName"/></strong></p>
<table class='tstyle5 thlight thright  mtop05'>
<tr>
	<th>
		<bean:message key="label.teacherServiceDistribution.executionYear"/>:
	</th>
	<td>
		<html:select property="executionYear" onchange="this.form.submit();">
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
			<html:select property="executionPeriod" onchange="this.form.submit();">
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
	<bean:define id="tsdId" name="tsdProcess" property="idInternal" />
	<tr>
	 	<td class="highlight7" align="left" width="250">
			<html:link page='<%= "/globalTSDProcessValuation.do?method=showTSDProcess&amp;tsdProcess=" + tsdId %>'>
				<bean:write name="tsdProcess" property="name"/>
			</html:link>
		</td> 
		<td align="center">
			<bean:write name="tsdProcess" property="executionYear.year" />
		</td>
		<td align="center">
			<logic:iterate id="executionPeriod" name="tsdProcess" property="orderedExecutionPeriods">
				<bean:write name="executionPeriod" property="semester"/>&#186;&nbsp;
			</logic:iterate>
		</td>
	</tr>
</logic:iterate>
</table>
<br/>
</logic:notEmpty>
	
<br/>
<br/>
<html:link page="/tsdProcess.do?method=prepareForTSDProcessCreation">
	<bean:message key="link.back"/>
</html:link>	
</html:form>