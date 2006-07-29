<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<script language="JavaScript" type="text/javascript" src="<%= request.getContextPath() %>/javaScript/editor/html2xhtml.js"></script>
<script language="JavaScript" type="text/javascript" src="<%= request.getContextPath() %>/javaScript/editor/richtext.js"></script>
<script language="JavaScript" type="text/javascript" src="<%= request.getContextPath() %>/javaScript/editor/htmleditor.js"></script>

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

<span class="error"><html:errors/>
 <logic:present name="errors">
  <bean:write name="errors" filter="true" />
 </logic:present >
</span>

<h2><bean:message key="title.summary.insert" /></h2>

<html:form action="/summariesManagerInsertSummary">
 <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.forHidden" property="forHidden" />
 <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
 <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="insertSummary"/>
 <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode"/>
 <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.save" property="save"/>
 
 <logic:present name="verEditor">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.summaryText" property="summaryText"/>
 </logic:present>
  
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
		<logic:present name="verEditor">
			<td><html:select bundle="HTMLALT_RESOURCES" altKey="select.shift" property="shift" onchange="this.form.method.value='prepareInsertSummary';this.form.page.value=0;this.form.summaryText.value=update();this.form.submit();" >
					<html:options collection="shifts" property="idInternal" labelProperty="lessons"/>
				</html:select>
				<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</td>
		</logic:present>
		<logic:notPresent name="verEditor">
			<td><html:select bundle="HTMLALT_RESOURCES" altKey="select.shift" property="shift" onchange="this.form.method.value='prepareInsertSummary';this.form.page.value=0;this.form.submit();" >
					<html:options collection="shifts" property="idInternal" labelProperty="lessons"/>
				</html:select>
				<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</td>
		</logic:notPresent>
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
				<logic:notEmpty name="infoLesson" property="infoSala.nome">
					<bean:write name="infoLesson" property="infoSala.nome" />
				</logic:notEmpty>	
			:</td>
			<logic:present name="checked">
				<logic:present name="verEditor">	
					<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.lesson" property="lesson" value="<%= lessonId.toString() %>" onclick="this.form.summaryDateInputOption.value='null';this.form.method.value='prepareInsertSummary';this.form.forHidden.value='true';this.form.page.value=0;this.form.summaryText.value=update();this.form.submit();"/></td>
				</logic:present>
				<logic:notPresent name="verEditor">	
					<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.lesson" property="lesson" value="<%= lessonId.toString() %>" onclick="this.form.summaryDateInputOption.value='null';this.form.method.value='prepareInsertSummary';this.form.forHidden.value='true';this.form.page.value=0;this.form.submit();"/></td>
				</logic:notPresent>	
			</logic:present>
			<logic:notPresent name="checked">	
				<logic:present name="verEditor">	
					<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.lesson" property="lesson" value="<%= lessonId.toString() %>" onclick="this.form.method.value='prepareInsertSummary';this.form.forHidden.value='true';this.form.page.value=0;this.form.summaryText.value=update();this.form.submit();"/></td>
				</logic:present>
				<logic:notPresent name="verEditor">	
					<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.lesson" property="lesson" value="<%= lessonId.toString() %>" onclick="this.form.method.value='prepareInsertSummary';this.form.forHidden.value='true';this.form.page.value=0;this.form.submit();"/></td>
				</logic:notPresent>	
			</logic:notPresent>	
		</tr>
	</logic:iterate>
	<tr>
		<td><bean:message key="label.extra.lesson" />:</td>
		<logic:present name="verEditor">	
			<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.lesson" property="lesson" value="0" onclick="this.form.method.value='prepareInsertSummary';this.form.forHidden.value='false';this.form.page.value=0;this.form.summaryText.value=update();this.form.submit();"/></td>
		</logic:present>
		<logic:notPresent name="verEditor">	
			<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.lesson" property="lesson" value="0" onclick="this.form.method.value='prepareInsertSummary';this.form.forHidden.value='false';this.form.page.value=0;this.form.submit();"/></td>
		</logic:notPresent>
	</tr>
	<tr>
		<td colspan='2'>&nbsp;</td>
	</tr>
	<logic:present name="datesVisible">
		<logic:equal name="datesVisible" value="true">
			<tr>
				<td><bean:message key="label.summaryOptions"/></td>
			</tr>	
			<logic:iterate id="date" name="dates" type="java.util.Date">
				<tr>
					<bean:define id="summaryDate" type="java.lang.String"><dt:format pattern="dd/MM/yyyy"><bean:write name="date" property="time"/></dt:format></bean:define>
					<td>&nbsp;&nbsp;&nbsp;&nbsp;<b><bean:write name="summaryDate"/></b></td>
					<td width="50%">
						<logic:present name="verEditor">
							<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.summaryDateInputOption" property="summaryDateInputOption"
								onclick="<%= "this.form.summaryDateInput.value='"
					   			    + summaryDate 
									+ "';this.form.method.value='prepareInsertSummary';this.form.page.value=0;this.form.summaryText.value=update();this.form.submit();" %>" />																	
						</logic:present>
						<logic:notPresent name="verEditor">	
							<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.summaryDateInputOption" property="summaryDateInputOption"
								onclick="<%= "this.form.summaryDateInput.value='"
					   			    + summaryDate 
									+ "';this.form.method.value='prepareInsertSummary';this.form.page.value=0;this.form.submit();" %>" />																	
						
						</logic:notPresent>					
					</td>
				</tr>
			</logic:iterate>
			<tr>
				<td><bean:message key="label.summaryDateOptions"/></td>
				<logic:present name="verEditor">	
					<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.dateEmpty" property="dateEmpty" size="10" maxlength="10"
							onchange="this.form.summaryDateInputOption.value='null';this.form.summaryDateInput.value=this.form.dateEmpty.value;this.form.method.value='prepareInsertSummary';this.form.page.value=0;this.form.summaryText.value=update();this.form.submit();"/>
				</logic:present>
				<logic:notPresent name="verEditor">	
					<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.dateEmpty" property="dateEmpty" size="10" maxlength="10"
							onchange="this.form.summaryDateInputOption.value='null';this.form.summaryDateInput.value=this.form.dateEmpty.value;this.form.method.value='prepareInsertSummary';this.form.page.value=0;this.form.submit();"/>
				</logic:notPresent>
				<html:submit styleId="javascriptButtonID3" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
				<bean:message key="message.dateFormat"/></td>
			</tr>							
		</logic:equal>
	</logic:present>				
	<logic:present name="forHidden">
		<logic:equal name="forHidden" value="false">
		<tr>
			<td><bean:message key="label.summaryDate"/></td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.summaryDateInput" property="summaryDateInput" size="10" maxlength="10"/><bean:message key="message.dateFormat"/></td>
		</tr>
		<tr>
			<td><bean:message key="label.summaryHour"/></td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.summaryHourInput" property="summaryHourInput" size="5" maxlength="5"/><bean:message key="message.hourFormat"/></td>		
		<tr/>	
		<tr>
			<td><bean:message key="label.room"/>:</td>
			<td><html:select bundle="HTMLALT_RESOURCES" altKey="select.room"  property="room">
					<html:option key="label.summary.select" value="" />
					<html:options collection="rooms" property="idInternal" labelProperty="nome"/>
				</html:select></td>		
		<tr/>	
		</logic:equal>
		<logic:equal name="forHidden" value="true">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.summaryHourInput" property="summaryHourInput"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.room" property="room"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.summaryDateInput" property="summaryDateInput" />
		</logic:equal>
	</logic:present>
	<logic:notPresent name="forHidden">
		<tr>
			<td><bean:message key="label.summaryHour"/></td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.summaryHourInput" property="summaryHourInput" size="5" maxlength="5"/><bean:message key="message.hourFormat"/></td>		
		<tr/>	
		<tr>
			<td><bean:message key="label.room"/>:</td>
			<td><html:select bundle="HTMLALT_RESOURCES" altKey="select.room"  property="room">
					<html:option key="label.summary.select" value="" />
					<html:options collection="rooms" property="idInternal" labelProperty="nome"/>
				</html:select></td>		
		<tr/>	
	</logic:notPresent>	
	<tr>
		<td><bean:message key="label.studentNumber.attended.lesson" />:</td>
		<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.studentsNumber" property="studentsNumber" size="4"/></td>
	</tr>
