<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-string.tld" prefix="str" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoPeriod" %>
<table>
	<tr>
		<logic:present name="infoDegreeCurricularPlan">
			<td>
				<h3><bean:message bundle="MANAGER_RESOURCES" key="label.manager.degreeCurricularPlan.administrating"/></h3>
			</td>
			<td>
				<h2><b><bean:write name="infoDegreeCurricularPlan" property="name"/></b></h2>
			</td>
		</logic:present>		
	</tr>
</table>
<bean:define id="degreeCurricularPlanId" name="infoDegreeCurricularPlan" property="idInternal"/>
<ul style="list-style-type: square;">
	<li><html:link module="/manager" page="<%="/editDegreeCurricularPlan.do?method=prepareEdit&degreeId="  + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")%>"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.edit.degreeCurricularPlan"/></html:link></li>
	<li><html:link module="/manager" page="<%="/insertCurricularCourse.do?method=prepareInsert&degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")%>"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.curricularCourse"/></html:link></li>
	<li><html:link module="/manager" page="<%="/manageBranches.do?method=showBranches&degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")%>"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.branches.management"/></html:link></li>
	<li><html:link module="/manager" page="<%="/manageCurricularCourseGroups.do?method=viewCurricularCourseGroups&degreeId=" + request.getParameter("degreeId")%>" paramId="degreeCurricularPlanId" paramName="degreeCurricularPlanId"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.curricularCourseGroups.management"/></html:link></li>
	<li><html:link module="/manager" page="<%="/managePrecedences.do?method=showMenu&degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")%>"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.precedences.management"/></html:link></li>
<%--	<li><html:link module="/manager" page="<%="/insertExecutionDegree.do?method=prepareInsert&degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")%>"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.executionDegree"/></html:link></li>--%>
</ul>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<br/>
<h3><bean:message bundle="MANAGER_RESOURCES" key="label.manager.curricularCourses"/></h3>

<logic:empty name="curricularCoursesList">
<i><bean:message bundle="MANAGER_RESOURCES" key="label.manager.curricularCourses.nonExisting"/></i>
</logic:empty>

<logic:present name="curricularCoursesList" scope="request">
<logic:notEmpty name="curricularCoursesList">
	
	<html:form action="/deleteCurricularCourses" method="get">
	
	 <bean:define id="onclick">
			return confirm('<bean:message bundle="MANAGER_RESOURCES" key="message.confirm.delete.curricular.courses"/>')
	 </bean:define>
	
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanId" property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeId" property="degreeId" value="<%= request.getParameter("degreeId") %>"/>
			<table width="100%" cellpadding="0" border="0">
				<tr>
					<th class="listClasses-header">
					</th>
					<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.curricularCourse.name" />
					</th>
					<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.curricularCourse.code" />
					</th>
					<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.curricularCourse.acronym" />
					</th>
				</tr>
				<logic:iterate id="curricularCourse" name="curricularCoursesList">
				<tr>	 
					<td class="listClasses">
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.internalIds" property="internalIds">
							<bean:write name="curricularCourse" property="idInternal"/>
						</html:multibox>
					</td>				
					<td class="listClasses"><p align="left"><html:link module="/manager" page="<%= "/readCurricularCourse.do?degreeId=" + request.getParameter("degreeId") + "&degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")%>" paramId="curricularCourseId" paramName="curricularCourse" paramProperty="idInternal"><bean:write name="curricularCourse" property="name"/></html:link></p>
					</td>
					<td class="listClasses"><html:link module="/manager" page="<%= "/readCurricularCourse.do?degreeId=" + request.getParameter("degreeId") + "&degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")%>" paramId="curricularCourseId" paramName="curricularCourse" paramProperty="idInternal"><bean:write name="curricularCourse" property="code"/></html:link>
					</td>
					<td class="listClasses"><html:link module="/manager" page="<%= "/readCurricularCourse.do?degreeId=" + request.getParameter("degreeId") + "&degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")%>" paramId="curricularCourseId" paramName="curricularCourse" paramProperty="idInternal"><bean:write name="curricularCourse" property="acronym"/></html:link>
					</td>
	 			</tr>
	 			</logic:iterate>			
			</table>
			
<br/>

		<html:submit bundle="HTMLALT_RESOURCES" altKey='submit.submit' onclick='<%=onclick.toString() %>'><bean:message bundle="MANAGER_RESOURCES" key="label.manager.delete.selected.curricularCourses"/></html:submit>
	</html:form> 
</logic:notEmpty>	 	
</logic:present>

<br/>

</logic:present>