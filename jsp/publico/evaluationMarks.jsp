<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<bean:define id="component" name="siteView" property="component"/>
<bean:define id="evaluation" name="component" property="evaluation"/>

<h2><bean:message key="title.evaluation" /></h2>
<br/>
<br/>
<bean:message key="classification.nonOfficial.information" />
<br/>
<br/>
<h3><bean:message key="label.publishedMarks"/>
<logic:equal name="evaluation" property="class.name" value="net.sourceforge.fenixedu.domain.onlineTests.OnlineTest">
	<bean:message key="label.online.test"/>: <bean:write name="evaluation" property="distributedTest.title"/>
</logic:equal>
<logic:equal name="evaluation" property="class.name" value="net.sourceforge.fenixedu.domain.Exam">
	<bean:message key="label.exam"/>: <bean:write name="evaluation" property="season"/>
</logic:equal>
<logic:equal name="evaluation" property="class.name" value="net.sourceforge.fenixedu.domain.WrittenTest">
	<bean:message key="label.written.test"/>: <bean:write name="evaluation" property="description"/>
</logic:equal>
<logic:equal name="evaluation" property="class.name" value="net.sourceforge.fenixedu.domain.FinalEvaluation">
	<bean:message key="label.final.evaluation"/>
</logic:equal>
</h3>

<table class="tab_complex" width="70%" cellspacing="1" cellpadding="2">
	<tr>
		<th><bean:message key="label.number"/></th>
		<th><bean:message key="label.name"/></th>
		<th><bean:message key="label.grade"/></th>
	</tr>
	<logic:iterate id="mark" name="component" property="sortedMarks">
		<tr>
			<td>
				<bean:write name="mark" property="attend.aluno.number"/>
			</td>
			<td>
				<bean:write name="mark" property="attend.aluno.person.nome"/>
			</td>
			<td>
				<bean:write name="mark" property="publishedMark"/>
			</td>
		</tr>
	</logic:iterate>
</table>