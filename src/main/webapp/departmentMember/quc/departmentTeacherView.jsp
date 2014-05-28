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
<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/inquiries/hideButtons.js" %>"></script>
<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/inquiries/check.js" %>"></script>
<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/inquiries/checkall.js" %>"></script>
<link href="<%= request.getContextPath() %>/CSS/quc_results.css" rel="stylesheet" media="screen, print" type="text/css" />

<script type="text/javascript" language="javascript">switchGlobal();</script> 
<h2><bean:message key="title.inquiry.quc.department" bundle="INQUIRIES_RESOURCES"/></h2>

<h3><bean:write name="departmentTeacherDetailsBean" property="teacher.name"/> 
	(<bean:write name="executionPeriod" property="semester"/>º <bean:message key="label.inquiries.semester" bundle="INQUIRIES_RESOURCES"/> 
	<bean:write name="executionPeriod" property="executionYear.year"/>)</h3>

<div class="teacher">
	<bean:define id="exceptionClass" value=""/>
	<logic:present name="first-sem-2010">
		<bean:define id="exceptionClass" type="java.lang.String">
			<bean:write name="first-sem-2010"/>
		</bean:define>
	</logic:present>
	<div id="report" class="<%= exceptionClass %>">
		<logic:equal name="showComment" value="true">
			<fr:form action="/viewQucResults.do?method=saveTeacherComment">	
				<p><bean:message key="message.department.teacher.details.results" bundle="INQUIRIES_RESOURCES"/></p>
				<fr:edit id="departmentTeacherDetailsBean" name="departmentTeacherDetailsBean" visible="false"/>
				
				<h3 class="separator2 mtop15">
					<span style="font-weight: normal;">
						<bean:message key="title.inquiry.teacher.measuresToApply" bundle="INQUIRIES_RESOURCES"/>
					</span>
				</h3>
				<fr:edit name="departmentTeacherDetailsBean">
					<fr:schema bundle="INQUIRIES_RESOURCES" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.DepartmentTeacherDetailsBean">
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
			<logic:notEmpty name="departmentTeacherDetailsBean" property="allTeacherComments">
				<h3 class="separator2 mtop15">
					<span style="font-weight: normal;">
						<bean:message key="label.inquiry.comments" bundle="INQUIRIES_RESOURCES"/>
					</span>
				</h3>
				<div class="mtop15 mbottom25">
					<logic:iterate id="globalComment" name="departmentTeacherDetailsBean" property="allTeacherComments">
						<div class="comment">
							<p class="mbottom05"><b><fr:view name="globalComment" property="personCategory"/> 
							(<bean:write name="globalComment" property="person.name"/>) :</b></p>
							<p class="mtop05"><bean:write name="globalComment" property="comment"/></p>
						</div>
					</logic:iterate>
				</div>
			</logic:notEmpty>
		</logic:equal>
		
		<!-- Teachers Inquiry Results to Improve -->
		<bean:define id="teacherToImproveToogleFunctions" value=""/>
		<logic:notEmpty name="departmentTeacherDetailsBean" property="teachersResultsToImproveMap">
			<div id="teacher-results">
				<h3 class="separator2 mtop2"><span style="font-weight: normal;"><bean:message key="title.inquiry.results.toImprove" bundle="INQUIRIES_RESOURCES"/></span></h3>		
				<logic:iterate id="entrySet" name="departmentTeacherDetailsBean" property="teachersResultsToImproveMap">
					<logic:iterate indexId="teacherIter" id="teacherShiftTypeResult" name="entrySet" property="value" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.TeacherShiftTypeResultsBean">
						<div style="margin: 2.5em 0 3.5em 0;">
							<h3>
								<bean:write name="teacherShiftTypeResult" property="professorship.person.name"/> / 
								<bean:write name="teacherShiftTypeResult" property="professorship.executionCourse.name"/> /
								<bean:message name="teacherShiftTypeResult" property="shiftType.name"  bundle="ENUMERATION_RESOURCES"/> 
								<span class="color888" style="font-weight: normal;">
									(
									<logic:iterate id="executionDegree" indexId="degreeIter" name="teacherShiftTypeResult" property="professorship.executionCourse.executionDegrees">
										<logic:notEqual name="degreeIter" value="0">, </logic:notEqual>
										<bean:write name="executionDegree" property="degree.sigla"/>
									</logic:iterate>
									)
								</span>
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
								<bean:define id="teacherToImproveToogleFunctions">
									<bean:write name="teacherToImproveToogleFunctions" filter="false"/>
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
			</div>
		</logic:notEmpty>
		
		<!-- Teachers Inquiry Normal Results -->
		<bean:define id="teacherToogleFunctions" value=""/>
		<logic:notEmpty name="departmentTeacherDetailsBean" property="teachersResultsMap">	
			<div id="teacher-results">
				<h3 class="separator2 mtop2"><span style="font-weight: normal;"><bean:message key="title.inquiry.results.regular" bundle="INQUIRIES_RESOURCES"/></span></h3>					
				<logic:iterate id="entrySet" name="departmentTeacherDetailsBean" property="teachersResultsMap">
					<logic:iterate indexId="teacherIter" id="teacherShiftTypeResult" name="entrySet" property="value" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.TeacherShiftTypeResultsBean">
						<div style="margin: 2.5em 0 3.5em 0;">
							<h3>
								<bean:write name="teacherShiftTypeResult" property="professorship.person.name"/> / 
								<bean:write name="teacherShiftTypeResult" property="professorship.executionCourse.name"/> /
								<bean:message name="teacherShiftTypeResult" property="shiftType.name"  bundle="ENUMERATION_RESOURCES"/> 
								<span class="color888" style="font-weight: normal;">
									(
									<logic:iterate id="executionDegree" indexId="degreeIter" name="teacherShiftTypeResult" property="professorship.executionCourse.executionDegrees">
										<logic:notEqual name="degreeIter" value="0">, </logic:notEqual>
										<bean:write name="executionDegree" property="degree.sigla"/>
									</logic:iterate>
									)
								</span>
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
			</div>
		</logic:notEmpty>
				
	</div>
</div>

<bean:define id="scriptTeacherToogleFunctions">
	<script>
	<bean:write name="teacherToogleFunctions" filter="false"/>
	</script>
</bean:define>

<bean:write name="scriptTeacherToogleFunctions" filter="false"/>

<bean:define id="scriptTeacherToImproveToogleFunctions">
	<script>
	<bean:write name="teacherToImproveToogleFunctions" filter="false"/>
	</script>
</bean:define>

<bean:write name="scriptTeacherToImproveToogleFunctions" filter="false"/>
