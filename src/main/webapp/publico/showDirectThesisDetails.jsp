<%@ page language="java" %>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<h2><bean:message key="public.degree.information.label.thesis"  bundle="PUBLIC_DEGREE_INFORMATION" /></h2>

<h3 class="mtop15 mbottom05"><bean:message key="title.thesis.details.details"/></h3>

<fr:view name="thesis" schema="public.thesis.details">
    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle2 thwhite thnowrap thlight thleft thtop ulnomargin width100pc"/>
        <fr:property name="columnClasses" value="width10em,,"/>
        <fr:property name="rowClasses" value=",,bold,,,,"/>
    </fr:layout>
</fr:view>

<h3 class="mtop15 mbottom05"><bean:message key="title.thesis.details.discussion"/></h3>

<fr:view name="thesis" schema="thesis.discussion.date">
    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle2 thwhite thnowrap thlight thleft thtop ulnomargin width100pc"/>
        <fr:property name="columnClasses" value="width10em,,"/>
    </fr:layout>
</fr:view>

<h3 class="mtop15 mbottom05"><bean:message key="title.thesis.details.coordination"/></h3>

<logic:notEmpty name="thesis" property="orientator">
    <fr:view name="thesis" property="orientator" schema="thesis.jury.proposal.person">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle2 thwhite thnowrap thlight thleft thtop ulnomargin width100pc"/>
        <fr:property name="columnClasses" value="width10em,,"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>

<logic:notEmpty name="thesis" property="coorientator">
    <fr:view name="thesis" property="coorientator" schema="thesis.jury.proposal.person">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle2 thwhite thnowrap thlight thleft thtop ulnomargin width100pc"/>
        <fr:property name="columnClasses" value="width10em,,"/>
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
    
    <bean:define id="publicationId" name="thesis" property="publication.externalId"/>
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
