<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="link.writtenEvaluationManagement" bundle="SOP_RESOURCES"/></em>
<h2><bean:message key="link.courses.management" bundle="SOP_RESOURCES"/></h2>

<logic:notEmpty name="courseLoadBean">
	<logic:notEmpty name="courseLoadBean" property="executionCourse">
		<bean:define id="executionCourse"name="courseLoadBean" property="executionCourse" toScope="request"/>
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
		            <fr:property name="param(delete)" value="idInternal/courseLoadID"/>
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
<logic:present name="<%= SessionConstants.LIST_INFOCLASS %>" scope="request">
	<table class="tstyle2 tdcenter mtop05">
		<tr>
			<th>
				<bean:message key="label.degree.name"/>
			</th>
			<th>
				<bean:message key="label.name"/>
			</th>
		</tr>
		<logic:iterate id="infoClass" name="<%= SessionConstants.LIST_INFOCLASS %>" scope="request">
			<bean:define id="classOID" name="infoClass" property="idInternal"/>
			<bean:define id="curricularYearOID" name="infoClass" property="anoCurricular"/>
			<bean:define id="executionDegreeOID" name="infoClass" property="infoExecutionDegree.idInternal"/>
			<tr>
				<td>
					<bean:write name="infoClass" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.sigla"/>
				</td>
				<td>
					<html:link page="<%= "/manageClass.do?method=prepare&amp;"
							+ SessionConstants.CLASS_VIEW_OID + "="
							+ pageContext.findAttribute("classOID")	+ "&amp;"
							+ SessionConstants.EXECUTION_PERIOD_OID + "="
							+ pageContext.findAttribute("executionPeriodOID") + "&amp;"
							+ SessionConstants.CURRICULAR_YEAR_OID + "="
							+ pageContext.findAttribute("curricularYearOID") + "&amp;"
							+ SessionConstants.EXECUTION_DEGREE_OID	+ "="
							+ pageContext.findAttribute("executionDegreeOID") %>">
						<bean:write name="infoClass" property="nome"/>
					</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:present>

<logic:notPresent name="<%= SessionConstants.LIST_INFOCLASS %>" scope="request">
	<span class="error"><!-- Error messages go here --><bean:message key="message.executionCourse.classes.none"/></span>
</logic:notPresent>