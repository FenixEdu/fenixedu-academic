<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<jsp:include page="/commons/renderers/treeRendererHeader.jsp" />

<h2>
	<bean:message key="title.unitSite.institutionSection" bundle="SITE_RESOURCES"/>
</h2>

<bean:define id="parentId" name="parent" property="externalId"/>
<bean:define id="siteId" name="site" property="externalId"/>

<logic:messagesPresent message="true">
    <div class="mvert15">
        <span class="error0">
            <html:messages id="error" message="true" bundle="APPLICATION_RESOURCES"> 
                <bean:write name="error"/>
            </html:messages>
        </span>
    </div>
</logic:messagesPresent>

<p class="mtop15 mbottom05"><bean:message key="label.unitSite.institutionSection.choose" bundle="SITE_RESOURCES"/>:</p>

<ul>
    <c:forEach var="section" items="${template.templatedSectionSet}">
        <li><html:link page="${actionName}?method=addFromPool&containerId=${parentId}&contentId=${section.externalId}&oid=${siteId}&sectionID=${parentId}&${context}">${section.name}</html:link></li>
    </c:forEach>
</ul>
