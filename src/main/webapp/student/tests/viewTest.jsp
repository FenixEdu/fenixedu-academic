<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h3><bean:message key="message.tests.student" bundle="TESTS_RESOURCES" /></h3>
<h2><fr:view name="test" property="testGroup.name" /></h2>

<span class="errors"><html:errors/></span>

<script type="text/javascript" language="javascript" src="<%= request.getContextPath() + "/javascript/tests/student.js" %>">
</script>

<logic:equal name="test" property="unansweredQuestionsCount" value="0">
	<p>
		<span class="success5">J� respondeu a todas as perguntas<br />Desistiu de <fr:view name="test" property="givenUpQuestionsCount" /></span>
	</p>
</logic:equal>

<logic:notEqual name="test" property="unansweredQuestionsCount" value="0">
<div class="infoop2">
<p>Consegue ver <fr:view name="test" property="visibleQuestionsValuesSum" />
valor(es) de <fr:view name="test" property="scale" />,
correspondente(s) a <fr:view name="test" property="visibleQuestionsCount" /> pergunta(s) vis�vel(eis)<br />
Existem ainda <fr:view name="test" property="unansweredQuestionsCount" /> pergunta(s) por responder</p>
</div>
</logic:notEqual>

<fr:view name="test" layout="template-student-tree" />

<script type="text/javascript" language="javascript">
switchGlobal();
</script>
