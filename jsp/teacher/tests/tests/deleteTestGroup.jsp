<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="f" %>

<bean:define id="testGroup" name="testGroup" type="net.sourceforge.fenixedu.domain.tests.NewTestGroup" />

<h3><bean:message key="message.tests.manage" bundle="TESTS_RESOURCES" /></h3>
<h2><bean:message key="title.deleteTestGroup" bundle="TESTS_RESOURCES" /></h2>

<h4><bean:message key="message.willDelete" bundle="TESTS_RESOURCES" /></h4>
<strong><fr:view name="testGroup" property="name" /></strong>

<ul>
	<li>
		<html:link page="/tests/tests.do?method=manageTests">
			<bean:message key="message.cancel" bundle="TESTS_RESOURCES" />
		</html:link>
	</li>
	<li>
		<html:link page="/tests/tests.do?method=deleteTestGroup"
		           paramId="oid" paramName="testGroup"
		           paramProperty="idInternal">
			<bean:message key="message.delete" bundle="TESTS_RESOURCES" />
		</html:link>
	</li>
</ul>
