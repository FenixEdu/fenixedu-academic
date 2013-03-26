<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<% final String url = net.sourceforge.fenixedu._development.PropertiesManager.getProperty("institution.url"); %>
<div id="latnav">
        <ul>
        	<li><a href="<%= url %>/html/instituto/">Instituto</a></li>
        	<li><a href="<%= url %>/html/estrutura/">Estrutura</a></li>
        	<li><a href="<%= url %>/html/ensino/">Ensino</a></li>
        	<li><a href="<%= url %>/html/id/">I &amp; D</a></li>
        	<li><a href="<%= url %>/html/viverist/">Viver no IST</a></li>
        </ul>
</div>

