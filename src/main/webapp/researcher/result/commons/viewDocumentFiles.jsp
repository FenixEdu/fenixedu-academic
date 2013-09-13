<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


	<bean:define id="documents" name="result" property="resultDocumentFiles"/>
	
	<logic:empty name="documents">
		<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultDocumentFiles.emptyList"/>.</em></p>
	</logic:empty>
	<logic:notEmpty name="documents">
		<fr:view name="documents" schema="resultDocumentFile.summary">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight"/>
				<fr:property name="columnClasses" value=",acenter,acenter,acenter"/>
				<fr:property name="sortBy" value="uploadTime=desc"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
