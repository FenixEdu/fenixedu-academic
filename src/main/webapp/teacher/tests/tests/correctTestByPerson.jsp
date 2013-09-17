<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="f" %>

<h3><bean:message key="message.tests.manage" bundle="TESTS_RESOURCES" /></h3>
<h2><fr:view name="test" property="testGroup.name" /></h2>

<script type="text/javascript" language="javascript" src="<%= request.getContextPath() + "/javascript/tests/teacher.js" %>">
</script>

<bean:define id="test" name="test" type="net.sourceforge.fenixedu.domain.tests.NewTest" />
<bean:define id="person" name="person" type="net.sourceforge.fenixedu.domain.Person" />
<% pageContext.setAttribute("correctableQuestionCount", test.getAllUncorrectedQuestionsCount(person)); %>

<ul>
	<li>
		<f:parameterLink page="/tests/tests.do?method=correctTestGroup">
			<f:parameter id="oid" name="test" property="testGroup.externalId" />
			<bean:message key="message.back" bundle="TESTS_RESOURCES" />
		</f:parameterLink>
	</li>
</ul>

<logic:equal name="correctableQuestionCount" value="0">
<p><span class="success5"><bean:message key="message.allCorrected" bundle="TESTS_RESOURCES" /></span></p>
</logic:equal>

<logic:notEqual name="correctableQuestionCount" value="0">
<div><p><span class="warning0"><strong><bean:message key="message.toCorrect" bundle="TESTS_RESOURCES" />:</strong> <fr:view name="correctableQuestionCount" /></span></p></div>
</logic:notEqual>

<fr:view name="test" layout="template-correctByPerson-tree" />

<script type="text/javascript" language="javascript">
switchGlobal();
</script>
