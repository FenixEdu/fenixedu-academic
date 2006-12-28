<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h3><bean:message key="message.tests.manage" bundle="TESTS_RESOURCES" /></h3>
<h2><bean:message key="title.manageTests" bundle="TESTS_RESOURCES" /><h2>

<script type="text/javascript" language="javascript" src="tests.js">
</script>

<h4><bean:message key="label.generatedTests" bundle="TESTS_RESOURCES" /></h4>

<logic:notEmpty name="testGroups">
<fr:view name="testGroups" schema="tests.testGroup.manage">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1" />
		
		<fr:property name="link(publish)" value="/tests/tests.do?method=publishTestGroup" />
		<fr:property name="param(publish)" value="idInternal/oid" />
		<fr:property name="key(publish)" value="message.publish" />
		<fr:property name="bundle(publish)" value="TESTS_RESOURCES" />
		<fr:property name="order(publish)" value="1" />
		<fr:property name="visibleIf(publish)" value="publishable" />
		
		<fr:property name="link(unpublish)" value="/tests/tests.do?method=unpublishTestGroup" />
		<fr:property name="param(unpublish)" value="idInternal/oid" />
		<fr:property name="key(unpublish)" value="message.unpublish" />
		<fr:property name="bundle(unpublish)" value="TESTS_RESOURCES" />
		<fr:property name="order(unpublish)" value="2" />
		<fr:property name="visibleIf(unpublish)" value="unpublishable" />
		
		<fr:property name="link(delete)" value="/tests/tests.do?method=prepareDeleteTestGroup" />
		<fr:property name="param(delete)" value="idInternal/oid" />
		<fr:property name="key(delete)" value="message.delete" />
		<fr:property name="bundle(delete)" value="TESTS_RESOURCES" />
		<fr:property name="order(delete)" value="3" />
		<fr:property name="visibleIf(delete)" value="deletable" />
		
		<fr:property name="link(finish)" value="/tests/tests.do?method=finishTestGroup" />
		<fr:property name="param(finish)" value="idInternal/oid" />
		<fr:property name="key(finish)" value="message.finish" />
		<fr:property name="bundle(finish)" value="TESTS_RESOURCES" />
		<fr:property name="order(finish)" value="4" />
		<fr:property name="visibleIf(finish)" value="finishable" />
		
		<fr:property name="link(correct)" value="/tests/tests.do?method=correctTestGroup" />
		<fr:property name="param(correct)" value="idInternal/oid" />
		<fr:property name="key(correct)" value="message.correct" />
		<fr:property name="bundle(correct)" value="TESTS_RESOURCES" />
		<fr:property name="order(correct)" value="5" />
		<fr:property name="visibleIf(correct)" value="correctable" />
		
		<fr:property name="sortBy" value="name" />
		<fr:property name="groupLinks" value="true" />
		<fr:property name="linkGroupSeparator" value=", " />
		
		<fr:property name="columnClasses" value=",,acenter,acenter,acenter,acenter,," />
	</fr:layout>
</fr:view>
</logic:notEmpty>
<logic:empty name="testGroups">
	<bean:message key="tests.testGroups.empty" bundle="TESTS_RESOURCES" />
</logic:empty>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>
