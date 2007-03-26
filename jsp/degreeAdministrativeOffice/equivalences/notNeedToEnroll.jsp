<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message bundle="DEGREE_ADM_OFFICE" key="link.notNeedToEnroll"/></h2>

<fr:edit name="chooseSCPBean" schema="choose.studentCurricularPlan" action="/showNotNeedToEnroll.do?method=prepare">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop025"/>
	</fr:layout>
	<fr:destination name="registrationPostBack" path="/showNotNeedToEnroll.do?method=prepare"/>
	<fr:destination name="studentCurricularPlanPostBack" path="/showNotNeedToEnroll.do?method=prepare"/>	
</fr:edit>

<html:form action="showNotNeedToEnroll">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="insertNotNeedToEnroll"/>
	
	<br/><br/>
	<logic:present name="infoStudentCurricularPlan">
		<html:link page="/showNotNeedToEnroll.do?method=prepareNotNeedToEnroll&amp;insert=true" paramId="scpID" paramName="infoStudentCurricularPlan" 
			paramProperty="idInternal"><bean:message bundle="DEGREE_ADM_OFFICE" key="link.notNeedToEnroll.insert"/></html:link>
		<br/><br/>
		
		<bean:define id="infoDegreeCurricularPlan" name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan"/>
		<h3><bean:message bundle="DEGREE_ADM_OFFICE" key="title.student.notNeedToEnroll.current"/></h3>
		<table cellpadding=3>
			<tr>
				<th class="listClasses-header"><bean:message bundle="DEGREE_ADM_OFFICE" key="label.student.notNeedToEnroll.curricularPlan"/></th>
				<th class="listClasses-header"><bean:message bundle="DEGREE_ADM_OFFICE" key="label.curricular.course.name"/></th>
				<td class="listClasses-header"></td>
			</tr>
		
		<bean:define id="scpID" name="infoStudentCurricularPlan" property="idInternal" />
		<logic:iterate id="infoNotNeedToEnroll" name="infoNotNeedToEnrollCurricularCourses">
			<tr>
				<td class="listClasses"><bean:write name="infoNotNeedToEnroll" property="infoCurricularCourse.infoDegreeCurricularPlan.name"/></td>
				<td class="listClasses">
					<bean:write name="infoNotNeedToEnroll" property="infoCurricularCourse.name"/> - 
					<bean:write name="infoNotNeedToEnroll" property="infoCurricularCourse.code"/>
				</td>
				<td class="listClasses">
					<html:link page="<%= "/showNotNeedToEnroll.do?method=deleteNotNeedToEnroll&amp;scpID=" + scpID %>"
					paramId="notNeedToEnrollID" paramName="infoNotNeedToEnroll" paramProperty="idInternal">
					<bean:message bundle="DEGREE_ADM_OFFICE" key="link.notNeedToEnroll.delete"/></html:link>
				</td>
			</tr>
		</logic:iterate>
		</table>
		
		<logic:present name="insert">
			<bean:define id="infoStudentCurricularPlanID" name="infoStudentCurricularPlan" property="idInternal" type="java.lang.Integer"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentCurricularPlanID" property="studentCurricularPlanID" value="<%= infoStudentCurricularPlanID.toString() %>"/>			

			<h3><bean:message bundle="DEGREE_ADM_OFFICE" key="title.student.notNeedToEnroll.toInsert"/></h3>
			<table cellpadding=3>
				<tr>
					<th class="listClasses-header"><bean:message bundle="DEGREE_ADM_OFFICE" key="label.student.notNeedToEnroll.curricularPlan"/></th>
					<th class="listClasses-header"><bean:message bundle="DEGREE_ADM_OFFICE" key="label.curricular.course.name"/></th>
					<td class="listClasses-header"></td>
				</tr>
			<logic:iterate id="infoCurricularCourse" name="infoDegreeCurricularPlanCurricularCourses" indexId="index">
				<tr>
					<td class="listClasses"><bean:write name="infoDegreeCurricularPlan" property="name"/></td>
					<td class="listClasses">
						<bean:write name="infoCurricularCourse" property="name"/> - <bean:write name="infoCurricularCourse" property="code"/>
					</td>
					<td class="listClasses">
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.curricularCoursesID" property="curricularCoursesID">
							<bean:write name="infoCurricularCourse" property="idInternal"/> 
						</html:multibox>
					</td>
				</tr>
			</logic:iterate>
			</table>
			<br/>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" >
				<bean:message bundle="DEGREE_ADM_OFFICE" key="button.insert"/></html:submit>
		</logic:present>
	</logic:present>
	
</html:form>