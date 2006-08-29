<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>

<logic:present name="executionCourse">
			
	<bean:define id="showLessonPlannings">/manageExecutionCourse.do?method=lessonPlannings&executionCourseID=<bean:write name="executionCourse" property="idInternal"/></bean:define>	

	<logic:notEmpty name="lessonPlanningBean">
		<h2><bean:message key="link.create.lessonPlanning"/></h2>
		<bean:define id="createLessonPlanningPath">/manageExecutionCourse.do?method=createLessonPlanning&executionCourseID=<bean:write name="executionCourse" property="idInternal"/></bean:define>	
		<fr:edit id="lessonPlanningBeanID" name="lessonPlanningBean" action="<%= createLessonPlanningPath %>" type="net.sourceforge.fenixedu.dataTransferObject.gesdis.CreateLessonPlanningBean" 
			schema="CreateLessonPlanning">	
			<fr:destination name="cancel" path="<%= showLessonPlannings %>"/>
				<fr:layout name="tabular">
	    		    <fr:property name="classes" value="thtop thlight thright mbottom1"/>
			    </fr:layout>
		</fr:edit>
	</logic:notEmpty>

	<logic:empty name="lessonPlanningBean">
		<h2><bean:message key="link.edit.lessonPlanning"/></h2>		
		<fr:edit id="lessonPlanningBeanID_" name="lessonPlanning" action="<%= showLessonPlannings %>" type="net.sourceforge.fenixedu.domain.LessonPlanning" 
			schema="EditLessonPlanning">
			<fr:destination name="cancel" path="<%= showLessonPlannings %>"/>		
				<fr:layout name="tabular">
	    		    <fr:property name="classes" value="thtop thlight thright mbottom1"/>
			    </fr:layout>
		</fr:edit>
	</logic:empty>

</logic:present>