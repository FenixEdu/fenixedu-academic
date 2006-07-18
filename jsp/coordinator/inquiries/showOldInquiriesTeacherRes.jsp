<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoTeacher" %>
<%@ page import="net.sourceforge.fenixedu.util.InquiriesUtil" %>
<%@ page import="net.sourceforge.fenixedu.util.NumberUtils" %>
<%@ page import = "java.util.Iterator" %>
<%@ page import = "net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoOldInquiriesTeachersRes" %>
<%@ page import = "java.util.List" %>

<h2>
	<bean:message key="title.teacher.inquiries.results" bundle="INQUIRIES_RESOURCES"/>
</h2>
<h2>
	<bean:message key="title.teacher.inquiries.individual.results" bundle="INQUIRIES_RESOURCES"/>
</h2>


<%--ISTO VAI SER PRA POR O NOME DA CADEIRA, PERÍODO DE EXECUÇÃO
<table class="infoselected" width="100%">
	<tr>
		<td width="50%">
			<bean:message key="message.teacherInformation.name" />
			&nbsp;
		</td> 
		<td width="50%">
			<bean:message key="message.teacherInformation.birthDate" />
			&nbsp;<bean:write name="infoSiteTeacherInformation" property="infoTeacher.infoPerson.nascimento" />
		</td>	
	</tr>
	<tr>
		<td><bean:message key="message.teacherInformation.category" />
			&nbsp;</td>
	</tr>
</table>
 --%>
 
<logic:present name="oldInquiriesTeachersResListOfLists">
	<logic:iterate id="oldInquiriesTeachersResList" name="oldInquiriesTeachersResListOfLists" type="java.util.List">
		
		<%
		Iterator iter = oldInquiriesTeachersResList.iterator();
		InfoOldInquiriesTeachersRes ioitr = (InfoOldInquiriesTeachersRes) iter.next();
		%>
		<br/>
		<hr/>
		<% if(ioitr.getTeacher() != null) { %>
		<h4>
			<%= ioitr.getTeacher().getInfoPerson().getNome() %>
			&nbsp;-&nbsp;
			<%= ioitr.getTeacher().getTeacherNumber() %>
		</h4>
		<h4>
			<%= ioitr.getTeacher().getInfoCategory().getLongName() %>
		</h4>
		<% } else { %>
		<h4>
			Docente n&uacute;mero <%= ioitr.getTeacherNumber() %>
		</h4>
		<% }%>
		
		<logic:iterate id="oldInquiryTeachersRes" name="oldInquiriesTeachersResList" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoOldInquiriesTeachersRes"> 
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
	</logic:iterate>
</logic:present>
