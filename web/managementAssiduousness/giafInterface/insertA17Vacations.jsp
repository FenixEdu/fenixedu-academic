<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="link.A17Vacations" /></h2>

<html:messages id="message">
	<p><span class="error0"><bean:write name="message" /></span></p>
</html:messages>


<logic:present name="giafInterfaceBean">
	<fr:edit id="giafInterfaceBean" name="giafInterfaceBean" 
	schema="edit.giafInterfaceBean.file" action="/giafInterface.do?method=insertA17Vacations">
	</fr:edit>
</logic:present>





<bean:message key="title.exportedFilesToGiaf"/>
