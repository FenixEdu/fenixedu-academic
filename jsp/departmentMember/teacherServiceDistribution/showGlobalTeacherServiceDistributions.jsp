<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhase" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationGrouping" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.formbeans.teacherServiceDistribution.GlobalTeacherServiceDistributionValuationForm" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.ValuationGroupingDTOEntry" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

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
table.vtsbc td.courses {
background-color: #f4f4f8;
width: 300px;
padding: 0.25em 0.25em;
text-align: left;
}
.center {
text-align: center;
}

.right td {
text-align: right;
}

.left td {
text-align: left;
}

table.search {
background-color: #f5f5f5;
border-collapse: collapse;
}
table.search tr td {
border: 1px solid #fff;
padding: 0.3em;
}
.leftcolumn {
text-align: right;
}
</style>


<h3>
	<html:link page='/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution'>
		<bean:message key="link.teacherServiceDistribution"/>
	</html:link>
	>
	<bean:message key="link.teacherServiceDistribution.teacherServiceDistributionVisualization"/>
</h3>


<html:form action="/globalTeacherServiceDistributionValuation">
<html:hidden property="method" value="prepareForGlobalTeacherServiceDistributionValuation"/>
<html:hidden property="viewType"/>

<table class='vtsbc'>
<tr>
	<td>
		<b><bean:message key="label.teacherServiceDistribution.executionYear"/>:</b>
		&nbsp;&nbsp;
		<html:select property="executionYear" onchange="this.form.submit();">
			<html:option value="-1">&nbsp;</html:option>
			<html:options collection="executionYearList" property="idInternal" labelProperty="year"/>
		</html:select>
	</td>
	<td>
		<b><bean:message key="label.teacherServiceDistribution.semester"/>:</b>
		&nbsp;&nbsp;
		<html:select property="executionPeriod" onchange="this.form.submit();">
			<html:option value="-1">&nbsp;</html:option>
			<html:options collection="executionPeriodsList" property="idInternal" labelProperty="semester"/>
		</html:select>
	</td>
</tr>
</table>

<br/>
<br/>

<logic:empty name="teacherServiceDistributionList">
<span class="error">
	<bean:message key="label.teacherServiceDistribution.noPublishedTeacherServiceDistributionsForExecutionPeriod"/>
</span>
</logic:empty>
<logic:notEmpty name="teacherServiceDistributionList">
<b><bean:message key="label.teacherServiceDistribution.availableTeacherServiceDistributions"/>:</b>
<br/>

<table class='vtsbc'>
	<tr>
		<th>
			<bean:message key="label.teacherServiceDistribution.name"/>
		</th>
		<th>
			<bean:message key="label.teacherServiceDistribution.valuationPhase"/>
		</th>
		<th>
			<bean:message key="label.teacherServiceDistribution.ValuationGrouping"/>
		</th>
	</tr>
<logic:iterate name="teacherServiceDistributionList" id="teacherServiceDistribution">
	<bean:define name="teacherServiceDistribution" id="teacherServiceDistribution"/>
	<% 
		String teacherServiceDistributionProperty = "teacherServiceDistribution(" + ((TeacherServiceDistribution) teacherServiceDistribution).getIdInternal().toString() + ")";
		String valuationPhaseProperty = "valuationPhase(" + ((TeacherServiceDistribution) teacherServiceDistribution).getIdInternal().toString() + ")";
		String valuationGroupingProperty = "valuationGrouping(" + ((TeacherServiceDistribution) teacherServiceDistribution).getIdInternal().toString() + ")"; 
	%>
	<tr>
		<td align="left">
			<html:checkbox property="<%= teacherServiceDistributionProperty %>" value="true"/>
			<bean:write name="teacherServiceDistribution" property="name"/>
		</td>
		<td align="left">
			<bean:define name="teacherServiceDistribution" property="orderedPublishedValuationPhases" id="orderedPublishedValuationPhases"/>
			<html:select property="<%= valuationPhaseProperty %>">
				<html:options collection="orderedPublishedValuationPhases" property="idInternal" labelProperty="name"/>
			</html:select>
		</td>
		<td align="left">
		
		<%  request.setAttribute("groupings", ValuationGroupingDTOEntry.getValuationGroupingOptionEntries(((TeacherServiceDistribution)teacherServiceDistribution).getCurrentValuationPhase())); %>
				
			<bean:define name="groupings" id="groupings"/>
			<html:select property="<%= valuationGroupingProperty %>">
				<html:options collection="groupings" property="idInternal" labelProperty="name"/>
			</html:select>
		</td>
	</tr>
</logic:iterate>
</table>

<html:button property="" onclick="this.form.method.value='viewGlobalTeacherServiceDistributionValuation'; this.form.submit()">
	<bean:message key="label.teacherServiceDistribution.view"/>
</html:button>

</logic:notEmpty>
	
<br/>
<br/>
<html:link page="/teacherServiceDistribution.do?method=prepareForTeacherServiceDistributionCreation">
	<bean:message key="link.back"/>
</html:link>	
</html:form>