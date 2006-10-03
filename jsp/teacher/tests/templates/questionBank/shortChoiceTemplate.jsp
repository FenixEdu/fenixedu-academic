<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-template.tld" prefix="ft" %>

<ft:define id="choice" />

<div>
<!-- <div style="float: left; width: 20px;">
<strong>
<ft:view property="position" layout="short" />)
</strong> 
</div> -->

<div style="padding-left: 2em">
<ft:view property="orderedPresentationMaterials">
	<ft:layout name="flowLayout">
		<ft:property name="emptyMessageKey" value="tests.presentationMaterials.empty" />
		<ft:property name="emptyMessageClasses" value="emptyMessage" />
		<ft:property name="emptyMessageBundle" value="TESTS_RESOURCES" />
	</ft:layout>
</ft:view>
</div>
</div>
