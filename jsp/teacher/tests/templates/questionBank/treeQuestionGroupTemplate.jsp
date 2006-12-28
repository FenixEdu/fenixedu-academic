<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-template.tld" prefix="ft" %>

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
