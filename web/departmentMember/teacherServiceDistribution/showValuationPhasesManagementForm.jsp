<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhaseStatus" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<em><bean:message key="link.teacherServiceDistribution"/></em>
<h2><bean:message key="link.teacherServiceDistribution.tsdProcessPhasesManagement"/></h2>

<p class="breadcumbs">
	<em>
		<html:link page='/tsdProcess.do?method=prepareTSDProcess'>
		<bean:message key="link.teacherServiceDistribution"/>
		</html:link>
		>
		<html:link page='<%= "/tsdProcess.do?method=showTSDProcessServices&amp;tsdProcess=" + ((TSDProcess) request.getAttribute("tsdProcess")).getIdInternal().toString() %>'>
			<bean:write name="tsdProcess" property="name"/>&nbsp;
			<bean:write name="tsdProcess" property="executionYear.year"/>
		</html:link>
		>
		<bean:message key="link.teacherServiceDistribution.tsdProcessPhasesManagement"/>
	</em>
</p>

<html:form action="/tsdProcessPhasesManagement">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createTSDProcessPhase"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tsdProcess" property="tsdProcess"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tsdProcessPhase" property="tsdProcessPhase"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.isPublished" property="isPublished"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

<bean:define id="tsdProcess" name="tsdProcessPhasesManagementForm" property="tsdProcess"/>

<ul class="list5">
	<span class="switchInline"><li><a href="#" onclick="switchDisplay('createNewTSDProcessPhase')"><bean:message key="label.teacherServiceDistribution.createNewTSDProcessPhase"/></a></li></span>
</ul>


<div id="createNewTSDProcessPhase" class="switchNone">
<table class="tstyle5 mtop0 thlight">
	<tr>
		<th>
			<bean:message key="label.teacherServiceDistribution.name"/>:
		</th>
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


<span class="mtop15 mbottom05">
	<b><bean:message key="label.teacherServiceDistribution.existingTSDProcessPhases"/>:</b>
</span>

<table class='tstyle4 thlight mtop05'>
	<tr>
		<th>
			<b><bean:message key="label.teacherServiceDistribution.name"/></b>
		</th>
		<th>
			<b><bean:message key="label.teacherServiceDistribution.status"/></b>
		</th>
		<th>
			<b><bean:message key="label.teacherServiceDistribution.groupings.number"/></b>
		</th>
		<th>
		</th>		
		<th>
		</th>		
		<th>
		</th>		
		<th>
			<b><bean:message key="label.teacherServiceDistribution.isPublished"/></b>
		</th>		
	</tr>
	
	<bean:define id="deleteConfirm" type="java.lang.String">
		<bean:message key="message.confirm.submit.deleteTSDProcessPhase"/>
	</bean:define>
	
	
