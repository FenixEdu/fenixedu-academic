<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<table>
	<tr>
		<logic:present name="infoCurricularCourse">
			<td>
				<h3><bean:message key="label.manager.curricularCourse.administrating"/></h3>
			</td>
			<td>
				<h2><b><bean:write name="infoCurricularCourse" property="name"/></b></h2>
			</td>
		</logic:present>		
	</tr>
</table>

<ul style="list-style-type: square;">
	<li><html:link page="<%="/editCurricularCourse.do?method=prepareEdit&amp;degreeCurricularPlanId="  + request.getAttribute("degreeCurricularPlanId")%>"  paramId="curricularCourseId" paramName="curricularCourseId"><bean:message key="label.manager.edit.curricularCourse"/></html:link></li>
</ul>

<h3><bean:message key="label.manager.executionCourses"/></h3>

<logic:empty name="executionCoursesList">
<i><bean:message key="label.manager.executionCourses.nonExisting"/></i>
</logic:empty>

<bean:define id="degreeCurricularPlanId" name="degreeCurricularPlanId"/>
<bean:define id="curricularCourseId" name="curricularCourseId"/>

<logic:present name="executionCoursesList" scope="request">
<logic:notEmpty name="executionCoursesList">
	
	<html:form action="/deleteCurricularCourses" method="get">
		<html:hidden property="degreeCurricularPlanId" value="<%= degreeCurricularPlanId.toString() %>"/>
		<html:hidden property="curricularCourseId" value="<%= curricularCourseId.toString() %>"/>
			<table width="50%" cellpadding="0" border="0">
				<tr>
					<%--<td class="listClasses-header">
					</td>--%>
					<td class="listClasses-header"><bean:message key="label.manager.executionCourse.name" />
					</td>
					<td class="listClasses-header"><bean:message key="label.manager.executionCourse.code" />
					</td>
					<td class="listClasses-header"><bean:message key="label.manager.executionCourse.executionPeriod" />
					</td>
				</tr>
				
				<logic:iterate id="executionCourse" name="executionCoursesList">
				<bean:define id="infoExecutionPeriod" name="executionCourse" property="infoExecutionPeriod"/>
				<tr>	 
					<%--<td class="listClasses">
						<html:multibox property="internalIds">
							<bean:write name="executionCourse" property="idInternal"/>
						</html:multibox>
					</td>--%>				
					<td class="listClasses"><bean:write name="executionCourse" property="nome"/>
					</td>
					<td class="listClasses"><bean:write name="executionCourse" property="sigla"/>
					</td>
					<td class="listClasses"><bean:write name="infoExecutionPeriod" property="name"/>
					</td>
	 			</tr>
	 			</logic:iterate>			
			
			</table>
			
<br>

		<%--<html:submit><bean:message key="label.manager.delete.selected.executionCourses"/></html:submit>--%>
	</html:form> 
</logic:notEmpty>	 	
</logic:present>


<h3><bean:message key="label.manager.studentCurricularPlans"/></h3>

<logic:empty name="studentCurricularPlansList">
<i><bean:message key="label.manager.studentCurricularPlans.nonExisting"/></i>
</logic:empty>

<bean:define id="degreeCurricularPlanId" name="degreeCurricularPlanId"/>
<bean:define id="curricularCourseId" name="curricularCourseId"/>

<logic:present name="studentCurricularPlansList" scope="request">
<logic:notEmpty name="studentCurricularPlansList">
	
	
		<html:hidden property="degreeCurricularPlanId" value="<%= degreeCurricularPlanId.toString() %>"/>
		<html:hidden property="curricularCourseId" value="<%= curricularCourseId.toString() %>"/>
			<table width="50%" cellpadding="0" border="0">
				<tr>
					<td class="listClasses-header"><bean:message key="label.manager.studentCurricularPlan.name" />
					</td>
					<td class="listClasses-header"><bean:message key="label.manager.studentCurricularPlan.code" />
					</td>
				</tr>
				
				<logic:iterate id="studentCurricularPlan" name="studentCurricularPlansList">
				<tr>	 
					<td class="listClasses"><bean:write name="studentCurricularPlan" property="name"/>
					</td>
					<td class="listClasses"><bean:write name="studentCurricularPlans" property="code"/>
					</td>
	 			</tr>
	 			</logic:iterate>			
			
			</table>
			
<br>

		</html:form> 
</logic:notEmpty>	 	
</logic:present>


<br>
<span class="error"><html:errors/></span>
<br>