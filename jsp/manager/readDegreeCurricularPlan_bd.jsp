<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="Util.Data" %>
<%@ page import="java.util.Date" %>

<table>
	<tr>
		<logic:present name="infoDegreeCurricularPlan">
			<td>
				<h3><bean:message key="label.manager.degreeCurricularPlan.administrating"/></h3>
			</td>
			<td>
				<h2><b><bean:write name="infoDegreeCurricularPlan" property="name"/></b></h2>
			</td>
		</logic:present>		
	</tr>
</table>

<ul style="list-style-type: square;">
	<li><html:link page="<%="/editDegreeCurricularPlan.do?method=prepareEdit&amp;degreeId="  + request.getAttribute("degreeId")%>"  paramId="degreeCurricularPlanId" paramName="degreeCurricularPlanId"><bean:message key="label.manager.edit.degreeCurricularPlan"/></html:link></li>
	<li><html:link page="<%="/insertCurricularCourse.do?method=prepareInsert&degreeId=" + request.getAttribute("degreeId")%>" paramId="degreeCurricularPlanId" paramName="degreeCurricularPlanId"><bean:message key="label.manager.insert.curricularCourse"/></html:link></li>			
	<li><html:link page="<%="/insertExecutionDegree.do?method=prepareInsert&degreeId=" + request.getAttribute("degreeId")%>" paramId="degreeCurricularPlanId" paramName="degreeCurricularPlanId"><bean:message key="label.manager.insert.executionDegree"/></html:link></li>			
</ul>

<h3><bean:message key="label.manager.curricularCourses"/></h3>

<logic:empty name="curricularCoursesList">
<i><bean:message key="label.manager.curricularCourses.nonExisting"/></i>
</logic:empty>

<bean:define id="degreeCurricularPlanId" name="degreeCurricularPlanId"/>
<bean:define id="degreeId" name="degreeId"/>

<logic:present name="curricularCoursesList" scope="request">
<logic:notEmpty name="curricularCoursesList">
	
	<html:form action="/deleteCurricularCourses" method="get">
		<html:hidden property="degreeCurricularPlanId" value="<%= degreeCurricularPlanId.toString() %>"/>
		<html:hidden property="degreeId" value="<%= degreeId.toString() %>"/>
			<table width="50%" cellpadding="0" border="0">
				<tr>
					<td class="listClasses-header">
			
					</td>
					<td class="listClasses-header"><bean:message key="label.manager.curricularCourse.name" />
					</td>
					<td class="listClasses-header"><bean:message key="label.manager.curricularCourse.code" />
					</td>
				</tr>
				<logic:iterate id="curricularCourse" name="curricularCoursesList">
				<tr>	 
					<td class="listClasses">
						<html:multibox property="internalIds">
							<bean:write name="curricularCourse" property="idInternal"/>
						</html:multibox>
					</td>				
					<td class="listClasses"><html:link page="<%= "/readCurricularCourse.do?degreeId=" + request.getAttribute("degreeId") + "&degreeCurricularPlanId=" + request.getAttribute("degreeCurricularPlanId")%>" paramId="curricularCourseId" paramName="curricularCourse" paramProperty="idInternal"><bean:write name="curricularCourse" property="name"/></html:link>
					</td>
					<td class="listClasses"><html:link page="<%= "/readCurricularCourse.do?degreeId=" + request.getAttribute("degreeId") + "&degreeCurricularPlanId=" + request.getAttribute("degreeCurricularPlanId")%>" paramId="curricularCourseId" paramName="curricularCourse" paramProperty="idInternal"><bean:write name="curricularCourse" property="code"/></html:link>
					</td>
	 			</tr>
	 			</logic:iterate>			
				<span class="error"><html:errors/></span>		
			</table>
			
<br>	

		<html:submit><bean:message key="label.manager.delete.selected.curricularCourses"/></html:submit>
	</html:form> 
</logic:notEmpty>	 	
</logic:present>

<br>

<h3><bean:message key="label.manager.executionDegrees"/></h3>

<logic:empty name="executionDegreesList">
<i><bean:message key="label.manager.executionDegrees.nonExisting"/></i>
</logic:empty>

<logic:present name="executionDegreesList" scope="request">
<logic:notEmpty name="executionDegreesList">
	
	<html:form action="/deleteExecutionDegrees" method="get">
		<html:hidden property="degreeCurricularPlanId" value="<%= degreeCurricularPlanId.toString() %>"/>
		<html:hidden property="degreeId" value="<%= degreeId.toString() %>"/>
			<table width="50%" cellpadding="0" border="0">
				<tr>
					<td class="listClasses-header">
			
					</td>
					<td class="listClasses-header"><bean:message key="label.manager.executionDegree.executionYear" />
					</td>
					<td class="listClasses-header"><bean:message key="label.manager.executionDegree.coordinator" />
					</td>
				</tr>
				<logic:iterate id="executionDegree" name="executionDegreesList">
				<tr>	 
					<td class="listClasses">
						<html:multibox property="internalIds">
							<bean:write name="executionDegree" property="idInternal"/>
						</html:multibox>
					</td>
					<bean:define id="executionYear" name="executionDegree" property="infoExecutionYear"/>				
					<td class="listClasses"><bean:write name="executionYear" property="year"/>
					</td>
					<bean:define id="coordinator" name="executionDegree" property="infoCoordinator"/>
					<bean:define id="person" name="coordinator" property="infoPerson"/>
					<td class="listClasses"><html:link page="/readTeacher.do"><bean:write name="person" property="nome"/></html:link>
					</td>
	 			</tr>
	 			</logic:iterate>				
				<span class="error"><html:errors/></span>		
			</table>
			
<br>	

		<html:submit><bean:message key="label.manager.delete.selected.executionDegrees"/></html:submit>
	</html:form> 
</logic:notEmpty>	 	
</logic:present>