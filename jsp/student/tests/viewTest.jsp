<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h3><bean:message key="message.tests.student" bundle="TESTS_RESOURCES" /></h3>
<h2><fr:view name="test" property="testGroup.name" /></h2>

<span class="errors"><html:errors/></span>

<script type="text/javascript" language="javascript" src="tests.js">
</script>

<logic:equal name="test" property="unansweredQuestionsCount" value="0">
	<p>
		<span class="success5">Já respondeu a todas as perguntas<br />Desistiu de <fr:view name="test" property="givenUpQuestionsCount" /></span>
	</p>
</logic:equal>

<logic:notEqual name="test" property="unansweredQuestionsCount" value="0">
<div class="infoop2">
<p>Consegue ver <fr:view name="test" property="visibleQuestionsValuesSum" />
valor(es) de <fr:view name="test" property="scale" />,
correspondente(s) a <fr:view name="test" property="visibleQuestionsCount" /> pergunta(s) visível(eis)<br />
Existem ainda <fr:view name="test" property="unansweredQuestionsCount" /> pergunta(s) por responder</p>
</div>
</logic:notEqual>

<fr:view name="test" layout="template-student-tree" />

<script type="text/javascript" language="javascript">
switchGlobal();
</script>
