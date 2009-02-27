<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<h2>
	<bean:message key="title.library.card.generateMissingCards"/>
</h2>	

<p class="infoop2">
	<bean:message key="card.message.help.generateMissingCards" bundle="LIBRARY_RESOURCES"/>
</p>

<p class="mtop1">
	<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
	<html:link page="/cardManagement.do?method=generateMissingCards">
		<bean:message key="card.link.generateMissingCards" bundle="LIBRARY_RESOURCES"/>
	</html:link>
</p>

<logic:present name="nothingMissing">
	<p><em><bean:message key="card.message.feedback.noMissingCards" bundle="LIBRARY_RESOURCES"/></em></p>
</logic:present>


<logic:present name="libraryMissingCards">
	<h4 class="mtop15 mbottom05"><bean:message key="title.library.card.history" bundle="LIBRARY_RESOURCES"/></h4>
	<fr:view name="libraryMissingCards" schema="library.missingdocument.list">
	    <fr:layout name="tabular">
	    	<fr:property name="classes" value="tstyle1 thlight mtop05"/>
		</fr:layout>
	</fr:view>
</logic:present>

		
