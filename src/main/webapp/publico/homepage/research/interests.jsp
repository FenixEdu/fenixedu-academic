<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h1><bean:message key="researcher.interests.title.complete" bundle="RESEARCHER_RESOURCES"/></h1>

<logic:notEmpty name="interests">
	   
		<fr:view name="interests" >
			<fr:layout>
				<fr:property name="sortBy" value="order"/>
				<fr:property name="eachLayout" value="values"/>
				<fr:property name="eachSchema" value="researchInterest.title"/>
			</fr:layout>
		</fr:view>
</logic:notEmpty>