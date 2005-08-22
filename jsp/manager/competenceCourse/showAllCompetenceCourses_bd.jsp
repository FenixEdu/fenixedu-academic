<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>			
<h2><bean:message key="label.manager.competence.course.management"/></h2>
<ul style="list-style-type: square;">
<li><html:link page="/competenceCourseManagement.do?method=start"><bean:message key="label.manager.insert.competence.course"/></html:link></li>
</ul>
<br>
<br>

<span class="error"><html:errors/></span>

<logic:notEmpty name="competenceCourses">
	<html:form action="/deleteCompetenceCourses">
		<html:hidden property="method" value="deleteCompetenceCourses"/>
		<table width="100%" cellpadding="0" border="0">
			<tr>
				<td class="listClasses-header">
				</td>
				<td class="listClasses-header"><bean:message key="label.manager.degree.code" />
				</td>
				<td class="listClasses-header"><bean:message key="label.manager.degree.name" />
				</td>
				<td class="listClasses-header"><bean:message key="label.manager.department" />
				</td>
			</tr>
				
				 
			<logic:iterate id="competenceCourse" name="competenceCourses">			
				<tr>	
					<td class="listClasses">
					
					<html:multibox property="competenceCoursesIds">
						<bean:write name="competenceCourse" property="idInternal"/>
					</html:multibox>
					</td>	
					<td class="listClasses"><html:link page="/competenceCourseManagement.do?method=showCompetenceCourse" paramId="competenceCourseID" paramName="competenceCourse" paramProperty="idInternal"><bean:write name="competenceCourse" property="code"/></html:link>
					</td>			
					<td class="listClasses"><p align="left"><html:link page="/competenceCourseManagement.do?method=showCompetenceCourse" paramId="competenceCourseID" paramName="competenceCourse" paramProperty="idInternal"><bean:write name="competenceCourse" property="name"/></html:link></p>
					</td>
					<td class="listClasses">
						<logic:present name="competenceCourse" property="department">
							<bean:write name="competenceCourse" property="department.name"/>
						</logic:present>
					</td>
			 	</tr>
			 		
			 </logic:iterate>
			
		</table>
		<br>
		<br>	
		<html:submit>
		   <bean:message key="label.manager.delete.selected.competences"/>
		</html:submit>
	</html:form>
</logic:notEmpty>