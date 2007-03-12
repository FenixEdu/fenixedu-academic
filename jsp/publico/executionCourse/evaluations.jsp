<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<h2><bean:message key="title.evaluation" /></h2>

<bean:define id="evaluations" name="executionCourse" property="associatedEvaluations"/>

<logic:notEmpty name="executionCourse" property="comment">
	*) <bean:write name="executionCourse" property="comment"/>
</logic:notEmpty>

<br/>

<html:link page="/executionCourse.do?method=marks"
		paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
	<bean:message key="label.publishedMarks"/>
</html:link>

<br/>

<table class="tab_complex" width="70%" cellspacing="1" cellpadding="2">
	<tr>
		<th><bean:message key="label.online.test"/></th>
	</tr>
	<logic:iterate id="evaluation" name="evaluations">
		<bean:define id="evaluationOID" name="evaluation" property="idInternal"/>
		<logic:equal name="evaluation" property="class.name" value="net.sourceforge.fenixedu.domain.onlineTests.OnlineTest">
			<tr>
				<td>
					<bean:message key="label.online.test"/>: <bean:write name="evaluation" property="distributedTest.title"/>
				</td>
			</tr>
		</logic:equal>
	</logic:iterate>
</table>

<table class="tab_complex" width="70%" cellspacing="1" cellpadding="2">
	<tr>
		<th><bean:message key="label.projects"/></th>
		<th><bean:message key="label.beginning"/></th>
		<th><bean:message key="label.end"/></th>
	</tr>
	<logic:iterate id="evaluation" name="evaluations">
		<bean:define id="evaluationOID" name="evaluation" property="idInternal"/>
		<logic:equal name="evaluation" property="class.name" value="net.sourceforge.fenixedu.domain.Project">
			<tr>
				<td>
					<bean:message key="label.project"/>: <bean:write name="evaluation" property="name"/>
				</td>
				<td>
					<dt:format pattern="dd/MM/yyyy HH:mm"><bean:write name="evaluation" property="begin.time"/></dt:format>
				</td>
				<td>
					<dt:format pattern="dd/MM/yyyy HH:mm"><bean:write name="evaluation" property="end.time"/></dt:format>
				</td>
			</tr>
		</logic:equal>
	</logic:iterate>
</table>

<table class="tab_complex" width="70%" cellspacing="1" cellpadding="2">
	<tr>
		<th><bean:message key="label.testsAndExams"/></th>
		<th><bean:message key="label.day"/></th>
		<th><bean:message key="label.beginning"/></th>
		<th><bean:message key="label.end"/></th>
		<th><bean:message key="label.evaluation.enrolment.period"/></th>
		<th><bean:message key="label.rooms"/></th>
	</tr>	
	<logic:iterate id="evaluation" name="executionCourse" property="writtenEvaluations">
		<bean:define id="evaluationOID" name="evaluation" property="idInternal"/>
		<logic:equal name="evaluation" property="class.name" value="net.sourceforge.fenixedu.domain.Exam">
		<logic:equal name="evaluation" property="isExamsMapPublished" value="true">
			<tr>
				<td>
					<bean:message key="label.exam"/>: <bean:write name="evaluation" property="season"/>
					<logic:notEmpty name="executionCourse" property="comment">
						*
					</logic:notEmpty>
				</td>
				<td>
					<dt:format pattern="dd/MM/yyyy"><bean:write name="evaluation" property="dayDate.time"/></dt:format>
				</td>
				<td>
					<logic:present name="evaluation" property="beginningDate">
						<dt:format pattern="HH:mm"><bean:write name="evaluation" property="beginningDate.time"/></dt:format>
					</logic:present>
				</td>
				<td>
					<logic:present name="evaluation" property="endDate">
						<dt:format pattern="HH:mm"><bean:write name="evaluation" property="endDate.time"/></dt:format>
					</logic:present>
				</td>
				<td>
					<logic:present name="evaluation" property="enrollmentBeginDayDate">
						<dt:format pattern="dd/MM/yyyy"><bean:write name="evaluation" property="enrollmentBeginDayDate.time"/></dt:format>
						<dt:format pattern="HH:mm"><bean:write name="evaluation" property="enrollmentBeginTimeDate.time"/></dt:format>
						-
						<dt:format pattern="dd/MM/yyyy"><bean:write name="evaluation" property="enrollmentEndDayDate.time"/></dt:format>
						<dt:format pattern="HH:mm"><bean:write name="evaluation" property="enrollmentEndTimeDate.time"/></dt:format>
					</logic:present>
				</td>
				<td>
					<logic:iterate id="roomOccupation" name="evaluation" property="associatedRoomOccupation">
						<bean:define id="room" name="roomOccupation" property="room"/>
						<bean:write name="room" property="nome"/>
					</logic:iterate>
				</td>
			</tr>
		</logic:equal>
		</logic:equal>
		<logic:equal name="evaluation" property="class.name" value="net.sourceforge.fenixedu.domain.WrittenTest">
			<tr>
				<td>
					<bean:message key="label.written.test"/>: <bean:write name="evaluation" property="description"/>
				</td>
				<td>
					<dt:format pattern="dd/MM/yyyy"><bean:write name="evaluation" property="dayDate.time"/></dt:format>
				</td>
				<td>
					<logic:present name="evaluation" property="beginningDate">
						<dt:format pattern="HH:mm"><bean:write name="evaluation" property="beginningDate.time"/></dt:format>
					</logic:present>
				</td>
				<td>
					<logic:present name="evaluation" property="endDate">
						<dt:format pattern="HH:mm"><bean:write name="evaluation" property="endDate.time"/></dt:format>
					</logic:present>
				</td>
				<td>
					<logic:present name="evaluation" property="enrollmentBeginDayDate">
						<dt:format pattern="dd/MM/yyyy"><bean:write name="evaluation" property="enrollmentBeginDayDate.time"/></dt:format>
						<dt:format pattern="HH:mm"><bean:write name="evaluation" property="enrollmentBeginTimeDate.time"/></dt:format>
						-
						<dt:format pattern="dd/MM/yyyy"><bean:write name="evaluation" property="enrollmentEndDayDate.time"/></dt:format>
						<dt:format pattern="HH:mm"><bean:write name="evaluation" property="enrollmentEndTimeDate.time"/></dt:format>
					</logic:present>
				</td>
				<td>
					<logic:iterate id="roomOccupation" name="evaluation" property="associatedRoomOccupation">
						<bean:define id="room" name="roomOccupation" property="room"/>
						<bean:write name="room" property="nome"/>
					</logic:iterate>
				</td>
			</tr>
		</logic:equal>
	</logic:iterate>
</table>
