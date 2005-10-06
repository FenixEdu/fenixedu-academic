<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.Calendar" %>
<table>
	<tr>
		<logic:present name="competenceCourse">
			<td>
				<h3><bean:message key="label.manager.competenceCourse.administrating"/></h3>
			</td>
			<td>
				<h2><b><bean:write name="competenceCourse" property="name"/> - <bean:write name="competenceCourse" property="code"/></b></h2>
			</td>
		</logic:present>		
	</tr>
</table>

<ul style="list-style-type: square;">
	<li><html:link page="/createEditCompetenceCourse.do?method=prepareEdit" paramId="competenceCourse" paramName="competenceCourse" paramProperty="idInternal"><bean:message key="label.manager.edit.competenceCourse"/></html:link></li>
	<li><html:link page="/curricularCoursesCompetenceCourse.do?method=readDegrees" paramId="competenceCourseID" paramName="competenceCourse" paramProperty="idInternal"><bean:message key="label.manager.associate.curricularCourses"/></html:link></li>
</ul>

<br>
<span class="error"><html:errors/></span>
<br>


<table width="70%" cellpadding="0" border="0">
	<tr>
		<h3><bean:message key="label.manager.competenceCourse.associated.departments"/></h3>
	</tr>
	<logic:iterate id="department" name="competenceCourse" property="departments">
		<tr>
			<td class="listClasses">
				<bean:write name="department" property="name"/>
			</td>
		</tr>
	</logic:iterate>
</table>
<br>
	
<h3><bean:message key="label.manager.curricularCourses"/></h3>

<logic:empty name="competenceCourse" property="associatedCurricularCourses">
	<i><bean:message key="label.manager.competenceCourse.nonExisting.curricularCourses"/></i>
</logic:empty>
	
<logic:notEmpty name="competenceCourse" property="associatedCurricularCourses">
	<html:form action="/curricularCoursesCompetenceCourse">
		<html:hidden property="method" value="removeFromCompetenceCourse"/>
		<bean:define id="idInternal" name="competenceCourse" property="idInternal"/>
		<html:hidden property="competenceCourseID" value='<%=idInternal.toString()%>'/>
		<table width="100%" cellpadding="0" border="0">
			<tr>
				<td class="listClasses-header">
				</td>
				<td class="listClasses-header"><bean:message key="label.manager.curricularCourse.code" />
				</td>
				<td class="listClasses-header"><bean:message key="message.manager.degree.curricular.plan.name" />
				</td>
				<td class="listClasses-header"><bean:message key="label.manager.degreeCurricularPlan" />
				</td>
			</tr>
			
			<logic:iterate id="curricularCourse" name="competenceCourse" property="associatedCurricularCourses">
				<tr>
					<td class="listClasses">
						<html:multibox property="curricularCoursesIds">
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
		<br>
		<br>	
		<html:submit>
		   <bean:message key="label.manager.delete.selected.competences"/>
		</html:submit>
	</html:form>
</logic:notEmpty>