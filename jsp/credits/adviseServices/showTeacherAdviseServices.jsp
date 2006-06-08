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
	<html:hidden property="teacherNumber"/>	
	<html:hidden property="executionPeriodId"/>
	
	<span class="error"><html:errors /></span>
	<logic:messagesPresent message="true">
		<hr class="error"/><u><b>Para prosseguir deverá corrigir os seguintes erros:</b></u><br/>		
			<ul>
				<html:messages id="msg" message="true">
					<span class="error"><li><i><bean:write name="msg" /></i></li></span>
				</html:messages>
			</ul>
			<hr class="error"/>
	</logic:messagesPresent>					
	
	<table>
		<tr>
			<td>
				<bean:message key="label.teacher-dfp-student.student-number"/>:			
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
				<bean:message key="label.teacher-dfp-student.percentage"/>:
			</td>
			
			<td>
				<html:text property="percentage" maxlength="3" size="4"/>
			</td>
		</tr>
	</table>	
	<html:submit styleClass="inputbutton">
		<bean:message key="button.add"/>
	</html:submit>
</html:form>	

<br/>

<logic:notPresent name="adviseServices">
	<span class="error"><bean:message key="label.teacher-dfp-student.no-students"/></span>
	<br/>
</logic:notPresent>

<logic:present name="adviseServices">	
	<h3>
		<bean:message key="label.teacher-dfp-student.associated-students"/>
	</h3>
	<table class="tstyle4 mvert0">
		<tr>
			<th>
				<bean:message key="label.teacher-dfp-student.student-number"/>
			</th>
			<th>
				<bean:message key="label.teacher-dfp-student.student-name"/>
			</th>
			<th>
				<bean:message key="label.teacher-dfp-student.percentage"/>
			</th>
			<th>
				<bean:message key="label.teacher-dfp-student.remove-student"/>
			</th>
			
		</tr>			
			<bean:define id="teacherId" name="teacher" property="idInternal"/>
			<logic:iterate id="teacherAdviseService" name="adviseServices">
				<tr>
					<td>
						<bean:write name="teacherAdviseService" property="advise.student.number"/>
					</td>
					<td>
						<bean:write name="teacherAdviseService" property="advise.student.person.nome"/>
					</td>
					<td>
						<bean:write name="teacherAdviseService" property="percentage"/> %
					</td>
					
					<td>
						<html:link page='<%= "/teacherAdviseServiceManagement.do?method=deleteAdviseService&amp;page=0&amp;teacherId=" + teacherId +"&amp;executionPeriodId=" + executionPeriodId + "&amp;teacherNumber=" + teacherNumber %>' paramId="teacherAdviseServiceID" paramName="teacherAdviseService" paramProperty="idInternal">
							<bean:message key="link.remove"/>
						</html:link>
					</td>
				</tr>				
			</logic:iterate>
	</table>
</logic:present>
<br/>
<bean:define id="link">
	/showFullTeacherCreditsSheet.do?method=showTeacherCredits&amp;executionPeriodId=<bean:write name="executionPeriod" property="idInternal"/>&amp;teacherId=<bean:write name="teacher" property="idInternal"/>
</bean:define>
<html:link page='<%= link %>'>
	<bean:message key="link.return"/>
</html:link>