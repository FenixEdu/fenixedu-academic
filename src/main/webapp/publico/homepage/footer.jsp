<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>

<div id="foot_links">
	<bean:define id="contactsUrl"><bean:message bundle="GLOBAL_RESOURCES" key="footer.contacts.link"/></bean:define>
	<a href="<%= contactsUrl %>"><bean:message bundle="GLOBAL_RESOURCES" key="footer.contacts.label"/></a>
	|  
	<bean:define id="webmasterUrl"><bean:message bundle="GLOBAL_RESOURCES" key="footer.webmaster.link"/></bean:define>
	<a href="<%= webmasterUrl %>"><bean:message bundle="GLOBAL_RESOURCES" key="footer.webmaster.label"/></a>
</div>

<div id="foot_copy">
	<bean:message bundle="GLOBAL_RESOURCES" key="footer.copyright.label"/>
	<dt:format pattern="yyyy"><dt:currentTime/></dt:format>
	- <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName()%>
</div>

<div class="clear"></div>
