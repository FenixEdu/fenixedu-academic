<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h3>
	<bean:message key="link.inquiries.execution.course.define.available.for.evaluation" bundle="INQUIRIES_RESOURCES"/>
</h3>

<span class="error"><!-- Error messages go here --><html:errors/><br /></span>

<logic:present name="executionCourseSearchBean">
	<fr:form action="/executionCourseInquiries.do">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="search"/>

		<fr:edit id="executionCourseSearchBean" name="executionCourseSearchBean" schema="net.sourceforge.fenixedu.domain.executionCourse.ExecutionCourseSearchBean" >
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle1"/>
	        	<fr:property name="columnClasses" value=",,noborder"/>
			</fr:layout>
		</fr:edit>
		<html:submit><bean:message key="button.submit"/></html:submit>
		<br/>

		<logic:present name="executionCourses">
			<fr:edit id="executionCourses" name="executionCourses" schema="net.sourceforge.fenixedu.domain.ExecutionCourse.ForInquiryEvaluationDefinition" >
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="tstyle1"/>
	    	    	<fr:property name="columnClasses" value=",,noborder"/>
				</fr:layout>
			</fr:edit>
			<html:submit><bean:message key="button.submit"/></html:submit>
		</logic:present>

	</fr:form>
</logic:present>
