<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<% final String url = net.sourceforge.fenixedu.domain.Instalation.getInstance().getInstituitionURL(); %>
<div id="latnav">
        <ul>
        	<li><a href="<%= url %>/html/instituto/">Instituto</a></li>
        	<li><a href="<%= url %>/html/estrutura/">Estrutura</a></li>
        	<li><a href="<%= url %>/html/ensino/">Ensino</a></li>
        	<li><a href="<%= url %>/html/id/">I &amp; D</a></li>
        	<li><a href="<%= url %>/html/viverist/">Viver no <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a></li>
        </ul>
</div>

