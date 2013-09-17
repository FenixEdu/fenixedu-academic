<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

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

	<logic:present name="executionCourses">
		<br/>
		<table><tr>
			<td>
				<fr:form action="/executionCourseInquiries.do">
					<fr:edit id="executionCourseSearchBean2" name="executionCourseSearchBean" visible="false" />
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="selectAll"/>
					<html:submit><bean:message key="button.selectAll"/></html:submit>
				</fr:form>
			</td>
			<td>
				<fr:form action="/executionCourseInquiries.do">
					<fr:edit id="executionCourseSearchBean2" name="executionCourseSearchBean" visible="false" />
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="unselectAll"/>
					<html:submit><bean:message key="button.selectNone"/></html:submit>
				</fr:form>
			</td>
		</tr></table>		
	</logic:present>

</logic:present>
