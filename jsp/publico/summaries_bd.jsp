<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>


<logic:present name="siteView"> 
<bean:define id="component" name="siteView" property="component"/>
<bean:define id="executionCourse" name="component" property="executionCourse"/>
<bean:define id="objectCode" name="component" property="infoSite.idInternal"/>
<bean:define id="lessonTypes" name="component" property="lessonTypes" />
<bean:define id="shifts" name="component" property="infoShifts" />
<bean:define id="professorships" name="component" property="infoProfessorships" />

<html:form action="/viewSiteSummaries">
<html:hidden property="page" value="1"/>
<html:hidden property="method" value="summaries"/>
<html:hidden property="objectCode" value="<%= objectCode.toString() %>"/>

<table width="60%" border='0'>
	<tr>
		<td colspan="2"><strong><bean:message key="label.showSummaries" />&nbsp;<bean:message key="label.for" />:</strong></td>
	</tr>
	<tr>
		<td width="20%"><bean:message key="message.summaryType" /></td>
		<td width="40%">
			<html:select property="bySummaryType">
				<html:option  value="0" key="label.showBy.all" />
				<html:options collection="lessonTypes" property="tipo" labelProperty="fullNameTipoAula"/>
			</html:select>		
		</td>	
	</tr>
	<tr>
		<td width="20%"><bean:message key="label.shift" /></td>
		<td width="40%">
			<html:select property="byShift">
				<html:option  value="0" key="label.showBy.all" />
				<html:options collection="shifts" property="idInternal" labelProperty="lessons"/>
			</html:select>		
		</td>
	</tr>
	<tr>
		<td width="20%"><bean:message key="label.teacher" /></td>
		<td width="40%">
			<html:select property="byTeacher">
				<html:option  value="0" key="label.showBy.all" />
				<html:options collection="professorships" property="idInternal" labelProperty="infoTeacher.infoPerson.nome"/>
				<html:option  value="-1" key="label.others" />
			</html:select>			
		</td>
	</tr>
	<tr>
		<td colspan='2' align='center'>
			<html:submit styleClass="inputbutton"><bean:message key="button.show"/></html:submit>		
		</td>
	</tr>
</table>
</html:form>

<table width="100%">
	<logic:iterate id="summary" name="component" property="infoSummaries" type="DataBeans.InfoSummary">
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
		<logic:notPresent name="summary" property="infoShift">
			<tr>
			   	<td>
				   	<bean:message key="label.summary.lesson" />
			       	&nbsp;<bean:write name="summary" property="summaryDateInput"/>
		           	&nbsp;<bean:write name="summary" property="summaryHourInput"/>
		   		</td>
			</tr>		
		</logic:notPresent>
				
		
		<tr>
			<td>
				<logic:notEmpty name="summary" property="infoProfessorship">
					<bean:message key="label.teacher.abbreviation" />&nbsp;				
					<bean:write name="summary" property="infoProfessorship.infoTeacher.infoPerson.nome" />&nbsp;
				</logic:notEmpty>
				<logic:notEmpty name="summary" property="infoTeacher">
				<bean:message key="label.teacher.abbreviation" />&nbsp;
					<bean:message key="label.teacher.abbreviation" />&nbsp;								
					<bean:write name="summary" property="infoTeacher.infoPerson.nome" />&nbsp;			
				</logic:notEmpty>
				<logic:notEmpty name="summary" property="teacherName">
					<bean:message key="label.teacher.abbreviation" />&nbsp;
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
	</logic:iterate>		
</table>
</logic:present>