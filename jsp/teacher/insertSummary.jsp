<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<logic:present name="summaryToInsert">
<bean:define id="component" name="siteView" property="commonComponent"/>
<bean:define id="executionCourse" name="component" property="executionCourse"/>
<bean:define id="objectCode" name="executionCourse" property="idInternal"/>
<bean:define id="bodyComponent" name="siteView" property="component"/>
<bean:define id="shifts" name="bodyComponent" property="infoShifts" />
<bean:define id="shiftSelected" name="summaryToInsert" property="infoShift" />
<bean:define id="shiftSelectedId" name="shiftSelected" property="idInternal" />
<bean:define id="professorships" name="bodyComponent" property="infoProfessorships" />
<bean:define id="rooms" name="bodyComponent" property="infoRooms" />

<span class="error"><html:errors/></span>

<h2><bean:message key="title.summary.insert" /></h2>

<html:form action="/summariesManagerInsertSummary">
	<html:hidden property="forHidden" value=""/>
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="insertSummary"/>
	<html:hidden property="objectCode"/>

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
		<td><html:select property="shift" onchange="this.form.method.value='prepareInsertSummary';this.form.page.value=0;this.form.submit();" >
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
			<td><html:radio property="lesson" value="<%= lessonId.toString() %>" onclick="this.form.method.value='prepareInsertSummary';this.form.forHidden.value='true';this.form.page.value=0;this.form.submit();"/></td>
		</tr>
	</logic:iterate>
	<tr>
		<td><bean:message key="label.extra.lesson" />:</td>
		<td><html:radio property="lesson" value="0" onclick="this.form.method.value='prepareInsertSummary';this.form.forHidden.value='false';this.form.page.value=0;this.form.submit();"/></td>
	</tr>
	<tr>
		<td colspan='2'>&nbsp;</td>
	</tr>
	<tr>
		<td><bean:message key="label.summaryDate"/></td>
		<td><html:text property="summaryDateInput" size="10" maxlength="10"/><bean:message key="message.dateFormat"/></td>
	</tr>
	<logic:present name="forHidden">
		<logic:notEqual name="forHidden" value="true">
		<tr>
			<td><bean:message key="label.summaryHour"/></td>
			<td><html:text property="summaryHourInput" size="5" maxlength="5"/><bean:message key="message.hourFormat"/></td>		
		<tr/>	
		<tr>
			<td><bean:message key="label.room"/>:</td>
			<td><html:select  property="room">
					<html:option key="label.summary.select" value="" />
					<html:options collection="rooms" property="idInternal" labelProperty="nome"/>
				</html:select></td>		
		<tr/>	
		</logic:notEqual>
		<logic:equal name="forHidden" value="true">
			<html:hidden property="summaryHourInput"/>
			<html:hidden property="room"/>
		</logic:equal>
	</logic:present>
	<logic:notPresent name="forHidden">
		<tr>
			<td><bean:message key="label.summaryHour"/></td>
			<td><html:text property="summaryHourInput" size="5" maxlength="5"/><bean:message key="message.hourFormat"/></td>		
		<tr/>	
		<tr>
			<td><bean:message key="label.room"/>:</td>
			<td><html:select  property="room">
					<html:option key="label.summary.select" value="" />
					<html:options collection="rooms" property="idInternal" labelProperty="nome"/>
				</html:select></td>		
		<tr/>	
	</logic:notPresent>	
	<tr>
		<td><bean:message key="label.studentNumber.attended.lesson" />:</td>
		<td><html:text property="studentsNumber" size="4"/></td>
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
			<td><html:radio property="teacher" value="<%= professorshipId.toString()%>" /></td>				
		</tr>
	</logic:iterate>
	<tr>
		<td><bean:message key="label.teacher.in" />:</td>
		<td><html:radio property="teacher" value="0" /></td>
		<td><bean:message key="label.number" />:</td><td><html:text property="teacherNumber" size="4" /></td>
	</tr>
	<tr>
		<td><bean:message key="label.teacher.out" />:</td>
		<td><html:radio property="teacher" value="-1" /></td>
		<td><bean:message key="label.name" />:</td><td><html:text property="teacherName" size="40"/></td>				
	</tr>				
</table>
<br/>
<br/>		
<table border="0">
	<tr>
		<td colspan='2'><strong><bean:message key="label.title"/></strong></td>
	</tr>
	<tr>
		<td colspan='2'><html:text size="66" property="title"/></td>
	</tr>
	<tr>
		<td colspan='2'><strong><bean:message key="label.summaryText"/></strong></td>
	</tr>
	<tr>
		<td colspan='2'><html:textarea rows="7" cols="50" property="summaryText"/></td>
	</tr>
</table>

<br/>
<br/>
<html:submit styleClass="inputbutton"><bean:message key="button.save"/>                    		         	
</html:submit> 
<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>  
</html:form>
</logic:present>

<logic:notPresent name="summaryToInsert">
	<h2><bean:message key="error.summary.impossible.insert" /></h2>
</logic:notPresent>
