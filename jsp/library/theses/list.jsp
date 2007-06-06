<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<h2>
	<bean:message key="title.library.theses.list"/>
</h2>

<logic:empty name="theses">
	<p>
		<em><bean:message key="message.library.theses.list.empty"/></em>
	</p>
</logic:empty>

<logic:notEmpty name="theses">
	<logic:messagesPresent property="success" message="true">
	    <html:messages id="message" message="true" property="warning">
	        <p>
	        	<span class="success0">
	        		<bean:write name="message"/>
	        	</span>
        	</p>
    	</html:messages>
	</logic:messagesPresent>

	<bean:define id="sortedBy">
		<%= request.getParameter("sortBy") == null ? "discussed" : request.getParameter("sortBy") %>
	</bean:define>

    <fr:view name="theses" schema="library.thesis.list">
        <fr:layout name="tabular-sortable">
	        <fr:property name="classes" value="tstyle1"/>
	        
            <fr:property name="sortParameter" value="sortBy"/>
            <fr:property name="sortUrl" value="/theses/list.do?method=prepare"/>
            <fr:property name="sortBy" value="<%= sortedBy %>"/>

			<fr:property name="link(verify)" value="<%= "/theses/checkThesis.do?method=prepare&amp;index=0&amp;sortedBy=" + sortedBy %>"/>
			<fr:property name="param(verify)" value="idInternal/thesisID"/>
			<fr:property name="key(verify)" value="link.thesis.verify"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>
