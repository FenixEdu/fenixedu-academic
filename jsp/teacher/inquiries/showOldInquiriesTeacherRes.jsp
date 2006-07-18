<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoTeacher" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoOldInquiriesCoursesRes" %>
<%@ page import="net.sourceforge.fenixedu.util.InquiriesUtil" %>
<%@ page import="net.sourceforge.fenixedu.util.NumberUtils" %>

<h2>
	<bean:message key="title.teacher.inquiries.results" bundle="INQUIRIES_RESOURCES"/>
</h2>



 
<logic:present name="oldInquiriesCoursesRes">
	<%
	InfoOldInquiriesCoursesRes oldInquiriesCoursesRes = (InfoOldInquiriesCoursesRes)request.getAttribute("oldInquiriesCoursesRes");
	%>
	<table class="listClasses" width="100%">
		<tr>
			<td>
				<b>
<bean:define id="degreeType"  name="oldInquiriesCoursesRes" property="degree.tipoCurso"/>

   <logic:equal name="degreeType" value="DEGREE" >
       <bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.degreeType" />
    </logic:equal>
    <logic:equal name="degreeType" value="MASTER_DEGREE" >
		    <bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.masterDegreeType" />
	</logic:equal>
	<bean:message bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.in" />


					<bean:write name="oldInquiriesCoursesRes" property="degree.nome"/>
				</b>
			</td>
		</tr>
	</table>

	<br />

	<br/>

	<table class="infoselected" width="100%">
		<tr>
			<td width="65%">
				<b>
					<bean:write name="oldInquiriesCoursesRes" property="gepCourseName"/>
				</b>
			</td> 
			<td width="10%">
				<logic:notEmpty name="oldInquiriesCoursesRes" property="curricularYear" >
					<bean:write name="oldInquiriesCoursesRes" property="curricularYear" />
					&ordm;&nbsp;
					<bean:message key="label.inquiries.year" bundle="INQUIRIES_RESOURCES"/>
				</logic:notEmpty>
			</td>
			<td width="25%" align="right">
				<logic:notEmpty name="oldInquiriesCoursesRes" property="numberEnrollments" >
					<%
					out.print(InquiriesUtil.formatAnswer(oldInquiriesCoursesRes.getNumberEnrollments()));
					%>
					&nbsp;
					<bean:message key="label.inquiries.enrolled.students" bundle="INQUIRIES_RESOURCES"/>
				</logic:notEmpty>
			</td>	
		</tr>
		<tr>
			<td width="65%">
			</td>
			<td width="10%">
			</td>
			<td width="25%" align="right">
				<%
				Double answerRatio = new Double(Double.MIN_VALUE);
				%>
				<logic:notEmpty name="oldInquiriesCoursesRes" property="numberAnswers" >
					<logic:notEmpty name="oldInquiriesCoursesRes" property="numberEnrollments" >
						<%
						if(oldInquiriesCoursesRes.getNumberEnrollments().doubleValue() != 0) {
							answerRatio = new Double(
									(oldInquiriesCoursesRes.getNumberAnswers().intValue() * 100) / 
									oldInquiriesCoursesRes.getNumberEnrollments().doubleValue());
						}
						%>
					</logic:notEmpty>
				</logic:notEmpty>
				
				<%
				out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(answerRatio, 1)));
				%>
				%&nbsp;
				<bean:message key="label.inquiries.answers" bundle="INQUIRIES_RESOURCES"/>
			</td>	
		</tr>
	</table>
	
	<br/>

	<table width="90%"cellpadding="5" border="0">
		<tr>
			<td class="invisible" style="text-align:left" rowspan="10" valign="top">
				&nbsp;&nbsp;
			</td>
			<th class="listClasses-header" style="text-align:left">
				<bean:message key="table.header.inquiries.questions"  bundle="INQUIRIES_RESOURCES"/>
			</th>
			<th class="listClasses-header">
				<bean:message key="table.header.inquiries.average"  bundle="INQUIRIES_RESOURCES"/>
			</th>
			<th class="listClasses-header">
				<bean:message key="table.header.inquiries.deviation"  bundle="INQUIRIES_RESOURCES"/>
			</th>
			<th class="listClasses-header">
				<bean:message key="table.header.inquiries.tolerance"  bundle="INQUIRIES_RESOURCES"/>
			</th>
			<th class="listClasses-header">
				<bean:message key="table.header.inquiries.number.answers"  bundle="INQUIRIES_RESOURCES"/>
			</th>
		</tr>

		<%--############ CLASS COORDINATION ############## --%>
		<bean:define id="average2_2" name="oldInquiriesCoursesRes" property="average2_2" type="java.lang.Double"/>
		<bean:define id="deviation2_2" name="oldInquiriesCoursesRes" property="deviation2_2" type="java.lang.Double"/>
		<bean:define id="tolerance2_2" name="oldInquiriesCoursesRes" property="tolerance2_2" type="java.lang.Double"/>
		<bean:define id="numAnswers2_2" name="oldInquiriesCoursesRes" property="numAnswers2_2" type="java.lang.Integer"/>

		<tr>
			<td class="listClasses" style="text-align:left">
				<b>
					<bean:message key="table.colname.inquiries.class.coordination"  bundle="INQUIRIES_RESOURCES"/>
				</b>
			</td>			
			<td class="listClasses">
				<%
				out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(average2_2, 2)));
				%>
			</td>			
			<td class="listClasses">
				<%
				out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(deviation2_2, 2)));
				%>
			</td>			
			<td class="listClasses">
				<%
				out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(tolerance2_2, 2)));
				%>
			</td>			
			<td class="listClasses">
				<%
				out.print(InquiriesUtil.formatAnswer(numAnswers2_2));
				%>
			</td>			
		</tr>

		<%--############ STUDY ELEMENTS CONTRIBUTION ############## --%>
		<bean:define id="average2_3" name="oldInquiriesCoursesRes" property="average2_3" type="java.lang.Double"/>
		<bean:define id="deviation2_3" name="oldInquiriesCoursesRes" property="deviation2_3" type="java.lang.Double"/>
		<bean:define id="tolerance2_3" name="oldInquiriesCoursesRes" property="tolerance2_3" type="java.lang.Double"/>
		<bean:define id="numAnswers2_3" name="oldInquiriesCoursesRes" property="numAnswers2_3" type="java.lang.Integer"/>

		<tr>
			<td class="listClasses" style="text-align:left">
				<b>
					<bean:message key="table.colname.inquiries.study.elements.contribution"  bundle="INQUIRIES_RESOURCES"/>
				</b>
			</td>			
			<td class="listClasses">
				<%
				out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(average2_3, 2)));
				%>
			</td>			
			<td class="listClasses">
				<%
				out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(deviation2_3, 2)));
				%>
			</td>			
			<td class="listClasses">
				<%
				out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(tolerance2_3, 2)));
				%>
			</td>			
			<td class="listClasses">
				<%
				out.print(InquiriesUtil.formatAnswer(numAnswers2_3));
				%>
			</td>			
		</tr>

		<%--############ PREVIOUS KNOWLEGDE ARTICULATION ############## --%>
		<bean:define id="average2_4" name="oldInquiriesCoursesRes" property="average2_4" type="java.lang.Double"/>
		<bean:define id="deviation2_4" name="oldInquiriesCoursesRes" property="deviation2_4" type="java.lang.Double"/>
		<bean:define id="tolerance2_4" name="oldInquiriesCoursesRes" property="tolerance2_4" type="java.lang.Double"/>
		<bean:define id="numAnswers2_4" name="oldInquiriesCoursesRes" property="numAnswers2_4" type="java.lang.Integer"/>

		<tr>
			<td class="listClasses" style="text-align:left">
				<b>
					<bean:message key="table.colname.inquiries.previous.knowlegde.articulation"  bundle="INQUIRIES_RESOURCES"/>
				</b>
			</td>			
			<td class="listClasses">
				<%
				out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(average2_4, 2)));
				%>
			</td>			
			<td class="listClasses">
				<%
				out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(deviation2_4, 2)));
				%>
			</td>			
			<td class="listClasses">
				<%
				out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(tolerance2_4, 2)));
				%>
			</td>			
			<td class="listClasses">
				<%
				out.print(InquiriesUtil.formatAnswer(numAnswers2_4));
				%>
			</td>			
		</tr>

		<%--############ CONTRIBUTION FOR GRADUATION ############## --%>
		<bean:define id="average2_5" name="oldInquiriesCoursesRes" property="average2_5" type="java.lang.Double"/>
		<bean:define id="deviation2_5" name="oldInquiriesCoursesRes" property="deviation2_5" type="java.lang.Double"/>
		<bean:define id="tolerance2_5" name="oldInquiriesCoursesRes" property="tolerance2_5" type="java.lang.Double"/>
		<bean:define id="numAnswers2_5_number" name="oldInquiriesCoursesRes" property="numAnswers2_5_number" type="java.lang.Integer"/>

		<tr>
			<td class="listClasses" style="text-align:left">
				<b>
					<bean:message key="table.colname.inquiries.course.contribution.for.graduation"  bundle="INQUIRIES_RESOURCES"/>
				</b>
			</td>			
			<td class="listClasses">
				<%
				out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(average2_5, 2)));
				%>
			</td>			
			<td class="listClasses">
				<%
				out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(deviation2_5, 2)));
				%>
			</td>			
			<td class="listClasses">
				<%
				out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(tolerance2_5, 2)));
				%>
			</td>			
			<td class="listClasses">
				<%
				out.print(InquiriesUtil.formatAnswer(numAnswers2_5_number));
				%>
			</td>			
		</tr>

		<%--############ EVALUATION METHOD ADEQUATION ############## --%>
		<bean:define id="average2_6" name="oldInquiriesCoursesRes" property="average2_6" type="java.lang.Double"/>
		<bean:define id="deviation2_6" name="oldInquiriesCoursesRes" property="deviation2_6" type="java.lang.Double"/>
		<bean:define id="tolerance2_6" name="oldInquiriesCoursesRes" property="tolerance2_6" type="java.lang.Double"/>
		<bean:define id="numAnswers2_6" name="oldInquiriesCoursesRes" property="numAnswers2_6" type="java.lang.Integer"/>

		<tr>
			<td class="listClasses" style="text-align:left">
				<b>
					<bean:message key="table.colname.inquiries.evaluation.method.adequation"  bundle="INQUIRIES_RESOURCES"/>
				</b>
			</td>			
			<td class="listClasses">
				<%
				out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(average2_6, 2)));
				%>
			</td>			
			<td class="listClasses">
				<%
				out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(deviation2_6, 2)));
				%>
			</td>			
			<td class="listClasses">
				<%
				out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(tolerance2_6, 2)));
				%>
			</td>			
			<td class="listClasses">
				<%
				out.print(InquiriesUtil.formatAnswer(numAnswers2_6));
				%>
			</td>			
		</tr>

		<%--############ WEEKLY SPENT HOURS ############## --%>
		<bean:define id="average2_7" name="oldInquiriesCoursesRes" property="average2_7" type="java.lang.String"/>
		<bean:define id="numAnswers2_7" name="oldInquiriesCoursesRes" property="numAnswers2_7" type="java.lang.Integer"/>

		<tr>
			<td class="listClasses" style="text-align:left">
				<b>
					<bean:message key="table.colname.inquiries.weekly.spent.hours"  bundle="INQUIRIES_RESOURCES"/>
				</b>
			</td>			
			<td class="listClasses">
				<%
				out.print(InquiriesUtil.formatAnswer(average2_7));
				%>
			</td>			
			<td class="listClasses">
				-
			</td>			
			<td class="listClasses">
				-
			</td>			
			<td class="listClasses">
				<%
				out.print(InquiriesUtil.formatAnswer(numAnswers2_7));
				%>
			</td>			
		</tr>

		<%--############ GLOBAL APPRECIATION ############## --%>
		<bean:define id="average2_8" name="oldInquiriesCoursesRes" property="average2_8" type="java.lang.Double"/>
		<bean:define id="deviation2_8" name="oldInquiriesCoursesRes" property="deviation2_8" type="java.lang.Double"/>
		<bean:define id="tolerance2_8" name="oldInquiriesCoursesRes" property="tolerance2_8" type="java.lang.Double"/>
		<bean:define id="numAnswers2_8" name="oldInquiriesCoursesRes" property="numAnswers2_8" type="java.lang.Integer"/>

		<tr>
			<td class="listClasses" style="text-align:left">
				<b>
					<bean:message key="table.colname.inquiries.global.appreciation"  bundle="INQUIRIES_RESOURCES"/>
				</b>
			</td>			
			<td class="listClasses">
				<%
				out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(average2_8, 2)));
				%>
			</td>			
			<td class="listClasses">
				<%
				out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(deviation2_8, 2)));
				%>
			</td>			
			<td class="listClasses">
				<%
				out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(tolerance2_8, 2)));
				%>
			</td>			
			<td class="listClasses">
				<%
				out.print(InquiriesUtil.formatAnswer(numAnswers2_8));
				%>
			</td>			
		</tr>


	</table>

	
