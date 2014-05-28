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
Teacher Inquiry Specific Questions
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
 
/* Teacher specific */

div#teacher-results div.workload-left {
margin-top: 0;
float: none;
}
</style>

<script src="<%= request.getContextPath() + "/javaScript/inquiries/jquery.min.js" %>" type="text/javascript" ></script>
<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/inquiries/hideButtons.js" %>"></script>
<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/inquiries/check.js" %>"></script>
<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/inquiries/checkall.js" %>"></script>
<link href="<%= request.getContextPath() %>/CSS/quc_results.css" rel="stylesheet" media="screen, print" type="text/css" />

<script type="text/javascript" language="javascript">switchGlobal();</script> 
<h2><bean:message key="title.inquiry.quc.teacher" bundle="INQUIRIES_RESOURCES"/></h2>

<h3><bean:write name="executionCourse" property="name"/> - <bean:write name="executionCourse" property="sigla"/> (<bean:write name="executionPeriod" property="semester"/>º Semestre <bean:write name="executionPeriod" property="executionYear.year"/>)</h3>

<p><bean:message key="message.teacher.details.inquiry" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="INQUIRIES_RESOURCES"/></p>

<html:messages id="message" message="true" bundle="INQUIRIES_RESOURCES">
	<p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>
	
<div id="report" class="teacher">
<fr:form action="/teachingInquiry.do">
	<html:hidden property="method" value="saveChanges"/>
	<fr:edit id="teacherInquiryBean" name="teacherInquiryBean" visible="false"/>
	
	<!-- Teachers Inquiry Results -->
	
	<bean:define id="exceptionClass" value=""/>
	<logic:present name="first-sem-2010">
		<bean:define id="exceptionClass" type="java.lang.String">
			<bean:write name="first-sem-2010"/>
		</bean:define>
	</logic:present>
	<div id="teacher-results" class="<%= exceptionClass %>">
		<h3 class="separator2 mtop2"><span style="font-weight: normal;">1. Resultados do Inquérito aos alunos</span></h3>
		<bean:define id="teacherToogleFunctions" value=""/>
		<logic:notEmpty name="teacherInquiryBean" property="teachersResults">
			<logic:iterate indexId="teacherIter" id="teacherShiftTypeResult" name="teacherInquiryBean" property="teachersResults" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.TeacherShiftTypeResultsBean">
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
		</logic:notEmpty>
		<logic:empty name="teacherInquiryBean" property="teachersResults">
			<p><em><bean:message key="label.withNoResults" bundle="INQUIRIES_RESOURCES"/></em></p>
		</logic:empty>
	</div>
	
	<!-- Teacher Inquiry -->
	<logic:iterate id="inquiryBlockDTO" name="teacherInquiryBean" property="teacherInquiryBlocks" indexId="blockIndex">
		<h3 class="separator2 mtop25">
			<span style="font-weight: normal;">
				<fr:view name="inquiryBlockDTO" property="inquiryBlock.inquiryQuestionHeader.title"/>
			</span>
		</h3>		
		<div class="max-width"> 			
		<logic:iterate id="inquiryGroup" name="inquiryBlockDTO" property="inquiryGroups" indexId="groupIndex">
			<a name="<%= "inquiry" + blockIndex + groupIndex %>"> </a>				
			<fr:edit id="<%= "inquiry" + blockIndex + groupIndex %>" name="inquiryGroup">
				<fr:layout>
					<fr:property name="postBackMethod" value="postBackTeacherInquiry"/>
					<fr:property name="postBackAnchor" value="<%= "inquiry" + blockIndex + groupIndex %>"/>
				</fr:layout>
			</fr:edit>
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

<bean:define id="scriptTeacherToogleFunctions">
	<script>
	<bean:write name="teacherToogleFunctions" filter="false"/>
	</script>
</bean:define>

<bean:write name="scriptTeacherToogleFunctions" filter="false"/>