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
<%@page import="net.sourceforge.fenixedu.domain.CurricularYear"%>
<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<h2><bean:message key="link.courses.management" bundle="SOP_RESOURCES"/></h2>

<logic:notEmpty name="courseLoadBean">
	<logic:notEmpty name="courseLoadBean" property="executionCourse">
		<bean:define id="executionCourse" name="courseLoadBean" property="executionCourse" toScope="request"/>
		<jsp:include page="contextExecutionCourse.jsp"/>		
	</logic:notEmpty>
</logic:notEmpty>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>

<p><span class="warning0"><bean:message key="label.manage.execution.course.note" bundle="SOP_RESOURCES"/></span></p>

<logic:messagesPresent message="true">
	<p>
		<span class="error0"><!-- Error messages go here -->
			<html:messages id="message" message="true">
				<bean:write name="message" filter="true"/>
			</html:messages>
		</span>
	</p>
</logic:messagesPresent>

<fr:hasMessages for="courseLoadBeanID" type="conversion">
	<p><span class="error0">			
		<fr:message for="courseLoadBeanID" show="message"/>
	</span></p>
</fr:hasMessages>	
	
<logic:notEmpty name="courseLoadBean">
	<logic:notEmpty name="courseLoadBean" property="executionCourse">
	
		<logic:notEmpty name="courseLoadBean" property="executionCourse.courseLoads">
			<fr:view name="courseLoadBean" property="executionCourse.courseLoads" schema="ExecutionCourseCourseLoadView">			
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 vamiddle thlight" />
					<fr:property name="columnClasses" value="acenter,acenter,acenter"/>
					
					<fr:property name="link(delete)" value="/manageExecutionCourse.do?method=deleteCourseLoad"/>
		            <fr:property name="param(delete)" value="externalId/courseLoadID"/>
			        <fr:property name="key(delete)" value="link.delete"/>
		            <fr:property name="bundle(delete)" value="SOP_RESOURCES"/>
		            <fr:property name="order(delete)" value="0"/>	            				
				</fr:layout>				
			</fr:view>
		</logic:notEmpty>
		
		<p class="mbottom05"><b><bean:message key="label.edit.executionCourse.course.load" bundle="SOP_RESOURCES"/></b></p>
		
		<fr:form action="/manageExecutionCourse.do?method=edit">
			<fr:edit name="courseLoadBean" id="courseLoadBeanID" schema="ExecutionCourseCourseLoadManagement">			 
				<fr:destination name="postBack" path="/manageExecutionCourse.do?method=preparePostBack"/> 
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 vamiddle thright thlight mtop05" />				
				</fr:layout>				
				<fr:destination name="invalid" path="/manageExecutionCourse.do?method=showCourseLoad"/>
			</fr:edit>
			<html:submit><bean:message key="label.submit"/></html:submit>
		</fr:form>

	</logic:notEmpty>
</logic:notEmpty>

<p class="mtop15 mbottom05"><bean:message key="label.execution.course.classes"/>:</p>
<logic:present name="<%= PresentationConstants.LIST_INFOCLASS %>" scope="request">
	<table class="tstyle2 tdcenter mtop05">
		<tr>
			<th>
				<bean:message key="label.degree.name"/>
			</th>
			<th>
				<bean:message key="label.name"/>
			</th>
		</tr>
		<logic:iterate id="infoClass" name="<%= PresentationConstants.LIST_INFOCLASS %>" scope="request">
			<bean:define id="classOID" name="infoClass" property="externalId"/>
			<bean:define id="curricularYearInt" type="java.lang.Integer" name="infoClass" property="anoCurricular"/>
			<bean:define id="executionDegreeOID" name="infoClass" property="infoExecutionDegree.externalId"/>
			<tr>
				<td>
					<bean:write name="infoClass" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.sigla"/>
				</td>
				<td>
					<html:link page="<%= "/manageClass.do?method=prepare&amp;"
							+ PresentationConstants.CLASS_VIEW_OID + "="
							+ classOID + "&amp;"
							+ PresentationConstants.ACADEMIC_INTERVAL + "="
							+ pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL) + "&amp;"
							+ PresentationConstants.CURRICULAR_YEAR_OID + "="
							+ CurricularYear.readByYear(curricularYearInt).getExternalId() + "&amp;"
							+ PresentationConstants.EXECUTION_DEGREE_OID	+ "="
							+ executionDegreeOID %>">
						<bean:write name="infoClass" property="nome"/>
					</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:present>

<logic:notPresent name="<%= PresentationConstants.LIST_INFOCLASS %>" scope="request">
	<span class="error"><!-- Error messages go here --><bean:message key="message.executionCourse.classes.none"/></span>
</logic:notPresent>