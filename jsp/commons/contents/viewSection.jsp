<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.SectionProcessor" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.ItemProcessor" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<logic:present name="section">
    <bean:define id="section" name="section" type="net.sourceforge.fenixedu.domain.Section"/>

    <h2>
        <fr:view name="section" property="name" type="net.sourceforge.fenixedu.util.MultiLanguageString"/>
        <logic:present name="directLinkContext">
            <bean:define id="directLinkContext" name="directLinkContext"/>
            <span class="permalink1">(<a href="<%= directLinkContext + SectionProcessor.getSectionPath(section) %>"><bean:message key="label.link" bundle="SITE_RESOURCES"/></a>)</span>
        </logic:present>    
    </h2>

	<%-- 
    <logic:present name="hasRestrictedItems">
        <p>
            <em><bean:message key="message.section.items.hasRestricted" bundle="SITE_RESOURCES"/></em>
            <html:link page="<%= String.format("%s?method=sectionWithLogin&amp;%s&amp;sectionID=%s", actionName, context, section.getIdInternal()) %>">
                <bean:message key="link.section.view.login" bundle="SITE_RESOURCES"/>
           </html:link>.
        </p>
    </logic:present>

	<fr:view name="section" type="net.sourceforge.fenixedu.domain.Section" layout="section-sub-menu">
		<fr:layout>
			<fr:property name="sectionUrl" value="<%= String.format("%s?method=section&amp;%s", actionName, context) %>"/>
		</fr:layout>
	</fr:view>
 --%>

    <logic:notEmpty name="protectedItems">
       	
       	<logic:iterate id="protectedItem" name="protectedItems">
           
            <bean:define id="item" name="protectedItem" property="item" type="net.sourceforge.fenixedu.domain.Item"/>
            <bean:define id="available" name="protectedItem" property="available"/>
            
            <logic:equal name="item" property="nameVisible" value="true">
	       		<h3 class="mtop2 mbottom05">
	                <a name="<%= "item" + item.getIdInternal() %>" ></a>
	                <fr:view name="item" property="name"/>
	                <logic:present name="directLinkContext">
	                    <bean:define id="directLinkContext" name="directLinkContext"/>
	                    <span class="permalink1">(<a href="<%= directLinkContext + ItemProcessor.getItemPath(item) %>"><bean:message key="label.link" bundle="SITE_RESOURCES"/></a>)</span>
	                </logic:present>
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
