<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="java.util.ArrayList" %>
       <table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td bgcolor="#FFFFFF" class="infoselected">
			  <strong><bean:message key="property.name"/></strong> <bean:write name="infoStudent" property="infoPerson.nome"/>
			  <br/>
			  <strong><bean:message key="property.number"/></strong> <bean:write name="infoStudent" property="number"/>
            </td>
          </tr>
		</table>
        <br />
	<html:link page="/viewSchedule.do"> <bean:message key="link.student.view.schedule"/> </html:link>
	<br/>
	<center><font color='#034D7A' size='5'> <b> <bean:message key="title.student.enrolment"/> </b> </font></center>
	<br/>
	<logic:present name="infoShiftEnrolment" scope="session">
	<html:form action="/viewShiftsList.do">
		<table align="center" border='1' cellpadding='10' cellspacing='1'>		
			<logic:iterate id="pairInfoCEList" name="infoShiftEnrolment" property="infoEnrolmentWithShift" indexId="i_index">
			<bean:define id="i" value="i_index" />
				<tr align="center">
					<th rowspan="<%= ((java.util.List) ((DataBeans.InfoCourseExecutionAndListOfTypeLessonAndInfoShift) pairInfoCEList).getTypeLessonsAndInfoShifts()).size() %>">
						<bean:write name="pairInfoCEList" property="infoExecutionCourse.nome"/>
					</th>
						
					<logic:iterate id="pairTypeLessonAndInfoShift" name="pairInfoCEList" property="typeLessonsAndInfoShifts" indexId="j_index">
						<td>
							<bean:write name="pairTypeLessonAndInfoShift" property="typeLesson"/>
						</td>
						<td>
								<logic:present name="pairTypeLessonAndInfoShift" property="infoShift">
										<bean:write name="pairTypeLessonAndInfoShift" property="infoShift.nome"/>
								</logic:present>
								<logic:notPresent name="pairTypeLessonAndInfoShift" property="infoShift">
										<bean:message key="message.student.enrolment.not.selected"/>
								</logic:notPresent>
						</td>
						<td>
								<html:radio  property="index" value="<%= String.valueOf(i_index)  + "-" +String.valueOf(j_index) %>" />
						</td>                			
						</tr>
					</logic:iterate>
			</logic:iterate>
		</table>
		<html:errors />
		<br/>
		<center>
		<html:submit>
			<bean:message key="label.enrolment.choose_change"/>
		</html:submit>
		</center>
		<logic:present name="infoShiftEnrolment" property="infoEnrolmentWithOutShift" scope="session">
			<br/>
			<br/>
			<br/>
			<br/>
			<center> <bean:message key="message.student.enrolment.courses.without.shifts"/> </center>
			<br/>

			<table align="center" border='1' cellpadding='10' cellspacing='1'>
				<logic:iterate id="courseExecution" name="infoShiftEnrolment" property="infoEnrolmentWithOutShift">
					<tr align="center">
						<td>
							<bean:write name="courseExecution" property="nome"/>
						</td>
					</tr>
				</logic:iterate>
			</table>
		</logic:present>
	</html:form>
	</logic:present>
	<html:link page="/chooseOtherCourse.do"> <bean:message key="link.student.enrolment.other.shifts"/> </html:link>
	<logic:notPresent name="infoShiftEnrolment" scope="session">
		<table align="center" border='1' cellpadding='10'>
			<tr align="center">
				<td>
					<font color='red'> <bean:message key="message.student.enrolment.not.available"/> </font>
				</td>
			</tr>
		</table>
	</logic:notPresent>