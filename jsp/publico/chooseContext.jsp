<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %> 

<%--
<tiles:insert page="/istLayout.jsp" flush="true">
     <put name="title" value="Instituto Superior Técnico" /> 
	 <put name="symbols_row" value="/publico/degreeSite/symbolsRow.jsp" />
     <put name="profile_navigation" value="/publico/degreeSite/profileNavigation.jsp" />
	 <put name="main_navigation" value="/publico/degreeSite/degreeSiteMainNavigation.jsp" />
	 <put name="body" value="/publico/chooseContext_bd.jsp" />
  	 <put name="footer" value="/publico/degreeSite/footer.jsp" />
</tiles:insert>
	--%>



<tiles:insert page="/publicGesDisLayout_2col.jsp" flush="true">
  <tiles:put name="title" value=".Instituto Superior T&eacute;cnico" />
  <tiles:put name="serviceName" value="Instituto Superior T&eacute;cnico" />
  <tiles:put name="navGeral" value="" />
 <tiles:put name="navbarGeral" value="/publico/commonNavLocalPub.jsp" />
  <tiles:put name="body" value="/publico/chooseContext_bd.jsp" />
  <tiles:put name="footer" value="/publico/commonFooterPub.jsp" />
</tiles:insert>
