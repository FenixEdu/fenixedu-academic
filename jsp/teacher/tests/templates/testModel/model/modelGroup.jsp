<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-template.tld" prefix="ft" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="f" %>

<ft:define id="modelGroup" type="net.sourceforge.fenixedu.domain.tests.NewModelGroup" />

<ft:view property="position" />) <ft:view schema="tests.modelGroup.name" layout="values" />
(<f:parameterLink page="/tests/testModels.do?method=deleteModelRestriction">
	<f:parameter id="returnTo" value="/tests/testModels.do?method=editTestModel" />
	<f:parameter id="oid" name="modelGroup" property="idInternal" />
	Remover
</f:parameterLink>
,
<f:parameterLink page="/tests/testModels.do?method=editModelGroup">
	<f:parameter id="oid" name="modelGroup" property="idInternal" />
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
