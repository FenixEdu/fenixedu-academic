<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-template" prefix="ft" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="f" %>

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
