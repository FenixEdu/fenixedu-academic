<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhase" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationGrouping" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.formbeans.teacherServiceDistribution.GlobalTeacherServiceDistributionValuationForm" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.ValuationGroupingDTOEntry" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<em>
	<bean:message key="link.teacherServiceDistribution"/>
</em>

<h2><bean:message key="link.teacherServiceDistribution.teacherServiceDistributionVisualization"/></h2>

<ul>
	<li>
		<html:link page='/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution'>
			<bean:message key="link.back"/>
		</html:link>
	</li>
</ul>

<html:form action="/globalTeacherServiceDistributionValuation">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareForGlobalTeacherServiceDistributionValuation"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.viewType" property="viewType"/>

<table class='tstyle5 thlight thright thmiddle'>
<tr>
	<th>
		<bean:message key="label.teacherServiceDistribution.executionYear"/>:
	</th>
	<td>
		<html:select property="executionYear" onchange="this.form.submit();">
			<html:option value="-1">&nbsp;</html:option>
			<html:options collection="executionYearList" property="idInternal" labelProperty="year"/>
		</html:select>
	</td>
</tr>
<tr>
	<th>
		<bean:message key="label.teacherServiceDistribution.semester"/>:
	</th>
	<td>

		<html:select property="executionPeriod" onchange="this.form.submit();">
			<html:option value="-1">&nbsp;</html:option>
			<html:options collection="executionPeriodsList" property="idInternal" labelProperty="semester"/>
		</html:select>
	</td>
</tr>
</table>


<logic:empty name="teacherServiceDistributionList">
	<p>
		<em>
			<bean:message key="label.teacherServiceDistribution.noPublishedTeacherServiceDistributionsForExecutionPeriod"/>
		</em>
	</p>
</logic:empty>
<logic:notEmpty name="teacherServiceDistributionList">

<p>
	<b><bean:message key="label.teacherServiceDistribution.availableTeacherServiceDistributions"/>:</b>
</p>



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
		<td class="aleft">
			<html:checkbox alt="<%= teacherServiceDistributionProperty %>" property="<%= teacherServiceDistributionProperty %>" value="true"/>
			<bean:write name="teacherServiceDistribution" property="name"/>
		</td>
		<td class="aleft">
			<bean:define name="teacherServiceDistribution" property="orderedPublishedValuationPhases" id="orderedPublishedValuationPhases"/>
			<html:select property="<%= valuationPhaseProperty %>">
				<html:options collection="orderedPublishedValuationPhases" property="idInternal" labelProperty="name"/>
			</html:select>
		</td>
		<td class="aleft">
		
		<%  request.setAttribute("groupings", ValuationGroupingDTOEntry.getValuationGroupingOptionEntries(((TeacherServiceDistribution)teacherServiceDistribution).getCurrentValuationPhase())); %>
				
			<bean:define name="groupings" id="groupings"/>
			<html:select property="<%= valuationGroupingProperty %>">
				<html:options collection="groupings" property="idInternal" labelProperty="name"/>
			</html:select>
		</td>
	</tr>
</logic:iterate>
</table>

<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='viewGlobalTeacherServiceDistributionValuation'; this.form.submit()">
	<bean:message key="label.teacherServiceDistribution.view"/>
</html:button>

</logic:notEmpty>


</html:form>