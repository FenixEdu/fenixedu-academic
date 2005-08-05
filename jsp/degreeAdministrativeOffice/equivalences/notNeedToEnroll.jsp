<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h2>Disciplinas que o aluno não necessita de fazer</h2>
<br/><br/>
<html:form action="showNotNeedToEnroll">
	<html:hidden property="method" value="prepareNotNeedToEnroll"/>
	
	<bean:message key="label.choose.student"/> <html:text size='6' property="studentNumber"/>
	<br/><br/>
	<html:submit styleClass="inputbutton"><bean:message key="button.continue"/></html:submit>
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
				<th><bean:message key="label.student.notNeedToEnroll.curricularPlan"/></th>
				<th><bean_message key="label.curricular.course.name"/></th>
			</tr>
		
		<bean:define id="studentNumber" name="equivalencesForm" property="studentNumber" type="java.lang.String"/>
		<logic:iterate id="infoNotNeedToEnroll" name="notNeedToEnrollList">
			<tr>
				<td><bean:write name="infoDegreeCurricularPlan" property="name"/></td>
				<td><bean:write name="infoNotNeedToEnroll" property="infoCurricularCourse.name"/></td>
				<td>
					<html:link page="<%= "/showNotNeedToEnroll.do?method=deleteNotNeedToEnroll&amp;studentNumber=" + studentNumber %>"
					paramId="notNeedToEnrollID" paramName="infoNotNeedToEnroll" paramProperty="idInternal">
					<bean:message key="link.notNeedToEnroll.delete"/></html:link>
				</td>
			</tr>
		</logic:iterate>
		</table>
		
		<logic:present name="insert">
			<bean:define id="infoStudentCurricularPlanID" name="infoStudentCurricularPlan" property="idInternal" type="java.lang.Integer"/>
			<html:hidden property="studentCurricularPlanID" value="<%= infoStudentCurricularPlanID.toString() %>"/>			

			<h3><bean:message key="title.student.notNeedToEnroll.toInsert"/></h3>
			<table>
			<logic:iterate id="infoCurricularCourse" name="infoDegreeCurricularPlan" property="curricularCourses" indexId="index">
				<tr>
					<td><bean:write name="infoCurricularCourse" property="name"/></td>
						<td>
							<html:multibox property="curricularCoursesID">
								<bean:write name="infoCurricularCourse" property="idInternal"/> 
							</html:multibox>
						</td>
				</tr>
			</logic:iterate>
			</table>
			<html:submit styleClass="inputbutton" onclick="document.forms[0].method.value='insertNotNeedToEnroll'">
				<bean:message key="button.insert"/></html:submit>
		</logic:present>
	</logic:present>
	
</html:form>