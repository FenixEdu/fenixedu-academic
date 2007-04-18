<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<logic:present name="siteView"> 
	<bean:define id="component" name="siteView" property="component"/>
	<bean:define id="executionCourse" name="component" property="executionCourse"/>
	<bean:define id="objectCode" name="component" property="infoSite.idInternal"/>
	<bean:define id="lessonTypes" name="component" property="lessonTypes" />
	<bean:define id="shifts" name="component" property="infoShifts" />
	<bean:define id="professorships" name="component" property="infoProfessorships" />


	<h2><bean:message key="label.summaries" /></h2>

	<html:form action="/viewSiteSummaries">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="summaries"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%= objectCode.toString() %>"/>
        


		<table class="tab_simple" cellspacing="2" cellpadding="0">
			<tr>
				<td><bean:message key="label.summary.lesson" />:</td>
				<td>
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.bySummaryType" property="bySummaryType" onchange="this.form.method.value='summaries';this.form.page.value=0;this.form.submit();">
						<html:option  value="0" key="label.showBy.all" />
						<html:options collection="lessonTypes" property="name" labelProperty="fullNameTipoAula"/>
					</html:select>
					<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message key="button.submit"/>
					</html:submit>
				</td>	
			</tr>
			<tr>
				<td><bean:message key="label.shift" />:</td>
				<td>
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.byShift" property="byShift" onchange="this.form.method.value='summaries';this.form.page.value=0;this.form.submit();">
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
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.byTeacher" property="byTeacher" onchange="this.form.method.value='summaries';this.form.page.value=0;this.form.submit();">
						<html:option  value="0" key="label.showBy.all" />
						<html:options collection="professorships" property="idInternal" labelProperty="infoTeacher.infoPerson.nome"/>
						<html:option  value="-1" key="label.others" />
					</html:select>			
					<html:submit styleId="javascriptButtonID3" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message key="button.submit"/>
					</html:submit>
				</td>
			</tr>
		</table>	
	</html:form>


	<logic:iterate id="summary" name="component" property="infoSummaries" type="net.sourceforge.fenixedu.dataTransferObject.InfoSummary">
		<bean:define id="summaryCode" name="summary" property="idInternal" />
		<div id="s<%= summary.getIdInternal().toString() %>">
			<logic:present name="summary" property="infoShift">
			<h3>
				<bean:write name="summary" property="summaryDateInput"/>
				<bean:write name="summary" property="summaryHourInput"/>     			       				       			       	
				   	
				<logic:present name="summary" property="infoRoom">
					<logic:notEmpty name="summary" property="infoRoom">
						(<bean:message key="label.room" />
						<bean:write name="summary" property="infoRoom.nome" />)
		       		</logic:notEmpty>
		       	</logic:present>
				
				<br/>
				<span class="greytxt">
					<logic:empty name="summary" property="isExtraLesson">
						<bean:message key="label.summary.lesson" />
					</logic:empty>
		       	
					<logic:notEmpty name="summary" property="isExtraLesson">
			       		<logic:equal name="summary" property="isExtraLesson" value="false">		
							<bean:message key="label.summary.lesson" />
			       		</logic:equal>
			       	
			       		<logic:equal name="summary" property="isExtraLesson" value="true">		     
							<bean:message key="label.extra.lesson" />
			       		</logic:equal>
			       	</logic:notEmpty>
				
					<bean:write name="summary" property="infoShift.tipo.fullNameTipoAula" />	
				</span>       	
			</h3>
			</logic:present>
		
			<logic:notPresent name="summary" property="infoShift">
				<bean:message key="label.summary.lesson" />
			    &nbsp;<bean:write name="summary" property="summaryDateInput"/>
		        &nbsp;<bean:write name="summary" property="summaryHourInput"/>	
			</logic:notPresent>
		
			<logic:present name="summary" property="title">	
				<logic:notEmpty name="summary" property="title">		
					<p><strong><bean:write name="summary" property="title"/></strong></p>
				</logic:notEmpty>
			</logic:present>
		
			<bean:write name="summary" property="summaryText" filter="false"/><br/>
		
			<div class="details">
				<span class="updated-date">
					<bean:message key="message.modifiedOn" />
					<bean:write name="summary" property="lastModifiedDateFormatted" />
				</span>
		
				<logic:notEmpty name="summary" property="infoProfessorship">
					<span class="author">
						<bean:message key="label.teacher.abbreviation" />				
						<bean:write name="summary" property="infoProfessorship.infoTeacher.infoPerson.nome" />	
					</span>
				</logic:notEmpty>
		
				<logic:notEmpty name="summary" property="infoTeacher">
					<span class="author">
						<bean:message key="label.teacher.abbreviation" />
						<bean:write name="summary" property="infoTeacher.infoPerson.nome" />
					</span>
				</logic:notEmpty>
		
				<logic:notEmpty name="summary" property="teacherName">
					<span class="author"><bean:write name="summary" property="teacherName" /></span>
				</logic:notEmpty>				
		
				<logic:present name="summary" property="studentsNumber">
					<span class="comment">
						<bean:message key="message.presences" />			
						<logic:notEmpty name="summary" property="studentsNumber">			
							<bean:define id="studentsAttended" name="summary" property="studentsNumber" />
							
							<logic:greaterEqual name="studentsAttended" value="0">
								<bean:message key="message.students" arg0="<%= studentsAttended.toString() %>"/>
							</logic:greaterEqual>
				
							<logic:lessThan name="studentsAttended" value="0">
								<i><bean:message key="message.notSpecified" /></i>				
							</logic:lessThan>
						</logic:notEmpty>
			
						<logic:empty name="summary" property="studentsNumber">			
							<i><bean:message key="message.notSpecified" /></i>								
						</logic:empty>
					</span>
				</logic:present>				
		
				<logic:notPresent name="summary" property="studentsNumber">			
					<span class="comment">
						<bean:message key="message.presences" />
						<i><bean:message key="message.notSpecified" /></i>								
					</span>
				</logic:notPresent>
			</div>
		</div>
	</logic:iterate>	
</logic:present>