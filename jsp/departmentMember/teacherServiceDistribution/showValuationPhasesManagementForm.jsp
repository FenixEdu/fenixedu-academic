<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhase" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhaseStatus" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<h3>
	<html:link page='/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution'>
		<bean:message key="link.teacherServiceDistribution"/>
	</html:link>
	>
	<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
		<bean:write name="teacherServiceDistribution" property="name"/>&nbsp;
<%--		(<bean:write name="teacherServiceDistribution" property="executionPeriod.semester"/>�<bean:message key="label.common.courseSemester"/>--%>
		<bean:write name="teacherServiceDistribution" property="executionYear.year"/>
	</html:link>
	>
	<bean:message key="link.teacherServiceDistribution.valuationPhasesManagement"/>
</h3>

<html:form action="/valuationPhasesManagement">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createValuationPhase"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherServiceDistribution" property="teacherServiceDistribution"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.valuationPhase" property="valuationPhase"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.isPublished" property="isPublished"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

<bean:define id="teacherServiceDistribution" name="valuationPhasesManagementForm" property="teacherServiceDistribution"/>

<ul class="list5">
	<li>
		<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
			<bean:message key="link.back"/>
		</html:link>
	</li>
	<span class="switchInline"><li><a href="#" onclick="switchDisplay('createNewValuationPhase')"><bean:message key="label.teacherServiceDistribution.createNewValuationPhase"/></a></li></span>
</ul>



<div id="createNewValuationPhase" class="switchNone">
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
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name" size="24" maxlength="240"/> 
			&nbsp;&nbsp;&nbsp;
			<html:submit> 
			<bean:message key="label.teacherServiceDistribution.create"/>
			</html:submit>
		</td>
	</tr>
</table>
</div>

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
				<html:link href="<%= request.getContextPath() + "/departmentMember/valuationPhasesManagement.do?&method=setCurrentValuationPhase&valuationPhase=" + valuationPhaseId + "&teacherServiceDistribution=" + teacherServiceDistribution %>">
				<%-- <html:link href='<%= "javascript:document.valuationPhasesManagementForm.valuationPhase.value=" + valuationPhaseId + ";document.valuationPhasesManagementForm.method.value='setCurrentValuationPhase'; document.valuationPhasesManagementForm.submit();" %>'>
					--%> <bean:message key="label.teacherServiceDistribution.setCurrent"/>
				</html:link>			
			</logic:equal>
			<logic:notEqual name="valuationPhase" property="status.name" value="<%=ValuationPhaseStatus.OPEN.name()%>">
				<%-- <bean:message key="label.teacherServiceDistribution.setCurrent"/> --%>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</logic:notEqual>
		</td>
		<td>
			<logic:equal name="valuationPhase" property="status.name" value="<%=ValuationPhaseStatus.OPEN.name()%>">
			<html:link href="<%= request.getContextPath() + "/departmentMember/valuationPhasesManagement.do?&method=closeValuationPhase&valuationPhase=" + valuationPhaseId  + "&teacherServiceDistribution=" + teacherServiceDistribution%>">
			<%-- <html:link href="<%= "javascript:document.valuationPhasesManagementForm.valuationPhase.value=" + valuationPhaseId + ";document.valuationPhasesManagementForm.method.value='closeValuationPhase'; document.valuationPhasesManagementForm.submit();" %>"> 
				--%> <bean:message key="label.teacherServiceDistribution.close"/>
			</html:link>
			</logic:equal>
			<logic:equal name="valuationPhase" property="status.name" value="<%=ValuationPhaseStatus.CLOSED.name()%>">
				<html:link href="<%= request.getContextPath() + "/departmentMember/valuationPhasesManagement.do?&method=openValuationPhase&valuationPhase=" + valuationPhaseId  + "&teacherServiceDistribution=" + teacherServiceDistribution%>">
<%-- 				<html:link href='<%= "javascript:document.valuationPhasesManagementForm.valuationPhase.value=" + valuationPhaseId + ";document.valuationPhasesManagementForm.method.value='openValuationPhase'; document.valuationPhasesManagementForm.submit();" %>'> 
					--%> 
					<bean:message key="label.teacherServiceDistribution.open"/>
				</html:link>
			</logic:equal>
		</td>
		<td>
			<logic:equal name="valuationPhase" property="status.name" value="<%=ValuationPhaseStatus.CLOSED.name()%>">
		
			<html:link href="<%= request.getContextPath() + "/departmentMember/valuationPhasesManagement.do?&method=deleteValuationPhase&valuationPhase=" + ((ValuationPhase) valuationPhase).getIdInternal() + "&teacherServiceDistribution=" + teacherServiceDistribution%>">
		<%-- 	<html:link href='<%= "javascript:document.valuationPhasesManagementForm.valuationPhase.value=" + ((ValuationPhase) valuationPhase).getIdInternal() + ";document.valuationPhasesManagementForm.method.value='deleteValuationPhase'; document.valuationPhasesManagementForm.submit();" %>'> 
			--%>	<bean:message key="label.teacherServiceDistribution.delete"/>
			</html:link>
			</logic:equal>
			<logic:notEqual name="valuationPhase" property="status.name" value="<%=ValuationPhaseStatus.CLOSED.name()%>">
				<%-- <bean:message key="label.teacherServiceDistribution.delete"/> --%>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</logic:notEqual>
		</td>					
		<td>
			<logic:equal name="valuationPhase" property="isPublished" value="true">
				<html:link href="<%= request.getContextPath() + "/departmentMember/valuationPhasesManagement.do?&method=setPublishedStateOnValuationPhase&isPublished=false&valuationPhase=" + ((ValuationPhase) valuationPhase).getIdInternal() + "&teacherServiceDistribution=" + teacherServiceDistribution%>">
				<%-- <html:link href='<%= "javascript: document.valuationPhasesManagementForm.valuationPhase.value=" + ((ValuationPhase) valuationPhase).getIdInternal() + ";document.valuationPhasesManagementForm.method.value='setPublishedStateOnValuationPhase'; document.valuationPhasesManagementForm.isPublished.value='false'; document.valuationPhasesManagementForm.submit();" %>'> 
					--%> <bean:message key="label.unpublish"/>
				</html:link>
			</logic:equal>
			<logic:notEqual name="valuationPhase" property="isPublished" value="true">
				<html:link href="<%= request.getContextPath() + "/departmentMember/valuationPhasesManagement.do?&method=setPublishedStateOnValuationPhase&isPublished=true&valuationPhase=" + ((ValuationPhase) valuationPhase).getIdInternal() + "&teacherServiceDistribution=" + teacherServiceDistribution%>">
				<%-- <html:link href='<%= "javascript: document.valuationPhasesManagementForm.valuationPhase.value=" + ((ValuationPhase) valuationPhase).getIdInternal() + ";document.valuationPhasesManagementForm.method.value='setPublishedStateOnValuationPhase'; document.valuationPhasesManagementForm.isPublished.value='true'; document.valuationPhasesManagementForm.submit();" %>'> 
					--%> <bean:message key="label.publish"/>
				</html:link>
			</logic:notEqual>
		</td>					
	</tr>
</logic:iterate>
</table>
<br/>



</html:form>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>
