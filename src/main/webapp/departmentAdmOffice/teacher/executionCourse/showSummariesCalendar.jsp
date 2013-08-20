<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>

<logic:present name="showSummariesCalendarBean">

	<h2><bean:message key="label.summaries.calendar.title" bundle="DEFAULT"/></h2>
	<bean:define id="executionCourseID" name="showSummariesCalendarBean" property="executionCourse.externalId" />
	<bean:define id="teacherId" name="loggedTeacherProfessorship" property="teacher.teacherId" />
	
	<logic:messagesPresent message="true">
		<p>
		<span class="error"><!-- Error messages go here -->
			<html:messages id="message" message="true">
				<bean:write name="message" filter="true"/>
			</html:messages>
		</span>
		</p>
	</logic:messagesPresent>

	<ul class="mvert15 list5">
		<li>	
			<bean:define id="showSummariesURL" type="java.lang.String">/summariesManagement.do?method=prepareShowSummaries&page=0&executionCourseID=<bean:write name="executionCourseID"/>&teacherId_=<bean:write name="teacherId"/></bean:define>
			<html:link page="<%= showSummariesURL %>">
				<bean:message key="label.return" bundle="MANAGER_RESOURCES"/>
			</html:link>
		</li>
		<li>
			<bean:define id="createSummaryURL" type="java.lang.String">/summariesManagement.do?method=prepareInsertExtraSummary&page=0&executionCourseID=<bean:write name="executionCourseID"/>&teacherId_=<bean:write name="teacherId"/></bean:define>
			<html:link page="<%= createSummaryURL %>">
				<bean:message key="label.insert.extra.summary" bundle="DEFAULT"/>
			</html:link>							
		</li>
	</ul>
	
	<h3 class="mtop2 mbottom05"><bean:message key="label.visualization.options" bundle="DEFAULT"/></h3>	
	<bean:define id="postBackURL">/summariesManagement.do?method=showSummariesCalendarPostBack&teacherId_=<bean:write name="teacherId"/></bean:define>
	<fr:form action="<%= postBackURL %>">
		<fr:edit id="showSummariesCalendar" name="showSummariesCalendarBean" schema="ShowSummariesCalendar" nested="true">
			<fr:destination name="postBack" path="<%= postBackURL %>"/>		
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05"/>
				<fr:property name="columnClasses" value=",,tdclear"/>
			</fr:layout>
		</fr:edit>
	</fr:form>
	
	<logic:notEmpty name="summariesCalendarResult">	
		<bean:define id="createComplexSummaryURL" type="java.lang.String">/summariesManagement.do?method=prepareCreateComplexSummaryInSummariesCalendarMode&page=0&executionCourseID=<bean:write name="executionCourseID"/>&teacherId_=<bean:write name="teacherId"/></bean:define>
			
		<fr:view name="summariesCalendarResult" schema="SummariesCalendarList">
			<fr:layout name="tabular">
				<fr:property name="style" value=""/>			
				<fr:property name="classes" value="tstyle1 mtop025 mbottom0"/>
				<fr:property name="columnClasses" value=",,,,,smalltxt color888,smalltxt color888,,"/>				
				<fr:property name="link(insertNewSummary)" value="<%= createComplexSummaryURL %>"/>
	            <fr:property name="param(insertNewSummary)" value="checkBoxValue/summaryDate"/>
		        <fr:property name="key(insertNewSummary)" value="title.summary.insert"/>
	            <fr:property name="order(insertNewSummary)" value="0"/>	
	            <fr:property name="visibleIf(insertNewSummary)" value="withoutSummary"/>	
	            
			</fr:layout>		
		</fr:view>		
	</logic:notEmpty>
	
</logic:present>	
	
	