<logic:iterate name="tsdProcessPhaseList" id="tsdProcessPhase">
	<bean:define id="tsdProcessPhase" name="tsdProcessPhase" type="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase"/>
	<tr>
		<td class='aleft'>
			<bean:write name="tsdProcessPhase" property="name"/>
		</td>
		<td class='acenter'>
 			<bean:message name="tsdProcessPhase" property="status.qualifiedName" bundle="ENUMERATION_RESOURCES"/>
		</td>
		<td class='acenter'>
			<bean:write name="tsdProcessPhase" property="numberOfTeacherServiceDistributions"/>
		</td>
		<bean:define id="tsdProcessPhaseId" name="tsdProcessPhase" property="idInternal" />
		<td class='acenter'>
			<logic:equal name="tsdProcessPhase" property="status.name" value="<%=TSDProcessPhaseStatus.OPEN.name()%>">
				<%-- <html:link href="<%= request.getContextPath() + "/departmentMember/tsdProcessPhasesManagement.do?&method=setCurrentTSDProcessPhase&tsdProcessPhase=" + tsdProcessPhaseId + "&tsdProcess=" + tsdProcess %>">--%>
				 <html:link href="javascript:void(0)" onclick='<%= "document.forms[0].tsdProcessPhase.value=" + tsdProcessPhaseId + ";document.forms[0].method.value='setCurrentTSDProcessPhase'; document.forms[0].page.value=0; document.forms[0].submit();" %>'>
					<bean:message key="label.teacherServiceDistribution.setCurrent"/>
				</html:link>			
			</logic:equal>
			<logic:notEqual name="tsdProcessPhase" property="status.name" value="<%=TSDProcessPhaseStatus.OPEN.name()%>">
				<%-- <bean:message key="label.teacherServiceDistribution.setCurrent"/> --%>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</logic:notEqual>
		</td>
		<td class='acenter'>
			<logic:equal name="tsdProcessPhase" property="status.name" value="<%=TSDProcessPhaseStatus.OPEN.name()%>">
			<%--<html:link href="<%= request.getContextPath() + "/departmentMember/tsdProcessPhasesManagement.do?&method=closeTSDProcessPhase&tsdProcessPhase=" + tsdProcessPhaseId  + "&tsdProcess=" + tsdProcess%>">--%>
			<html:link href="javascript:void(0)" onclick='<%= "document.forms[0].tsdProcessPhase.value=" + tsdProcessPhaseId + ";document.forms[0].method.value='closeTSDProcessPhase'; document.forms[0].page.value=0; document.forms[0].submit();" %>'> 
				<bean:message key="label.teacherServiceDistribution.close"/>
			</html:link>
			</logic:equal>
			<logic:equal name="tsdProcessPhase" property="status.name" value="<%=TSDProcessPhaseStatus.CLOSED.name()%>">
				<%--<html:link href="<%= request.getContextPath() + "/departmentMember/tsdProcessPhasesManagement.do?&method=openTSDProcessPhase&tsdProcessPhase=" + tsdProcessPhaseId  + "&tsdProcess=" + tsdProcess%>">--%>
 				<html:link href="javascript:void(0)" onclick='<%= "document.forms[0].tsdProcessPhase.value=" + tsdProcessPhaseId + ";document.forms[0].method.value='openTSDProcessPhase'; document.forms[0].page.value=0; document.forms[0].submit();" %>'> 
					<bean:message key="label.teacherServiceDistribution.open"/>
				</html:link>
			</logic:equal>
		</td>
		<td class='acenter'>
			<logic:equal name="tsdProcessPhase" property="status.name" value="<%=TSDProcessPhaseStatus.CLOSED.name()%>">		
		 	<html:link href="javascript:void(0)" onclick='<%= "document.forms[0].tsdProcessPhase.value=" + tsdProcessPhaseId + ";document.forms[0].method.value='deleteTSDProcessPhase'; document.forms[0].page.value=0; if(confirm('" + deleteConfirm + "')) document.forms[0].submit();" %>'> 
				<bean:message key="label.teacherServiceDistribution.delete"/>
			</html:link>
			</logic:equal>
			<logic:notEqual name="tsdProcessPhase" property="status.name" value="<%=TSDProcessPhaseStatus.CLOSED.name()%>">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</logic:notEqual>
		</td>					
		<td class='acenter'>
			<logic:equal name="tsdProcessPhase" property="isPublished" value="true">

				<html:link href="javascript:void(0)" onclick='<%= "document.forms[0].tsdProcessPhase.value=" + tsdProcessPhaseId + ";document.forms[0].method.value='setPublishedStateOnTSDProcessPhase'; document.forms[0].isPublished.value='false'; document.forms[0].page.value=0; document.forms[0].submit();" %>'> 
					 <bean:message key="label.teacherServiceDistribution.unpublish"/>
				</html:link>
			</logic:equal>
			<logic:notEqual name="tsdProcessPhase" property="isPublished" value="true">
				 <html:link href="javascript:void(0)" onclick='<%= "document.forms[0].tsdProcessPhase.value=" + tsdProcessPhaseId + ";document.forms[0].method.value='setPublishedStateOnTSDProcessPhase'; document.forms[0].isPublished.value='true'; document.forms[0].page.value=0; document.forms[0].submit();" %>'> 
					 <bean:message key="label.teacherServiceDistribution.publish"/>
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

<html:link page='<%= "/tsdProcess.do?method=showTSDProcessServices&amp;tsdProcess=" + ((TSDProcess) request.getAttribute("tsdProcess")).getIdInternal().toString() %>'>
	<bean:message key="link.back"/>
</html:link>
