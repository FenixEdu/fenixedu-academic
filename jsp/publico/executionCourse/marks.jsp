<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<h2><bean:message key="title.evaluation" /></h2>
<br/>
<bean:message key="classification.nonOfficial.information" />
<br/>
<br/>
<table class="tab_complex" cellspacing="1" cellpadding="2">
	<tr>
		<th nowrap>
			<bean:message key="label.number"/>
		</th>
		<th nowrap>
			<bean:message key="label.name"/>
		</th>
		<logic:iterate id="evaluation" name="executionCourse" property="orderedAssociatedEvaluations">
			<logic:present name="evaluation" property="publishmentMessage">
				<th nowrap>
					<logic:equal name="evaluation" property="class.name" value="net.sourceforge.fenixedu.domain.onlineTests.OnlineTest">
						<bean:write name="evaluation" property="distributedTest.title"/>
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
			</logic:present>
		</logic:iterate>
	</tr>
	<logic:iterate id="attendEntry" name="attendsMap">
		<bean:define id="attends" name="attendEntry" property="key"/>
		<tr>
			<td nowrap>
				<bean:write name="attends" property="registration.number"/>
			</td>
			<td nowrap>
				<bean:write name="attends" property="registration.person.name"/>
			</td>
			<logic:iterate id="evaluationEntry" name="attendEntry" property="value">
				<td nowrap>
				<logic:present name="evaluationEntry" property="value">
					<bean:define id="mark" name="evaluationEntry" property="value"/>
						<logic:present name="mark">
							<bean:write name="mark" property="publishedMark"/>
						</logic:present>
				</logic:present>
				</td>
			</logic:iterate>
		</tr>
	</logic:iterate>
</table>
