<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-template.tld" prefix="ft" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="f" %>

<ft:define id="testModel" type="net.sourceforge.fenixedu.domain.tests.NewTestModel" />

<table>
<tr>
	<td colspan="2">
		<strong>Modelo: <ft:view schema="tests.testModel.name.view" layout="values" /></strong>
	</td>
</tr>

<ft:view property="orderedChildRestrictions">
	<ft:layout name="flowLayout">
		<ft:property name="eachLayout" value="template-sort-tree" />
	</ft:layout>
</ft:view>
</table>
