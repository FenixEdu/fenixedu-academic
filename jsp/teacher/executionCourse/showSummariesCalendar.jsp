<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<logic:present name="showSummariesCalendarBean">

	<h2><bean:message key="label.summaries.calendar.title"/></h2>
	<bean:define id="executionCourseID" name="showSummariesCalendarBean" property="executionCourse.idInternal" />
	
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
			<bean:define id="showSummariesURL" type="java.lang.String">/summariesManagement.do?method=prepareShowSummaries&page=0&executionCourseID=<bean:write name="executionCourseID"/></bean:define>
			<html:link page="<%= showSummariesURL %>">
				<bean:message key="label.return" bundle="MANAGER_RESOURCES"/>
			</html:link>
		</li>
		<li>
			<bean:define id="createSummaryURL" type="java.lang.String">/summariesManagement.do?method=prepareInsertSummary&page=0&executionCourseID=<bean:write name="executionCourseID"/></bean:define>
			<html:link page="<%= createSummaryURL %>">
				<bean:message key="label.insert.extra.summary"/>
			</html:link>							
		</li>
	</ul>
	
	<h3 class="mtop2 mbottom05"><bean:message key="label.visualization.options"/></h3>
	<fr:form>
		<fr:edit id="showSummariesCalendar" name="showSummariesCalendarBean" schema="ShowSummariesCalendar" nested="true">
			<fr:destination name="postBack" path="/summariesManagement.do?method=showSummariesCalendarPostBack"/>		
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05"/>
				<fr:property name="columnClasses" value=",,tdclear"/>
			</fr:layout>
		</fr:edit>
	</fr:form>
	
	<logic:notEmpty name="summariesCalendarResult">	
		<bean:define id="createComplexSummaryURL" type="java.lang.String">/summariesManagement.do?method=prepareCreateComplexSummaryInSummariesCalendarMode&page=0&executionCourseID=<bean:write name="executionCourseID"/></bean:define>
		<fr:view name="summariesCalendarResult" schema="SummariesCalendarList">
			<fr:layout name="tabular">
				<fr:property name="style" value="width: 500px;"/>			
				<fr:property name="classes" value="tstyle1 mtop025 mbottom0"/>
				<fr:property name="columnClasses" value="acenter,,,"/>
				
				<fr:property name="link(insertNewSummary)" value="<%= createComplexSummaryURL %>"/>
	            <fr:property name="param(insertNewSummary)" value="checkBoxValue/summaryDate"/>
		        <fr:property name="key(insertNewSummary)" value="title.summary.insert"/>
	            <fr:property name="order(insertNewSummary)" value="0"/>	
	            <fr:property name="visibleIf(insertNewSummary)" value="withoutSummary"/>	
	            
			</fr:layout>		
		</fr:view>		
	</logic:notEmpty>
	
</logic:present>	
	
	