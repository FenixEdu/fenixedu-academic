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
 
/* Department specific */

div#teacher-results div.workload-left {
margin-top: 0;
float: none;
}

h4 em {
font-weight: normal;
}
</style>

<script src="<%= request.getContextPath() + "/javaScript/inquiries/jquery.min.js" %>" type="text/javascript" ></script>
<link href="<%= request.getContextPath() %>/CSS/quc_results.css" rel="stylesheet" media="screen, print" type="text/css" />

<script type="text/javascript" language="javascript">switchGlobal();</script> 
<h2><bean:message key="title.inquiry.quc.department" bundle="INQUIRIES_RESOURCES"/></h2>

<h3><bean:write name="executionCourse" property="name"/> - <bean:write name="executionCourse" property="sigla"/> 
	(<bean:write name="executionPeriod" property="semester"/>º <bean:message key="label.inquiries.semester" bundle="INQUIRIES_RESOURCES"/> 
		<bean:write name="executionPeriod" property="executionYear.year"/>)</h3>

<div id="report">
<logic:equal name="showAllComments" value="false">
	<p><bean:message key="message.department.curricularCourse.details.results" bundle="INQUIRIES_RESOURCES"/></p>
	<fr:form action="/viewQucResults.do?method=saveExecutionCourseComment">
		<fr:edit id="departmentUCResultsBean" name="departmentUCResultsBean" visible="false"/>
		
		<h3 class="separator2 mtop15">
			<span style="font-weight: normal;">
				Medidas a aplicar em relação à UC
			</span>
		</h3>
		<fr:edit name="departmentUCResultsBean">
			<fr:schema bundle="INQUIRIES_RESOURCES" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.DepartmentUCResultsBean">
				<fr:slot name="comment" layout="longText" key="label.inquiry.comment">
					<fr:property name="columns" value="70"/>
					<fr:property name="rows" value="6"/>
				</fr:slot>
				<fr:layout name="tabular">				
					<fr:property name="columnClasses" value="thtop,"/>
				</fr:layout>
			</fr:schema>
		</fr:edit>
		
		<p class="mtop15">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
				<bean:message key="button.saveInquiry" bundle="INQUIRIES_RESOURCES"/>
			</html:submit>
		</p>
	</fr:form>
</logic:equal>	
<logic:equal name="showAllComments" value="true">
	<logic:notEmpty name="departmentUCResultsBean" property="allCourseComments">
		<h3 class="separator2 mtop15">
			<span style="font-weight: normal;">
				Comentários
			</span>
		</h3>
		<div class="mtop15 mbottom25">
			<logic:iterate id="globalComment" name="departmentUCResultsBean" property="allCourseComments">
				<div class="comment">
					<p class="mbottom05"><b><fr:view name="globalComment" property="personCategory"/> 
					(<bean:write name="globalComment" property="person.name"/>) :</b></p>
					<p class="mtop05"><bean:write name="globalComment" property="comment"/></p>
				</div>
			</logic:iterate>
		</div>	
	</logic:notEmpty>
</logic:equal>
	
	<!-- Curricular Inquiry Results -->
	<bean:define id="toogleFunctions" value=""/>
	
	<h3 class="separator2 mtop15">
		<span style="font-weight: normal;">
			<bean:message key="title.inquiry.resultsUC" bundle="INQUIRIES_RESOURCES"/>
			(<bean:write name="departmentUCResultsBean" property="executionCourse.name"/> - <bean:write name="departmentUCResultsBean" property="executionDegree.degree.sigla"/>)
		</span>
	</h3>
	<bean:define id="executionCourse" name="departmentUCResultsBean" property="executionCourse" type="net.sourceforge.fenixedu.domain.ExecutionCourse"/>
	<bean:define id="executionDegree" name="departmentUCResultsBean" property="executionDegree" type="net.sourceforge.fenixedu.domain.ExecutionDegree"/>
	<bean:define id="executionCourseOID" name="executionCourse" property="externalId"/>
	<bean:define id="degreeCurricularPlanOID" name="executionDegree" property="degreeCurricularPlan.externalId"/>
	<p class="mvert15">
		<html:link module="/publico" page="<%= "/viewCourseResults.do?executionCourseOID=" + executionCourseOID + 
											"&degreeCurricularPlanOID=" + degreeCurricularPlanOID %>" target="_blank">
			<bean:message bundle="INQUIRIES_RESOURCES" key="link.inquiry.showUCResults"/>
		</html:link>
	</p>		
	<bean:define id="hasNotRelevantData">
		<%= executionCourse.hasNotRelevantDataFor(executionDegree) %>
	</bean:define>
	<logic:iterate indexId="iter" id="blockResult" name="departmentUCResultsBean" property="curricularBlockResults" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.BlockResultsSummaryBean">
		<logic:equal name="hasNotRelevantData" value="false"> <!-- if group is not GREY -->
			<bean:define id="toogleFunctions">
				<bean:write name="toogleFunctions" filter="false"/>
				<%= "$('#block" + Integer.valueOf(iter) +"').click(function() { $('#block" + Integer.valueOf(iter) + "-content').toggle('normal', function() { }); });" %>			
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
						<span id="<%= "block" + Integer.valueOf(iter) %>" class="link">
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
			<div id="<%= "block" + Integer.valueOf(iter) + "-content"%>" style="<%= expand %>"> 
				<logic:iterate indexId="groupIter" id="groupResult" name="blockResult" property="groupsResults">
					<fr:view name="groupResult">
						<fr:layout>
							<fr:property name="showComments" value="true"/>
						</fr:layout>
					</fr:view>
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
			<h3 class="separator2 mtop2"><span style="font-weight: normal;"><bean:message key="title.inquiry.teachingStaff" bundle="INQUIRIES_RESOURCES"/></span></h3>
			<bean:define id="teacherToogleFunctions" value=""/>
			<logic:notEmpty name="departmentUCResultsBean" property="teachersResultsMap">
				<logic:iterate id="entrySet" name="departmentUCResultsBean" property="teachersResultsMap">
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
										<fr:view name="groupResult">
											<fr:layout>
												<fr:property name="showComments" value="true"/>
											</fr:layout>
										</fr:view>
									</logic:iterate>
								</div>
							</logic:iterate>
						</div>
					</logic:iterate>
				</logic:iterate>				
			</logic:notEmpty>
			<logic:empty name="departmentUCResultsBean" property="teachersResultsMap">
				<p><em><bean:message key="label.withNoResults" bundle="INQUIRIES_RESOURCES"/></em></p>
			</logic:empty>
		</div>	
	</div>	
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