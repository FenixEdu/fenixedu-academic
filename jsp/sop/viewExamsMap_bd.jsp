<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

 ############ Verify Info: ############
 <br/>
 <br/>
 
 <logic:present name="<%= SessionConstants.INFO_EXAMS_MAP %>" scope="session">
 	Curricular Years: <br/>
 	<logic:iterate id="item" name="<%= SessionConstants.INFO_EXAMS_MAP %>" property="curricularYears">
 		<bean:write name="item"/> <br/>
 	</logic:iterate>
 	<br/>
 	Start of Exam Season: <bean:write name="<%= SessionConstants.INFO_EXAMS_MAP %>" property="startSeason1.time"/> <br/>
 	End of Exam Season: <bean:write name="<%= SessionConstants.INFO_EXAMS_MAP %>" property="endSeason2.time"/> <br/>
 	<br/>
 	ExecutionCourses: <br/>
 	<logic:iterate id="item" name="<%= SessionConstants.INFO_EXAMS_MAP %>" property="executionCourses">
 		<bean:write name="item" property="sigla"/> - <bean:write name="item" property="nome"/> <br/>
 	</logic:iterate>
 	<br/>
 	<br/>
 </logic:present>

 <logic:notPresent name="<%= SessionConstants.INFO_EXAMS_MAP %>" scope="session">
	The info is not present!!!
 	<br/>
 	<br/>	
 </logic:notPresent>

 ######### End of Verify Info #########

Hello Crazy World... :0)

<app:generateExamsMap name="<%= SessionConstants.LESSON_LIST_ATT %>"/>