<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-template.tld" prefix="ft" %>

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

