<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>

<script language="JavaScript">	
function check(e,v){
	if (e.className == "dnone")
  	{
	  e.className = "dblock";
	  v.value = "-";
	}
	else {
	  e.className = "dnone";
  	  v.value = "+";
	}
}
</script>

<logic:present name="summariesManagementBean">

	<logic:empty name="summariesManagementBean" property="summary">			
		<h2><bean:message key="title.summary.insert" bundle="DEFAULT"/></h2>
	</logic:empty>
	<logic:notEmpty name="summariesManagementBean" property="summary">			
		<h2><bean:message key="title.summary.edit" bundle="DEFAULT"/></h2>
	</logic:notEmpty>
	
	<div class="infoop2">
		<a href="#" class="dnone" id="instructionsButton" onclick="check(document.getElementById('instructions'), document.getElementById('instructionsButton'));"><bean:message key="link.home" bundle="DEFAULT"/></a>
		<div id="instructions" class="dblock">
			<ul class="mvert025">
				<li><bean:message key="label.summary.management.instructions1" bundle="DEFAULT"/></li>
				<li><bean:message key="label.summary.management.instructions2" bundle="DEFAULT"/></li>
				<li><bean:message key="label.summary.management.instructions3" bundle="DEFAULT"/></li>
				<li><bean:message key="label.summary.management.instructions4" bundle="DEFAULT"/></li>
			</ul>
		</div>
	</div>
		
	<script>
		check(document.getElementById('instructions'), document.getElementById('instructionsButton'));
		document.getElementById('instructionsButton').className="dblock";
	</script>
	
	<bean:define id="executionCourseID" name="summariesManagementBean" property="executionCourse.idInternal" />
	<bean:define id="teacherNumber" name="loggedTeacherProfessorship" property="teacher.teacherNumber" />
	<bean:define id="submitURL">/summariesManagement.do?method=submit&teacherNumber__=<bean:write name="teacherNumber"/></bean:define>
	
	<logic:messagesPresent message="true">
		<p>
		<span class="error"><!-- Error messages go here -->
			<html:messages id="message" message="true">
				<bean:write name="message"/>
			</html:messages>
		</span>
		<p>
	</logic:messagesPresent>
		
	<%-- Lesson --%>			
	<h3 class="mbottom0"><bean:message key="label.lesson" bundle="DEFAULT"/></h3>
	<table class="tstyle5">
		<tr>
			<%-- SummaryType --%>				
			<td><bean:message key="label.type"/></td>
			<td>
				<div style="display: inline;">
					<fr:form>
						<fr:edit id="summariesManagementBeanWithSummaryType" name="summariesManagementBean" schema="ChooseSummaryType" nested="true">
							<fr:destination name="postBack" path="/summariesManagement.do?method=chooseSummaryType"/>
							<fr:layout name="flow">
								<fr:property name="labelTerminator" value=""/>
								<fr:property name="labelExcluded" value="true"/>
							</fr:layout>	
						</fr:edit>
					</fr:form>
				</div>				
			</td>	
		</tr>	
		<tr>
			<td><bean:message key="label.shift"/>:</td>
			<td>
				<%-- Shift --%>						
				<fr:form>
					<fr:edit id="summariesManagementBeanWithShifts" name="summariesManagementBean" schema="ListShiftsToCreateSummary" nested="true">
						<fr:destination name="postBack" path="<%= submitURL %>"/>				
						<fr:layout name="flow">
							<fr:property name="labelTerminator" value=""/>
							<fr:property name="labelExcluded" value="true"/>
						</fr:layout>
					</fr:edit>
				</fr:form>					
			</td>	
		</tr>	
		<logic:notEqual name="summariesManagementBean" property="summaryType" value="EXTRA_SUMMARY">
			<tr>
				<td><bean:message key="label.lesson" bundle="DEFAULT"/>:</td>
				<td>
					<%-- Lesson --%>
					<fr:form>
						<fr:edit id="summariesManagementBeanWithLessons" name="summariesManagementBean" schema="ListShiftLessonsToCreateSummary" nested="true">
							<fr:destination name="postBack" path="<%= submitURL %>"/>				
							<fr:layout name="flow">
								<fr:property name="labelTerminator" value=""/>
								<fr:property name="labelExcluded" value="true"/>
							</fr:layout>
						</fr:edit>
					</fr:form>
				</td>
			</tr>		
			<tr>
				<td><bean:message key="label.date" bundle="DEFAULT"/>:</td>
				<td>
					<%-- Date --%>
					<fr:form>
						<fr:edit id="summariesManagementBeanWithDate" name="summariesManagementBean" schema="LisPossibleDatesToCreateSummary" nested="true">
							<fr:destination name="postBack" path="<%= submitURL %>"/>	
							<fr:layout name="flow">
								<fr:property name="labelTerminator" value=""/>
								<fr:property name="labelExcluded" value="true"/>
							</fr:layout>			
						</fr:edit>
					</fr:form>
				</td>
			</tr>
		</logic:notEqual>
	</table>	
	
	<%-- Associate --%>
	<h3 class="mbottom0"> <bean:message key="label.use" bundle="DEFAULT"/></h3>
	<table class="tstyle5">
		<%-- LessonPlannings --%>
		<tr>
			<td><bean:message key="label.lessonPlanning" bundle="DEFAULT"/>:</td>
			<td>
				<bean:define id="chooseLessonPlanningURL">/summariesManagement.do?method=chooseLessonPlanning&teacherNumber_=<bean:write name="teacherNumber"/></bean:define>	
				<fr:form>
					<fr:edit id="summariesManagementBeanWithLessonPlanning" name="summariesManagementBean" schema="ListLessonPlanningsToSummariesManagement" nested="true">
						<fr:destination name="postBack" path="<%= chooseLessonPlanningURL %>"/>	
							<fr:layout name="flow">
								<fr:property name="labelTerminator" value=""/>
								<fr:property name="labelExcluded" value="true"/>
							</fr:layout>	
					</fr:edit>	
				</fr:form>
			</td>
		</tr>	
		<%-- LastSummaries --%>
		<tr>
			<td><bean:message key="message.summaryText.last" bundle="DEFAULT"/>:</td>
			<td>
				<bean:define id="chooseLastSummaryURL">/summariesManagement.do?method=chooseLastSummary&teacherNumber_=<bean:write name="teacherNumber"/></bean:define>	
				<fr:form>
					<fr:edit id="summariesManagementBeanWithLastSummary" name="summariesManagementBean" schema="ListLastSummariesToSummariesManagement" nested="true">
						<fr:destination name="postBack" path="<%= chooseLastSummaryURL %>"/>	
							<fr:layout name="flow">
								<fr:property name="labelTerminator" value=""/>
								<fr:property name="labelExcluded" value="true"/>
							</fr:layout>	
					</fr:edit>	
				</fr:form>	
			</td>
		</tr>
	</table>	
	
	<%-- Teacher --%>
	<logic:equal name="loggedIsResponsible" value="true">
		<h3 class="mbottom0"><bean:message key="label.teacher" bundle="DEFAULT"/></h3>
		<bean:define id="chooseTeacherURL">/summariesManagement.do?method=chooseTeacher&teacherNumber_=<bean:write name="teacherNumber"/></bean:define>
		<fr:form action="<%= chooseTeacherURL %>">			
			<fr:edit id="summariesManagementBeanWithTeacher" name="summariesManagementBean" visible="false" nested="true" />								
			<table class="tstyle5">
				<logic:notEmpty name="summariesManagementBean" property="professorship">
					<bean:define id="professorship" name="summariesManagementBean" property="professorship" />
				 	<bean:define id="professorshipId" name="summariesManagementBean" property="professorship.idInternal" />
					<tr>
				 		<td><html:radio name="summariesManagementForm" property="teacher" value="<%= professorshipId.toString()%>" onclick="this.form.submit();"/></td>				
				 		<td><bean:write name="professorship" property="teacher.person.name"/></td>
			 		</tr>
				</logic:notEmpty>
				<logic:empty name="summariesManagementBean" property="professorship">
					<bean:define id="professorship" name="summariesManagementBean" property="professorshipLogged" />
				 	<bean:define id="professorshipId" name="summariesManagementBean" property="professorshipLogged.idInternal" />
					<tr>
				 		<td><html:radio name="summariesManagementForm" property="teacher" value="<%= professorshipId.toString()%>" onclick="this.form.submit();"/></td>				
				 		<td><bean:write name="professorship" property="teacher.person.name"/></td>
			 		</tr>
				</logic:empty>
				<tr>
					<td><html:radio name="summariesManagementForm" property="teacher" value="0" /></td>
					<td>
						<bean:message key="label.teacher.in" />
						<html:text name="summariesManagementForm" onchange="this.form.submit();" property="teacherNumber" size="4" />
						(<bean:message key="label.number" />)
					</td>
				</tr>
				<tr>
					<td><html:radio name="summariesManagementForm" property="teacher" value="-1" /></td>
					<td>
						<bean:message key="label.teacher.out" />
						<html:text name="summariesManagementForm" onchange="this.form.submit();" property="teacherName" size="40"/>
						(<bean:message key="label.name" />)
					</td>
				</tr>									
			</table>
		</fr:form>
	</logic:equal>	
					
	<%-- Summary --%>	
	<h3 class="mbottom0"><bean:message key="message.summaryText" bundle="DEFAULT"/></h3>
	<bean:define id="showSummaries">/summariesManagement.do?method=prepareShowSummaries&page=0&executionCourseID=<bean:write name="executionCourseID"/>&teacherNumber_=<bean:write name="teacherNumber"/></bean:define>			
	<bean:define id="createSummaryURL">/summariesManagement.do?teacherNumber_=<bean:write name="teacherNumber"/></bean:define>		
	<logic:equal name="summariesManagementBean" property="summaryType" value="NORMAL_SUMMARY">		
		<fr:form action="<%= createSummaryURL %>">
			<html:hidden property="method" name="summariesManagementForm" value="createSummary"/>
			<fr:edit nested="true" id="summariesManagementBeanWithSummary" name="summariesManagementBean" schema="CreateSummaryToNormalSummary">
				<fr:destination name="cancel" path="<%= showSummaries %>"/>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight"/>
				</fr:layout>
			</fr:edit>
			<html:submit titleKey="message.button.save"><bean:message key="button.save" bundle="DEFAULT"/></html:submit>
			<logic:empty name="summariesManagementBean" property="summary">	
				<html:submit titleKey="message.button.save.new" onclick="this.form.method.value='createSummaryAndNew';this.form.submit();"><bean:message key="button.save.new" bundle="DEFAULT"/></html:submit>
				<html:submit titleKey="message.button.save.equal" onclick="this.form.method.value='createSummaryAndSame';this.form.submit();"><bean:message key="button.save.equal" bundle="DEFAULT"/></html:submit>
			</logic:empty>
			<html:submit titleKey="message.button.save" onclick="this.form.method.value='prepareShowSummaries';this.form.submit();"><bean:message key="button.cancel" bundle="DEFAULT"/></html:submit>
		</fr:form>
	</logic:equal>
	<logic:equal name="summariesManagementBean" property="summaryType" value="EXTRA_SUMMARY">		
			<fr:form action="<%= createSummaryURL %>">
				<html:hidden property="method" name="summariesManagementForm" value="createSummary"/>
				<fr:edit nested="true" id="summariesManagementBeanWithSummary" name="summariesManagementBean" schema="CreateSummaryToExtraSummary" >
					<fr:destination name="cancel" path="<%= showSummaries %>"/>
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight"/>
					</fr:layout>
				</fr:edit>	
			<html:submit titleKey="message.button.save"><bean:message key="button.save" bundle="DEFAULT"/></html:submit>			
			<logic:empty name="summariesManagementBean" property="summary">				
				<html:submit titleKey="message.button.save.new" onclick="this.form.method.value='createSummaryAndNew';this.form.submit();"><bean:message key="button.save.new" bundle="DEFAULT"/></html:submit>
				<html:submit titleKey="message.button.save.equal" onclick="this.form.method.value='createSummaryAndSame';this.form.submit();"><bean:message key="button.save.equal" bundle="DEFAULT"/></html:submit>
			</logic:empty>
			<html:submit titleKey="message.button.save" onclick="this.form.method.value='prepareShowSummaries';this.form.submit();"><bean:message key="button.cancel" bundle="DEFAULT"/></html:submit>
		</fr:form>		
	</logic:equal>		
				
</logic:present>
