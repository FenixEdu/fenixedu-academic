<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<html:xhtml/>

<em><bean:message key="link.writtenEvaluationManagement"/></em>
<h2><bean:message key="link.written.evaluations.search.by.date"/></h2>

<html:form action="/searchWrittenEvaluationsByDate" focus="day">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="search"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.academicInterval" property="academicInterval" value="<%= request.getAttribute(PresentationConstants.ACADEMIC_INTERVAL).toString() %>"/>

	<p>
		<span class="error"><!-- Error messages go here --><html:errors /></span>
	</p>

	<table class="tstyle5 thlight thright">
		<tr>
			<th>
			   	<bean:message key="property.exam.date"/>:
			</th>
			<td>
   				<html:text bundle="HTMLALT_RESOURCES" altKey="text.day" maxlength="2" size="2" property="day"/>
			   	/
		  		<html:text bundle="HTMLALT_RESOURCES" altKey="text.month" maxlength="2" size="2" property="month"/>
			   	/
		  		<html:text bundle="HTMLALT_RESOURCES" altKey="text.year" maxlength="4" size="4" property="year"/>
			   	<bean:message key="label.example.date"/>
			</td>
		</tr>
		<tr>
			<th>
			    <bean:message key="property.exam.beginning"/>:
			</th>
			<td>
		  		<html:text bundle="HTMLALT_RESOURCES" altKey="text.beginningHour" maxlength="2" size="2" property="beginningHour"/>
   				:
		  		<html:text bundle="HTMLALT_RESOURCES" altKey="text.beginningMinute" maxlength="2" size="2" property="beginningMinute"/> 
		  		<bean:message key="label.optional"/>
			</td>
		</tr>
		<tr>
			<th>
    			<bean:message key="property.exam.end"/>:
			</th>
			<td>
			   	<html:text bundle="HTMLALT_RESOURCES" altKey="text.endHour" maxlength="2" size="2" property="endHour"/>
			   	:
			   	<html:text bundle="HTMLALT_RESOURCES" altKey="text.endMinute" maxlength="2" size="2" property="endMinute"/>
			   	<bean:message key="label.optional"/>
			</td>
		</tr>
	</table>

	<p>	
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="lable.choose"/>
		</html:submit>
	</p>

</html:form>

<logic:present name="availableRoomIndicationMsg">
	<p>
		<bean:write name="availableRoomIndicationMsg"/>
	</p>
</logic:present>

<script type="text/javascript">
var sortedOn = 0;

function SortTable(sortOn) {

var table = document.getElementById('results');
var tbody = table.getElementsByTagName('tbody')[0];
var rows = tbody.getElementsByTagName('tr');

var rowArray = new Array();
for (var i=0, length=rows.length; i<length; i++) {
rowArray[i] = new Object;
rowArray[i].oldIndex = i;
rowArray[i].value = rows[i].getElementsByTagName('td')[sortOn].firstChild.nodeValue;
}

if (sortOn == sortedOn) { rowArray.reverse(); }
else {
sortedOn = sortOn;
/*
Decide which function to use from the three:RowCompareNumbers,
RowCompareDollars or RowCompare (default).
For first column, I needed numeric comparison.
*/
if (sortedOn == 5) {
rowArray.sort(RowCompareNumbers);
}
else {
rowArray.sort(RowCompare);
}
}

var newTbody = document.createElement('tbody');
for (var i=0, length=rowArray.length ; i<length; i++) {
newTbody.appendChild(rows[rowArray[i].oldIndex].cloneNode(true));
}

table.replaceChild(newTbody, tbody);
}

function RowCompare(a, b) { 

var aVal = a.value;
var bVal = b.value;
return (aVal == bVal ? 0 : (aVal > bVal ? 1 : -1));
}
// Compare number
function RowCompareNumbers(a, b) {

var aVal = parseInt( a.value);
var bVal = parseInt(b.value);
return (aVal - bVal);
}
// compare currency
function RowCompareDollars(a, b) {

var aVal = parseFloat(a.value.substr(1));
var bVal = parseFloat(b.value.substr(1));
return (aVal - bVal);
}
</script>

