<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>
<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<logic:present name="section">
    <bean:define id="section" name="section" type="net.sourceforge.fenixedu.domain.Section"/>


    <h2>
        <fr:view name="section" property="name" type="net.sourceforge.fenixedu.util.MultiLanguageString"/>
		<app:defineContentPath id="sectionURL" name="section" toScope="request"/>
		<bean:define id="url" name="sectionURL" type="java.lang.String"/>
  		<span class="permalink1">(<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= request.getContextPath()  + url %>"><bean:message key="label.link" bundle="SITE_RESOURCES"/></a>)</span>
    </h2>


    <logic:present name="hasRestrictedItems">
        <p>
            <em><bean:message key="message.section.items.hasRestricted" bundle="SITE_RESOURCES"/></em>
            <html:link page="<%= String.format("%s?method=sectionWithLogin&amp;%s&amp;sectionID=%s", actionName, context, section.getIdInternal()) %>">
                <bean:message key="link.section.view.login" bundle="SITE_RESOURCES"/>
           </html:link>.
        </p>
    </logic:present>


	<logic:notEmpty name="section" property="childrenFiles">
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
								<logic:iterate id="file" name="section" property="childrenFiles" type="net.sourceforge.fenixedu.domain.contents.Attachment">		
									<li>
									<app:contentLink name="file">
									<fr:view name="file" property="name"/> 
									</app:contentLink>
									 <span class="color888">(<fr:view name="file" property="file.filename"/>, <fr:view name="file" property="file.size" layout="fileSize"/>)</span></li>
								</logic:iterate>
						</ul>
					</td>						
				</tr>
			</table>       	
		</logic:notEmpty>

		<logic:notEmpty name="section" property="orderedSubSections">
				<ul>
					<logic:iterate id="section" name="section" property="orderedSubSections" type="net.sourceforge.fenixedu.domain.Section">		
							<li>
							<app:contentLink name="section">
							<fr:view name="section" property="name"/>
							</app:contentLink>
							</li>
						</logic:iterate>
				</ul>
		</logic:notEmpty>
			
			
    <logic:notEmpty name="protectedItems">
       	
       	<logic:iterate id="protectedItem" name="protectedItems">
           
            <bean:define id="item" name="protectedItem" property="item" type="net.sourceforge.fenixedu.domain.Item"/>
            <bean:define id="available" name="protectedItem" property="available"/>
            
            <logic:equal name="item" property="nameVisible" value="true">
	       		<h3 class="mtop2 mbottom05" id="<%= "item" + item.getIdInternal() %>">
	                <fr:view name="item" property="name"/>

		            <logic:equal name="item" property="publicAvailable" value="true">
	 				    <app:defineContentPath id="itemURL" name="item" toScope="request"/>
						<bean:define id="url" name="itemURL" type="java.lang.String"/>
		                  <span class="permalink1">(<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= request.getContextPath() + url %>"><bean:message key="label.link" bundle="SITE_RESOURCES"/></a>)</span>
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
                    
            		<logic:notEmpty name="item" property="sortedVisibleFileItems">
                    <fr:view name="item" property="sortedVisibleFileItems">
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
