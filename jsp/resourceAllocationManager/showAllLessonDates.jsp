<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants"%>
<html:xhtml/>

<h2><bean:message key="title.show.all.lesson.dates" bundle="SOP_RESOURCES"/></h2>

<logic:present role="RESOURCE_ALLOCATION_MANAGER">
			
	<bean:define id="parameters"><%=SessionConstants.LESSON_OID%>=<bean:write name="lesson_" property="idInternal"/>&amp;<%=SessionConstants.SHIFT_OID%>=<bean:write name="shift" property="idInternal"/>&amp;<%=SessionConstants.EXECUTION_COURSE_OID%>=<bean:write name="execution_course" property="idInternal"/>&amp;<%=SessionConstants.EXECUTION_PERIOD_OID%>=<bean:write name="execution_period" property="idInternal"/>&amp;<%=SessionConstants.CURRICULAR_YEAR_OID%>=<bean:write name="curricular_year" property="idInternal"/>&amp;<%=SessionConstants.EXECUTION_DEGREE_OID%>=<bean:write name="execution_degree" property="idInternal"/></bean:define>	
	<bean:define id="linkToReturn">/manageShift.do?method=prepareEditShift&amp;page=0&amp;<bean:write name="parameters" filter="false"/></bean:define>
	<bean:define id="linkToCreateNewLessonInstance">/manageLesson.do?method=prepareCreateNewLessonInstance&amp;page=0&amp;<bean:write name="parameters" filter="false"/></bean:define>
	
	<p class="mtop20">
		<ul class="mvert">
			<li>						
				<html:link page="<%= linkToReturn %>">
					<bean:message key="link.return"/>
				</html:link>		
			</li>			
		</ul>	
	</p>
				
	<%-- Delete Lesson Instances --%>		
	<bean:define id="linkToDelete">/manageLesson.do?method=deleteLessonInstance&amp;<bean:write name="parameters" filter="false"/></bean:define>				
	<fr:view name="lessonDates" schema="LessonDatesList">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 mtop025 mbottom0 tdcenter"/>
			<fr:property name="columnClasses" value=",,,,,smalltxt,,"/>
			<fr:property name="link(delete)" value="<%= linkToDelete %>"/>
	        <fr:property name="param(delete)" value="checkBoxValue/lessonDate"/>
	        <fr:property name="key(delete)" value="label.delete"/>
	        <fr:property name="order(delete)" value="0"/>
	        <fr:property name="visibleIf(delete)" value="isPossibleDeleteLessonInstance"/>	        
		</fr:layout>
	</fr:view>	
		
</logic:present>