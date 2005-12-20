<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<bean:define id="executionPeriodId" name="executionPeriod" property="idInternal"/>

<p class="infoselected">
	<b><bean:message key="label.teacher.name" /></b> <bean:write name="teacher" property="person.nome"/><br />
	<bean:define id="teacherNumber" name="teacher" property="teacherNumber"/>
	<b><bean:message key="label.teacher.number" /></b> <bean:write name="teacherNumber"/> <br />
	<b><bean:message key="label.execution-period" /></b> <bean:write name="executionPeriod" property="name"/> - <bean:write name="executionPeriod" property="executionYear.year"/><br />	
	
	(<i><html:link page='<%= "/showTeacherCredits.do?method=showTeacherCredits&page=1&amp;executionPeriodId=" + executionPeriodId %>' paramId="teacherId" paramName="teacher" paramProperty="idInternal">
		<bean:message key="label.departmentTeachersList.teacherCreditsSheet"/>
	</html:link></i>)
</p>

<h3>
		<bean:message key="label.teacher-dfp-student.add-student"/>			
</h3>
<html:form action="/teacherAdviseServiceManagement">
	<html:hidden property="method" value="editAdviseService"/>
	<html:hidden property="page" value="1"/>	
	<html:hidden property="idInternal"/>
	<html:hidden property="studentId"/>	
	<html:hidden property="teacherId"/>	
	<html:hidden property="executionPeriodId"/>
	
	<span class="error"><html:errors /></span>
	<logic:messagesPresent message="true">
		<hr class="error"/><u><b>Para prosseguir deverá corrigir os seguintes erros:</b></u><br/><br/>
			- <span class="error"><bean:message key="message.teacherAdvise.percentageExceed"/></span><br/>
			<ul>
			<html:messages id="msg" message="true">
				<span class="error"><li><i><bean:write name="msg" /></i></li></span>
			</html:messages>
		</ul><hr class="error"/>
	</logic:messagesPresent>					
	
	<table>
		<tr>
			<td>
				<bean:message key="label.teacher-dfp-student.student-number"/>				
			</td>
			<td>
				<logic:messagesPresent>
					<html:text property="studentNumber" size="6"/>		
				</logic:messagesPresent>
				<logic:messagesNotPresent>
					<html:text property="studentNumber" value="" size="6"/>		
				</logic:messagesNotPresent>		
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.teacher-dfp-student.percentage"/>
			</td>
			
			<td>
				<html:text property="percentage" size="4"/>
				<html:submit styleClass="inputbutton">
					<bean:message key="button.ok"/>
				</html:submit>
			</td>
		</tr>
	</table>	
</html:form>	

<logic:notPresent name="adviseServices">
	<span class="error"><bean:message key="label.teacher-dfp-student.no-students"/></span>
</logic:notPresent>

<logic:present name="adviseServices">	
	<h3>
		<bean:message key="label.teacher-dfp-student.associated-students"/>
	</h3>
	<table>
		<tr>
			<td class="listClasses-header">
				<bean:message key="label.teacher-dfp-student.student-number"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="label.teacher-dfp-student.student-name"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="label.teacher-dfp-student.percentage"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="label.teacher-dfp-student.remove-student"/>
			</td>
			
		</tr>			
			<bean:define id="teacherId" name="teacher" property="idInternal"/>
			<logic:iterate id="teacherAdviseService" name="adviseServices">
				<tr>
					<td class="listClasses">
						<bean:write name="teacherAdviseService" property="advise.student.number"/>
					</td>
					<td class="listClasses">
						<bean:write name="teacherAdviseService" property="advise.student.person.nome"/>
					</td>
					<td class="listClasses">
						<bean:write name="teacherAdviseService" property="percentage"/> %
					</td>
					
					<td class="listClasses">
						<html:link page='<%= "/teacherAdviseServiceManagement.do?method=deleteAdviseService&amp;page=0&amp;teacherId=" + teacherId +"&amp;executionPeriodId=" + executionPeriodId %>' paramId="teacherAdviseServiceID" paramName="teacherAdviseService" paramProperty="idInternal">
							<bean:message key="link.remove"/>
						</html:link>
					</td>
				</tr>				
			</logic:iterate>
	</table>
</logic:present>