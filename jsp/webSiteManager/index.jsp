<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<tiles:insert page="/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="serviceName" value="Gestão WebSite" />
  <tiles:put name="institutionName" value="Instituto Superior T&eacute;cnico" />
   <tiles:put name="navGeral" value="/webSiteManager/commonNavGeral.jsp" />  
  <tiles:put name="body" value="/webSiteManager/firstPage.jsp" />
  <tiles:put name="navLocal" value="/webSiteManager/navbar.jsp" type="page"/>
  <tiles:put name="footer" value="/sop/commonFooterSop.jsp" />
</tiles:insert>