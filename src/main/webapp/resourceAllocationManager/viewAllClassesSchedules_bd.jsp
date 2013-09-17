<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>

<br /> 
<logic:present name="<%=PresentationConstants.ALL_INFO_VIEW_CLASS_SCHEDULE %>" scope="request">
	<logic:iterate id="viewClassSchedule" name="<%=PresentationConstants.ALL_INFO_VIEW_CLASS_SCHEDULE %>" scope="request" indexId="i">
				
		<logic:equal name="i" value="0">
			<table width="100%" cellspacing="0">
		</logic:equal>
		<logic:notEqual name="i" value="0">
			<br style="page-break-before" />
			<table class="break-before" width="100%" cellspacing="0">
		</logic:notEqual>
			<tr>
				<td class="infoselected">
					<strong>
						<bean:define id="infoDegree" name="viewClassSchedule" property="infoClass.infoExecutionDegree.infoDegreeCurricularPlan.infoDegree"/>					  
                    	<bean:define id="academicInterval" name="viewClassSchedule" property="infoClass.academicInterval"/>
						<jsp:getProperty name="infoDegree" property="presentationName" />
						<br/>
						<jsp:getProperty name="academicInterval" property="pathName"/> 
					</strong>
		         </td>
		    </tr>
		</table>

		<h2><bean:message key="title.class.timetable" /><bean:write name="viewClassSchedule" property="infoClass.nome" /></h2>
	   	<br/>
	   	<bean:define id="lessons" name="viewClassSchedule" property="classLessons"/>
		<div><app:gerarHorario name="lessons" definedWidth="false" type="<%= TimeTableType.CLASS_TIMETABLE_WITHOUT_LINKS %>"/></div>
	</logic:iterate>
</logic:present>
<logic:notPresent name="<%=PresentationConstants.ALL_INFO_VIEW_CLASS_SCHEDULE %>" scope="request">
	<span class="error"><!-- Error messages go here --><bean:message key="message.classes.notExisting"/></span>
</logic:notPresent>