<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-template.tld" prefix="ft" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="f" %>

<ft:define id="modelRestriction" type="net.sourceforge.fenixedu.domain.tests.NewModelRestriction" />

<tr>
<td>
<span style="display: block; margin-left: <%= 2 * (modelRestriction.getPositionPath().size() - 1) %>em;">
<logic:equal name="modelRestriction" property="question.composite" value="true">
	<ft:view property="position" />) Grupo <ft:view property="question.name" />
</logic:equal>
<logic:equal name="modelRestriction" property="question.composite" value="false">
	<ft:view property="position" />) Pergunta
</logic:equal>
</span>
</td>

<td>
<logic:equal name="modelRestriction" property="first" value="false">
<f:parameterLink page="/tests/testModels.do?method=switchModelRestriction">
	<f:parameter id="relativePosition" value="-1" />
	<f:parameter id="oid" name="modelRestriction" property="idInternal" />
	Cima
</f:parameterLink>
</logic:equal>
<logic:equal name="modelRestriction" property="first" value="true">Cima</logic:equal>
,
<logic:equal name="modelRestriction" property="last" value="false">
<f:parameterLink page="/tests/testModels.do?method=switchModelRestriction">
	<f:parameter id="relativePosition" value="1" />
	<f:parameter id="oid" name="modelRestriction" property="idInternal" />
	Baixo
</f:parameterLink>)
</logic:equal>
<logic:equal name="modelRestriction" property="last" value="true">Baixo</logic:equal>
</td>
</tr>