<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
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

<h3 class="mtop15 mbottom05"><bean:message key="title.thesis.details.orientation"/></h3>

<logic:notEmpty name="thesis" property="orientator">
	<h4 class="mtop2 mbottom05"><bean:message key="title.public.thesis.section.orientation.orientator"/></h4>
    <fr:view name="thesis" property="orientator" schema="thesis.jury.proposal.person">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle2 thwhite thnowrap thlight thleft thtop ulnomargin width100pc"/>
        <fr:property name="columnClasses" value="width10em,,"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>

<logic:notEmpty name="thesis" property="coorientator">
	<h4 class="mtop2 mbottom05"><bean:message key="title.public.thesis.section.orientation.coorientator"/></h4>
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

	<p>
		<bean:message bundle="RESEARCHER_RESOURCES" key="label.publication.subject.to.copyright"/>
	</p>
</logic:notEmpty>

<logic:empty name="thesis" property="publication">
    <em><bean:message key="message.thesis.publication.notAvailable"/></em>
</logic:empty>
