<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<bean:define id="executionPeriodId" name="executionPeriod" property="idInternal"/>

<h2><bean:message key="label.teaching.service.alter" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h2>

<div class="infoop mtop2 mbottom1">
	<p class="mvert025"><b><bean:message key="label.teacher.name"/>:</b> <bean:write name="teacher" property="person.nome"/></p>
	<p class="mvert025"><bean:define id="teacherNumber" name="teacher" property="teacherNumber"/><b><bean:message key="label.teacher.number"/>:</b> <bean:write name="teacherNumber"/></p>
	<p class="mvert025"><b><bean:message key="label.execution-period"/>:</b> <bean:write name="executionPeriod" property="name"/> - <bean:write name="executionPeriod" property="executionYear.year"/></p>
</div>

<ul>
	<li>
	<bean:define id="link">/showFullTeacherCreditsSheet.do?method=showTeacherCredits&amp;executionPeriodId=<bean:write name="executionPeriod" property="idInternal"/>&amp;teacherId=<bean:write name="teacher" property="idInternal"/></bean:define>
	<html:link page='<%= link %>'><bean:message key="link.return"/></html:link>
	</li>
</ul>


<p class="infoop2">
	<bean:message key="label.teacher.advise.service.help" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
</p>	


<p class="mbottom05 mtop1"><strong><bean:message key="label.teacher-dfp-student.add-student"/></strong></p>


<span class="error"><!-- Error messages go here --><html:errors /></span>
<logic:messagesPresent message="true">
	<hr class="error"/><u><b>Para prosseguir deverá corrigir os seguintes erros:</b></u><br/>		
	<ul>
		<html:messages id="msg" message="true">
			<span class="error"><!-- Error messages go here --><li><i><bean:write name="msg"/></i></li></span>
		</html:messages>
	</ul>
	<hr class="error"/>
</logic:messagesPresent>	

<html:form action="/teacherAdviseServiceManagement">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editAdviseService"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentId" property="studentId"/>	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherId" property="teacherId"/>	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherNumber" property="teacherNumber"/>	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriodId" property="executionPeriodId"/>						
	
	<table>
		<tr>
			<td>
				<bean:message key="label.teacher-dfp-student.student-number"/>:			
			</td>
			<td>
				<logic:messagesPresent>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.studentNumber" property="studentNumber" size="6"/>		
				</logic:messagesPresent>
				<logic:messagesNotPresent>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.studentNumber" property="studentNumber" value="" size="6"/>		
				</logic:messagesNotPresent>		
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.teacher-dfp-student.percentage"/>:
			</td>
			
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.percentage" property="percentage" maxlength="3" size="4"/>
			</td>
		</tr>
	</table>	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.add"/>
	</html:submit>
</html:form>	

<br/>

<logic:notPresent name="adviseServices">
	<span class="error"><!-- Error messages go here --><bean:message key="label.teacher-dfp-student.no-students"/></span>
	<br/>
</logic:notPresent>

<logic:present name="adviseServices">	
	<p>
		<strong><bean:message key="label.teacher-dfp-student.associated-students"/></strong>
	</p>
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
