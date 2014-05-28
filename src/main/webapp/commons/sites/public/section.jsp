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
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>

<logic:present name="section">
    <bean:define id="section" name="section" type="net.sourceforge.fenixedu.domain.Section"/>


    <h2>
        <fr:view name="section" property="name" type="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString"/>
  		<span class="permalink1">(<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="${section.fullPath}"><bean:message key="label.link" bundle="SITE_RESOURCES"/></a>)</span>
    </h2>

    <logic:present name="hasRestrictedItems">
        <p>
            <em><bean:message key="message.section.items.hasRestricted" bundle="SITE_RESOURCES"/></em>
		<%
		    if (org.fenixedu.bennu.core.util.CoreConfiguration.casConfig().isCasEnabled()) {
							    final String schema = request.getScheme();
							    final String server = request.getServerName();
							    final int port = request.getServerPort();
		%>
				<a href="<%= "https://barra.tecnico.ulisboa.pt/login?next=https://id.ist.utl.pt/cas/login?service=" + schema + "://" + server + (port == 80 || port == 443 ? "" : ":" + port) + section.getFullPath() %>">
            		<bean:message key="link.section.view.login" bundle="SITE_RESOURCES"/>
       			</a>.
		<%
			} else {
		%>
	            <html:link page="<%= String.format("%s?method=sectionWithLogin&amp;%s&amp;sectionID=%s", actionName, context, section.getExternalId()) %>">
    	            <bean:message key="link.section.view.login" bundle="SITE_RESOURCES"/>
        	   </html:link>.
       	<%
			}
       	%>
        </p>
    </logic:present>


	<logic:notEmpty name="section" property="visibleFiles">
			<table class="box" style="float: right;" cellspacing="0">   
				<tr>
					<td class="box_header">
						<strong>
							<bean:message key="label.files" bundle="SITE_RESOURCES"/>
						</strong>
					</td>
				</tr>						
				<tr>
					<td class="box_cell">
						<ul>
								<logic:iterate id="file" name="section" property="visibleFiles">		
									<li>
										<a href="${file.downloadUrl}">${file.displayName}</a>
									 <span class="color888">(${file.filename}, <fr:view name="file" property="size" layout="fileSize"/>)</span></li>
								</logic:iterate>
						</ul>
					</td>						
				</tr>
			</table>       	
		</logic:notEmpty>

		<logic:equal name="section" property="showSubSectionTree" value="true">
				<ul>
					<logic:iterate id="section" name="section" property="orderedVisibleSubSections" type="net.sourceforge.fenixedu.domain.Section">		
							<li>
								<a href="${section.fullPath}">${section.name}</a>
							</li>
						</logic:iterate>
				</ul>
		</logic:equal>
			
			
    <logic:notEmpty name="protectedItems">
       	
       	<logic:iterate id="protectedItem" name="protectedItems">
           
            <bean:define id="item" name="protectedItem" property="item" type="net.sourceforge.fenixedu.domain.Item"/>
            <bean:define id="available" name="protectedItem" property="available"/>
            
            <logic:equal name="item" property="nameVisible" value="true">
	       		<h3 class="mtop2 mbottom05" id="<%= "item" + item.getExternalId() %>">
	                <fr:view name="item" property="name"/>

		            <logic:equal name="item" property="publicAvailable" value="true">
		                  <span class="permalink1">(<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="${item.fullPath}"><bean:message key="label.link" bundle="SITE_RESOURCES"/></a>)</span>
	    			</logic:equal>
	            </h3>
            </logic:equal>

            <logic:equal name="available" value="true">
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
            
            <logic:equal name="logged" value="true">
                <logic:equal name="available" value="false">
                    <p>
                        <em><bean:message key="message.item.view.notAllowed" bundle="SITE_RESOURCES"/></em>
                    </p>
                </logic:equal>
            </logic:equal>

            <logic:equal name="logged" value="false">
                <logic:equal name="available" value="false">
                    <p>
                        <em><bean:message key="message.item.view.mustLogin" bundle="SITE_RESOURCES"/></em>
                    </p>
                </logic:equal>
            </logic:equal>
            
       	</logic:iterate>
       	
    </logic:notEmpty>
</logic:present>
