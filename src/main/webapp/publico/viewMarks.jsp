<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>

<h2><bean:message key="title.evaluation" /></h2>
<br/>
<bean:message key="classification.nonOfficial.information" />
<br/>
<br/>

<bean:define id="component" name="siteView" property="component"/>
<bean:define id="executionCourse" name="component" property="executionCourse"/>

<table class="tab_complex" cellspacing="1" cellpadding="2">
	<tr>
		<th nowrap>
			<bean:message key="label.number"/>
		</th>
		<th nowrap>
			<bean:message key="label.name"/>
		</th>
		<logic:iterate id="evaluation" name="executionCourse" property="orderedAssociatedEvaluations">
			<th nowrap>
				<logic:equal name="evaluation" property="class.name" value="net.sourceforge.fenixedu.domain.onlineTests.OnlineTest">
					<bean:write name="evaluation" property="distributedTest.evaluationTitle"/>
				</logic:equal>
				<logic:equal name="evaluation" property="class.name" value="net.sourceforge.fenixedu.domain.Project">
					<bean:write name="evaluation" property="name"/>
				</logic:equal>
				<logic:equal name="evaluation" property="class.name" value="net.sourceforge.fenixedu.domain.WrittenTest">
					<bean:write name="evaluation" property="description"/>
				</logic:equal>
				<logic:equal name="evaluation" property="class.name" value="net.sourceforge.fenixedu.domain.Exam">
					<bean:write name="evaluation" property="season"/>
				</logic:equal>
				<logic:equal name="evaluation" property="class.name" value="net.sourceforge.fenixedu.domain.FinalEvaluation">
					<bean:message key="label.final.evaluation"/>
				</logic:equal>
			</th>
		</logic:iterate>
	</tr>
	<logic:iterate id="attends" name="executionCourse" property="orderedAttends">
		<tr>
			<td nowrap>
				<bean:write name="attends" property="registration.number"/>
			</td>
			<td nowrap>
				<bean:write name="attends" property="registration.person.name"/>
			</td>
			<logic:iterate id="mark" name="attends" property="associatedMarksOrderedByEvaluationDate">
				<td nowrap>
					<logic:present name="mark">
						<bean:write name="mark" property="publishedMark"/>
					</logic:present>
				</td>
			</logic:iterate>
		</tr>
	</logic:iterate>
</table>
