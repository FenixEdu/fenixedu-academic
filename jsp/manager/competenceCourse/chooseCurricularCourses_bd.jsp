<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>			
<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.competence.course.management"/></h2>

<br>
<br>

<span class="error"><html:errors/></span>

<logic:notEmpty name="curricularCourses">
	<html:form action="/curricularCoursesCompetenceCourse">
		<html:hidden property="method" value="addToCompetenceCourse"/>
		<html:hidden property="competenceCourseID"/>
		<table width="100%" cellpadding="0" border="0">
			<tr>
				<td class="listClasses-header">
				</td>
				<td class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.degree.code" />
				</td>
				<td class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.degree.name" />
				</td>
				<td class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.degreeCurricularPlan" />
				</td>
			</tr>
				
				 
			<logic:iterate id="curricularCourse" name="curricularCourses">			
				<tr>	
					<td class="listClasses">
					
					<html:multibox property="curricularCoursesIds">
						<bean:write name="curricularCourse" property="idInternal"/>
					</html:multibox>
					</td>	
					<td class="listClasses"><bean:write name="curricularCourse" property="code"/>
					</td>			
					<td class="listClasses"><p align="left"><bean:write name="curricularCourse" property="name"/></p>
					</td>
					<td class="listClasses">
						<logic:present name="curricularCourse" property="infoDegreeCurricularPlan">
							<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.name"/>
						</logic:present>
					</td>
			 	</tr>
			 		
			 </logic:iterate>
			
		</table>
		<br>
		<br>	
		<html:submit>
		   <bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.associate.curricularCourses"/>
		</html:submit>
	</html:form>
</logic:notEmpty>