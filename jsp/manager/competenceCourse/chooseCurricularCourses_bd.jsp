<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>			
<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.competence.course.management"/></h2>

<br/>
<br/>

<span class="error"><html:errors/></span>

<logic:notEmpty name="curricularCourses">
	<html:form action="/curricularCoursesCompetenceCourse">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="addToCompetenceCourse"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.competenceCourseID" property="competenceCourseID"/>
		<table width="100%" cellpadding="0" border="0">
			<tr>
				<th class="listClasses-header">
				</th>
				<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.degree.code" />
				</th>
				<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.degree.name" />
				</th>
				<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.degreeCurricularPlan" />
				</th>
			</tr>
				
				 
			<logic:iterate id="curricularCourse" name="curricularCourses">			
				<tr>	
					<td class="listClasses">
					
					<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.curricularCoursesIds" property="curricularCoursesIds">
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
		<br/>
		<br/>	
		<html:submit>
		   <bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.associate.curricularCourses"/>
		</html:submit>
	</html:form>
</logic:notEmpty>