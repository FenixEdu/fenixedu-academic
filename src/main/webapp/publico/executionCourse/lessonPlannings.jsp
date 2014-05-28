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
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>

<logic:present name="executionCourse">
	<h2><bean:message key="link.lessonPlannings"/></h2>
	<logic:empty name="lessonPlanningsMap">
		<p><em><bean:message key="label.lessonPlannings.not.available"/></em></p>
	</logic:empty>			
	<logic:iterate id="lessonPlannings" name="lessonPlanningsMap">		
		<logic:notEmpty name="lessonPlannings" property="value">
			<h3 class="arrow_bullet mtop2 greytxt"><bean:message key="label.lessons"/> <bean:message name="lessonPlannings" property="key.name" bundle="DEFAULT"/></h3>
		</logic:notEmpty>

		<logic:iterate id="lessonPlanning" name="lessonPlannings" property="value" indexId="index">
			<p class="mtop15 mbottom0"><em><bean:message key="label.lesson"/> <bean:write name="lessonPlanning" property="orderOfPlanning"/></em></p>
			
			<div style="line-height: 1.5em;">
			<fr:view name="lessonPlanning" schema="ViewLessonPlanning">
				<fr:layout name="flow">
					<fr:property name="classes" value="coutput2"/>
					<fr:property name="labelTerminator" value=""/>
					<fr:property name="labelExcluded" value="true"/>
					<fr:property name="eachClasses" value="bold," />
					<fr:property name="eachInline" value="false" />
				</fr:layout>
			</fr:view>	
			</div>

		</logic:iterate>				
	</logic:iterate>

</logic:present>


