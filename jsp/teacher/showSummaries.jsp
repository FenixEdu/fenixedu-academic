<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<h2><bean:message key="link.summaries" /></h2>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<logic:present name="siteView"> 
<bean:define id="component" name="siteView" property="commonComponent"/>
<bean:define id="executionCourse" name="component" property="executionCourse"/> 
<bean:define id="objectCode" name="executionCourse" property="idInternal"/>
<bean:define id="bodyComponent" name="siteView" property="component"/>
<bean:define id="lessonTypes" name="bodyComponent" property="lessonTypes" />
<bean:define id="shifts" name="bodyComponent" property="infoShifts" />
<bean:define id="professorships" name="bodyComponent" property="infoProfessorships" />


<div class="infoop2"><bean:message key="label.summary.explanation" /></div>

<html:form action="/showSummaries">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="showSummaries"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%= objectCode.toString() %>"/>


<p class="mtop15 mbottom05"><strong><bean:message key="label.showSummaries" /> <bean:message key="label.for" /></strong>:</p>
<table class="tstyle5 thright thlight mtop05">
	<tr>
	<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.ShiftType" bundle="ENUMERATION_RESOURCES"/>
		<td><bean:message key="message.summaryType" />:</td>
		<td>
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.bySummaryType" property="bySummaryType" onchange="this.form.method.value='showSummaries';this.form.page.value=0;this.form.submit();">
				<html:option  value="0" key="label.showBy.all" />
				<html:options collection="values" property="value" labelProperty="label"/>
			</html:select>
			<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
				<bean:message key="button.submit"/>
			</html:submit>	
		</td>	
	</tr>
	<tr>
		<td><bean:message key="label.shift" />:</td>
		<td>
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.byShift" property="byShift" onchange="this.form.method.value='showSummaries';this.form.page.value=0;this.form.submit();">
				<html:option  value="0" key="label.showBy.all" />
				<html:options collection="shifts" property="idInternal" labelProperty="lessons"/>
			</html:select>
			<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
				<bean:message key="button.submit"/>
			</html:submit>
		</td>
	</tr>
	<tr>
		<td><bean:message key="label.teacher" />:</td>
		<td>
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.byTeacher" property="byTeacher" onchange="this.form.method.value='showSummaries';this.form.page.value=0;this.form.submit();">
				<html:option  value="0" key="label.showBy.all" />
				<html:options collection="professorships" property="idInternal" labelProperty="infoTeacher.infoPerson.nome"/>
				<html:option  value="-1" key="label.others"/>
			</html:select>
			<html:submit styleId="javascriptButtonID3" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
				<bean:message key="button.submit"/>
			</html:submit>
		</td>
	</tr>
	<%--<tr>
		<td colspan='2' align='center'>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.show"/></html:submit>		
		</td>
	</tr>--%>
</table>
</html:form>



<div class="gen-button mbottom2">
	<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
	<html:link page="<%= "/summariesManagement.do?method=prepareInsertSummary&amp;page=0&amp;executionCourseID=" + pageContext.findAttribute("objectCode") %>">
			<bean:message key="label.insertSummary" />
	</html:link>
</div>



	<logic:iterate id="summary" name="bodyComponent" property="infoSummaries" type="net.sourceforge.fenixedu.dataTransferObject.InfoSummary">
		<bean:define id="summaryCode" name="summary" property="idInternal" />
		<logic:present name="summary" property="title">	
		<logic:notEmpty name="summary" property="title">		

<%-- Título--%>
		<h3 class="mtop2 mbottom05"><bean:write name="summary" property="title"/></h3>

		</logic:notEmpty>
		</logic:present>

<%-- Corpo--%>
		<p class="mvert05">
			<bean:write name="summary" property="summaryText" filter="false"/>
		</p>
	
	<%--
		<logic:present name="summary" property="infoShift">
		<tr>
		   	<td>		    
		       	<logic:empty name="summary" property="isExtraLesson">
		       		<!-- normal lesson -->
			       	<bean:message key="label.summary.lesson" />
		       	</logic:empty>				       			        	

		       	<logic:notEmpty name="summary" property="isExtraLesson">
		       		<logic:equal name="summary" property="isExtraLesson" value="false">		
			       		<!-- normal lesson -->
				       	<bean:message key="label.summary.lesson" />
		       		</logic:equal>
		       	
		       		<logic:equal name="summary" property="isExtraLesson" value="true">		     
		       	   		<!-- extra lesson -->
			    		<bean:message key="label.extra.lesson" />
		       		</logic:equal>
		       	</logic:notEmpty>

		       	<bean:write name="summary" property="infoShift.tipo.fullNameTipoAula" />		       	
			    &nbsp;<bean:write name="summary" property="summaryDateInput"/>
	           	&nbsp;<bean:write name="summary" property="summaryHourInput"/>     			       				       			       	
	           	
	           	<logic:present name="summary" property="infoRoom">
	           		<logic:notEmpty name="summary" property="infoRoom">
	           			&nbsp;<bean:write name="summary" property="infoRoom.nome" />
	           		</logic:notEmpty>
	           	</logic:present>
		   	</td>
		</tr>
		</logic:present>
	--%>