</logic:present>
 

	
		
			
<logic:present name="oldInquiryTeachersResList">
	<br/><br/><br/>
	<h2>
		<bean:message key="title.teacher.inquiries.individual.results" bundle="INQUIRIES_RESOURCES"/>
	</h2>

	<table class="infoselected" width="100%">
		<tr>
			<td width="65%">
				<b>
					<bean:message key="message.teacherInformation.name" />
				</b>
				&nbsp;
				<bean:write name="infoTeacher" property="infoPerson.nome" />
			</td> 
			<td width="35%">
				<b>
					<bean:message key="label.teachersInformation.number" />
				</b>
				&nbsp;
				<bean:write name="infoTeacher" property="teacherNumber" />
			</td>	
		</tr>
		<tr>
			<td width="65%">
				<b>
					<bean:message key="message.teacherInformation.category" />
				</b>
				&nbsp;
				<logic:notEmpty name="infoTeacher" property="infoCategory" >
				<bean:write name="infoTeacher" property="infoCategory.shortName" />
				</logic:notEmpty>
			</td>
			<td width="35%">
				<b>
					<bean:message key="message.teacherInformation.birthDate" />
				</b>
				&nbsp;
				<bean:write name="infoTeacher" property="infoPerson.nascimento" />
			</td>	
		</tr>
	</table>

	<br/>

