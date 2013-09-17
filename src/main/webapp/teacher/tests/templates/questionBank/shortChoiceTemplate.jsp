<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-template" prefix="ft" %>

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
