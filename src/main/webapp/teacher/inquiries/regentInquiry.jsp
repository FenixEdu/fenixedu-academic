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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<style>

/*
Regent Inquiry Specific Questions
*/

input.bright { position: absolute; bottom: 0; left: 70px; }
 
.question {
border-collapse: collapse;
margin: 10px 0;
 
width: 100%;
}
.question th {
padding: 5px 10px;
font-weight: normal;
text-align: left;
border: none;
border-top: 1px solid #ccc;
border-bottom: 1px solid #ccc;
background: #f5f5f5;
vertical-align: bottom;
}
.question th.firstcol {
vertical-align: top;
}
.question td {
padding: 5px;
text-align: center;
border: none;
border-bottom: 1px solid #ccc;
border-top: 1px solid #ccc;
background-color: #fff;
}
 
th.firstcol {
width: 300px;
text-align: left;
}
 
.q1col td { text-align: left; }
 
.q9col .col1, .q9col .col9  { width: 30px; }
.q10col .col1, .q10col .col2, .q10col .col10  { width: 20px; }
.q11col .col1, .q11col .col2, .q11col .col3, .q11col .col11  { width: 20px; }
 
th.col1, th.col2, th.col3, th.col4, th.col5, th.col6, th.col7, th.col8, th.col9, th.col10 {
text-align: center !important;
}
 
 
.max-width {
min-width: 650px;
max-width: 900px;
}
 
/* Regent specific */

div#teacher-results div.workload-left {
margin-top: 0;
float: none;
}

h4 em {
font-weight: normal;
}
</style>

<script src="<%= request.getContextPath() + "/javaScript/inquiries/jquery.min.js" %>" type="text/javascript" ></script>
<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/inquiries/hideButtons.js" %>"></script>
<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/inquiries/check.js" %>"></script>
<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/inquiries/checkall.js" %>"></script>
<link href="<%= request.getContextPath() %>/CSS/quc_results.css" rel="stylesheet" media="screen, print" type="text/css" />

<script type="text/javascript" language="javascript">switchGlobal();</script> 
<h2><bean:message key="title.inquiry.quc.regent" bundle="INQUIRIES_RESOURCES"/></h2>

<h3><bean:write name="executionCourse" property="name"/> - <bean:write name="executionCourse" property="sigla"/> (<bean:write name="executionPeriod" property="semester"/>º Semestre <bean:write name="executionPeriod" property="executionYear.year"/>)</h3>

<p><bean:message key="message.regent.details.inquiry" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="INQUIRIES_RESOURCES"/></p>

<html:messages id="message" message="true" bundle="INQUIRIES_RESOURCES">
	<p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>
	
