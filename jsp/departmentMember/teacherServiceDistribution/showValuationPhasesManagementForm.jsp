<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhase" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhaseStatus" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution" %>
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
	<bean:message key="link.teacherServiceDistribution.valuationPhasesManagement"/>
</h3>

<html:form action="/valuationPhasesManagement">
<html:hidden property="method" value=""/>
<html:hidden property="teacherServiceDistribution"/>
<html:hidden property="valuationPhase"/>
<html:hidden property="isPublished"/>
<html:hidden property="page" value="0"/>

<table class="vtsbc">
	<tr>
		<th colspan="2">
			<b><bean:message key="label.teacherServiceDistribution.createNewValuationPhase"/></b>
		</th>
	</tr>
	<tr>
		<td align="left">
			<b><bean:message key="label.teacherServiceDistribution.name"/>:</b> 
		</td>
		<td>
			<html:text property="name" size="24" maxlength="240"/> 
			&nbsp;&nbsp;&nbsp;
			<html:button property="" onclick="this.form.method.value='createValuationPhase'; this.form.page.value=1; this.form.submit();">
			<bean:message key="label.teacherServiceDistribution.create"/>
		</html:button>
		</td>
	</tr>
</table>

<br/>
<span class="error">
	<html:errors property="name"/>
</span>
<br/>
<br/>

<b><bean:message key="label.teacherServiceDistribution.existingValuationPhases"/>:</b>
<table class='vtsbc'>
	<tr>
		<th>
			<bean:message key="label.teacherServiceDistribution.name"/>
		</th>
		<th>
			<bean:message key="label.teacherServiceDistribution.status"/>
		</th>
		<th>
			<bean:message key="label.teacherServiceDistribution.groupings.number"/>
		</th>
		<th>
		</th>		
		<th>
		</th>		
		<th>
		</th>		
		<th>
			<bean:message key="label.teacherServiceDistribution.isPublished"/>
		</th>		
	</tr>
	
<logic:iterate name="valuationPhaseList" id="valuationPhase">
	<bean:define id="valuationPhase" name="valuationPhase" type="net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhase"/>
	<tr class='center'>
		<td class='courses'>
			<bean:write name="valuationPhase" property="name"/>
		</td>
		<td>
 			<bean:message name="valuationPhase" property="status.qualifiedName" bundle="ENUMERATION_RESOURCES"/>
		</td>
		<td>
			<bean:write name="valuationPhase" property="numberOfValuationGroupings"/>
		</td>
		<bean:define id="valuationPhaseId" name="valuationPhase" property="idInternal" />
		<td>
			<logic:equal name="valuationPhase" property="status.name" value="<%=ValuationPhaseStatus.OPEN.name()%>">
				<html:link href='<%= "javascript:document.valuationPhasesManagementForm.valuationPhase.value=" + valuationPhaseId + ";document.valuationPhasesManagementForm.method.value='setCurrentValuationPhase'; document.valuationPhasesManagementForm.submit();" %>'>
					<bean:message key="label.teacherServiceDistribution.setCurrent"/>
				</html:link>			
			</logic:equal>
			<logic:notEqual name="valuationPhase" property="status.name" value="<%=ValuationPhaseStatus.OPEN.name()%>">
				<%-- <bean:message key="label.teacherServiceDistribution.setCurrent"/> --%>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</logic:notEqual>
		</td>
		<td>
			<logic:equal name="valuationPhase" property="status.name" value="<%=ValuationPhaseStatus.OPEN.name()%>">
			<html:link href='<%= "javascript:document.valuationPhasesManagementForm.valuationPhase.value=" + valuationPhaseId + ";document.valuationPhasesManagementForm.method.value='closeValuationPhase'; document.valuationPhasesManagementForm.submit();" %>'> 
				<bean:message key="label.teacherServiceDistribution.close"/>
			</html:link>
			</logic:equal>
			<logic:equal name="valuationPhase" property="status.name" value="<%=ValuationPhaseStatus.CLOSED.name()%>">
				<html:link href='<%= "javascript:document.valuationPhasesManagementForm.valuationPhase.value=" + valuationPhaseId + ";document.valuationPhasesManagementForm.method.value='openValuationPhase'; document.valuationPhasesManagementForm.submit();" %>'> 
					<bean:message key="label.teacherServiceDistribution.open"/>
				</html:link>
			</logic:equal>
		</td>
		<td>
			<logic:equal name="valuationPhase" property="status.name" value="<%=ValuationPhaseStatus.CLOSED.name()%>">
			<html:link href='<%= "javascript:document.valuationPhasesManagementForm.valuationPhase.value=" + ((ValuationPhase) valuationPhase).getIdInternal() + ";document.valuationPhasesManagementForm.method.value='deleteValuationPhase'; document.valuationPhasesManagementForm.submit();" %>'> 
				<bean:message key="label.teacherServiceDistribution.delete"/>
			</html:link>
			</logic:equal>
			<logic:notEqual name="valuationPhase" property="status.name" value="<%=ValuationPhaseStatus.CLOSED.name()%>">
				<%-- <bean:message key="label.teacherServiceDistribution.delete"/> --%>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</logic:notEqual>
		</td>					
		<td>
			<logic:equal name="valuationPhase" property="isPublished" value="true">
				<html:link href='<%= "javascript: document.valuationPhasesManagementForm.valuationPhase.value=" + ((ValuationPhase) valuationPhase).getIdInternal() + ";document.valuationPhasesManagementForm.method.value='setPublishedStateOnValuationPhase'; document.valuationPhasesManagementForm.isPublished.value='false'; document.valuationPhasesManagementForm.submit();" %>'> 
					<bean:message key="label.yes"/>
				</html:link>
			</logic:equal>
			<logic:notEqual name="valuationPhase" property="isPublished" value="true">
				<html:link href='<%= "javascript: document.valuationPhasesManagementForm.valuationPhase.value=" + ((ValuationPhase) valuationPhase).getIdInternal() + ";document.valuationPhasesManagementForm.method.value='setPublishedStateOnValuationPhase'; document.valuationPhasesManagementForm.isPublished.value='true'; document.valuationPhasesManagementForm.submit();" %>'> 
					<bean:message key="label.no"/>
				</html:link>
			</logic:notEqual>
		</td>					
	</tr>
</logic:iterate>
</table>
<br/>

<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
	<bean:message key="link.back"/>
</html:link>

</html:form>
