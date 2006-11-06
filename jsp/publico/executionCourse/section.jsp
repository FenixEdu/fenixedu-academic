<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.SectionProcessor" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.ItemProcessor" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<logic:present name="section">
    <bean:define id="executionCourse" name="executionCourse" type="net.sourceforge.fenixedu.domain.ExecutionCourse"/>
    <bean:define id="section" name="section" type="net.sourceforge.fenixedu.domain.Section"/>


    <h2>
        <fr:view name="section" property="name" type="net.sourceforge.fenixedu.util.MultiLanguageString"/>
		<span class="permalink1">(<a href="<%= request.getContextPath() + SectionProcessor.getSectionAbsolutePath(executionCourse, section) %>"><bean:message key="label.link" bundle="SITE_RESOURCES"/></a>)</span>
    </h2>

    
    <logic:present name="hasRestrictedItems">
        <p>
            <em><bean:message key="message.section.items.hasRestricted" bundle="SITE_RESOURCES"/></em>
            <html:link page="<%= String.format("/executionCourse.do?method=sectionWithLogin&executionCourseID=%s&sectionID=%s", executionCourse.getIdInternal(), section.getIdInternal()) %>">
                <bean:message key="link.section.view.login" bundle="SITE_RESOURCES"/>
           </html:link>.
        </p>
    </logic:present>
    
    <logic:notEmpty name="protectedItems">
        	<logic:iterate id="protectedItem" name="protectedItems">
            
            <bean:define id="item" name="protectedItem" property="item" type="net.sourceforge.fenixedu.domain.Item"/>
            <bean:define id="available" name="protectedItem" property="available"/>
            
       		<h3 class="mtop2">
                <a name="<%= "item" + item.getIdInternal() %>"></a>
                <fr:view name="item" property="name"/>
            <span class="permalink1">(<a href="<%= request.getContextPath() + ItemProcessor.getItemAbsolutePath(executionCourse, item) %>"><bean:message key="label.link" bundle="SITE_RESOURCES"/></a>)</span>
            </h3>

            <logic:equal name="available" value="true">
                <logic:notEmpty name="item" property="information">
            			<fr:view name="item" property="information">
            				<fr:layout>
            					<fr:property name="classes" value="coutput1" />
            					<fr:property name="escaped" value="false" />
            					<fr:property name="newlineAware" value="false" />
            				</fr:layout>
            			</fr:view>
            		</logic:notEmpty>
                    
            		<logic:notEmpty name="item" property="sortedVisibleFileItems">
                        <fr:view name="item" property="sortedVisibleFileItems">
                            <fr:layout name="list">
	                            <fr:property name="classes" value="coutput1 mvert0" />
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
