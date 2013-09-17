<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>
<html:xhtml/>

<h2><bean:message key="label.schedule"/></h2>

<logic:present name="infoLessons">
	<logic:notEmpty name="executionCourse" property="courseLoads">
		<fr:view name="executionCourse" property="courseLoads" schema="ExecutionCourseWeeklyCourseLoadView">			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thwhite vamiddle tdcenter mtop05" />
				<fr:property name="columnClasses" value="aleft,acenter" />
			</fr:layout>				
		</fr:view>
	</logic:notEmpty>

	<div class="mtop15">
		<app:gerarHorario name="infoLessons" type="<%= TimeTableType.EXECUTION_COURSE_TIMETABLE %>"/>
	</div>
</logic:present>	
	
<logic:notPresent name="infoLessons">
	<h3><bean:message key="info.cannot.view.schedule"/></h3>
</logic:notPresent>