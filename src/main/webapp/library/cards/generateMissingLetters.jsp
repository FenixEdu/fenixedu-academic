<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<h2>
	<bean:message key="title.library.card.generateMissingLetters"/>
</h2>

<p class="infoop2">
	<bean:message key="text.library.card.generateMissingLetters" bundle="LIBRARY_RESOURCES"/>
</p>

<p class="mtop1">
	<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
	<html:link page="/cardManagement.do?method=generateMissingLetters&amp;students=no">
		<bean:message key="link.card.generateMissingLetters" bundle="LIBRARY_RESOURCES"/>
	</html:link>
</p>

<logic:present name="nothingMissing">
	<p><em><bean:message key="message.card.noMissingLetters" bundle="LIBRARY_RESOURCES"/></em></p>
</logic:present>


<logic:present name="libraryMissingLetters">
		<h4 class="mtop15 mbottom05"><bean:message key="title.library.letter.history" bundle="LIBRARY_RESOURCES"/></h4>
		<fr:view name="libraryMissingLetters" schema="library.missingdocument.list">
		    <fr:layout name="tabular">
		    	<fr:property name="classes" value="tstyle1 thlight mtop05"/>
			</fr:layout>
		</fr:view>
</logic:present>


