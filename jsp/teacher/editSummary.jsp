<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<bean:define id="component" name="siteView" property="commonComponent"/>
<bean:define id="executionCourse" name="component" property="executionCourse"/>
<bean:define id="objectCode" name="executionCourse" property="idInternal"/>
<bean:define id="bodyComponent" name="siteView" property="component"/>
<bean:define id="infoSummary" name="bodyComponent" property="infoSummary"/>
<bean:define id="summaryCode" name="infoSummary" property="idInternal"/>
<bean:define id="professorships" name="bodyComponent" property="infoProfessorships" />
<bean:define id="rooms" name="bodyComponent" property="infoRooms" />
<bean:define id="shifts" name="bodyComponent" property="infoShifts" />
<bean:define id="shiftSelected" name="bodyComponent" property="infoSummary.infoShift" />

<span class="error"><html:errors/></span>

<h2><bean:message key="title.summary.edit" /></h2>

<html:form action="/summariesManager">
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="editSummary"/>
	<html:hidden property="objectCode"/>
	<html:hidden  property="summaryCode" value="<%= summaryCode.toString() %>"/>

<!-- Shifts -->
<table width="100%">
	<tr>
		<td class="infoop"><bean:message key="help.summary.chooseShift" /></td>
	</tr>
</table>
<br />
<table border="0">	
	<tr>
		<td><bean:message key="property.shift" />:</td>
		<td><html:select name="infoSummary" property="shift" onchange="this.form.method.value='prepareEditSummary';this.form.page.value=0;this.form.submit();" >
				<html:option key="label.summary.select" value="null" />
				<html:options collection="shifts" property="idInternal" labelProperty="lessons"/>
			</html:select>
		</td>
		<td>&nbsp;</td>
	</tr>
</table>
<br/>
<br/>
<!-- Lessons -->
<table width="100%">
	<tr>
		<td class="infoop"><bean:message key="help.summary.chooseLesson" /></td>
	</tr>
</table>
<br />
<table width="100%">		
	<logic:iterate id="infoLesson" name="shiftSelected" property="infoLessons">
		<bean:define id="lessonId" name="infoLesson" property="idInternal"/>
		<tr>
			<td><bean:message key="label.summary.lesson" />
				<bean:write name="infoLesson" property="tipo.fullNameTipoAula" />
				<bean:write name="infoLesson" property="diaSemana" />
				<dt:format pattern="HH:mm"><bean:write name="infoLesson" property="inicio.timeInMillis"/></dt:format>
				- <dt:format pattern="HH:mm"><bean:write name="infoLesson" property="fim.timeInMillis"/></dt:format>
				<bean:write name="infoLesson" property="infoSala.nome" />
			:</td>
			<td><html:radio name="infoSummary" property="lesson" value="<%= lessonId.toString() %>"/></td>
		</tr>
	</logic:iterate>
	<tr>
		<td><bean:message key="label.extra.lesson" />:</td>
		<td><html:radio name="infoSummary" property="lesson" value="0"/></td>
	</tr>
	<tr>
		<td colspan='2'>&nbsp;</td>
	</tr>
	<tr>
		<td><bean:message key="label.summaryDate"/></td>
		<td><html:text name="infoSummary" property="summaryDateInput" size="10" maxlength="10"/><bean:message key="message.dateFormat"/></td>
	</tr>
	<tr>
		<td><bean:message key="label.summaryHour"/></td>
		<td><html:text name="infoSummary" property="summaryHourInput" size="5" maxlength="5"/><bean:message key="message.hourFormat"/></td>		
	<tr/>	
	<tr>
		<td><bean:message key="label.room"/>:</td>
		<td><html:select name="infoSummary" property="room">
				<html:option key="label.summary.select" value="" />
				<html:options collection="rooms" property="idInternal" labelProperty="nome"/>
			</html:select></td>		
	<tr/>	
	<tr>
		<td><bean:message key="label.studentNumber.attended.lesson" />:</td>
		<td><html:text name="infoSummary" property="studentsNumber" size="4"/></td>
	</tr>
</table>
<br/>
<br/>	
<!-- Teachers -->
<table width="100%">
	<tr>
		<td class="infoop"><bean:message key="help.summary.chooseTeacher" /></td>
	</tr>
</table>
<br />
<table border="0">
	<logic:iterate id="professorship" name="professorships">
		<bean:define id="professorshipId" name="professorship" property="idInternal" />
		<tr>
			<td><bean:write name="professorship" property="infoTeacher.infoPerson.nome"/>:</td>
			<td><html:radio name="infoSummary" property="teacher" value="<%= professorshipId.toString()%>" /></td>				
		</tr>
	</logic:iterate>
	<tr>
		<td><bean:message key="label.teacher.in" />:</td>
		<td><html:radio name="infoSummary" property="teacher" value="0" /></td>
		<td><bean:message key="label.number" />:</td><td><html:text name="infoSummary" property="teacherNumber" size="4" /></td>
	</tr>
	<tr>
		<td><bean:message key="label.teacher.out" />:</td>
		<td><html:radio name="infoSummary" property="teacher" value="-1" /></td>
		<td><bean:message key="label.name" />:</td><td><html:text name="infoSummary" property="teacherName" size="40"/></td>				
	</tr>				
</table>
<br/>
<br/>		
<table border="0">
	<tr>
		<td colspan='2'><strong><bean:message key="label.title"/></strong></td>
	</tr>
	<tr>
		<td colspan='2'><html:text size="66" name="infoSummary" property="title"/></td>
	</tr>
	<tr>
		<td colspan='2'><strong><bean:message key="label.summaryText"/></strong></td>
	</tr>
	<tr>
		<td colspan='2'><html:textarea rows="7" cols="50" name="infoSummary" property="summaryText"/></td>
	</tr>
</table>

<br/>
<br/>
<html:submit styleClass="inputbutton"><bean:message key="button.save"/>                    		         	
</html:submit> 
<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>  
</html:form>