<logic:present name="writtenEvaluations">
	<table class="tstyle4" id="results">
		<thead>
		<tr>
			<th>
				<a onclick="SortTable(0);" href="javascript:;">
					<bean:message key="lable.execution.course"/>
				</a>
			</th>
			<th>
				<a onclick="SortTable(1);" href="javascript:;">
					<bean:message key="lable.degree"/>
				</a>
			</th>
			<th>
				<bean:message key="lable.season"/>
			</th>
			<th>
				<bean:message key="lable.hour"/>
			</th>
			<th>
				<bean:message key="lable.rooms"/>
			</th>
			<th>
				<a onclick="SortTable(5);" href="javascript:;">
					<bean:message key="lable.number.enroled.students"/>
				</a>
			</th>
			<th>
				<bean:message key="lable.number.missing.seats"/>
			</th>	
		</tr>
		</thead>
		<tbody>
		
		<logic:iterate id="writtenEvaluation" name="writtenEvaluations">
			<bean:define id="evaluationID" name="writtenEvaluation" property="idInternal"/>
			<bean:define id="evaluationTypeClassname" name="writtenEvaluation" property="class.name"/>
			<tr>
				<td>
					<logic:iterate id="executionCourse" name="writtenEvaluation" property="associatedExecutionCourses">
						<bean:write name="executionCourse" property="nome"/><br />
						<bean:define id="executionCourseID" name="executionCourse" property="idInternal"/>
						<bean:define id="academicInterval" name="academicInterval"/>
						<bean:define id="executionYearID" name="executionCourse" property="executionPeriod.executionYear.idInternal" type="java.lang.Integer"/>
					</logic:iterate>
				</td>
				<td>
				
					<logic:iterate id="degreeModuleScope" name="writtenEvaluation" property="degreeModuleScopes">
						<bean:write name="degreeModuleScope" property="curricularCourse.degreeCurricularPlan.degree.sigla"/><br />
					</logic:iterate>
				
                    <bean:define id="executionDegreeID" name="writtenEvaluation" property="executionDegree.idInternal" />
    
					<logic:iterate id="executionCourse" name="writtenEvaluation" property="associatedExecutionCourses">
						<logic:iterate id="curricularCourse" name="executionCourse" property="associatedCurricularCourses">
							<logic:iterate id="degreeModuleScope" name="curricularCourse" property="degreeModuleScopes">
								<bean:define id="curricularYearID" name="degreeModuleScope" property="curricularYear"/>
							</logic:iterate>
						</logic:iterate>						
					</logic:iterate>
					
				</td>
				<td>
				<bean:define id="selectedBegin"><logic:present name="examSearchByDateForm" property="beginningHour"><logic:notEmpty name="examSearchByDateForm" property="beginningHour">true</logic:notEmpty><logic:empty name="examSearchByDateForm" property="beginningHour">false</logic:empty></logic:present><logic:notPresent name="examSearchByDateForm" property="beginningHour">false</logic:notPresent></bean:define>
				<bean:define id="selectedEnd"><logic:present name="examSearchByDateForm" property="endHour"><logic:notEmpty name="examSearchByDateForm" property="endHour">true</logic:notEmpty><logic:empty name="examSearchByDateForm" property="endHour">false</logic:empty></logic:present><logic:notPresent name="examSearchByDateForm" property="endHour">false</logic:notPresent></bean:define>

					<html:link page="<%= "/writtenEvaluations/editWrittenTest.faces?"
										+ "executionDegreeID"
										+ "="
										+ pageContext.findAttribute("executionDegreeID")
										+ "&amp;"
										+ "evaluationID"
										+ "="
										+ pageContext.findAttribute("evaluationID")
										+ "&amp;"
										+ "evaluationTypeClassname"
										+ "="
										+ pageContext.findAttribute("evaluationTypeClassname") 
										+ "&amp;"
										+ "academicInterval"
										+ "="
										+ pageContext.findAttribute("academicInterval")
										+ "&amp;"
										+ "executionCourseID"
										+ "="
										+ pageContext.findAttribute("executionCourseID")
										+ "&amp;"
										+ "curricularYearIDsParameterString"
										+ "="
										+ pageContext.findAttribute("curricularYearID") 
										+ "&amp;"
										+ "originPage=showWrittenEvaluationsByDate"
										+ "&amp;"
										+ "selectedBegin"
										+ "="
										+ pageContext.findAttribute("selectedBegin") 
										+ "&amp;"
										+ "selectedEnd"
										+ "="
										+ pageContext.findAttribute("selectedEnd") 
										%>">
						<logic:equal name="writtenEvaluation" property="class.name" value="net.sourceforge.fenixedu.domain.Exam">
							<bean:write name="writtenEvaluation" property="season"/>
						</logic:equal>
						<logic:equal name="writtenEvaluation" property="class.name" value="net.sourceforge.fenixedu.domain.WrittenTest">
							<bean:write name="writtenEvaluation" property="description"/>
						</logic:equal>
					</html:link>
				</td>
				<td class="nowrap">
					<dt:format pattern="HH:mm">
						<bean:write name="writtenEvaluation" property="beginningDate.time"/>
					</dt:format>
					-
					<dt:format pattern="HH:mm">
						<bean:write name="writtenEvaluation" property="endDate.time"/>
					</dt:format>
				</td>
				<td>
					<logic:iterate id="roomOccupation" name="writtenEvaluation" property="writtenEvaluationSpaceOccupations">
						<bean:write name="roomOccupation" property="room.nome"/>;
					</logic:iterate>
				</td>
				<td class="acenter">
					<bean:define id="countStudentsEnroledAttendingExecutionCourses" name="writtenEvaluation" property="countStudentsEnroledAttendingExecutionCourses"
							type="java.lang.Integer"/>
					<bean:write name="countStudentsEnroledAttendingExecutionCourses"/>
				</td>
				<td class="acenter">
					<bean:define id="countNumberReservedSeats" name="writtenEvaluation" property="countNumberReservedSeats"
							type="java.lang.Integer"/>
					<%= "" + (countStudentsEnroledAttendingExecutionCourses.intValue() - countNumberReservedSeats.intValue()) %>
				</td>
			</tr>
		</logic:iterate>
		</tbody>
	</table>

</logic:present>
