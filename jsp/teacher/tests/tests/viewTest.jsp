<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="f" %>

<h3><bean:message key="message.tests.manage" bundle="TESTS_RESOURCES" /></h3>
<h2><bean:message key="title.viewTest" bundle="TESTS_RESOURCES" /> <fr:view name="test" property="position" /></h2>

<ul>
	<li>
		<f:parameterLink page="/tests/tests.do?method=manageTests">
			<f:parameter id="oid" name="executionCourse" property="idInternal" />
			<bean:message key="message.back" bundle="TESTS_RESOURCES" />
		</f:parameterLink>
	</li>
</ul>

<script type="text/javascript" language="javascript" src="tests.js">
</script>

<bean:define id="test" name="test" type="net.sourceforge.fenixedu.domain.tests.NewTest" />

<strong><bean:message key="label.navigateVariations" bundle="TESTS_RESOURCES" />:</strong>
<fr:view name="test" schema="tests.test.navigation" layout="values" />

<fr:view name="test" layout="template-variations-tree" />

<script type="text/javascript" language="javascript">
switchGlobal();
</script>