<logic:iterate id="oldInquiryTeachersRes" name="oldInquiryTeachersResList" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoOldInquiriesTeachersRes"> 

		<h3>
			<bean:write name="oldInquiryTeachersRes" property="classTypeLong" />
		</h3>
		<table width="90%"cellpadding="5" border="0">
			<tr>
				<td class="invisible" style="text-align:left" rowspan="10" valign="top">
					&nbsp;&nbsp;
				</td>
				<th class="listClasses-header" style="text-align:left">
					<bean:message key="table.header.inquiries.questions"  bundle="INQUIRIES_RESOURCES"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="table.header.inquiries.average"  bundle="INQUIRIES_RESOURCES"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="table.header.inquiries.deviation"  bundle="INQUIRIES_RESOURCES"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="table.header.inquiries.tolerance"  bundle="INQUIRIES_RESOURCES"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="table.header.inquiries.number.answers"  bundle="INQUIRIES_RESOURCES"/>
				</th>
			</tr>
	
			<%--############ ASSIDUITY (TEACHER) ############## --%>
			<bean:define id="average3_4" name="oldInquiryTeachersRes" property="average3_4" type="java.lang.String"/>
			<bean:define id="deviation3_4" name="oldInquiryTeachersRes" property="deviation3_4" type="java.lang.Double"/>
			<bean:define id="tolerance3_4" name="oldInquiryTeachersRes" property="tolerance3_4" type="java.lang.Double"/>
			<bean:define id="numAnswers3_4" name="oldInquiryTeachersRes" property="numAnswers3_4" type="java.lang.Integer"/>
	
			<tr>
				<td class="listClasses" style="text-align:left">
					<b>
						<bean:message key="table.colname.inquiries.assiduity"  bundle="INQUIRIES_RESOURCES"/>
					</b>
				</td>			
				<td class="listClasses">
					<%
					out.print(InquiriesUtil.formatAnswer(average3_4));
					%>
				</td>			
				<td class="listClasses">
					<%
					out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(deviation3_4, 2)));
					%>
				</td>			
				<td class="listClasses">
					<%
					out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(tolerance3_4, 2)));
					%>
				</td>			
				<td class="listClasses">
					<%
					out.print(InquiriesUtil.formatAnswer(numAnswers3_4));
					%>
				</td>			
			</tr>
	
	
			<%--############ TIMMING ############## --%>
			<bean:define id="average3_5" name="oldInquiryTeachersRes" property="average3_5" type="java.lang.Double"/>
			<bean:define id="deviation3_5" name="oldInquiryTeachersRes" property="deviation3_5" type="java.lang.Double"/>
			<bean:define id="tolerance3_5" name="oldInquiryTeachersRes" property="tolerance3_5" type="java.lang.Double"/>
			<bean:define id="numAnswers3_5" name="oldInquiryTeachersRes" property="numAnswers3_5" type="java.lang.Integer"/>
	
			<tr>
				
				<td class="listClasses" style="text-align:left">
					<b>
						<bean:message key="table.colname.inquiries.timming"  bundle="INQUIRIES_RESOURCES"/>
					</b>
				</td>			
				<td class="listClasses">
					<%
					out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(average3_5, 2)));
					%>
				</td>			
				<td class="listClasses">
					<%
					out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(deviation3_5, 2)));
					%>
				</td>			
				<td class="listClasses">
					<%
					out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(tolerance3_5, 2)));
					%>
				</td>			
				<td class="listClasses">
					<%
					out.print(InquiriesUtil.formatAnswer(numAnswers3_5));
					%>
				</td>			
			</tr>
	
			<%--############ CLARITY ############## --%>
			<bean:define id="average3_6" name="oldInquiryTeachersRes" property="average3_6" type="java.lang.Double"/>
			<bean:define id="deviation3_6" name="oldInquiryTeachersRes" property="deviation3_6" type="java.lang.Double"/>
			<bean:define id="tolerance3_6" name="oldInquiryTeachersRes" property="tolerance3_6" type="java.lang.Double"/>
			<bean:define id="numAnswers3_6" name="oldInquiryTeachersRes" property="numAnswers3_6" type="java.lang.Integer"/>
	
			<tr>
				
				<td class="listClasses" style="text-align:left">
					<b>
						<bean:message key="table.colname.inquiries.clarity"  bundle="INQUIRIES_RESOURCES"/>
					</b>
				</td>			
				<td class="listClasses">
					<%
					out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(average3_6, 2)));
					%>
				</td>			
				<td class="listClasses">
					<%
					out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(deviation3_6, 2)));
					%>
				</td>			
				<td class="listClasses">
					<%
					out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(tolerance3_6, 2)));
					%>
				</td>			
				<td class="listClasses">
					<%
					out.print(InquiriesUtil.formatAnswer(numAnswers3_6));
					%>
				</td>			
			</tr>
	
			<%--############ ASSURANCE ############## --%>
			<bean:define id="average3_7" name="oldInquiryTeachersRes" property="average3_7" type="java.lang.Double"/>
			<bean:define id="deviation3_7" name="oldInquiryTeachersRes" property="deviation3_7" type="java.lang.Double"/>
			<bean:define id="tolerance3_7" name="oldInquiryTeachersRes" property="tolerance3_7" type="java.lang.Double"/>
			<bean:define id="numAnswers3_7" name="oldInquiryTeachersRes" property="numAnswers3_7" type="java.lang.Integer"/>
	
			<tr>
				
				<td class="listClasses" style="text-align:left">
					<b>
						<bean:message key="table.colname.inquiries.assurance"  bundle="INQUIRIES_RESOURCES"/>
					</b>
				</td>			
				<td class="listClasses">
					<%
					out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(average3_7, 2)));
					%>
				</td>			
				<td class="listClasses">
					<%
					out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(deviation3_7, 2)));
					%>
				</td>			
				<td class="listClasses">
					<%
					out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(tolerance3_7, 2)));
					%>
				</td>			
				<td class="listClasses">
					<%
					out.print(InquiriesUtil.formatAnswer(numAnswers3_7));
					%>
				</td>			
			</tr>
	
			<%--############ INTEREST STIMULATION ############## --%>
			<bean:define id="average3_8" name="oldInquiryTeachersRes" property="average3_8" type="java.lang.Double"/>
			<bean:define id="deviation3_8" name="oldInquiryTeachersRes" property="deviation3_8" type="java.lang.Double"/>
			<bean:define id="tolerance3_8" name="oldInquiryTeachersRes" property="tolerance3_8" type="java.lang.Double"/>
			<bean:define id="numAnswers3_8" name="oldInquiryTeachersRes" property="numAnswers3_8" type="java.lang.Integer"/>
	
			<tr>
				
				<td class="listClasses" style="text-align:left">
					<b>
						<bean:message key="table.colname.inquiries.interest.stimulation"  bundle="INQUIRIES_RESOURCES"/>
					</b>
				</td>			
				<td class="listClasses">
					<%
					out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(average3_8, 2)));
					%>
				</td>			
				<td class="listClasses">
					<%
					out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(deviation3_8, 2)));
					%>
				</td>			
				<td class="listClasses">
					<%
					out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(tolerance3_8, 2)));
					%>
				</td>			
				<td class="listClasses">
					<%
					out.print(InquiriesUtil.formatAnswer(numAnswers3_8));
					%>
				</td>			
			</tr>
	
			<%--############ AVAILABILITY ############## --%>
			<bean:define id="average3_9" name="oldInquiryTeachersRes" property="average3_9" type="java.lang.Double"/>
			<bean:define id="deviation3_9" name="oldInquiryTeachersRes" property="deviation3_9" type="java.lang.Double"/>
			<bean:define id="tolerance3_9" name="oldInquiryTeachersRes" property="tolerance3_9" type="java.lang.Double"/>
			<bean:define id="numAnswers3_9" name="oldInquiryTeachersRes" property="numAnswers3_9" type="java.lang.Integer"/>
	
			<tr>
				
				<td class="listClasses" style="text-align:left">
					<b>
						<bean:message key="table.colname.inquiries.availability"  bundle="INQUIRIES_RESOURCES"/>
					</b>
				</td>			
				<td class="listClasses">
					<%
					out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(average3_9, 2)));
					%>
				</td>			
				<td class="listClasses">
					<%
					out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(deviation3_9, 2)));
					%>
				</td>			
				<td class="listClasses">
					<%
					out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(tolerance3_9, 2)));
					%>
				</td>			
				<td class="listClasses">
					<%
					out.print(InquiriesUtil.formatAnswer(numAnswers3_9));
					%>
				</td>			
			</tr>
	
			<%--############ REASONING STIMULATION ############## --%>
			<bean:define id="average3_10" name="oldInquiryTeachersRes" property="average3_10" type="java.lang.Double"/>
			<bean:define id="deviation3_10" name="oldInquiryTeachersRes" property="deviation3_10" type="java.lang.Double"/>
			<bean:define id="tolerance3_10" name="oldInquiryTeachersRes" property="tolerance3_10" type="java.lang.Double"/>
			<bean:define id="numAnswers3_10" name="oldInquiryTeachersRes" property="numAnswers3_10" type="java.lang.Integer"/>
	
			<tr>
				
				<td class="listClasses" style="text-align:left">
					<b>
						<bean:message key="table.colname.inquiries.reasoning.stimulation"  bundle="INQUIRIES_RESOURCES"/>
					</b>
				</td>			
				<td class="listClasses">
					<%
					out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(average3_10, 2)));
					%>
				</td>			
				<td class="listClasses">
					<%
					out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(deviation3_10, 2)));
					%>
				</td>			
				<td class="listClasses">
					<%
					out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(tolerance3_10, 2)));
					%>
				</td>			
				<td class="listClasses">
					<%
					out.print(InquiriesUtil.formatAnswer(numAnswers3_10));
					%>
				</td>			
			</tr>
	
			<%--############ GLOBAL APPRECIATION ############## --%>
			<bean:define id="average3_11" name="oldInquiryTeachersRes" property="average3_11" type="java.lang.Double"/>
			<bean:define id="deviation3_11" name="oldInquiryTeachersRes" property="deviation3_11" type="java.lang.Double"/>
			<bean:define id="tolerance3_11" name="oldInquiryTeachersRes" property="tolerance3_11" type="java.lang.Double"/>
			<bean:define id="numAnswers3_11" name="oldInquiryTeachersRes" property="numAnswers3_11" type="java.lang.Integer"/>
	
			<tr>
				
				<td class="listClasses" style="text-align:left">
					<b>
						<bean:message key="table.colname.inquiries.global.appreciation"  bundle="INQUIRIES_RESOURCES"/>
					</b>
				</td>			
				<td class="listClasses">
					<%
					out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(average3_11, 2)));
					%>
				</td>			
				<td class="listClasses">
					<%
					out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(deviation3_11, 2)));
					%>
				</td>			
				<td class="listClasses">
					<%
					out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(tolerance3_11, 2)));
					%>
				</td>			
				<td class="listClasses">
					<%
					out.print(InquiriesUtil.formatAnswer(numAnswers3_11));
					%>
				</td>			
			</tr>
	
			<%--############ ASSIDUITY (STUDENTS) ############## --%>
			<bean:define id="average3_3" name="oldInquiryTeachersRes" property="average3_3" type="java.lang.String"/>
			<bean:define id="deviation3_3" name="oldInquiryTeachersRes" property="deviation3_3" type="java.lang.Double"/>
			<bean:define id="tolerance3_3" name="oldInquiryTeachersRes" property="tolerance3_3" type="java.lang.Double"/>
			<bean:define id="numAnswers3_3" name="oldInquiryTeachersRes" property="numAnswers3_3" type="java.lang.Integer"/>
	
			<tr>
				
				<td class="invisible" style="text-align:left">
					<b>
						<bean:message key="table.colname.inquiries.student.assiduity"  bundle="INQUIRIES_RESOURCES"/>
					</b>
				</td>			
				<td class="invisible" style="text-align:center">
					<%
					out.print(InquiriesUtil.formatAnswer(average3_3));
					%>
				</td>			
				<td class="invisible" style="text-align:center">
					<%
					out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(deviation3_3, 2)));
					%>
				</td>			
				<td class="invisible" style="text-align:center">
					<%
					out.print(InquiriesUtil.formatAnswer(NumberUtils.formatNumber(tolerance3_3, 2)));
					%>
				</td>			
				<td class="invisible" style="text-align:center">
					<%
					out.print(InquiriesUtil.formatAnswer(numAnswers3_3));
					%>
				</td>			
			</tr>
	
	
	
		</table>
	</logic:iterate>
</logic:present>
