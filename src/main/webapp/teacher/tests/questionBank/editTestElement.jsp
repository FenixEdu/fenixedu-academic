<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h3><bean:message key="message.questionBank.manage" bundle="TESTS_RESOURCES" /></h3>

<script type="text/javascript" language="javascript" src="<%= request.getContextPath() + "/javascript/tests/teacher.js" %>">
</script>

<fr:view name="testElement" layout="details" />

<script type="text/javascript" language="javascript">
switchGlobal();
</script>
