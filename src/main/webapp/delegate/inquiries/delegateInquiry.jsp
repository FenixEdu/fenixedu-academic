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

<link href="<%= request.getContextPath() %>/CSS/quc_results.css" rel="stylesheet" media="screen, print" type="text/css" />

<style>

/*
Delegate Inquiry Specific Questions
*/

.question {
border-collapse: collapse;
margin: 10px 0;
}
.question th {
padding: 5px;
font-weight: normal;
text-align: left;
border: none;
border-top: 1px solid #ccc;
border-bottom: 1px solid #ccc;
vertical-align: top;
font-weight: bold;
}
.question td {
padding: 5px;
border: none;
border-bottom: 1px solid #ccc;
border-top: 1px solid #ccc;
}

/* Delegate specific */

div#teacher-results div.workload-left {
margin-top: 0;
}
</style>

<script src="<%= request.getContextPath() + "/javaScript/inquiries/jquery.min.js" %>" type="text/javascript" ></script>
<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/inquiries/hideButtons.js" %>"></script>
<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/inquiries/check.js" %>"></script>
<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/inquiries/checkall.js" %>"></script>

<script type="text/javascript" language="javascript">switchGlobal();</script> 
<em>Portal do Delegado</em>
<h2><bean:message key="link.yearDelegateInquiries" bundle="DELEGATES_RESOURCES"/></h2>
<h3 class="mtop15">
<span class="highlight1"><bean:write name="delegateInquiryBean" property="executionCourse.name"/></span> - 
<bean:write name="delegateInquiryBean" property="executionDegree.degree.sigla"/> (<bean:write name="executionPeriod" property="semester"/>º Semestre <bean:write name="executionPeriod" property="executionYear.year"/>)
</h3>
<p>
Por baixo de cada pergunta que teve um resultado "Inadequado" ou "a melhorar" está um espaço para colocares o motivo que te levou e aos teus colegas a assinalar o problema. 
Sê o mais objectivo possível. O teu contributo é indispensável para os problemas serem resolvidos!
</p>
<div id="report">
<fr:form action="/delegateInquiry.do?method=saveChanges">
	<fr:edit id="delegateInquiryBean" name="delegateInquiryBean" visible="false"/>
	
	<!-- Curricular Inquiry Results -->
	<bean:define id="toogleFunctions" value=""/>
	<h3 class="separator2 mtop15">
		<span style="font-weight: normal;">
			<bean:message key="title.inquiry.resultsUC" bundle="DELEGATES_RESOURCES"/>
			(<bean:write name="delegateInquiryBean" property="executionCourse.name"/> - <bean:write name="delegateInquiryBean" property="executionDegree.degree.sigla"/>)
		</span>
	</h3>
	<bean:define id="executionCourseOID" name="delegateInquiryBean" property="executionCourse.externalId"/>
	<bean:define id="degreeCurricularPlanOID" name="delegateInquiryBean" property="executionDegree.degreeCurricularPlan.externalId"/>
	<p class="mvert15">
		<html:link page="<%= "/delegateInquiry.do?method=viewCourseInquiryResults&executionCourseOID=" + executionCourseOID + 
											"&degreeCurricularPlanOID=" + degreeCurricularPlanOID %>" target="_blank">
			<bean:message bundle="DELEGATES_RESOURCES" key="link.inquiry.showUCResults"/>
		</html:link>
	</p>
	<logic:iterate indexId="iter" id="blockResult" name="delegateInquiryBean" property="curricularBlockResults" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.BlockResultsSummaryBean">
		<logic:equal name="hasNotRelevantData" value="false"> <!-- if group is GREY -->
			<bean:define id="toogleFunctions">
				<bean:write name="toogleFunctions" filter="false"/>
				<%= "$('#block" + (Integer.valueOf(iter)+(int)1) + "').click(function() { $('#block" + (Integer.valueOf(iter)+(int)1) + "-content').toggle('normal', function() { }); });" %>			
			</bean:define>
		</logic:equal>
		<h4 class="mtop15">
			<logic:notEmpty name="blockResult" property="blockResultClassification">
				<div class="<%= "bar-" + blockResult.getBlockResultClassification().name().toLowerCase() %>"><div>&nbsp;</div></div>
			</logic:notEmpty>			
			<bean:write name="blockResult" property="inquiryBlock.inquiryQuestionHeader.title"/>
			<bean:define id="expand" value=""/>
			<logic:equal name="hasNotRelevantData" value="false"> <!-- if group is GREY -->				
				<logic:notEqual value="true" name="blockResult" property="mandatoryComments">
					<span style="font-weight: normal;">| 
						<span id="<%= "block" + (Integer.valueOf(iter)+(int)1) %>" class="link">
							<bean:message bundle="DELEGATES_RESOURCES" key="link.inquiry.showResults"/>
						</span>
					</span>
					<bean:define id="expand" value="display: none;"/>
				</logic:notEqual>
			</logic:equal>
		</h4>
		<logic:equal name="hasNotRelevantData" value="false"> <!-- if group is GREY -->
			<div id="<%= "block" + (Integer.valueOf(iter)+(int)1) + "-content"%>" style="<%= expand %>"> 
				<logic:iterate indexId="groupIter" id="groupResult" name="blockResult" property="groupsResults">
					<fr:edit id="<%= "group" + iter + groupIter %>" name="groupResult" layout="inquiry-group-resume-input"/>
				</logic:iterate>
			</div>
		</logic:equal>
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
			<bean:size id="teachersListSize" name="delegateInquiryBean" property="teachersResults"/>
			<logic:notEqual name="teachersListSize" value="0">
				<h3 class="separator2 mtop25">
					<span style="font-weight: normal;">
						<bean:message key="title.inquiry.resultsTeachers" bundle="DELEGATES_RESOURCES"/>
						(<logic:iterate indexId="iter" id="degree" name="delegateInquiryBean" property="executionCourse.degreesSortedByDegreeName">
							<logic:notEqual name="iter" value="0">,</logic:notEqual>
							<bean:write name="degree" property="sigla"/>
						</logic:iterate>)
					</span>
				</h3>
			</logic:notEqual>
			<bean:define id="teacherToogleFunctions" value=""/>
			<logic:iterate indexId="teacherIter" id="teacherShiftTypeResult" name="delegateInquiryBean" property="teachersResults" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.TeacherShiftTypeResultsBean">
				<div style="margin: 2.5em 0 3.5em 0;">
					<h3>
						<bean:write name="teacherShiftTypeResult" property="professorship.person.name"/> / 
						<bean:message name="teacherShiftTypeResult" property="shiftType.name"  bundle="ENUMERATION_RESOURCES"/>
					</h3>
					<bean:define id="professorshipOID" name="teacherShiftTypeResult" property="professorship.externalId"/>
					<bean:define id="shiftType" name="teacherShiftTypeResult" property="shiftType"/>
					<p class="mvert15">
						<html:link page="<%= "/delegateInquiry.do?method=viewTeacherShiftTypeInquiryResults&professorshipOID=" + professorshipOID + 
															"&shiftType=" + shiftType %>" target="_blank">
							<bean:message bundle="DELEGATES_RESOURCES" key="link.inquiry.showTeacherResults"/>
						</html:link>
					</p>
					<logic:iterate indexId="iter" id="blockResult" name="teacherShiftTypeResult" property="blockResults" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.BlockResultsSummaryBean">
						<bean:define id="teacherToogleFunctions">
							<bean:write name="teacherToogleFunctions" filter="false"/>
							<%= "$('#teacher-block" + teacherShiftTypeResult.getProfessorship().getExternalId() + teacherShiftTypeResult.getShiftType() + (Integer.valueOf(iter)+(int)1) + "').click(function()" 
								+ "{ $('#teacher-block" + teacherShiftTypeResult.getProfessorship().getExternalId() + teacherShiftTypeResult.getShiftType() + (Integer.valueOf(iter)+(int)1) + "-content').toggle('normal', function() { }); });" %>			
						</bean:define>
						<h4 class="mtop15" style="clear: left;">
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
		</div>
	</div>
	
	<!-- Delegate Inquiry -->	
	<logic:iterate id="inquiryBlockDTO" name="delegateInquiryBean" property="delegateInquiryBlocks">
		<h3 class="separator2 mtop25">
			<span style="font-weight: normal;">
				<fr:view name="inquiryBlockDTO" property="inquiryBlock.inquiryQuestionHeader.title"/>
			</span>
		</h3>					
		<logic:iterate id="inquiryGroup" name="inquiryBlockDTO" property="inquiryGroups" indexId="index">					
			<fr:edit id="<%= "iter" + index --%>" name="inquiryGroup"/>
		</logic:iterate>
	</logic:iterate>
	<p class="mtop15">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="button.saveInquiry" bundle="DELEGATES_RESOURCES"/>
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