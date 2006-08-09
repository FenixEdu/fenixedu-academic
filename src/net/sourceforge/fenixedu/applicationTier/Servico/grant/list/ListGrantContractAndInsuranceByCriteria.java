package net.sourceforge.fenixedu.applicationTier.Servico.grant.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantInsurance;
import net.sourceforge.fenixedu.dataTransferObject.grant.list.InfoListGrantOwnerByOrder;
import net.sourceforge.fenixedu.dataTransferObject.grant.list.InfoSpanByCriteriaListGrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.GrantInsurance;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPart;
import net.sourceforge.fenixedu.domain.grant.contract.GrantSubsidy;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.Action.grant.utils.SessionConstants;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.NameUtils;
import net.sourceforge.fenixedu.util.projectsManagement.FormatDouble;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

public class ListGrantContractAndInsuranceByCriteria extends Service {
	 private static final double dayValueOfInsurance = 63.45;
	/**
	 * Query the grant owner by criteria of grant contract
	 * @throws ExcepcaoPersistencia 
	 * 
	 * @returns an array of objects object[0] List of result object[1]
	 *          IndoSpanCriteriaListGrantOwner
	 */
	public Object[] run(InfoSpanByCriteriaListGrantContract infoSpanByCriteriaListGrantOwner)
			throws FenixServiceException, ExcepcaoPersistencia,FenixFilterException,Exception {

		// Read the grant contracts ordered by persistentSupportan
		List<GrantContractRegime> grantContractBySpanAndCriteria = readAllContractsByCriteria(
				propertyOrderBy(infoSpanByCriteriaListGrantOwner.getOrderBy()),
				infoSpanByCriteriaListGrantOwner.getJustActiveContract(),
				infoSpanByCriteriaListGrantOwner.getJustDesactiveContract(),
				infoSpanByCriteriaListGrantOwner.getBeginContract(), infoSpanByCriteriaListGrantOwner
						.getEndContract(), infoSpanByCriteriaListGrantOwner.getSpanNumber(),
				SessionConstants.NUMBER_OF_ELEMENTS_IN_SPAN, infoSpanByCriteriaListGrantOwner
						.getGrantTypeId(),infoSpanByCriteriaListGrantOwner.getValidToTheDate());

		List<InfoListGrantOwnerByOrder> listGrantContract = null;

		if (grantContractBySpanAndCriteria != null && grantContractBySpanAndCriteria.size() != 0) {
		    // Construct the info list and add to the result.
			listGrantContract = new ArrayList<InfoListGrantOwnerByOrder>();

			for (GrantContractRegime grantContractRegime : grantContractBySpanAndCriteria ){
				convertToInfoListGrantOwnerByOrder(grantContractRegime, infoSpanByCriteriaListGrantOwner, listGrantContract);
			}
		}

		if (infoSpanByCriteriaListGrantOwner.getTotalElements() == null) {
			// Setting the search attributes
			infoSpanByCriteriaListGrantOwner.setTotalElements(countAllByCriteria(infoSpanByCriteriaListGrantOwner.getJustActiveContract(),
							infoSpanByCriteriaListGrantOwner.getJustDesactiveContract(),
							infoSpanByCriteriaListGrantOwner.getBeginContract(),
							infoSpanByCriteriaListGrantOwner.getEndContract(),
							infoSpanByCriteriaListGrantOwner.getGrantTypeId(),infoSpanByCriteriaListGrantOwner.getValidToTheDate()));
		}
		Object[] result = { listGrantContract, infoSpanByCriteriaListGrantOwner };
		return result;
	}

