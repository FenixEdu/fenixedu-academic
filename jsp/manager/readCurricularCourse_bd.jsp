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
	<li><html:link page="<%="/editCurricularCourse.do?method=prepareEdit&amp;degreeId=" + request.getAttribute("degreeId") + "&amp;degreeCurricularPlanId=" + request.getAttribute("degreeCurricularPlanId")%>" paramId="curricularCourseId" paramName="curricularCourseId"><bean:message key="label.manager.edit.curricularCourse"/></html:link></li>
	<li><html:link page="<%="/insertCurricularCourseScope.do?method=prepareInsert&amp;degreeId=" + request.getAttribute("degreeId") + "&amp;degreeCurricularPlanId=" + request.getAttribute("degreeCurricularPlanId")%>" paramId="curricularCourseId" paramName="curricularCourseId"><bean:message key="label.manager.insert.curricularCourseScope"/></html:link></li>
</ul>

<bean:define id="degreeCurricularPlanId" name="degreeCurricularPlanId"/>
<bean:define id="curricularCourseId" name="curricularCourseId"/>
<bean:define id="degreeId" name="degreeId"/>

	
<html:form action="/readCurricularCourse">
	<h3><bean:message key="label.manager.executionCourses"/></h3>

<logic:empty name="executionCoursesList">
		<i><bean:message key="label.manager.executionCourses.nonExisting"/></i>
	</logic:empty>
	
	<logic:present name="executionCoursesList" scope="request">
		<logic:notEmpty name="executionCoursesList">

			<html:hidden property="degreeCurricularPlanId" value="<%= degreeCurricularPlanId.toString() %>"/>
			<html:hidden property="curricularCourseId" value="<%= curricularCourseId.toString() %>"/>
			<html:hidden property="degreeId" value="<%= degreeId.toString() %>"/>
			
			<table width="50%" cellpadding="0" border="0">
				<tr>
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
		</logic:notEmpty>	 	
	</logic:present>
	
<span class="error"><html:errors/></span>
	
	<h3><bean:message key="label.manager.curricularCourseScopes"/></h3>

	<logic:empty name="curricularCourseScopesList">
		<i><bean:message key="label.manager.curricularCourseScopes.nonExisting"/></i>
	</logic:empty>

	<logic:present name="curricularCourseScopesList" scope="request">
		<logic:notEmpty name="curricularCourseScopesList">
	
			<html:hidden property="degreeCurricularPlanId" value="<%= degreeCurricularPlanId.toString() %>"/>
			<html:hidden property="curricularCourseId" value="<%= curricularCourseId.toString() %>"/>
			<html:hidden property="degreeId" value="<%= degreeId.toString() %>"/>
			
		<table width="70%" cellpadding="0" border="0">
				<tr>
					<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.theoreticalHours" />
					</td>
					<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.praticalHours" />
					</td>
					<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.theoPratHours" />
					</td>
					<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.labHours" />
					</td>
					<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.maxIncrementNac" />
					</td>
					<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.minIncrementNac" />
					</td>
					<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.credits" />
					</td>
					<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.weigth" />
					</td>
					<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.branch" />
					</td>
					<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.curricularYear" />
					</td>
					<td class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.curricularSemester" />
					</td>
					
				</tr>
				<logic:iterate id="curricularCourseScope" name="curricularCourseScopesList">
				
					<bean:define id="infoBranch" name="curricularCourseScope" property="infoBranch"/>
					<bean:define id="infoCurricularSemester" name="curricularCourseScope" property="infoCurricularSemester"/>
					<bean:define id="infoCurricularYear" name="infoCurricularSemester" property="infoCurricularYear"/>
					<tr>	 			
						<td class="listClasses"><bean:write name="curricularCourseScope" property="theoreticalHours"/>
						</td>
						<td class="listClasses"><bean:write name="curricularCourseScope" property="praticalHours"/>
						</td>
						<td class="listClasses"><bean:write name="curricularCourseScope" property="theoPratHours"/>
						</td>
						<td class="listClasses"><bean:write name="curricularCourseScope" property="labHours"/>
						</td>
						<td class="listClasses"><bean:write name="curricularCourseScope" property="maxIncrementNac"/>
						</td>
						<td class="listClasses"><bean:write name="curricularCourseScope" property="minIncrementNac"/>
						</td>
						<td class="listClasses"><bean:write name="curricularCourseScope" property="credits"/>
						</td>
						<td class="listClasses"><bean:write name="curricularCourseScope" property="weigth"/>
						</td>
						<td class="listClasses"><bean:write name="infoBranch" property="code"/>
						</td>
						<td class="listClasses"><bean:write name="infoCurricularYear" property="year"/>
						</td>
						<td class="listClasses"><bean:write name="infoCurricularSemester" property="semester"/>
						</td>
						
						<td>
							<ul style="list-style-type: square;">
								<li><html:link page="<%="/editCurricularCourseScope.do?method=prepareEdit&amp;degreeId=" + request.getAttribute("degreeId") + "&amp;degreeCurricularPlanId=" + request.getAttribute("degreeCurricularPlanId")  + "&amp;curricularCourseId=" + request.getAttribute("curricularCourseId")%>" paramId="curricularCourseScopeId" paramName="curricularCourseScope" paramProperty="idInternal"><bean:message key="label.manager.edit.curricularCourseScope"/></html:link></li>
							</ul>
						</td>
	 				</tr>
	 				<!--PASSAR O ID DO SCOPE PA TIRAR O INFO NA ACTION-->
	 				
	 				
	 			</logic:iterate>			
			</table>
			
			<br>
		</logic:notEmpty>	 	
	</logic:present>
	
	
</html:form> 
