<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="label.generateFiles"/></h2>
<p>
<b>
	<bean:define id="fileToGenerate" name="file"/>
	<bean:write name="fileToGenerate" />
	
	<logic:present name="fileToGenerate">
		<logic:notEmpty name="fileToGenerate">
			<logic:equal name="fileToGenerate" value="sibs">
				<bean:message key="label.generateFiles.SIBS" />
			</logic:equal>
			<logic:equal name="fileToGenerate" value="letters">
				<bean:message key="label.generateFiles.letters" />
			</logic:equal>
		<logic:notEmpty>			
		<logic:empty name="fileToGenerate">
			<bean:message key="label.generateFiles.file" />
		</logic:empty>
	</logic:present>
	<logic:notPresent name="fileToGenerate">
		<bean:message key="label.generateFiles.file" />
	</logic:notPresent>
	<bean:message key="label.manager.generateFiles.confirmation"/>	
</b>
<p>