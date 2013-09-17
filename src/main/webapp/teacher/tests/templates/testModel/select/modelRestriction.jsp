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
<logic:empty name="modelRestriction" property="value">
	<bean:message key="label.atomicQuestion.grade.null" bundle="TESTS_RESOURCES" />
</logic:empty>
<logic:notEmpty name="modelRestriction" property="value">
	(<ft:view property="value" />/<ft:view property="testModel.scale" />)
</logic:notEmpty>
</td>
</tr>