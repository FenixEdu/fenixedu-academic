<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
		
<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.competence.course.management"/></h2>
<ul style="list-style-type: square;">
<li><html:link module="/manager" module="/manager" page="/createEditCompetenceCourse.do?method=prepareCreate"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.competence.course"/></html:link></li>
</ul>
<br/>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<br/>
<html:form action="/competenceCourseManagement">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="deleteCompetenceCourses"/>
	<table>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="label.manager.department"/>
			</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.departmentID" property="departmentID" onchange="this.form.method.value='showDepartmentCompetenceCourses';this.form.submit();">
					<html:option value="null">&nbsp;</html:option>
					<html:options collection="departments" property="idInternal" labelProperty="realName"/>
    			</html:select>
				<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</td>
		</tr>
	</table>
	<br/>
	<logic:notEmpty name="competenceCourses">	
			
			<table width="100%" cellpadding="0" border="0">
				<tr>
					<th class="listClasses-header">
					</th>
					<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.code" />
					</th>
					<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.competence.course" />
					</th>
				</tr>
					
					 
				<logic:iterate id="competenceCourse" name="competenceCourses">			
					<tr>	
						<td class="listClasses">
						
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.competenceCoursesIds" property="competenceCoursesIds">
							<bean:write name="competenceCourse" property="idInternal"/>
						</html:multibox>
						</td>	
						<td class="listClasses"><html:link module="/manager" module="/manager" page="/competenceCourseManagement.do?method=showCompetenceCourse" paramId="competenceCourseID" paramName="competenceCourse" paramProperty="idInternal"><bean:write name="competenceCourse" property="code"/></html:link>
						</td>			
						<td class="listClasses"><p align="left"><html:link module="/manager" module="/manager" page="/competenceCourseManagement.do?method=showCompetenceCourse" paramId="competenceCourseID" paramName="competenceCourse" paramProperty="idInternal"><bean:write name="competenceCourse" property="name"/></html:link></p>
						</td>
				 	</tr>
				 		
				 </logic:iterate>
				
			</table>
			<br/>
			<br/>	
			<html:submit>
			   <bean:message bundle="MANAGER_RESOURCES" key="label.manager.delete.selected.competences"/>
			</html:submit>
	</logic:notEmpty>
</html:form>