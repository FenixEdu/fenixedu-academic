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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><%@page import="java.util.Enumeration"%><%@page import="java.util.Calendar"%><html:xhtml/><%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %><%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %><%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %><h2><bean:message bundle="MANAGER_RESOURCES" key="manager.monitor.system.title"/></h2><br /><html:link module="/manager" page="/monitorSystem.do?method=warmUpCacheForEnrolmentPeriodStart">	<bean:message bundle="MANAGER_RESOURCES" key="manager.monitor.system.warm.up.cache.for.enrolment.period.start"/></html:link>		<%-- Barra as authentication broker Switch --%>	<style>		.barra-auth-div { background-color: rgba(166,193,199,0.1); border-style: dashed; border-width: 1px; border-color: #8FC0D9; padding-bottom: 8px; padding-top: 8px; margin-bottom: 15px; width: 300px; }		.barra-auth-label { padding-left: 15px; }		.field { display:inline-block; vertical-align: middle; padding-left: 15px; }		.cb-enable, .cb-disable, .cb-enable span, .cb-disable span { background: url('<%= request.getContextPath() + "/images/switch.gif" %>') repeat-x; display: block; float: left; }	    .cb-enable span, .cb-disable span { line-height: 30px; display: block; background-repeat: no-repeat; font-weight: bold; }	    .cb-enable span { background-position: left -90px; padding: 0 10px; }	    .cb-disable span { background-position: right -180px;padding: 0 10px; }	    .cb-disable.selected { background-position: 0 -30px; }	    .cb-disable.selected span { background-position: right -210px; color: #fff; }	    .cb-enable.selected { background-position: 0 -60px; }	    .cb-enable.selected span { background-position: left -150px; color: #fff; }	    .switch label { cursor: pointer; }	    .switch input { display: none; }	    .noAnchor { color: #333 !important; border-bottom: none !important; }	</style>	<div class="barra-auth-div">		<span class="barra-auth-label"><strong><em>Barra</em> authentication:</strong></span>		<div class="field switch">			<logic:equal name="useBarraAsAuth" value="true" >				<a class="noAnchor cb-enable selected" id="barraOn" href="#">					<span>Enable</span>				</a>				<a class="noAnchor cb-disable" id="barraOff" href="<%= request.getContextPath() + "/manager/monitorSystem.do?method=switchBarraAsAuthenticationBroker&useBarraAsAuth=false" %>">					<span>Disable</span>				</a>	    	</logic:equal>	    	<logic:equal name="useBarraAsAuth" value="false" >	    		<a class="noAnchor cb-enable" id="barraOn" href="<%= request.getContextPath() + "/manager/monitorSystem.do?method=switchBarraAsAuthenticationBroker&useBarraAsAuth=true" %>">					<span>Enable</span>				</a>				<a class="noAnchor cb-disable selected" id="barraOff" href="#">					<span>Disable</span>				</a>	    	</logic:equal>		</div>	</div>	<%-- End Barra as authentication broker Switch --%>	<bean:write name="startMillis"/>	<br/>	<bean:write name="endMillis"/>	<br/>	<bean:write name="chronology"/>	<br/>	<br/>