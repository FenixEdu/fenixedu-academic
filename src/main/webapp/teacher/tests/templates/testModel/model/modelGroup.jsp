<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-template" prefix="ft" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="f" %>

<ft:define id="modelGroup" type="net.sourceforge.fenixedu.domain.tests.NewModelGroup" />

<ft:view property="position" />) <ft:view schema="tests.modelGroup.name" layout="values" />
(<f:parameterLink page="/tests/testModels.do?method=deleteModelRestriction">
	<f:parameter id="returnTo" value="/tests/testModels.do?method=editTestModel" />
	<f:parameter id="oid" name="modelGroup" property="externalId" />
	Remover
</f:parameterLink>
,
<f:parameterLink page="/tests/testModels.do?method=editModelGroup">
	<f:parameter id="oid" name="modelGroup" property="externalId" />
	Editar
</f:parameterLink>)

<div>
<ft:view schema="tests.testElement.simple">
	<ft:layout name="tabular" >
		<ft:property name="style" value="width: 60em;" />
		<ft:property name="classes" value="tstyle1 thright" />
		<ft:property name="columnClasses" value="width10em," />
	</ft:layout>
</ft:view>


<logic:notEmpty name="modelGroup" property="orderedChildRestrictions">
	<ft:view property="orderedChildRestrictions">
		<ft:layout name="flowLayout">
			<ft:property name="style" value="margin-left: 2em; display: block;" />
			<ft:property name="eachLayout" value="template-model-tree" />
		</ft:layout>
	</ft:view>
</logic:notEmpty>