	/**
	 * For each Grant Owner 1- Read all grant contracts that are in the criteria
	 * 1.1 - Read The active regime of each contract 1.2 - Read the insurance of
	 * each contract 2- Construct the info and put it on the list result
	 */
	private void convertToInfoListGrantOwnerByOrder(GrantContractRegime grantContractRegime,
			InfoSpanByCriteriaListGrantContract infoSpanByCriteriaListGrantOwner,
			List<InfoListGrantOwnerByOrder> result) throws ExcepcaoPersistencia {
		 

		InfoListGrantOwnerByOrder infoListGrantOwnerByOrder = new InfoListGrantOwnerByOrder();

		infoListGrantOwnerByOrder.setGrantOwnerId(grantContractRegime.getGrantContract().getGrantOwner().getIdInternal());
		infoListGrantOwnerByOrder.setGrantOwnerNumber(grantContractRegime.getGrantContract().getGrantOwner().getNumber());

		if (!(grantContractRegime.getGrantContract().getGrantOwner().getPerson() == null) ){
			infoListGrantOwnerByOrder.setFirstName(NameUtils.getFirstName(grantContractRegime.getGrantContract().getGrantOwner()
					.getPerson().getNome()));
			infoListGrantOwnerByOrder.setLastName(NameUtils.getLastName(grantContractRegime.getGrantContract().getGrantOwner()
				.getPerson().getNome()));
		} else {
			infoListGrantOwnerByOrder.setFirstName("");
			infoListGrantOwnerByOrder.setLastName("");
		}

		infoListGrantOwnerByOrder.setContractNumber(grantContractRegime.getGrantContract().getContractNumber());
		//infoListGrantOwnerByOrder.setGrantType(grantContractRegime.getGrantContract().getGrantType().getSigla());

//		infoListGrantOwnerByOrder.setBeginContract(grantContractRegime.getDateBeginContract());
//		infoListGrantOwnerByOrder.setEndContract(grantContractRegime.getDateEndContract());
	
		
		List<GrantSubsidy> grantSubsidyList = grantContractRegime.getGrantContract().getAssociatedGrantSubsidies();
		for (GrantSubsidy grantSubsidy : grantSubsidyList){
			 for(GrantPart grantPart : rootDomainObject.getGrantParts()){ 
				 if (grantSubsidy.equals(grantPart.getGrantSubsidy())){
					 if(grantPart.getGrantPaymentEntity() != null){
						 infoListGrantOwnerByOrder.setInsurancePaymentEntity(grantPart.getGrantPaymentEntity().getNumber());
						 infoListGrantOwnerByOrder.setNumberPaymentEntity(grantPart.getGrantPaymentEntity().getNumber());
						 infoListGrantOwnerByOrder.setDesignation(grantPart.getGrantPaymentEntity().getDesignation());
					 }
				 }
			 }
		}	 
		GrantInsurance grantInsurance = (GrantInsurance)grantContractRegime.getGrantContract().getGrantInsurance();
		//InfoGrantInsurance infoGrantInsurance = new InfoGrantInsurance();
		
		 if (grantInsurance != null){
			 final long MILLIS_PER_DAY = 1000 * 60 * 60 * 24;
    	 	 long days = 0;
    	 	 Date beginDate=infoSpanByCriteriaListGrantOwner.getBeginContract();;
    	 	 Date endDate= infoSpanByCriteriaListGrantOwner.getEndContract();;
    	 
    	 
//        if (grantInsurance.getDateBeginInsurance() != null && grantInsurance.getDateEndInsurance() != null 
//        		&& (grantInsurance.getDateBeginInsurance().before(grantInsurance.getDateEndInsurance()) 
//        				|| grantInsurance.getDateBeginInsurance().equals(grantInsurance.getDateEndInsurance())) ) {
    	 	if (grantInsurance.getDateBeginInsurance() != null ){
	        	if (grantInsurance.getDateBeginInsurance().before(infoSpanByCriteriaListGrantOwner.getBeginContract())
	        			||grantInsurance.getDateBeginInsurance().equals(infoSpanByCriteriaListGrantOwner.getBeginContract()) )
	        	{	
	        		beginDate = infoSpanByCriteriaListGrantOwner.getBeginContract();	
	        	}
	        	if (grantInsurance.getDateBeginInsurance().after(infoSpanByCriteriaListGrantOwner.getBeginContract()) )
	        	{
	        		beginDate = grantInsurance.getDateBeginInsurance();
	        	}
    	 	}
    	 	if (grantInsurance.getDateEndInsurance() != null ){
	        	if  (grantInsurance.getDateEndInsurance().before(infoSpanByCriteriaListGrantOwner.getEndContract()))
	        	{
	        		endDate=grantInsurance.getDateEndInsurance();
	        	}
	        	if  (grantInsurance.getDateEndInsurance().after(infoSpanByCriteriaListGrantOwner.getEndContract())
	        			|| grantInsurance.getDateEndInsurance().equals(infoSpanByCriteriaListGrantOwner.getEndContract()))
	        	{
	        		endDate=infoSpanByCriteriaListGrantOwner.getEndContract();
	        	}
    	 	}
        	
	    	long deltaMillis = endDate.getTime() - beginDate.getTime();
		    days = (deltaMillis / MILLIS_PER_DAY) + 1;     
	        infoListGrantOwnerByOrder.setTotalInsurance(FormatDouble
	                .round((dayValueOfInsurance / 365)* days ));
	        infoListGrantOwnerByOrder.setBeginContract(grantInsurance.getDateBeginInsurance());
			infoListGrantOwnerByOrder.setEndContract(grantInsurance.getDateEndInsurance());
	        infoListGrantOwnerByOrder.setTotalOfDays(days);
		 
		}
		

		result.add(infoListGrantOwnerByOrder);
	}

