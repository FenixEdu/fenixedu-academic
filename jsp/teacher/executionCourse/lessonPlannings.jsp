<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>

<logic:present name="executionCourse">

	<H2><bean:message key="label.lessonPlannings.managent"/></H2>
	<br/>
	
	<html:link page="/manageExecutionCourse.do?method=prepareCreateLessonPlanning&amp;page=0" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
		<bean:message key="link.create.lessonPlanning"/>
	</html:link>
	<br/>
		
	<bean:define id="edit">
		/manageExecutionCourse.do?method=prepareEditLessonPlanning&executionCourseID=<bean:write name="executionCourse" property="idInternal"/>&page=0
	</bean:define>	
	<bean:define id="delete">
		/manageExecutionCourse.do?method=deleteLessonPlanning&executionCourseID=<bean:write name="executionCourse" property="idInternal"/>&page=0
	</bean:define>
	<bean:define id="moveUp">
		/manageExecutionCourse.do?method=moveUpLessonPlanning&executionCourseID=<bean:write name="executionCourse" property="idInternal"/>&page=0
	</bean:define>
	<bean:define id="moveDown">
		/manageExecutionCourse.do?method=moveDownLessonPlanning&executionCourseID=<bean:write name="executionCourse" property="idInternal"/>&page=0
	</bean:define>	
		
	<logic:iterate id="lessonPlannings" name="lessonPlanningsMap">		
		<logic:notEmpty name="lessonPlannings" property="value">
			<H3><bean:message key="label.lessons"/> <bean:message name="lessonPlannings" property="key.name" bundle="APPLICATION_RESOURCES"/></H3>
		</logic:notEmpty>
		<logic:iterate id="lessonPlanning" name="lessonPlannings" property="value" indexId="index">
			<i><bean:message key="label.lessonPlanning"/></i>&nbsp;<i><bean:write name="lessonPlanning" property="orderOfPlanning"/></i><br/>
			<b><bean:write name="lessonPlanning" property="title"/></b><br/>
			<bean:write name="lessonPlanning" property="planning" filter="false"/><br/>							
			
			<html:link page="<%= edit %>" paramId="lessonPlanningID" paramName="lessonPlanning" paramProperty="idInternal">
				<bean:message key="link.edit"/>
			</html:link>,&nbsp;
			<html:link page="<%= delete %>" paramId="lessonPlanningID" paramName="lessonPlanning" paramProperty="idInternal">
				<bean:message key="link.delete"/>
			</html:link>,&nbsp;
			<html:link page="<%= moveUp %>" paramId="lessonPlanningID" paramName="lessonPlanning" paramProperty="idInternal">
				<bean:message key="link.move.up"/>
			</html:link>,&nbsp;
			<html:link page="<%= moveDown %>" paramId="lessonPlanningID" paramName="lessonPlanning" paramProperty="idInternal">
				<bean:message key="link.move.down"/>
			</html:link>
			<br/><br/>
		</logic:iterate>				
	</logic:iterate>

</logic:present>