<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="f" %>

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
		           paramProperty="externalId">
			<bean:message key="message.delete" bundle="TESTS_RESOURCES" />
		</html:link>
	</li>
</ul>
