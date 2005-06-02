<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>

<br /> 
<logic:present name="<%=SessionConstants.ALL_INFO_VIEW_CLASS_SCHEDULE %>" scope="request">
	<logic:iterate id="viewClassSchedule" name="<%=SessionConstants.ALL_INFO_VIEW_CLASS_SCHEDULE %>" scope="request">
		<table width="100%" cellspacing="0">
			<tr>
				<td class="infoselected">
					<strong>
						<bean:define id="infoDegree" name="viewClassSchedule" property="infoClass.infoExecutionDegree.infoDegreeCurricularPlan.infoDegree"/>
					   	<bean:define id="infoExecutionPeriod" name="viewClassSchedule" property="infoClass.infoExecutionPeriod"/>
					   	<jsp:getProperty name="infoDegree" property="tipoCurso" /> em 
						<jsp:getProperty name="infoDegree" property="nome" />
						<br/>
						<jsp:getProperty name="infoExecutionPeriod" property="name"/> -
						<bean:write name="infoExecutionPeriod" property="infoExecutionYear.year"/>
					</strong>
		         </td>
		    </tr>
		</table>

		<h2 class="break-before"><bean:message key="title.class.timetable" /><bean:write name="viewClassSchedule" property="infoClass.nome" /></h2>
	   	<br/>
	   	<bean:define id="lessons" name="viewClassSchedule" property="classLessons"/>
		<div align="center"><app:gerarHorario name="lessons" type="<%= TimeTableType.CLASS_TIMETABLE_WITHOUT_LINKS %>"/></div>
	</logic:iterate>
</logic:present>
<logic:notPresent name="<%=SessionConstants.ALL_INFO_VIEW_CLASS_SCHEDULE %>" scope="request">
	<span class="error"><bean:message key="message.classes.notExisting"/></span>
</logic:notPresent>