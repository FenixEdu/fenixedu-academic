<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="ServidorApresentacao.TagLib.sop.v3.TimeTableType" %>
<%@ page import="DataBeans.InfoShiftWithAssociatedInfoClassesAndInfoLessons"%>
<%@ page import="DataBeans.InfoLesson"%>
<%@ page import="java.util.Calendar" %>

<logic:notPresent name="<%= SessionConstants.EXECUTION_COURSE_KEY %>" scope="session">
<table align="center"  cellpadding='0' cellspacing='0'>
			<tr align="center">
				<td>
					<span class="error"><bean:message key="message.public.notfound.executionCourse"/></span>
				</td>
			</tr>
		</table>
</logic:notPresent>

<logic:present name="<%= SessionConstants.EXECUTION_COURSE_KEY %>" scope="session">

	<h2><bean:message key="property.executionCourse.associatedCurricularCourses"/></h2>
	<logic:present name="publico.infoCurricularCourses" scope="session">
		<table align="center" cellspacing="0" cellpadding="5" >
			<tr class="timeTable_line" align="center">
				<td class="degreetablestd">
					<bean:message key="property.curricularCourse.name"/>
				</td>
				<td class="degreetablestd">
					<bean:message key="property.degree.initials"/>
				</td>
				<td class="degreetablestd">
					<bean:message key="property.curricularCourse.branch"/>
				</td>
				<td class="degreetablestd">
					<bean:message key="property.curricularCourse.curricularYear"/>
				</td>
				<td class="degreetablestd">
					<bean:message key="property.curricularCourse.semester"/>
				</td>
			</tr>
			<logic:iterate id="curricularCourse" name="publico.infoCurricularCourses" scope="session">
				<logic:iterate id="infoCurricularCourseScope" name="curricularCourse" property="infoScopes">
					<tr class="timeTable_line" align="center">
						<td class="degreetablestd">
							<bean:write name="curricularCourse" property="name"/>
						</td>
						<td class="degreetablestd">
							<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.sigla"/>
						</td>
						<td class="degreetablestd">
							<bean:write name="infoCurricularCourseScope" property="infoBranch.name"/>&nbsp;
						</td>
						<td class="degreetablestd">
							<bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year"/>&nbsp;
						</td>
						<td class="degreetablestd">
							<bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.semester"/>&nbsp;
						</td>
					</tr>
				</logic:iterate>
			</logic:iterate>
		</table>
	</logic:present>
	
<logic:notPresent name="publico.infoCurricularCourses" scope="session">
		<bean:message key="message.public.notfound.curricularCourses"/>
</logic:notPresent>
</logic:present>		
