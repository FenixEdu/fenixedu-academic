<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<bean:define id="listThesesActionPath" name="listThesesActionPath"/>
<bean:define id="listThesesContext" name="listThesesContext"/>

<h3 class="mtop15 mbottom05"><bean:message key="title.thesis.details.details"/></h3>

<fr:view name="thesis" schema="public.thesis.details">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thleft thlight thtop"/>
		<fr:property name="columnClasses" value="width10em, width50em"/>
	</fr:layout>
</fr:view>

<h3 class="mtop15 mbottom05"><bean:message key="title.thesis.details.coordination"/></h3>

<logic:notEmpty name="thesis" property="orientator">
	<fr:view name="thesis" property="orientator" schema="thesis.jury.proposal.person">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thleft thlight thtop"/>
			<fr:property name="columnClasses" value="width10em, width50em"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:notEmpty name="thesis" property="coorientator">
	<fr:view name="thesis" property="coorientator" schema="thesis.jury.proposal.person">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thleft thlight thtop"/>
			<fr:property name="columnClasses" value="width10em, width50em"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<h3 class="mtop15 mbottom05"><bean:message key="title.thesis.details.publication"/></h3>

<logic:notEmpty name="thesis" property="publication">

	<bean:define id="files" name="thesis" property="publication.resultDocumentFiles"/>

	<fr:view name="thesis" property="publication.title"/>,
	<fr:view name="thesis" property="publication.authorsNames"/>,
	<fr:view name="thesis" property="publication.year"/>,
	<fr:view name="thesis" property="publication.organization"/>
	
	<bean:define id="publicationId" name="thesis" property="publication.idInternal"/>
	(<html:link target="_blank" page="<%="/bibtexExport.do?method=exportPublicationToBibtex&publicationId="+ publicationId %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.exportToBibTeX" />
	</html:link><logic:iterate id="file" name="files" length="1">,
			<bean:define id="downloadUrl" name="file" property="downloadUrl" type="java.lang.String"/>
			
			<html:link href="<%= downloadUrl %>">
				<html:img page="/images/icon_pdf.gif" module=""/>
				<fr:view name="file" property="size" layout="fileSize"/>
			</html:link></logic:iterate>)
</logic:notEmpty>

<logic:empty name="thesis" property="publication">
	<em><bean:message key="message.thesis.publication.notAvailable"/></em>
</logic:empty>
