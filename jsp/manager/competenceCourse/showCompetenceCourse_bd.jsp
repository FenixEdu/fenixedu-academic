<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.Calendar" %>
<table>
	<tr>
		<logic:present name="competenceCourse">
			<td>
				<h3><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.competenceCourse.administrating"/></h3>
			</td>
			<td>
				<h2><b><bean:write name="competenceCourse" property="name"/> - <bean:write name="competenceCourse" property="code"/></b></h2>
			</td>
		</logic:present>		
	</tr>
</table>

<ul style="list-style-type: square;">
	<li><html:link module="/manager" module="/manager" page="/createEditCompetenceCourse.do?method=prepareEdit" paramId="competenceCourse" paramName="competenceCourse" paramProperty="idInternal"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.edit.competenceCourse"/></html:link></li>
	<li><html:link module="/manager" module="/manager" page="/curricularCoursesCompetenceCourse.do?method=readDegrees" paramId="competenceCourseID" paramName="competenceCourse" paramProperty="idInternal"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.associate.curricularCourses"/></html:link></li>
</ul>

<br/>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<br/>


<table width="70%" cellpadding="0" border="0">
	<tr>
		<h3><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.competenceCourse.associated.departments"/></h3>
	</tr>
	<logic:iterate id="department" name="competenceCourse" property="departments">
		<tr>
			<td class="listClasses">
				<bean:write name="department" property="name"/>
			</td>
		</tr>
	</logic:iterate>
</table>
<br/>
	
<h3><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.curricularCourses"/></h3>

<logic:empty name="competenceCourse" property="associatedCurricularCourses">
	<i><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.competenceCourse.nonExisting.curricularCourses"/></i>
</logic:empty>
	
<logic:notEmpty name="competenceCourse" property="associatedCurricularCourses">
	<html:form action="/curricularCoursesCompetenceCourse">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="removeFromCompetenceCourse"/>
		<bean:define id="idInternal" name="competenceCourse" property="idInternal"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.competenceCourseID" property="competenceCourseID" value='<%=idInternal.toString()%>'/>
		<table width="100%" cellpadding="0" border="0">
			<tr>
				<th class="listClasses-header">
				</th>
				<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.curricularCourse.code" />
				</th>
				<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.degree.curricular.plan.name" />
				</th>
				<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.degreeCurricularPlan" />
				</th>
			</tr>
			
			<logic:iterate id="curricularCourse" name="competenceCourse" property="associatedCurricularCourses">
				<tr>
					<td class="listClasses">
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.curricularCoursesIds" property="curricularCoursesIds">
							<bean:write name="curricularCourse" property="idInternal"/>
						</html:multibox>
					</td>		
					<td class="listClasses"><bean:write name="curricularCourse" property="code"/>
					</td>
					<td class="listClasses"><bean:write name="curricularCourse" property="name"/>
					</td>
					<td class="listClasses"><bean:write name="curricularCourse" property="infoDegreeCurricularPlan.name"/>
					</td>
 				</tr>
 			</logic:iterate>						
		</table>
		<br/>
		<br/>	
		<html:submit>
		   <bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.delete.selected.competences"/>
		</html:submit>
	</html:form>
</logic:notEmpty>