</table>
<br/>
<br/>	
<!-- Teachers -->

<logic:equal name="loggedIsResponsible" value="true">
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
		 		<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.teacher" property="teacher" value="<%= professorshipId.toString()%>" /></td>				
		 	</tr>
		 </logic:iterate>
		<tr>
			<td><bean:message key="label.teacher.in" />:</td>
			<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.teacher" property="teacher" value="0" /></td>
			<td><bean:message key="label.number" />:</td><td><html:text bundle="HTMLALT_RESOURCES" altKey="text.teacherNumber" property="teacherNumber" size="4" /></td>
		</tr>
		<tr>
			<td><bean:message key="label.teacher.out" />:</td>
			<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.teacher" property="teacher" value="-1" /></td>
			<td><bean:message key="label.name" />:</td><td><html:text bundle="HTMLALT_RESOURCES" altKey="text.teacherName" property="teacherName" size="40"/></td>				
		</tr>				
	</table>
</logic:equal>
<logic:equal name="loggedIsResponsible" value="false">
	<bean:define name="loggedTeacherProfessorship" id="loggedTeacherProfessorship"/>
	<input alt="input.teacher" type="hidden" name="teacher" value="<%=loggedTeacherProfessorship%>" />
</logic:equal>
<br/>
<br/>		
<table border="0">
	<tr>
		<td colspan='2'><strong><bean:message key="label.title"/></strong></td>
	</tr>
	<tr>		
		<td colspan='2'><html:text bundle="HTMLALT_RESOURCES" altKey="text.title" size="66" property="title"/></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td colspan='2'><strong><bean:message key="label.summaryText"/></strong></td>
	</tr>
	<tr>
		<td colspan='2'>	
			<logic:present name="naoVerEditor">
				<bean:message key="label.editor"/>
				<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.editor" property="editor" value="false" disabled="true"/>
				&nbsp;
				<bean:message key="label.plain.text"/>
				<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.editor" property="editor" value="true"/>						
			</logic:present>
			<logic:notPresent name="naoVerEditor">						
			    <bean:message key="label.editor"/>
				<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.editor" property="editor" value="true" onclick="this.form.method.value='prepareInsertSummary';this.form.summaryText.value='';this.form.page.value=0;this.form.submit();"/>
				&nbsp;
				<bean:message key="label.plain.text"/>
				<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.editor" property="editor" value="false" onclick="this.form.method.value='prepareInsertSummary';this.form.page.value=0;this.form.submit();"/>					
			</logic:notPresent>				
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
	   <logic:present name="verEditor">
		<td>	
			<script language="JavaScript" type="text/javascript"> 
			<!--
			initEditor();		
			//-->
			</script>
			
			<noscript>JavaScript must be enable to use this form <br/> </noscript>
			
			<script language="JavaScript" type="text/javascript"> 
			<!--
			writeTextEditor(700, 500, document.forms[0].summaryText.value);		
			//-->
			</script>		
		</td>
	  </logic:present>
	  <logic:notPresent name="verEditor">	 	 
		 <td colspan='2'><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.summaryText" rows="30" cols="100" property="summaryText"/></td>
	  </logic:notPresent>
	</tr>
