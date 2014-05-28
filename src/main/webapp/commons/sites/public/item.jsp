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

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<logic:present name="item">
    <bean:define id="section" name="section" type="net.sourceforge.fenixedu.domain.Section"/>
    <bean:define id="item" name="item" type="net.sourceforge.fenixedu.domain.Item"/>

    <h2>
        <fr:view name="section" property="name" type="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString"/>
  		<span class="permalink1">(<a href="${section.fullPath}"><bean:message key="label.link" bundle="SITE_RESOURCES"/></a>)</span>
    </h2>

		<logic:equal name="section" property="showSubSectionTree" value="true">
				<ul>
					<logic:iterate id="section" name="section" property="orderedVisibleSubSections" type="net.sourceforge.fenixedu.domain.Section">		
							<li>
								<a href="${section.fullPath}">${section.name}</a>
							</li>
						</logic:iterate>
				</ul>
		</logic:equal>
   
    <bean:define id="item" name="item" type="net.sourceforge.fenixedu.domain.Item"/>
            
	<h3 class="mtop2 mbottom05">
        <fr:view name="item" property="name"/>
  		<span class="permalink1">(<a href="${item.fullPath}"><bean:message key="label.link" bundle="SITE_RESOURCES"/></a>)</span>
    </h3>

	<logic:equal name="itemAvailable" value="true">
	    <logic:notEmpty name="item" property="body">
	        	<fr:view name="item" property="body">
	        		<fr:layout>
	        			<fr:property name="escaped" value="false" />
	        			<fr:property name="newlineAware" value="false" />
	        		</fr:layout>
	        	</fr:view>
		</logic:notEmpty>
	    
		<logic:notEmpty name="item" property="visibleFiles">
	        <fr:view name="item" property="visibleFiles">
	            <fr:layout name="list">
	                <fr:property name="eachSchema" value="site.item.file.basic"/>
	                <fr:property name="eachLayout" value="values"/>
	                <fr:property name="style" value="<%= "list-style-image: url(" + request.getContextPath() + "/images/icon_file.gif);" %>"/>
	            </fr:layout>
	        </fr:view>
		</logic:notEmpty>
	</logic:equal>
	<logic:equal name="itemAvailable" value="false">
		<bean:define id="itemId" name="item" property="externalId"/>
		<p>
		<em><bean:message key="message.item.view.mustLogin" bundle="SITE_RESOURCES"/></em>
		<%
		    if (org.fenixedu.bennu.core.util.CoreConfiguration.casConfig().isCasEnabled()) {
							    final String schema = request.getScheme();
							    final String server = request.getServerName();
							    final int port = request.getServerPort();
		%>
				<a href="<%= "https://barra.tecnico.ulisboa.pt/login?next=https://id.ist.utl.pt/cas/login?service=" + schema + "://" + server + (port == 80 || port == 443 ? "" : ":" + port) + request.getContextPath() + item.getFullPath() %>">
            		<bean:message key="link.section.view.login" bundle="SITE_RESOURCES"/>
       			</a>.
		<%
			} else {
		%>
		       <html:link page="<%= String.format("%s?method=itemWithLogin&amp;%s&amp;itemID=%s", actionName, context, itemId) %>">
		            <bean:message key="link.section.view.login" bundle="SITE_RESOURCES"/>
	    	   </html:link>.
       	<%
			}
       	%>

	    </p>
	    <bean:message key="label.permittedGroup" bundle="SITE_RESOURCES"/> ${item.permittedGroup.presentationName}
	</logic:equal>
</logic:present>