	/*
	 * Returns the order string to add to the criteria
	 */
	private String propertyOrderBy(String orderBy) {
		String result = null;
		if (orderBy.equals("orderByGrantOwnerNumber")) {
			result = "grantOwner.number";
		} else if (orderBy.equals("orderByGrantContractNumber")) {
			result = "contractNumber";
		} else if (orderBy.equals("orderByFirstName")) {
			result = "grantOwner.person.name";
		} else if (orderBy.equals("orderByGrantType")) {
			result = "grantType.sigla";
		} else if (orderBy.equals("orderByDateBeginContract")) {
			result = "contractRegimes.dateBeginContract";
		} else if (orderBy.equals("orderByDateEndContract")) {
			result = "contractRegimes.dateEndContract";
		}
		return result;
	}
    
	public List<GrantContractRegime> readAllContractsByCriteria(String orderBy, Boolean justActiveContracts,
            Boolean justDesactiveContracts, Date dateBeginContract, Date dateEndContract,
            Integer spanNumber, Integer numberOfElementsInSpan, Integer grantTypeId,Date validToTheDate)
            	throws FenixFilterException, FenixServiceException, ExcepcaoPersistencia ,Exception{
			
			List<GrantContractRegime> result = new ArrayList<GrantContractRegime>();
			Date ToTheDate = null;
			
            List<GrantContractRegime> grantContractRegimes = new ArrayList<GrantContractRegime>();
			grantContractRegimes.addAll(rootDomainObject.getGrantContractRegimes());
			ComparatorChain comparatorChain = new ComparatorChain(new BeanComparator("grantContract.grantOwner.number"), true);            
	        Collections.sort(grantContractRegimes, comparatorChain);
	        Collections.reverse(grantContractRegimes);
	        for (final GrantContractRegime grantContractRegime : ((List<GrantContractRegime>) grantContractRegimes)) {
	            final GrantContract grantContract = grantContractRegime.getGrantContract();
	            if (grantContract == null) {
	                continue;
	            }
	            
	            if ((validToTheDate == null || validToTheDate.equals(""))
	                    && (dateBeginContract == null || dateBeginContract.equals(""))
	                    && (dateEndContract == null || dateEndContract.equals(""))){
                	if (justActiveContracts != null
                	        && justActiveContracts.booleanValue()){
                		if (grantContractRegime.getGrantContract().getEndContractMotive()!= null && !grantContractRegime.getGrantContract().getEndContractMotive().equals("")){
    	            		continue;
    	            	}
                		if(!grantContractRegime.getContractRegimeActive()){
                			continue;
                         }
                	}
                	if (justDesactiveContracts != null
                	        && justDesactiveContracts.booleanValue()){
                		if(grantContractRegime.getContractRegimeActive()
                				&& (grantContractRegime.getGrantContract().getEndContractMotive()== null 
                						|| grantContractRegime.getGrantContract().getEndContractMotive().equals(""))) {
	                    continue;
                		}
                	}
	            }
	            
	            if (validToTheDate != null){
	            	if (grantContractRegime.getDateBeginContract() == null || grantContractRegime.getDateEndContract() == null) {
	                    continue;
	                }
	            	if (DateFormatUtil.isBefore("yyyy-MM-dd",grantContractRegime.getDateEndContract(),validToTheDate)) {		                    
	                    continue;
	                }
	                if (DateFormatUtil.isAfter("yyyy-MM-dd",grantContractRegime.getDateBeginContract(),validToTheDate)) {
	                    continue;
	                }
	            	if (grantContractRegime.getGrantContract().getEndContractMotive()!= null && !grantContractRegime.getGrantContract().getEndContractMotive().equals("")){
	            		continue;
	            	}
	                
            	}
	            
	            if ((dateBeginContract != null && !dateBeginContract.equals("")) 
	            		&& (dateEndContract != null && !dateEndContract.equals(""))){

	            	if (grantContractRegime.getDateBeginContract() == null || grantContractRegime.getDateEndContract() == null) {
	                    continue;
	                }
	            	
	            	if (!grantContractRegime.belongsToPeriod(dateBeginContract,dateEndContract)) {		                    
	                    continue;
	                }
	            	
	            	if (justActiveContracts != null
	                        && justActiveContracts.booleanValue()){
	            		if(!grantContractRegime.getState().equals(new Integer(1))){
	            			continue;
	            		}      
                		if (grantContractRegime.getGrantContract().getEndContractMotive()!= null && !grantContractRegime.getGrantContract().getEndContractMotive().equals("")){
    	            		continue;
    	            	}
	            	}
	            	
	            	if (justDesactiveContracts != null
	                        && justDesactiveContracts.booleanValue()){
	            		if(grantContractRegime.getState().equals(new Integer(1))&& (grantContractRegime.getGrantContract().getEndContractMotive()== null 
        						|| grantContractRegime.getGrantContract().getEndContractMotive().equals(""))){
	            			continue;
	            		}
	                        
	            	}
	            }

	            if (grantTypeId != null) {
	            	if(!grantContractRegime.getGrantContract().getGrantType().getIdInternal().equals(grantTypeId)){
	            		continue;
	            	}
	            }
	            result.add(grantContractRegime);
	        }

	        int begin = (spanNumber - 1) * numberOfElementsInSpan;
	        int end = begin + numberOfElementsInSpan;


	        return result.subList(begin, Math.min(end,result.size()));
    }

