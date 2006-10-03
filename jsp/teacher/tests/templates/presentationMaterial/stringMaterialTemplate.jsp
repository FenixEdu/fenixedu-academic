<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-template.tld" prefix="ft" %>

<ft:define id="stringMaterial" type="net.sourceforge.fenixedu.domain.tests.NewStringMaterial" />

<logic:equal name="stringMaterial" property="inline" value="true">
	<ft:view property="text">
		<ft:layout name="html">
			<ft:property name="classes" value="dinline" />
		</ft:layout>
	</ft:view>
</logic:equal>

<logic:equal name="stringMaterial" property="inline" value="false">
	<ft:view property="text">
		<ft:layout name="html">
			<ft:property name="classes" value="dblock" />
		</ft:layout>
	</ft:view>
</logic:equal>