</table>

<br/>
<br/>
<logic:present name="verEditor">
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" titleKey="message.button.save" onclick="this.form.save.value='0';this.form.summaryText.value=update()"><bean:message key="button.save"/>                    		         	
	</html:submit> 
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" titleKey="message.button.save.new" onclick="this.form.save.value='1';this.form.summaryText.value=update()" ><bean:message key="button.save.new"/>                    		         	
	</html:submit> 
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" titleKey="message.button.save.equal" onclick="this.form.save.value='2';this.form.summaryText.value=update()"><bean:message key="button.save.equal"/>                                
	</html:submit> 
</logic:present>
<logic:notPresent name="verEditor"> 
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" titleKey="message.button.save" onclick="this.form.save.value='0'"><bean:message key="button.save"/>                                
	</html:submit> 
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" titleKey="message.button.save.new" onclick="this.form.save.value='1'"><bean:message key="button.save.new"/>                                
	</html:submit> 
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" titleKey="message.button.save.equal" onclick="this.form.save.value='2'"><bean:message key="button.save.equal"/>                                
	</html:submit> 
</logic:notPresent>

<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>  
<br/><br/>
<bean:message key="message.text.editor.requires"/>
</html:form>
</logic:present>

<logic:notPresent name="summaryToInsert">
	<h2><bean:message key="error.summary.impossible.insert" /></h2>
</logic:notPresent>
