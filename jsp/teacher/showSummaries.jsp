<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<span class="error"><html:errors/></span>
<br />

<logic:present name="siteView"> 
<bean:define id="component" name="siteView" property="commonComponent"/>
<bean:define id="executionCourse" name="component" property="executionCourse"/> 
<bean:define id="objectCode" name="executionCourse" property="idInternal"/>
<bean:define id="bodyComponent" name="siteView" property="component"/>
<bean:define id="lessonTypes" name="bodyComponent" property="lessonTypes" />
<bean:define id="shifts" name="bodyComponent" property="infoShifts" />
<bean:define id="professorships" name="bodyComponent" property="infoProfessorships" />

<table width="100%">
	<tr>
		<td class="infoop"><bean:message key="label.summary.explanation" /></td>
	</tr>
</table>
<br />

<html:form action="/showSummaries">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="showSummaries"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%= objectCode.toString() %>"/>

<table width="55%" border='0'>
	<tr>
		<td colspan="2"><strong><bean:message key="label.showSummaries" />&nbsp;<bean:message key="label.for" />:</strong></td>
	</tr>
	<tr>
	<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.ShiftType" bundle="ENUMERATION_RESOURCES"/>
		<td width="15%"><bean:message key="message.summaryType" /></td>
		<td width="40%">
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.bySummaryType" property="bySummaryType" onchange="this.form.method.value='showSummaries';this.form.page.value=0;this.form.submit();">
				<html:option  value="0" key="label.showBy.all" />
				<html:options collection="values" property="value" labelProperty="label"/>
			</html:select>		
		</td>	
	</tr>
	<tr>
		<td width="15%"><bean:message key="label.shift" /></td>
		<td width="40%">
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.byShift" property="byShift" onchange="this.form.method.value='showSummaries';this.form.page.value=0;this.form.submit();">
				<html:option  value="0" key="label.showBy.all" />
				<html:options collection="shifts" property="idInternal" labelProperty="lessons"/>
			</html:select>		
		</td>
	</tr>
	<tr>
		<td width="15%"><bean:message key="label.teacher" /></td>
		<td width="40%">
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.byTeacher" property="byTeacher" onchange="this.form.method.value='showSummaries';this.form.page.value=0;this.form.submit();">
				<html:option  value="0" key="label.showBy.all" />
				<html:options collection="professorships" property="idInternal" labelProperty="infoTeacher.infoPerson.nome"/>
				<html:option  value="-1" key="label.others"/>
			</html:select>
		</td>
	</tr>
	<%--<tr>
		<td colspan='2' align='center'>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.show"/></html:submit>		
		</td>
	</tr>--%>
</table>
</html:form>

<div class="gen-button">
	<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
	<html:link page="<%= "/summariesManager.do?method=prepareInsertSummary&amp;page=0&amp;objectCode=" + pageContext.findAttribute("objectCode") %>">
			<bean:message key="label.insertSummary" />
	</html:link>
</div>
<br />
<br />


<table width="100%">
	<logic:iterate id="summary" name="bodyComponent" property="infoSummaries" type="net.sourceforge.fenixedu.dataTransferObject.InfoSummary">
		<bean:define id="summaryCode" name="summary" property="idInternal" />
		<logic:present name="summary" property="title">	
		<logic:notEmpty name="summary" property="title">		
		<tr>
		   	<td>
		   		<strong><bean:write name="summary" property="title"/></strong>
			</td>
		</tr>
		</logic:notEmpty>
		</logic:present>
		<!--
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
		-->
		<!--<logic:notPresent name="summary" property="infoShift">-->
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
					       	<logic:notEmpty name="summary" property="summaryType">
				   				<bean:write name="summary" property="summaryType.fullNameTipoAula" />						   	
			       			</logic:notEmpty>
			       		</logic:equal>
			       	
			       		<logic:equal name="summary" property="isExtraLesson" value="true">		     
			       	   		<!-- extra lesson -->
				    		<bean:message key="label.extra.lesson" />
			       		</logic:equal>
			       	</logic:notEmpty>
			       				       	
			       	&nbsp;<bean:write name="summary" property="summaryDateInput"/>
		           	&nbsp;<bean:write name="summary" property="summaryHourInput"/>
		   		</td>
			</tr>		
		<!--</logic:notPresent>-->

		<tr>
			<td>
				<logic:notEmpty name="summary" property="infoProfessorship">
					<bean:message key="label.teacher.abbreviation" />&nbsp;				
					<bean:write name="summary" property="infoProfessorship.infoTeacher.infoPerson.nome" />&nbsp;
				</logic:notEmpty>
				<logic:notEmpty name="summary" property="infoTeacher">
					<bean:message key="label.teacher.abbreviation" />&nbsp;								
					<bean:write name="summary" property="infoTeacher.infoPerson.nome" />&nbsp;			
				</logic:notEmpty>
				<logic:notEmpty name="summary" property="teacherName">
					<!--<bean:message key="label.teacher.abbreviation" />&nbsp;-->
					<bean:write name="summary" property="teacherName" />&nbsp;			
				</logic:notEmpty>				
			</td>
		</tr>
		<tr>
			<td>
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
			</td>
		</tr>
		<tr>
		   	<td>
		   		<br/>
		       	<bean:write name="summary" property="summaryText" filter="false"/>
		   	</td>
		</tr>
		<tr>
		   	<td>
				<span class="px9"><bean:message key="label.lastModificationDate" /> <bean:write name="summary" property="lastModifiedDateFormatted"/> </span>
		   	</td>
		</tr>
		<tr><td>&nbsp;</td></tr>    
		<tr>
		  	<td>
				<div class="gen-button">
					<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
					<html:link page="<%= "/summariesManager.do?method=prepareEditSummary&amp;page=0&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;summaryCode=" + summaryCode %>">
						<bean:message key="button.edit" /> 
					</html:link>
				</div>
				<div class="gen-button">
					<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
					<html:link page="<%= "/summariesManager.do?method=deleteSummary&amp;page=0&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;summaryCode=" + summaryCode %>" onclick="return confirm('Tem a certeza que deseja apagar este sum�rio?')">
						<bean:message key="button.delete" />
					</html:link>
				</div>
				<br />
				<br />
			</td>
		</tr>      
	</logic:iterate>		
</table>
</logic:present>

<logic:notPresent name="siteView"> 
	<span class="error"><bean:message key="error.summary.impossible.show" /></span>
</logic:notPresent>
