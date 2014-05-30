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
<%@page import="org.fenixedu.bennu.core.security.Authenticate"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


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

<h3 class="mtop15 mbottom05"><bean:message key="title.thesis.details.orientation"/></h3>

<logic:notEmpty name="thesis" property="orientator">
	<h4 class="mtop2 mbottom05"><bean:message key="title.public.thesis.section.orientation.orientator"/></h4>
	<fr:view name="thesis" property="orientator" schema="thesis.jury.proposal.person">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thleft thlight thtop"/>
			<fr:property name="columnClasses" value="width10em, width50em"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:notEmpty name="thesis" property="coorientator">
	<h4 class="mtop2 mbottom05"><bean:message key="title.public.thesis.section.orientation.coorientator"/></h4>
	<fr:view name="thesis" property="coorientator" schema="thesis.jury.proposal.person">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thleft thlight thtop"/>
			<fr:property name="columnClasses" value="width10em, width50em"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<h3 class="mtop15 mbottom05"><bean:message key="title.thesis.details.publication"/></h3>

<logic:equal name="thesis" property="evaluated" value="true">

	<fr:view name="thesis" property="finalTitle"/>,
	<fr:view name="thesis" property="student.name"/>,
	<fr:view name="thesis" property="discussed.year"/>,

	<bean:define id="thesis" name="thesis" type="net.sourceforge.fenixedu.domain.thesis.Thesis"/>
	<p>
	<%
		if (thesis.getDissertation().isAccessible(Authenticate.getUser())) {
	%>
		(<bean:define id="extAbstractDownloadUrl" name="thesis" property="extendedAbstract.downloadUrl" type="java.lang.String"/>
		<html:link href="<%= extAbstractDownloadUrl %>">
			<html:img page="/images/icon_pdf.gif" module=""/>
			<bean:message bundle="RESEARCHER_RESOURCES" key="link.dissertation.download.extendedAbstract"/>
			<fr:view name="thesis" property="extendedAbstract.size" layout="fileSize"/>
		</html:link>

		<bean:define id="downloadUrl" name="thesis" property="dissertation.downloadUrl" type="java.lang.String"/>
		<html:link href="<%= downloadUrl %>">
			<html:img page="/images/icon_pdf.gif" module=""/>
			<bean:message bundle="RESEARCHER_RESOURCES" key="link.dissertation.download.thesis"/>
			<fr:view name="thesis" property="dissertation.size" layout="fileSize"/>
		</html:link>)
	<%
		} else {
	%>
		(<html:img page="/images/icon_pdf.gif" module=""/>
		<bean:message bundle="RESEARCHER_RESOURCES" key="link.dissertation.download.extendedAbstract"/>
		<fr:view name="thesis" property="extendedAbstract.size" layout="fileSize"/>

		<html:img page="/images/icon_pdf.gif" module=""/>
		<bean:message bundle="RESEARCHER_RESOURCES" key="link.dissertation.download.thesis"/>
		<fr:view name="thesis" property="dissertation.size" layout="fileSize"/>)
	<%
		}
	%>
	</p>
	<p>
		<bean:message bundle="RESEARCHER_RESOURCES" key="label.publication.subject.to.copyright"/>
	</p>
</logic:equal>

<logic:equal name="thesis" property="evaluated" value="false">
	<em><bean:message key="message.thesis.publication.notAvailable"/></em>
</logic:equal>