    public List<GrantContract> readBySpan(Integer spanNumber, Integer numberOfElementsInSpan,List<GrantContract> grantContract){
		List<GrantContract> result = new ArrayList<GrantContract>();
		Iterator iter = grantContract.iterator();

		int begin = (spanNumber.intValue() - 1) * numberOfElementsInSpan.intValue();
        int end = begin + numberOfElementsInSpan.intValue();
        if (begin != 0) {
            for (int j = 0; j < (begin - 1) && iter.hasNext(); j++) {
                iter.next();
            }
        }

        for (int i = begin; i < end && iter.hasNext(); i++) {
            GrantContract grantContract1 = (GrantContract) iter.next();
          
            result.add(grantContract1);
        }

        return result;
	}
	  public Integer countAllByCriteria(Boolean justActiveContracts, Boolean justDesactiveContracts,
	            Date dateBeginContract, Date dateEndContract, Integer grantTypeId,Date validToTheDate) 
	  			throws FenixServiceException,FenixFilterException, ExcepcaoPersistencia {
			Integer result =new Integer(0);
			
			List<GrantContractRegime> grantContractRegimes = new ArrayList<GrantContractRegime>();
            grantContractRegimes.addAll(rootDomainObject.getGrantContractRegimes());
			ComparatorChain comparatorChain = new ComparatorChain(new BeanComparator("grantContract.grantOwner.number"), true);
	        Collections.sort(grantContractRegimes, comparatorChain);
	        Collections.reverse(grantContractRegimes);
	        for (final GrantContractRegime grantContractRegime : ((List<GrantContractRegime>) grantContractRegimes)) {
	            final GrantContract grantContract = grantContractRegime.getGrantContract();

	            if (grantContract == null) {
	                continue;
	            }

	            if ((validToTheDate == null || validToTheDate.equals(""))
	            		&&(dateBeginContract == null || dateBeginContract.equals(""))
	            			&&(dateEndContract == null || dateEndContract.equals(""))){
                	if (justActiveContracts != null
	                        && justActiveContracts.booleanValue()){
	                        
                		if (grantContractRegime.getGrantContract().getEndContractMotive()!= null && !grantContractRegime.getGrantContract().getEndContractMotive().equals("")){
    	            		continue;
    	            	}
                		if(!grantContractRegime.getContractRegimeActive()){
                			continue;
                         }
                	}
                	if (justDesactiveContracts != null
	                        && justDesactiveContracts.booleanValue()){
                		
                		if(grantContractRegime.getContractRegimeActive()
                				&& (grantContractRegime.getGrantContract().getEndContractMotive()== null 
                						|| grantContractRegime.getGrantContract().getEndContractMotive().equals(""))) {
	                    continue;
                		}
                	}
	            }
	            
	            if (validToTheDate != null){
	            	
	            	if (grantContractRegime.getDateBeginContract() == null || grantContractRegime.getDateEndContract() == null) {
	                    continue;
	                }
	            	if (DateFormatUtil.isBefore("yyyy-MM-dd",grantContractRegime.getDateEndContract(),validToTheDate)) {		                    
	                    continue;
	                }
	                if (DateFormatUtil.isAfter("yyyy-MM-dd",grantContractRegime.getDateBeginContract(),validToTheDate)) {
	                    continue;
	                }
	            	if (grantContractRegime.getGrantContract().getEndContractMotive()!= null && !grantContractRegime.getGrantContract().getEndContractMotive().equals("")){
	            		continue;
	            	}
	               
            	}
	            
	            if ((dateBeginContract != null && !dateBeginContract.equals("")) 
	            		&& (dateEndContract != null && !dateEndContract.equals(""))){
	            	
	            	if (grantContractRegime.getDateBeginContract() == null || grantContractRegime.getDateEndContract() == null) {
	                    continue;
	                }
	            	
	            	if (!grantContractRegime.belongsToPeriod(dateBeginContract,dateEndContract)) {		                    
	                    continue;
	                }
	            	
	            	if (justActiveContracts != null
	                        && justActiveContracts.booleanValue()){
	            		if(!grantContractRegime.getState().equals(new Integer(1))){
	            			continue;
	            		}      
                		if (grantContractRegime.getGrantContract().getEndContractMotive()!= null && !grantContractRegime.getGrantContract().getEndContractMotive().equals("")){
    	            		continue;
    	            	}
	            	}
	            	
	            	if (justDesactiveContracts != null
	                        && justDesactiveContracts.booleanValue()){
	            		if(grantContractRegime.getState().equals(new Integer(1))&& (grantContractRegime.getGrantContract().getEndContractMotive()== null 
        						|| grantContractRegime.getGrantContract().getEndContractMotive().equals(""))){
	            			continue;
	            		}
	                        
	            	}
	                        
	            }else if ((dateBeginContract != null && !dateBeginContract.equals("")) 
	            		&& (dateEndContract == null || dateEndContract.equals(""))){
	            	if (grantContractRegime.getDateBeginContract() == null) {
	                    continue;
	                }
	            	if (!grantContractRegime.getDateBeginContract().equals(dateBeginContract)){
	            		continue;
	            	}
	            	if (justActiveContracts != null
	                        && justActiveContracts.booleanValue()){
	            		if(!grantContractRegime.getState().equals(new Integer(1))){
	            			continue;
	            		}      
                		if (grantContractRegime.getGrantContract().getEndContractMotive()!= null && !grantContractRegime.getGrantContract().getEndContractMotive().equals("")){
    	            		continue;
    	            	}
	            	}
	            	
	            	if (justDesactiveContracts != null
	                        && justDesactiveContracts.booleanValue()){
	            		if(grantContractRegime.getState().equals(new Integer(1))&& (grantContractRegime.getGrantContract().getEndContractMotive()== null 
        						|| grantContractRegime.getGrantContract().getEndContractMotive().equals(""))){
	            			continue;
	            		}
	            	}
	            }
	            	
	            if (grantTypeId != null) {
	            	if(!grantContractRegime.getGrantContract().getGrantType().getIdInternal().equals(grantTypeId)){
	            		continue;
	            	}
	            }

	            result++;
	        }
	       return result;
	  }
      
}
