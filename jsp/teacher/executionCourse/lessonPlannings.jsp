<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>

<logic:present name="executionCourse">

	<h2><bean:message key="label.lessonPlannings.managent"/></h2>
	
	<ul>
		<li>
			<html:link page="/manageExecutionCourse.do?method=prepareCreateLessonPlanning&amp;page=0" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
				<bean:message key="link.create.lessonPlanning"/>
			</html:link>
		</li>
	</ul>

		
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
			<h3 class="mtop2"><span class="underline1"><bean:message key="label.lessons"/> <bean:message name="lessonPlannings" property="key.name" bundle="APPLICATION_RESOURCES"/></span></h3>
		</logic:notEmpty>
		<logic:iterate id="lessonPlanning" name="lessonPlannings" property="value" indexId="index">
		<div>
			<p class="mtop2 mbottom0"><em><bean:message key="label.lessonPlanning"/></em> <em><bean:write name="lessonPlanning" property="orderOfPlanning"/></em></p>
			<p class="mtop0 mbottom025"><b><bean:write name="lessonPlanning" property="title"/></b></p>
			<div class="mvert0"><bean:write name="lessonPlanning" property="planning" filter="false"/></div>							
			<p class="mtop05">
				<html:link page="<%= edit %>" paramId="lessonPlanningID" paramName="lessonPlanning" paramProperty="idInternal">
					<bean:message key="link.edit"/>
				</html:link>, 
				<html:link page="<%= delete %>" paramId="lessonPlanningID" paramName="lessonPlanning" paramProperty="idInternal">
					<bean:message key="link.delete"/>
				</html:link>&nbsp;&nbsp;
				<span style="color: #555;"><bean:message key="label.moveTo"/>: </span>
				<html:link page="<%= moveUp %>" paramId="lessonPlanningID" paramName="lessonPlanning" paramProperty="idInternal">
					<bean:message key="link.move.up"/>
				</html:link>, 
				<html:link page="<%= moveDown %>" paramId="lessonPlanningID" paramName="lessonPlanning" paramProperty="idInternal">
					<bean:message key="link.move.down"/>
				</html:link>
			</p>
		</div>
		</logic:iterate>				
	</logic:iterate>

</logic:present>