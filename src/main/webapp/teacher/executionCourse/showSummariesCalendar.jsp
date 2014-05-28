<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<logic:present name="showSummariesCalendarBean">

	<h2><bean:message key="label.summaries.calendar.title"/></h2>
	<bean:define id="executionCourseID" name="showSummariesCalendarBean" property="executionCourse.externalId" />
	
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
			<bean:define id="createSummaryURL" type="java.lang.String">/summariesManagement.do?method=prepareInsertExtraSummary&page=0&executionCourseID=<bean:write name="executionCourseID"/></bean:define>
			<html:link page="<%= createSummaryURL %>">
				<bean:message key="label.insert.extra.summary"/>
			</html:link>							
		</li>
	</ul>
	
	<h3 class="mtop15 mbottom05"><bean:message key="label.visualization.options"/></h3>
	<fr:form action="/summariesManagement.do?method=showSummariesCalendarPostBack">
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
				<fr:property name="classes" value="tstyle1 mtop025 mbottom0 tdcenter"/>
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
	
	