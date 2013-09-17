<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="f" %>

<h3><bean:message key="message.tests.manage" bundle="TESTS_RESOURCES" /></h3>
<h2><bean:message key="title.viewTest" bundle="TESTS_RESOURCES" /> <fr:view name="test" property="position" /></h2>

<ul>
	<li>
		<f:parameterLink page="/tests/tests.do?method=manageTests">
			<f:parameter id="oid" name="executionCourse" property="externalId" />
			<bean:message key="message.back" bundle="TESTS_RESOURCES" />
		</f:parameterLink>
	</li>
</ul>

<script type="text/javascript" language="javascript" src="<%= request.getContextPath() + "/javascript/tests/teacher.js" %>">
</script>

<bean:define id="test" name="test" type="net.sourceforge.fenixedu.domain.tests.NewTest" />

<strong><bean:message key="label.navigateVariations" bundle="TESTS_RESOURCES" />:</strong>
<fr:view name="test" schema="tests.test.navigation" layout="values" />

<fr:view name="test" layout="template-variations-tree" />

<script type="text/javascript" language="javascript">
switchGlobal();
</script>
