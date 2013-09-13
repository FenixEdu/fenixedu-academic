<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-template" prefix="ft" %>

<ft:view>
	<ft:layout name="summary" />
</ft:view>

<ft:define id="orderedChildQuestionGroups" property="orderedChildQuestionGroups" />

<logic:notEmpty name="orderedChildQuestionGroups">
	<ft:view property="orderedChildQuestionGroups">
		<ft:layout name="flowLayout">
			<ft:property name="eachLayout" value="template-tree" />
			<ft:property name="eachInline" value="false" />
			<ft:property name="eachStyle" value="margin-left: 2em; margin-top: 0.5em;" />
		</ft:layout>
	</ft:view>
</logic:notEmpty>
