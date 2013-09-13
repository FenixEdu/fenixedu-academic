<%@page import="net.sourceforge.fenixedu.domain.thesis.ThesisVisibilityType"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl"%><html:xhtml/>

<bean:define id="listThesesActionPath" name="listThesesActionPath"/>
<bean:define id="listThesesContext" name="listThesesContext"/>

<h3 class="mtop15 mbottom05"><bean:message key="title.thesis.details.details"/></h3>

<fr:view name="thesis" schema="public.thesis.details">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thleft thlight thtop"/>
		<fr:property name="columnClasses" value="width10em, width50em"/>
	</fr:layout>
</fr:view>

<h3 class="mtop15 mbottom05"><bean:message key="title.thesis.details.discussion"/></h3>

<fr:view name="thesis" schema="thesis.discussion.date">
	<fr:layout name="tabular">
	   	<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
	   	<fr:property name="columnClasses" value="width12em,,"/>
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
	<bean:define id="extAbstract" name="thesis" property="extendedAbstract"/>

	<fr:view name="thesis" property="publication.title"/>,
	<fr:view name="thesis" property="publication.authorsNames"/>,
	<fr:view name="thesis" property="publication.year"/>,
	<fr:view name="thesis" property="publication.organization"/>

	<bean:define id="thesis" name="thesis" type="net.sourceforge.fenixedu.domain.thesis.Thesis"/>
	<bean:define id="publicationId" name="thesis" property="publication.externalId"/>
	<p>
	<%
		if (thesis.getDissertation().isPersonAllowedToAccess(AccessControl.getPerson())) {
	%>
		(<html:link target="_blank" page="<%="/bibtexExport.do?method=exportPublicationToBibtex&publicationId="+ publicationId %>">
			<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.exportToBibTeX" />
		</html:link>,
		
		<bean:define id="extAbstractDownloadUrl" name="extAbstract" property="downloadUrl" type="java.lang.String"/>
		<html:link href="<%= extAbstractDownloadUrl %>">
			<html:img page="/images/icon_pdf.gif" module=""/>
			<bean:message bundle="RESEARCHER_RESOURCES" key="link.dissertation.download.extendedAbstract"/>
			<fr:view name="extAbstract" property="size" layout="fileSize"/>
		</html:link>
		
		<logic:iterate id="file" name="files" length="1">,
			<bean:define id="downloadUrl" name="file" property="downloadUrl" type="java.lang.String"/>	
			<html:link href="<%= downloadUrl %>">
				<html:img page="/images/icon_pdf.gif" module=""/>
				<bean:message bundle="RESEARCHER_RESOURCES" key="link.dissertation.download.thesis"/>
				<fr:view name="file" property="size" layout="fileSize"/>
			</html:link>
		</logic:iterate>)
	<%
		} else {
	%>
		(<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.exportToBibTeX" />,
		
		<html:img page="/images/icon_pdf.gif" module=""/>
		<bean:message bundle="RESEARCHER_RESOURCES" key="link.dissertation.download.extendedAbstract"/>
		<fr:view name="extAbstract" property="size" layout="fileSize"/>
		
		<logic:iterate id="file" name="files" length="1">,
		<bean:define id="downloadUrl" name="file" property="downloadUrl" type="java.lang.String"/>	
			<html:img page="/images/icon_pdf.gif" module=""/>
			<bean:message bundle="RESEARCHER_RESOURCES" key="link.dissertation.download.thesis"/>
			<fr:view name="file" property="size" layout="fileSize"/>
		</logic:iterate>)
	<%
		}
	%>
	</p>
		
</logic:notEmpty>

<logic:empty name="thesis" property="publication">
	<em><bean:message key="message.thesis.publication.notAvailable"/></em>
</logic:empty>
