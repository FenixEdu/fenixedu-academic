<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="label.generateFiles"/></h2>
<p>
<b>
	<bean:define id="fileToGenerate" name="file"/>
	<logic:equal name="fileToGenerate" value="sibs">
		<bean:message key="label.generateFiles.SIBS" />
	</logic:equal>
	<logic:equal name="fileToGenerate" value="letters">
		<bean:message key="label.generateFiles.letters" />
	</logic:equal>
	<bean:message key="label.manager.generateFiles.confirmation"/>
</b>
<p>