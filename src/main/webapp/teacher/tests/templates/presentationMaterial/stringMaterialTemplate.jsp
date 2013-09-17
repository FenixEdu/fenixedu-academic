<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-template" prefix="ft" %>

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
