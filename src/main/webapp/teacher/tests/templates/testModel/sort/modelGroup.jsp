<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-template" prefix="ft" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="f" %>

<ft:define id="modelGroup" type="net.sourceforge.fenixedu.domain.tests.NewModelGroup" />

<tr>
<td>
<span style="display: block; margin-left: <%= 2 * (modelGroup.getPositionPath().size() - 1) %>em;">
<ft:view property="position" />) <strong><ft:view schema="tests.modelGroup.name" layout="values" /></strong>
</span>
</td>

<td>
<logic:equal name="modelGroup" property="first" value="false">
<f:parameterLink page="/tests/testModels.do?method=switchModelRestriction">
	<f:parameter id="relativePosition" value="-1" />
	<f:parameter id="oid" name="modelGroup" property="externalId" />
	Cima
</f:parameterLink>
</logic:equal>
<logic:equal name="modelGroup" property="first" value="true">Cima</logic:equal>
,
<logic:equal name="modelGroup" property="last" value="false">
<f:parameterLink page="/tests/testModels.do?method=switchModelRestriction">
	<f:parameter id="relativePosition" value="1" />
	<f:parameter id="oid" name="modelGroup" property="externalId" />
	Baixo
</f:parameterLink>)
</logic:equal>
<logic:equal name="modelGroup" property="last" value="true">Baixo</logic:equal>
</td>
</tr>

<logic:notEmpty name="modelGroup" property="orderedChildRestrictions">
	<ft:view property="orderedChildRestrictions">
		<ft:layout name="flowLayout">
			<ft:property name="eachLayout" value="template-sort-tree" />
		</ft:layout>
	</ft:view>
</logic:notEmpty>
