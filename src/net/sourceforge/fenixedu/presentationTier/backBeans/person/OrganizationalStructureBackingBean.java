
 /*
 * Created on Nov 10, 2005
 *	by angela
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.person;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.component.html.HtmlInputHidden;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitType;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;


public class OrganizationalStructureBackingBean extends FenixBackingBean {
	 public Integer choosenExecutionYearID;
	 public Unit unit;
	 public Integer personID;
	 public Integer unitID;
	 public Integer subUnit;
	 public HtmlInputHidden unitTypeNameHidden;   
	 public ResourceBundle bundle;
	
	
	

	public OrganizationalStructureBackingBean() {
		if (getRequestParameter("unitID") != null) {
	        this.unitID = Integer.valueOf(getRequestParameter("unitID").toString());
	    }
		if (getRequestParameter("subUnit") != null) {
	        this.subUnit = Integer.valueOf(getRequestParameter("subUnit").toString());
	    }
		if (getRequestParameter("choosenExecutionYearID") == null) {
	        this.choosenExecutionYearID = Integer.valueOf(0);
	    }
		
		this.bundle = ResourceBundle
        .getBundle("resources.EnumerationResources");
    }

    public List getExecutionYears() throws FenixFilterException, FenixServiceException {

        List<InfoExecutionYear> executionYears = (List<InfoExecutionYear>) ServiceUtils.executeService(
                getUserView(), "ReadExecutionYearsService", null);

        List<SelectItem> result = new ArrayList<SelectItem>(executionYears.size());
        for (InfoExecutionYear executionYear : executionYears) {
	        if(executionYear.getYear().compareTo("2005/2006") >= 0) {
	             result.add(new SelectItem(executionYear.getIdInternal(), executionYear.getYear(),executionYear.getState().getStateCode()));
	        }
        }
       
        Collections.reverse(result);  
        if (this.choosenExecutionYearID == 0){
        	for (SelectItem selectExecutionYear : result){
        		if (selectExecutionYear.getDescription().equals(PeriodState.CURRENT_CODE)){
        			this.choosenExecutionYearID = (Integer)selectExecutionYear.getValue();
        		}
        	}
        }

        return result;
    }

    public String getUnits() throws FenixFilterException, FenixServiceException, ExcepcaoPersistencia {
        StringBuffer buffer = new StringBuffer();
        Date currentDate = Calendar.getInstance().getTime();
        final Object[] argsToRead = { Unit.class };

        List<Unit> allUnits = getAllUnitsWithoutParent();
        Collections.sort(allUnits, new Comparator(){
        	
			public int compare(Object arg0, Object arg1) {
				Unit Unit1 = (Unit) arg0;
				Unit Unit2 = (Unit) arg1;
				if(Unit1.getType() == null && Unit2.getType() == null){
				 return 0;
				}else if(Unit1.getType() == null){
					return 1;
					}else if(Unit2.getType() == null){
						return -1;
					}else{
						return Unit1.getType().compareTo(Unit2.getType());
					}
			}
        	
        });
    
            
        UnitType unitType = null;
        boolean flag = false;
        boolean flag1 = false;
       
        for(Unit unit : allUnits){

        	if (unit.isActive(currentDate)){
	        	if (unit.getType() != null && unit.getType().name().length() > 0){     
		        	unitType = unit.getType();
		        	break;
	        	}
        	}
	        
        }
        
    	for (Unit unit : allUnits) {

    		if (unit.isActive(currentDate)){
	    		if (unit.getType() != null && unit.getType().name().length() > 0){
				    if (!unitType.equals(unit.getType())){	    	
				    	unitType = unit.getType();
	    				buffer.append("<h3 class='mtop2'>").append(this.bundle.getString(unitType.getName()));	
	    				buffer.append("</h3>\r\n");   
	    				
				    }else if (flag == false){
				    	buffer.append("<h3 class='mtop2'>").append(this.bundle.getString(unitType.getName()));	
	    				buffer.append("</h3>\r\n");   
	    				flag = true;
				    }
				   
			     }else if(unit.getType() == null){
			    	 if (flag1 == false){
				    	 buffer.append("<div class='mtop2' style='color: #aaa;'>- - - - - - - -");
				    	 buffer.append("</div>\r\n");
				    	 flag1 = true;
			    	 }else{
			    		 buffer.append("<div class='mtop2'>");
			    		 buffer.append("</div>\r\n");
			    	 }
			     }

	            	buffer.append("<ul class='padding nobullet'>\r\n");
	            	
					if(unit.getActiveSubUnits(currentDate).size() != 0){
						if(unit.getType() != null ){
							 if(!unit.getType().equals(UnitType.ACADEMIC_UNIT)){
	
								 buffer.append("\t<li><img ").append("src='").append(getContextPath()).append("/images/toggle_plus10.gif' id=\"").append(unit.getIdInternal()).append("\" ").append("indexed='true' onClick=\"").append("check(document.getElementById('")
					                .append("aa").append(unit.getIdInternal()).append("'),document.getElementById('").append(unit.getIdInternal()).append("'));return false;").append("\"> ");
					            	buffer.append("<a href=\"").append(getContextPath()).append("/person/organizationalStructure/chooseUnit.faces?unitID=").append(unit.getIdInternal()).append("&amp;subUnit=").append(unit.getIdInternal()).append("#").append(unit.getIdInternal()).append("\">")
					                .append(unit.getName()).append("</a></li>\r\n");
							 }else{
								 getDepartment(buffer,unit);
								 continue;
								 
							 }
						}else{
							buffer.append("\t<li><img ").append("src='").append(getContextPath()).append("/images/toggle_plus10.gif' id=\"").append(unit.getIdInternal()).append("\" ").append("indexed='true' onClick=\"").append("check(document.getElementById('")
			                .append("aa").append(unit.getIdInternal()).append("'),document.getElementById('").append(unit.getIdInternal()).append("'));return false;").append("\"> ");
			            	buffer.append("<a href=\"").append(getContextPath()).append("/person/organizationalStructure/chooseUnit.faces?unitID=").append(unit.getIdInternal()).append("&amp;subUnit=").append(unit.getIdInternal()).append("#").append(unit.getIdInternal()).append("\">")
			                .append(unit.getName()).append("</a></li>\r\n");
						}
		            	
					}else {
						buffer.append("\t<li><a href=\"").append(getContextPath()).append("/person/organizationalStructure/chooseUnit.faces?unitID=").append(unit.getIdInternal()).append("&amp;subUnit=").append(unit.getIdInternal()).append("#").append(unit.getIdInternal()).append("\">")
		                .append(unit.getName()).append("</a></li>\r\n");
						
	 				}
			
	                getUnitTree(buffer, unit);    
	                buffer.append("</ul>\r\n");

    		}
    	}
    
    return buffer.toString();
    }
              
    public List<Unit> getAllUnitsWithoutParent() throws FenixFilterException, FenixServiceException, ExcepcaoPersistencia {
    	Unit istUnit = UnitUtils.readUnitWithoutParentstByName(UnitUtils.IST_UNIT_NAME);
        if(istUnit == null){
        	return new ArrayList();
        }
        List<Unit> unitsIst = new ArrayList();
        unitsIst.addAll(istUnit.getSubUnits());
        return unitsIst;    
    }
    
    public void getDepartment(StringBuffer buffer,Unit unit) throws FenixFilterException, FenixServiceException {
    	Date currentDate = Calendar.getInstance().getTime();
    	
    	if(unit.getActiveSubUnits(currentDate).size() > 0){
    		List <Unit> departmentUnitList = unit.getActiveSubUnits(currentDate);
    		Collections.sort(departmentUnitList,new BeanComparator("name"));
    		Integer i=0;
    		for (Unit subUnit : departmentUnitList){
    			
    			buffer.append("\t<li><img ").append("src='").append(getContextPath()).append("/images/toggle_plus10.gif' id=\"").append(subUnit.getIdInternal()).append("\" ").append("indexed='true' onClick=\"").append("check(document.getElementById('")
		        .append("aa").append(subUnit.getIdInternal()).append("'),document.getElementById('").append(subUnit.getIdInternal()).append("'));return false;").append("\"> ");
		    	buffer.append("<a href=\"").append(getContextPath()).append("/person/organizationalStructure/chooseUnit.faces?unitID=").append(subUnit.getIdInternal()).append("&amp;subUnit=").append(subUnit.getIdInternal()).append("#").append(subUnit.getIdInternal()).append("\">")
		        .append(subUnit.getName()).append("</a></li>\r\n");

                getUnitTree(buffer, subUnit);    
                buffer.append("</ul>\r\n");
               
                if(i.intValue() < (departmentUnitList.size()-1)){
                	i=i+1;
                	buffer.append("<ul class='padding nobullet'>\r\n");
                }
    		}
    		
    	}
    }
    
    private List<Unit> readAllUnits() throws FenixServiceException, FenixFilterException {
        List<Unit> allUnits = readAllDomainObjects(Unit.class);
   
        List<Unit> unitsIst = new ArrayList();
        for(Unit unitList : allUnits){
        	if (!(unitList.getType() != null && unitList.getType().equals(UnitType.EXTERNAL_INSTITUTION))){
        		unitsIst.add(unitList);
        	}
        }
  
        return unitsIst;
        
    }
    
    public void getUnitTree(StringBuffer buffer, Unit parentUnit) {
    	Date currentDate = Calendar.getInstance().getTime();
    	int parentUnitId=0;
    	boolean past = false;
    	if(parentUnit.getActiveSubUnits(currentDate).size() > 0){
			buffer.append("\t<ul class='mvert0' id=\"").append("aa").append(parentUnit.getIdInternal()).append("\" ").append("style='display:none'>\r\n");
    		parentUnitId = parentUnit.getIdInternal().intValue();
			getUnitsList(parentUnit, 0, buffer,parentUnitId,past);
			buffer.append("\t</ul>\r\n");
    	}  
    }

    private void getUnitsList(Unit parentUnit, int index, StringBuffer buffer,int parentUnitId,boolean past) {
    	Date currentDate = Calendar.getInstance().getTime();

    	if(parentUnit.getIdInternal() != parentUnitId){	
	    	buffer.append("\t\t<li>").append("<a href=\"").append(getContextPath()).append("/person/organizationalStructure/chooseUnit.faces?unitID=").append(parentUnitId).append("&amp;subUnit=").append(parentUnit.getIdInternal()).append("#").append(parentUnit.getIdInternal()).append("\">")
	        .append(parentUnit.getName()).append("</a>")
	        .append("</li>\r\n");
	    	if (parentUnit.getActiveSubUnits(currentDate).size() > 0 ){
	    		past=true;
	        	buffer.append("\t\t<ul class='mvert0'>\r\n");
	    	}else{
	    		past=false;
	    	}
    	}		     
    	
        for (Unit subUnit : parentUnit.getActiveSubUnits(currentDate)) { 	
    		 getUnitsList(subUnit, index + 1, buffer,parentUnitId,past);
        }    
        if(past == true){
        	buffer.append("\t\t</ul>\r\n");
        	
        }
    }
    
    
    public String getTitle() throws ExcepcaoPersistencia,FenixFilterException, FenixServiceException {
    	StringBuffer buffer = new StringBuffer();		
        buffer.append("<p><em>");
    	buffer.append(getInstituitionName());
    	if (this.getUnit().getType() != null){
			 buffer.append(" - ");
			 if (this.getUnit().getType().equals(UnitType.DEPARTMENT)){
				 buffer.append(this.bundle.getString(UnitType.ACADEMIC_UNIT.getName() ));  
			 }else{
				 buffer.append(this.bundle.getString(this.getUnit().getType().getName())); 
			 }
    	}
	    buffer.append("</em></p>");
	    buffer.append("<h2>").append(this.getUnit().getName()).append("</h2>");
    	return buffer.toString();
    }
    
    public String getInstituitionName()throws ExcepcaoPersistencia, FenixServiceException {
         
    	Unit institution = UnitUtils.readUnitWithoutParentstByName(UnitUtils.IST_UNIT_NAME);
        if (institution != null) {                        
            return institution.getName();
        }        
        return null;
    }
    
    public String getFunctions() throws FenixFilterException, FenixServiceException {
	       
	    StringBuffer buffer = new StringBuffer();	
        Date currentDate = Calendar.getInstance().getTime();
        
        buffer.append("<ul class='mtop3'>\r\n");
        if(this.subUnit.intValue() == this.getUnit().getIdInternal().intValue()){
				 buffer.append("\t<li><strong class='eo_highlight'id=\"").append(this.getUnit().getIdInternal()).append("\" >").append(this.getUnit().getName()).append("</strong>\r\n");
		}else{
			 buffer.append("\t<li><strong id=\"").append(this.getUnit().getIdInternal()).append("\" >").append(this.getUnit().getName()).append("</strong>\r\n");
		}
	       
			for (Function function : getSortFunctionList(this.getUnit())) { 
				 ExecutionYear iExecutionYear = null;  
			     iExecutionYear = getExecutionYear(this.choosenExecutionYearID);
				 if (function.belongsToPeriod(iExecutionYear.getBeginDate(),iExecutionYear.getEndDate())){
					  buffer.append("\t<ul>\r\n"); 
					  getFunctionsList(this.getUnit(), function, buffer,iExecutionYear);
					  buffer.append("\t</ul>\r\n");	
				 }
				 
			}	        
	        buffer.append("\t</li>\r\n\t</ul>\r\n");
	      
	        
	        
			List<Unit> activeSubUnit = this.getUnit().getActiveSubUnits(currentDate);
	
    		for (Unit unit : (List<Unit>)activeSubUnit){
    			buffer.append("<ul>\r\n");	
	  			if(this.subUnit.intValue() == unit.getIdInternal().intValue()){
						 buffer.append("\t<li><strong class='eo_highlight'id=\"").append(unit.getIdInternal()).append("\" >").append(unit.getName()).append("</strong>\r\n");
				}else{
					 buffer.append("\t<li><strong id=\"").append(unit.getIdInternal()).append("\" >").append(unit.getName()).append("</strong>\r\n");
				}

				for (Function function : getSortFunctionList(unit)) {
					 ExecutionYear iExecutionYear = null;  
				     iExecutionYear = getExecutionYear(this.choosenExecutionYearID);
					 if (function.belongsToPeriod(iExecutionYear.getBeginDate(),iExecutionYear.getEndDate())){
						buffer.append("\t<ul>\r\n");	
						getFunctionsList(unit, function, buffer,iExecutionYear);	 
						buffer.append("\t</ul>\r\n");	
					 }
				}
				for (Unit subUnit : (List<Unit>)unit.getActiveSubUnits(currentDate)){
						buffer.append("\t<ul>\r\n");	
						if(this.subUnit.intValue() == subUnit.getIdInternal().intValue()){
							 buffer.append("\t<li><strong class='eo_highlight'id=\"").append(subUnit.getIdInternal()).append("\" >").append(subUnit.getName()).append("</strong>\r\n");
						}else{
							 buffer.append("\t<li><strong id=\"").append(subUnit.getIdInternal()).append("\" >").append(subUnit.getName()).append("</strong>\r\n");
						}
					 
						if (subUnit.getFunctions().size() > 0){
							
							for (Function subFunction : getSortFunctionList(subUnit)) { 
								ExecutionYear iExecutionYear = null;  
							     iExecutionYear = getExecutionYear(this.choosenExecutionYearID);
									 if (subFunction.belongsToPeriod(iExecutionYear.getBeginDate(),iExecutionYear.getEndDate())){
										 buffer.append("\t<ul>\r\n"); 	
										 getFunctionsList(subUnit, subFunction, buffer,iExecutionYear);
										 buffer.append("\t</ul>\r\n");
									 }
										 
							}
								
						}
						buffer.append("\t</li>\r\n");
						buffer.append("\t</ul>\r\n");	
					}
					buffer.append("\t</li>\r\n");
					buffer.append("\t</ul>\r\n");
				}
            					
				buffer.append("\t</li>\r\n");
				buffer.append("\t</ul>\r\n");

			return buffer.toString();
    }
    
    private List<Function> getSortFunctionList(Unit unit){
    	 
    	 	List<Function> allFunctions = new ArrayList<Function>();

	    	 	allFunctions.addAll(unit.getFunctions());
		
	    	 	Collections.sort(allFunctions, new Comparator(){
	
	    			public int compare(Object arg0, Object arg1) {
	    				Function Function1 = (Function) arg0;
	    				Function Function2 = (Function) arg1;
	    				if(Function1.getType() == null && Function2.getType() == null){
	    				 return 0;
	    				}else if(Function1.getType() == null){
	    					return 1;
	    					}else if(Function2.getType() == null){
	    						return -1;
	    					}else{
	    						return Function1.getType().compareTo(Function2.getType());
	    					}
	    			}
	            	
	            });
	    	
		 
    	 	return allFunctions;
    }
    
    private void getFunctionsList(Unit unit, Function function, StringBuffer buffer, ExecutionYear iExecutionYear)
    		throws FenixFilterException, FenixServiceException {
	    	
	 
	    	buffer.append("\t<li class='tree_label'><span>").append(function.getName()).append(": ").append("</span>\r\n");      
		    	    if (function.getPersonFunctionsCount() > 0){
		    	        buffer.append("\t<ul class='unit1'>\r\n");
				    	for(PersonFunction personFunction : getValidPersonFunction(iExecutionYear,function)){
				    		
					    		if (personFunction.belongsToPeriod(iExecutionYear.getBeginDate(),iExecutionYear.getEndDate())){
					    			if (personFunction.getEndDate() == null ){
					    				buffer.append("\t\t<li class='eo_highlight'>");
					    			}else{
					    				buffer.append("\t\t<li>");
					    			}
						    	//	String userName = personFunction.getPerson().getUsername().substring(1);	    	
						    		buffer.append(personFunction.getPerson().getNome());
						    		buffer.append("</li>\r\n");	
					    		}
				    		}
				    	
				   
				    	buffer.append("\t</ul>\r\n");
				    	
		    	    }else{
		    	    	 if (function.getParentInherentFunction() != null){
		    	    		 if(function.getParentInherentFunction().getPersonFunctionsCount() > 0){
		    	    			 buffer.append("\t<ul class='unit1'>\r\n");
			    	    		 for(PersonFunction personFunction : getValidPersonFunction(iExecutionYear,function.getParentInherentFunction())){
							    		
							    		if (personFunction.belongsToPeriod(iExecutionYear.getBeginDate(),iExecutionYear.getEndDate())){
							    			if (personFunction.getEndDate() == null ){
							    				buffer.append("\t\t<li class='eo_highlight'>");
							    			}else{
							    				buffer.append("\t\t<li>");
							    			}
								    	//	String userName = personFunction.getPerson().getUsername().substring(1);	    	
								    		buffer.append(personFunction.getPerson().getNome());
								    		buffer.append("</li>\r\n");	
							    		}
						    		}
						    	
						   
						    	buffer.append("\t</ul>\r\n");
			    	    	}
		    	    		 
		    	    	 }
		    	    }
	            
		    buffer.append("\t</li>\r\n");  	    	
	   
    }
    
    public ExecutionYear getExecutionYear(Integer executionYear) throws FenixFilterException, FenixServiceException {
    	final Object[] argsexecutionYearToRead = {executionYear};
    	ExecutionYear iExecutionYear = (ExecutionYear) ServiceUtils.executeService(
    			 null, "ReadExecutionYearsService", argsexecutionYearToRead);
    	 return iExecutionYear;
    }
    
    public List<PersonFunction> getValidPersonFunction(ExecutionYear iExecutionYear,Function function){
    	
    	List<PersonFunction> list = new ArrayList<PersonFunction>();
        for (PersonFunction personFunction : function.getPersonFunctions()) {
            if (personFunction.belongsToPeriod(iExecutionYear.getBeginDate(), iExecutionYear.getEndDate())) {
                list.add(personFunction);
            }
        }
        return list;
    }
    
        
    public void setChoosenExecutionYearID(Integer choosenExecutionYearID) {
        this.choosenExecutionYearID = choosenExecutionYearID;
    }
	public Integer getChoosenExecutionYearID() {
		return this.choosenExecutionYearID;
	}
    public Unit getUnit() throws FenixFilterException, FenixServiceException {
    	if(unit == null){
    		final Object[] argsUnitToRead = { Unit.class, Integer.valueOf(this.unitID) };
            this.unit = (Unit) ServiceUtils.executeService(null, "ReadDomainObject",
                    argsUnitToRead);
    	}
		return unit;
	}
	public void setUnit(Unit unit) {
		this.unit = unit;
	}
    public Integer getPersonID() {
		return personID;
	}
	public void setPersonID(Integer personID) {
		this.personID = personID;
	}
	public Integer getUnitID() {
		return unitID;
	}
	public void setUnitID(Integer unitID) {
		this.unitID = unitID;
	}
	protected String getRequestParameter(String parameterName) {
	        return (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
	                .get(parameterName);
	}
	public HtmlInputHidden getUnitTypeNameHidden() {
		if(this.unitTypeNameHidden == null){
			this.unitTypeNameHidden = new HtmlInputHidden();
			
		}
		return unitTypeNameHidden;
	}

	public void setUnitTypeNameHidden(HtmlInputHidden unitTypeNameHidden) {
		this.unitTypeNameHidden = unitTypeNameHidden;
	}

	public ResourceBundle getBundle() {
		return bundle;
	}

	public void setBundle(ResourceBundle bundle) {
		this.bundle = bundle;
	}

	public Integer getSubUnit() {
		return this.subUnit;
	}

	public void setSubUnit(Integer subUnit) {
		this.subUnit = subUnit;
	}

	
}
