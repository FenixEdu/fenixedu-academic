<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-template" prefix="ft" %>

<ft:define id="mathMlMaterial" type="net.sourceforge.fenixedu.domain.tests.NewMathMlMaterial" />

<logic:equal name="mathMlMaterial" property="inline" value="true">
	<ft:view layout="equation" />
</logic:equal>

<logic:equal name="mathMlMaterial" property="inline" value="false">
	<ft:view>
		<ft:layout name="equation">
			<ft:property name="classes" value="dblock" />
		</ft:layout>
	</ft:view>
</logic:equal>

