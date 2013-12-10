<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@page import="net.sourceforge.fenixedu.util.FenixConfigurationManager"%>

<% final String url = FenixConfigurationManager.getConfiguration().getInstitutionUrl(); %>

<div id="latnav">
        <ul>
        	<li><a href="<%= url %>/html/instituto/">Instituto</a></li>
        	<li><a href="<%= url %>/html/estrutura/">Estrutura</a></li>
        	<li><a href="<%= url %>/html/ensino/">Ensino</a></li>
        	<li><a href="<%= url %>/html/id/">I &amp; D</a></li>
        	<li><a href="<%= url %>/html/viverist/">Viver no <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a></li>
        </ul>
</div>

