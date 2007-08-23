<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>
<html:xhtml/>

<h2><bean:message key="label.schedule" /></h2>

<logic:notEmpty name="executionCourse" property="courseLoads">
	<fr:view name="executionCourse" property="courseLoads" schema="ExecutionCourseWeeklyCourseLoadView">			
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 vamiddle thlight" />
			<fr:property name="columnClasses" value="acenter,acenter"/>					      		
		</fr:layout>				
	</fr:view>
</logic:notEmpty>

<app:gerarHorario name="infoLessons" type="<%= TimeTableType.EXECUTION_COURSE_TIMETABLE %>"/>
