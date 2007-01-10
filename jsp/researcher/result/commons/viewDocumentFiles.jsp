<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="documents" name="result" property="resultDocumentFiles"/>
	
	<logic:empty name="documents">
		<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultDocumentFiles.emptyList"/></em></p>
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
</logic:present>