<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="delegateSchema" name="delegateSchema" type="java.lang.String"/>
<fr:view name="delegateBean" layout="tabular" schema="<%= delegateSchema %>">
	<fr:layout>
		<fr:property name="classes" value="tstyle2 thlight thright tdleft mtop0 mbottom05"/>
		<fr:property name="rowClasses" value="bold,,,"/>
		<fr:property name="columnClasses" value="nowrap, width200px nowrap,"/>
	</fr:layout>
</fr:view>

<bean:define id="roleSchema" name="roleSchema" type="java.lang.String"/>
<p class="mtop2 mbottom1">
	<b><bean:message key="label.delegates.foundDelegatesRoles" bundle="DELEGATES_RESOURCES" /></b></p>	
<fr:view name="delegates" schema="<%= roleSchema %>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thcenter tdcenter mtop0"/>
		<fr:property name="columnClasses" value="aleft nowrap,aleft nowrap,,,"/>
	</fr:layout>
</fr:view>