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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>
<bean:define id="site" name="actual$site" toScope="request"/>
<bean:define id="degree" name="site" property="degree" toScope="request"/>

<bean:define id="institutionUrl" type="java.lang.String"><%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %></bean:define>
<bean:define id="degreeId" name="degree" property="externalId"/>

<div class="breadcumbs mvert0">
    <a href="<%= institutionUrl %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a>
    <bean:define id="institutionUrlTeaching" type="java.lang.String"><%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %><bean:message key="link.institution" bundle="GLOBAL_RESOURCES"/></bean:define>
    &nbsp;&gt;&nbsp;
    <a href="<%=institutionUrlTeaching%>"><bean:message  bundle="PUBLIC_DEGREE_INFORMATION" key="public.degree.information.label.education"/></a>
        
    &nbsp;&gt;&nbsp;
    <html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;degreeID=" + degreeId %>">
        <bean:write name="degree" property="sigla"/>
    </html:link>

    <logic:present name="sectionCrumbs">
        <logic:iterate id="crumbSection" name="sectionCrumbs">
            &nbsp;&gt;&nbsp;
            <bean:define id="crumbSectionId" name="crumbSection" property="externalId"/>
            <html:link page="<%= String.format("/showDegreeSiteContent.do?method=section&amp;degreeID=%s&amp;sectionID=%s", degreeId, crumbSectionId) %>">
                <fr:view name="crumbSection" property="name"/>
            </html:link>
        </logic:iterate>

        <logic:notPresent name="item">
            &nbsp;&gt;&nbsp;
            <fr:view name="section" property="name"/>
        </logic:notPresent>
    </logic:present>
    
    <logic:present name="item">
        &nbsp;&gt;&nbsp;
        <fr:view name="item" property="name"/>
    </logic:present>
</div>

<!-- COURSE NAME -->
<h1 class="mbottom15">
	<logic:notEmpty name="degree" property="phdProgram">
		<bean:write name="degree" property="phdProgram.presentationName"/>
	</logic:notEmpty>
	<logic:empty name="degree" property="phdProgram">
		<bean:write name="degree" property="presentationName"/>
	</logic:empty>
</h1>

<bean:define id="site" name="degree" property="site" toScope="request"/>
<bean:define id="siteActionName" value="/showDegreeSiteContent.do" toScope="request"/>
<bean:define id="siteContextParam" value="degreeID" toScope="request"/>
<bean:define id="siteContextParamValue" name="degree" property="externalId" toScope="request"/>
