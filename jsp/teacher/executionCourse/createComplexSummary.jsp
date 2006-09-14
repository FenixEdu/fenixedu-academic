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
		<h2><bean:message key="title.summary.insert"/></h2>
	</logic:empty>
	<logic:notEmpty name="summariesManagementBean" property="summary">			
		<h2><bean:message key="title.summary.edit"/></h2>
	</logic:notEmpty>

	<div class="infoop2">
		<a href="#" class="dnone" id="instructionsButton" onclick="check(document.getElementById('instructions'), document.getElementById('instructionsButton'));"><bean:message key="link.home"/></a>
		<div id="instructions" class="dblock">
			<ul class="mvert025">
				<li><bean:message key="label.summary.management.instructions1" /></li>
				<li><bean:message key="label.summary.management.instructions2" /></li>
				<li><bean:message key="label.summary.management.instructions3" /></li>
				<li><bean:message key="label.summary.management.instructions4" /></li>
			</ul>
		</div>
	</div>

	<script>
		check(document.getElementById('instructions'), document.getElementById('instructionsButton'));
		document.getElementById('instructionsButton').className="dblock";
	</script>

	<bean:define id="executionCourseID" name="summariesManagementBean" property="executionCourse.idInternal" />
	
	<logic:messagesPresent message="true">
		<p>
		<span class="error"><!-- Error messages go here -->
			<html:messages id="message" message="true">
				<bean:write name="message" filter="true"/>
			</html:messages>
		</span>
		<p>
	</logic:messagesPresent>			
	
	<%-- Associate --%>
	<h3 class="mbottom0"> <bean:message key="label.associate"/></h3>
	<table class="tstyle5">
		<%-- LessonPlannings --%>
		<tr>
			<td><bean:message key="label.lessonPlanning" />:</td>
			<td>				
				<fr:form>
					<fr:edit id="summariesManagementBeanWithLessonPlanning" name="summariesManagementBean" schema="ListLessonPlanningsToSummariesManagement" nested="true">
						<fr:destination name="postBack" path="/summariesManagement.do?method=chooseLessonPlanning"/>	
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
			<td><bean:message key="message.summaryText.last"/>:</td>
			<td>				
				<fr:form>
					<fr:edit id="summariesManagementBeanWithLastSummary" name="summariesManagementBean" schema="ListLastSummariesToSummariesManagement" nested="true">
						<fr:destination name="postBack" path="/summariesManagement.do?method=chooseLastSummary"/>	
							<fr:layout name="flow">
								<fr:property name="labelTerminator" value=""/>
								<fr:property name="labelExcluded" value="true"/>
							</fr:layout>	
					</fr:edit>	
				</fr:form>	
			</td>
		</tr>
	</table>
		
	<fr:form action="/summariesManagement.do">		

		<%-- Lessons --%>
		<logic:notEmpty name="nextPossibleLessonsDates">
			<logic:iterate name="nextPossibleLessonsDates" id="lesson" indexId="index">
				<bean:define id="index" name="index" />
				<fr:view name="lesson" schema="ShowPossibleNextSummaryLessonAndDate" />
				<fr:edit id="<%= "nextPossibleLessonsDatesBean" + (index + 1) %>" schema="EditPossibleNextSummaryLessonAndDates" />				
			</logic:iterate>			
		</logic:notEmpty>
		
		<%-- Summary --%>		
		<html:hidden property="method" name="summariesManagementForm" value="createSummary"/>	
		<bean:define id="showSummaries">/summariesManagement.do?method=prepareShowSummaries&page=0&executionCourseID=<bean:write name="executionCourseID"/></bean:define>			
		<logic:equal name="summariesManagementBean" property="summaryType" value="NORMAL_SUMMARY">								
			<%-- Teacher --%>
			<jsp:include page="chooseTeacher.jsp"/>							
			<h3 class="mbottom0"><bean:message key="message.summaryText"/></h3>
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
		</logic:equal>	
		<logic:equal name="summariesManagementBean" property="summaryType" value="EXTRA_SUMMARY">					
			<html:hidden property="method" name="summariesManagementForm" value="createSummary"/>		
			<%-- Teacher --%>
			<jsp:include page="chooseTeacher.jsp"/>			
			<h3 class="mbottom0"><bean:message key="message.summaryText"/></h3>								
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
		</logic:equal>		
	</fr:form>			
</logic:present>