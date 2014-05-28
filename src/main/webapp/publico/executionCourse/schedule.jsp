<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
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