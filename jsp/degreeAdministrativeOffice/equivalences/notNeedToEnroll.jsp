<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h2>Disciplinas que o aluno não necessita de fazer</h2>
<br/><br/>
<html:form action="showNotNeedToEnroll">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareNotNeedToEnroll"/>
	
	<bean:message key="label.choose.student"/> <html:text bundle="HTMLALT_RESOURCES" altKey='text.studentNumber' size='6' property="studentNumber"/>
	<br/><br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.continue"/></html:submit>
	<br/><br/>
	<logic:present name="infoStudentCurricularPlan">
		<html:link page="/showNotNeedToEnroll.do?method=prepareNotNeedToEnroll&amp;insert=true" paramId="studentNumber" paramName="equivalencesForm" 
			paramProperty="studentNumber"><bean:message key="link.notNeedToEnroll.insert"/></html:link>
		<br/><br/>
		<bean:define id="notNeedToEnrollList" name="infoStudentCurricularPlan" property="infoNotNeedToEnrollCurricularCourses"/>
		<bean:define id="infoDegreeCurricularPlan" name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan"/>
		<h3><bean:message key="title.student.notNeedToEnroll.current"/></h3>
		<table cellpadding=3>
			<tr>
				<th class="listClasses-header"><bean:message key="label.student.notNeedToEnroll.curricularPlan"/></th>
				<th class="listClasses-header"><bean:message key="label.curricular.course.name"/></th>
				<td class="listClasses-header"></td>
			</tr>
		
		<bean:define id="studentNumber" name="equivalencesForm" property="studentNumber" type="java.lang.String"/>
		<logic:iterate id="infoNotNeedToEnroll" name="notNeedToEnrollList">
			<tr>
				<td class="listClasses"><bean:write name="infoDegreeCurricularPlan" property="name"/></td>
				<td class="listClasses">
					<bean:write name="infoNotNeedToEnroll" property="infoCurricularCourse.name"/> - 
					<bean:write name="infoNotNeedToEnroll" property="infoCurricularCourse.code"/>
				</td>
				<td class="listClasses">
					<html:link page="<%= "/showNotNeedToEnroll.do?method=deleteNotNeedToEnroll&amp;studentNumber=" + studentNumber %>"
					paramId="notNeedToEnrollID" paramName="infoNotNeedToEnroll" paramProperty="idInternal">
					<bean:message key="link.notNeedToEnroll.delete"/></html:link>
				</td>
			</tr>
		</logic:iterate>
		</table>
		
		<logic:present name="insert">
			<bean:define id="infoStudentCurricularPlanID" name="infoStudentCurricularPlan" property="idInternal" type="java.lang.Integer"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentCurricularPlanID" property="studentCurricularPlanID" value="<%= infoStudentCurricularPlanID.toString() %>"/>			

			<h3><bean:message key="title.student.notNeedToEnroll.toInsert"/></h3>
			<table cellpadding=3>
				<tr>
					<th class="listClasses-header"><bean:message key="label.student.notNeedToEnroll.curricularPlan"/></th>
					<th class="listClasses-header"><bean:message key="label.curricular.course.name"/></th>
					<td class="listClasses-header"></td>
				</tr>
			<logic:iterate id="infoCurricularCourse" name="infoDegreeCurricularPlan" property="curricularCourses" indexId="index">
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
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="document.forms[0].method.value='insertNotNeedToEnroll'">
				<bean:message key="button.insert"/></html:submit>
		</logic:present>
	</logic:present>
	
</html:form>