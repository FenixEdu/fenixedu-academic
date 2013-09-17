<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-template" prefix="ft" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="f" %>

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
	<f:parameter id="oid" name="modelRestriction" property="externalId" />
	Cima
</f:parameterLink>
</logic:equal>
<logic:equal name="modelRestriction" property="first" value="true">Cima</logic:equal>
,
<logic:equal name="modelRestriction" property="last" value="false">
<f:parameterLink page="/tests/testModels.do?method=switchModelRestriction">
	<f:parameter id="relativePosition" value="1" />
	<f:parameter id="oid" name="modelRestriction" property="externalId" />
	Baixo
</f:parameterLink>)
</logic:equal>
<logic:equal name="modelRestriction" property="last" value="true">Baixo</logic:equal>
</td>
</tr>