<div id="report">
<fr:form action="/regentInquiry.do?method=saveChanges">
	<fr:edit id="regentInquiryBean" name="regentInquiryBean" visible="false"/>
	
	<!-- Curricular Inquiry Results -->
	<bean:define id="toogleFunctions" value=""/>
	
	<logic:iterate id="entrySet" name="regentInquiryBean" property="curricularBlockResultsMap" indexId="firstIter">	
				
		<h3 class="separator2 mtop15">
			<span style="font-weight: normal;">
				<bean:message key="title.inquiry.resultsUC" bundle="INQUIRIES_RESOURCES"/>
				(<bean:write name="regentInquiryBean" property="professorship.executionCourse.name"/> - <bean:write name="entrySet" property="key.degree.sigla"/>)
			</span>
		</h3>
		<bean:define id="executionCourse" name="executionCourse" type="net.sourceforge.fenixedu.domain.ExecutionCourse"/>
		<bean:define id="executionDegree" name="entrySet" property="key" type="net.sourceforge.fenixedu.domain.ExecutionDegree"/>
		<bean:define id="executionCourseOID" name="regentInquiryBean" property="professorship.executionCourse.externalId"/>
		<bean:define id="degreeCurricularPlanOID" name="entrySet" property="key.degreeCurricularPlan.externalId"/>
		<p class="mvert15">
			<html:link module="/publico" page="<%= "/viewCourseResults.do?executionCourseOID=" + executionCourseOID + 
												"&degreeCurricularPlanOID=" + degreeCurricularPlanOID %>" target="_blank">
				<bean:message bundle="INQUIRIES_RESOURCES" key="link.inquiry.showUCResults"/>
			</html:link>
		</p>		
		<bean:define id="hasNotRelevantData">
			<%= executionCourse.hasNotRelevantDataFor(executionDegree) %>
		</bean:define>
		<logic:iterate indexId="iter" id="blockResult" name="entrySet" property="value" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.BlockResultsSummaryBean">
			<logic:equal name="hasNotRelevantData" value="false"> <!-- if group is not GREY -->
				<bean:define id="toogleFunctions">
					<bean:write name="toogleFunctions" filter="false"/>
					<%= "$('#block" + (Integer.valueOf(iter)+(int)1) + "" + (Integer.valueOf(firstIter)+(int)1) +"').click(function() { $('#block" + (Integer.valueOf(iter)+(int)1) + "" + (Integer.valueOf(firstIter)+(int)1) + "-content').toggle('normal', function() { }); });" %>			
				</bean:define>
			</logic:equal>
			<h4 class="mtop15">
				<logic:notEmpty name="blockResult" property="blockResultClassification">
					<div class="<%= "bar-" + blockResult.getBlockResultClassification().name().toLowerCase() %>"><div>&nbsp;</div></div>
				</logic:notEmpty>			
				<bean:write name="blockResult" property="inquiryBlock.inquiryQuestionHeader.title"/>
				<bean:define id="expand" value=""/>
				<logic:equal name="hasNotRelevantData" value="false"> <!-- if group is not GREY -->				
					<logic:notEqual value="true" name="blockResult" property="mandatoryComments">
						<span style="font-weight: normal;">| 
							<span id="<%= "block" + (Integer.valueOf(iter)+(int)1 + "" + (Integer.valueOf(firstIter)+(int)1)) %>" class="link">
								<bean:message bundle="INQUIRIES_RESOURCES" key="link.inquiry.showResults"/>
							</span>
						</span>
						<bean:define id="expand" value="display: none;"/>
					</logic:notEqual>
				</logic:equal>
				<logic:notEqual name="hasNotRelevantData" value="false"> <!-- if group is GREY -->
					<em>(<bean:message key="label.inquiry.withoutRepresentation" bundle="INQUIRIES_RESOURCES"/>)</em>
				</logic:notEqual>
			</h4>
			<logic:equal name="hasNotRelevantData" value="false"> <!-- if group is not GREY -->
				<div id="<%= "block" + (Integer.valueOf(iter)+(int)1) + "" + (Integer.valueOf(firstIter)+(int)1) + "-content"%>" style="<%= expand %>"> 
					<logic:iterate indexId="groupIter" id="groupResult" name="blockResult" property="groupsResults">
						<fr:edit id="<%= "group" + iter + groupIter + firstIter %>" name="groupResult" layout="inquiry-group-resume-input"/>
					</logic:iterate>
				</div>
			</logic:equal>
		</logic:iterate>
	</logic:iterate>
	
	<!-- Teachers Inquiry Results -->	
	<div class="teacher">
		<bean:define id="exceptionClass" value=""/>
		<logic:present name="first-sem-2010">
			<bean:define id="exceptionClass" type="java.lang.String">
				<bean:write name="first-sem-2010"/>
			</bean:define>
		</logic:present>
		<div id="teacher-results" class="<%= exceptionClass %>">
			<h3 class="separator2 mtop2"><span style="font-weight: normal;"><bean:message key="title.inquiry.teachingStaff" bundle="INQUIRIES_RESOURCES"/></span></h3>
			<bean:define id="teacherToogleFunctions" value=""/>
			<logic:notEmpty name="regentInquiryBean" property="teachersResultsMap">
				<logic:iterate id="entrySet" name="regentInquiryBean" property="teachersResultsMap">
					<logic:iterate indexId="teacherIter" id="teacherShiftTypeResult" name="entrySet" property="value" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.TeacherShiftTypeResultsBean">
						<div style="margin: 2.5em 0 3.5em 0;">
							<h3>
								<bean:write name="teacherShiftTypeResult" property="professorship.person.name"/> / 
								<bean:message name="teacherShiftTypeResult" property="shiftType.name"  bundle="ENUMERATION_RESOURCES"/>
							</h3>
							<bean:define id="professorshipOID" name="teacherShiftTypeResult" property="professorship.externalId"/>
							<bean:define id="shiftType" name="teacherShiftTypeResult" property="shiftType"/>
							<p class="mvert15">
								<html:link page="<%= "/viewTeacherResults.do?professorshipOID=" + professorshipOID + "&shiftType=" + shiftType %>"
										 module="/publico" target="_blank">
									<bean:message bundle="INQUIRIES_RESOURCES" key="link.inquiry.showTeacherResults"/>
								</html:link>
							</p>
							<logic:iterate indexId="iter" id="blockResult" name="teacherShiftTypeResult" property="blockResults" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.BlockResultsSummaryBean">
								<bean:define id="teacherToogleFunctions">
									<bean:write name="teacherToogleFunctions" filter="false"/>
									<%= "$('#teacher-block" + teacherShiftTypeResult.getProfessorship().getExternalId() + teacherShiftTypeResult.getShiftType() + (Integer.valueOf(iter)+(int)1) + "').click(function()" 
										+ "{ $('#teacher-block" + teacherShiftTypeResult.getProfessorship().getExternalId() + teacherShiftTypeResult.getShiftType() + (Integer.valueOf(iter)+(int)1) + "-content').toggle('normal', function() { }); });" %>			
								</bean:define>
								<h4 class="mtop15">
									<logic:notEmpty name="blockResult" property="blockResultClassification">
										<div class="<%= "bar-" + blockResult.getBlockResultClassification().name().toLowerCase() %>"><div>&nbsp;</div></div>
									</logic:notEmpty>
									<bean:write name="blockResult" property="inquiryBlock.inquiryQuestionHeader.title"/>
									<bean:define id="expand" value=""/>
									<logic:notEqual value="true" name="blockResult" property="mandatoryComments">
										<span style="font-weight: normal;">| 
											<span id="<%= "teacher-block" + teacherShiftTypeResult.getProfessorship().getExternalId() + teacherShiftTypeResult.getShiftType()  + (Integer.valueOf(iter)+(int)1) %>" class="link">
												<bean:message key="link.inquiry.showResults" bundle="INQUIRIES_RESOURCES"/>
											</span>
										</span>
										<bean:define id="expand" value="display: none;"/>
									</logic:notEqual>
								</h4>
								<div id="<%= "teacher-block" + teacherShiftTypeResult.getProfessorship().getExternalId() + teacherShiftTypeResult.getShiftType() + (Integer.valueOf(iter)+(int)1) + "-content"%>" style="<%= expand %>"> 
									<logic:iterate indexId="groupIter" id="groupResult" name="blockResult" property="groupsResults">
										<fr:edit id="<%= "teacherGroup" + teacherIter + iter + groupIter %>" name="groupResult" layout="inquiry-group-resume-input"/>
									</logic:iterate>
								</div>
							</logic:iterate>
						</div>
					</logic:iterate>
				</logic:iterate>				
			</logic:notEmpty>
			<logic:empty name="regentInquiryBean" property="teachersResultsMap">
				<p><em><bean:message key="label.withNoResults" bundle="INQUIRIES_RESOURCES"/></em></p>
			</logic:empty>
		</div>
	</div>
	
	<!-- Regent Inquiry -->
	<logic:iterate id="inquiryBlockDTO" name="regentInquiryBean" property="regentInquiryBlocks">
		<h3 class="separator2 mtop25">
			<span style="font-weight: normal;">
				<fr:view name="inquiryBlockDTO" property="inquiryBlock.inquiryQuestionHeader.title"/>
			</span>
		</h3>		
		<div class="max-width"> 			
			<logic:iterate id="inquiryGroup" name="inquiryBlockDTO" property="inquiryGroups" indexId="index">					
				<fr:edit id="<%= "iter" + index --%>" name="inquiryGroup"/>
			</logic:iterate>
		</div>
	</logic:iterate>
	<p class="mtop15">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="button.saveInquiry" bundle="INQUIRIES_RESOURCES"/>
		</html:submit>
	</p>
</fr:form>
</div>

<bean:define id="scriptToogleFunctions">
	<script>
	<bean:write name="toogleFunctions" filter="false"/>
	</script>
</bean:define>

<bean:write name="scriptToogleFunctions" filter="false"/>

<bean:define id="scriptTeacherToogleFunctions">
	<script>
	<bean:write name="teacherToogleFunctions" filter="false"/>
	</script>
</bean:define>

<bean:write name="scriptTeacherToogleFunctions" filter="false"/>