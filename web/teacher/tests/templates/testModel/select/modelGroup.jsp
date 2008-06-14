<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-template.tld" prefix="ft" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="f" %>

<ft:define id="modelGroup" type="net.sourceforge.fenixedu.domain.tests.NewModelGroup" />

<tr>
<td>
<span style="display: block; margin-left: <%= 2 * (modelGroup.getPositionPath().size() - 1) %>em;">
<ft:view property="position" />) <strong><ft:view schema="tests.modelGroup.name" layout="values" /></strong>
</span>
</td>

<td>
<logic:empty name="modelGroup" property="value">
<bean:message key="label.atomicQuestion.grade.null" bundle="TESTS_RESOURCES" />
</logic:empty>
<logic:notEmpty name="modelGroup" property="value">
(<ft:view property="value" />/<ft:view property="testModel.scale" />)
</logic:notEmpty>
</td>
</tr>

<logic:empty name="modelGroup" property="orderedChildRestrictions">
	<tr><td colspan="2">
		<span style="display: block; margin-left: <%= 2 * modelGroup.getPositionPath().size() %>em;">
			<bean:message key="tests.questions.empty" bundle="TESTS_RESOURCES" />
		</span>
	</td></tr>
</logic:empty>
<logic:notEmpty name="modelGroup" property="orderedChildRestrictions">
	<ft:view property="orderedChildRestrictions">
		<ft:layout name="flowLayout">
			<ft:property name="eachLayout" value="template-select-tree" />
		</ft:layout>
	</ft:view>
</logic:notEmpty>
