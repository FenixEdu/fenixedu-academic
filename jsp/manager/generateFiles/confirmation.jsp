<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<h2><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.generateFiles"/></h2>
<p>
<b>
	<bean:define id="fileToGenerate" name="file"/>
	<logic:present name="fileToGenerate">
		<logic:notEmpty name="fileToGenerate">
			<logic:equal name="fileToGenerate" value="sibs">
				<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.generateFiles.SIBS" />
			</logic:equal>
			<logic:equal name="fileToGenerate" value="letters">
				<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.generateFiles.letters" />
			</logic:equal>
		</logic:notEmpty>			
		<logic:empty name="fileToGenerate">
			<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.generateFiles.file" />
		</logic:empty>
	</logic:present>
	<logic:notPresent name="fileToGenerate">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.generateFiles.file" />
	</logic:notPresent>
	<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.generateFiles.confirmation"/>	
</b>
<p>