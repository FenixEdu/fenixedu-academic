<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<em><bean:message bundle="EXTERNAL_SUPERVISION_RESOURCES" key="externalSupervision"/></em>
<h2><bean:message bundle="EXTERNAL_SUPERVISION_RESOURCES" key="title.MA"/></h2>

<table>
	<tr>
		<td><p class="width400px"><br/><bean:message bundle="EXTERNAL_SUPERVISION_RESOURCES" key="greetings.MA"/></p></td>
		<td><img src="<%= request.getContextPath() + "/images/academia_militar1.gif" %>" class="width50pc"/></td>
	</tr>
</table>