<%-- Tipo de aula --%>

	<!--<logic:notPresent name="summary" property="infoShift">-->
		<p class="smalltxt greytxt1 mvert05">
		<em>
			<logic:empty name="summary" property="isExtraLesson">
			  		<!-- normal lesson -->
				<bean:message key="label.summary.lesson" />
			</logic:empty>
			
			<logic:notEmpty name="summary" property="isExtraLesson">
				<logic:equal name="summary" property="isExtraLesson" value="false">		
					<!-- normal lesson -->
			 	<bean:message key="label.summary.lesson" />
			 	<logic:notEmpty name="summary" property="summaryType">
			<bean:write name="summary" property="summaryType.fullNameTipoAula" />						   	
					</logic:notEmpty>
				</logic:equal>
			
				<logic:equal name="summary" property="isExtraLesson" value="true">		     
			   		<!-- extra lesson -->
			<bean:message key="label.extra.lesson" />
					</logic:equal>
				</logic:notEmpty>
							       	
				<bean:write name="summary" property="summaryDateInput"/>
			   	<bean:write name="summary" property="summaryHourInput"/>
			 - 
	<!--</logic:notPresent>-->
	
<%-- Docente --%>

			<logic:notEmpty name="summary" property="infoProfessorship">
				<bean:message key="label.teacher.abbreviation" />&nbsp;				
				<bean:write name="summary" property="infoProfessorship.infoTeacher.infoPerson.nome" /> 
			</logic:notEmpty>
			<logic:notEmpty name="summary" property="infoTeacher">
				<bean:message key="label.teacher.abbreviation" />&nbsp;								
				<bean:write name="summary" property="infoTeacher.infoPerson.nome" /> 
			</logic:notEmpty>
			<logic:notEmpty name="summary" property="teacherName">
				<bean:message key="label.teacher.abbreviation" />
				<bean:write name="summary" property="teacherName" /> 
			</logic:notEmpty>				

<%-- Alunos presentes --%>

			<logic:present name="summary" property="studentsNumber">			
			<logic:notEmpty name="summary" property="studentsNumber">			
				<bean:define id="studentsAttended" name="summary" property="studentsNumber" />
				<logic:greaterThan name="studentsAttended" value="0">
					<bean:message key="message.studentsnumber.attended.lesson" arg0="<%= studentsAttended.toString() %>"/>
				</logic:greaterThan>
				<logic:lessEqual name="studentsAttended" value="0">
					<bean:message key="message.studentsnumber.attended.lesson.no" />				
				</logic:lessEqual>
			</logic:notEmpty>
			<logic:empty name="summary" property="studentsNumber">			
					<bean:message key="message.studentsnumber.attended.lesson.no" />								
			</logic:empty>
			</logic:present>				
			<logic:notPresent name="summary" property="studentsNumber">			
					<bean:message key="message.studentsnumber.attended.lesson.no" />								
			</logic:notPresent>
			 - 

<%-- Últimas modificações--%>

		<span class="px9"><bean:message key="label.lastModificationDate" /> <bean:write name="summary" property="lastModifiedDateFormatted"/> </span>
	</em>
	</p>

<%-- Links--%>
		<div class="gen-button">
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
			<html:link page="<%= "/summariesManagement.do?method=prepareEditSummary&amp;page=0&amp;executionCourseID=" + pageContext.findAttribute("objectCode") + "&amp;summaryID=" + summaryCode %>">
				<bean:message key="button.edit" /> 
			</html:link>
			 
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
			<html:link page="<%= "/summariesManager.do?method=deleteSummary&amp;page=0&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;summaryCode=" + summaryCode %>" onclick="return confirm('Tem a certeza que deseja apagar este sumário?')">
				<bean:message key="button.delete" />
			</html:link>
		</div>
   
	</logic:iterate>		
</logic:present>

<logic:notPresent name="siteView"> 
	<span class="error"><!-- Error messages go here --><bean:message key="error.summary.impossible.show" /></span>
</logic:notPresent>
