/*
 * SectionOJB.java
 *
 * Created on 23 de Agosto de 2002, 16:58
 */

package ServidorPersistente.OJB;

/**
 *
 * @author  ars
 * @author  lmac1
 */

import java.util.Iterator;
import java.util.List;

import org.odmg.QueryException;

import Dominio.IItem;
import Dominio.ISection;
import Dominio.ISite;
import Dominio.Item;
import Dominio.Section;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class SectionOJB extends ObjectFenixOJB implements IPersistentSection {

	/** Creates a new instance of SeccaoOJB */
	public SectionOJB() {
	}

	public ISection readBySiteAndSectionAndName(
		ISite site,
		ISection superiorSection,
		String name)
		throws ExcepcaoPersistencia {
			
			Section section = null;
			section = (Section) superiorSection;
		try {
			
			ISection resultSection = null;
			
			String oqlQuery = "select section from " + Section.class.getName();
			oqlQuery += " where name = $1 ";
			oqlQuery +=	" and site.executionCourse.code = $2";
			oqlQuery += " and site.executionCourse.executionPeriod.name = $3";
			oqlQuery += " and site.executionCourse.executionPeriod.executionYear.year = $4";
			if (section == null) {
				oqlQuery += " and is_undefined(keySuperiorSection) " ;
			} else {
				oqlQuery += " and keySuperiorSection = $5";
			}

			query.create(oqlQuery);
			
			query.bind(name);
			query.bind(site.getExecutionCourse().getSigla());
			query.bind(site.getExecutionCourse().getExecutionPeriod().getName());
			query.bind(site.getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear());
			
			if (section != null) {
			
				query.bind(section.getInternalCode());			
			}

			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0)
				resultSection = (ISection) result.get(0);
			return resultSection;
		} catch (QueryException queryEx) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, queryEx);
		}
	}


  //reads imediatly inferior sections of a section
	public List readBySiteAndSection(ISite site,ISection superiorSection)
		throws ExcepcaoPersistencia {
		
	try {
			
		Section section = (Section) superiorSection;
		String oqlQuery = "select all from " + Section.class.getName();
		oqlQuery +=	" where site.executionCourse.code = $1";
		oqlQuery += " and site.executionCourse.executionPeriod.name = $2";
		oqlQuery += " and site.executionCourse.executionPeriod.executionYear.year = $3";
		if (section == null) {
				
			oqlQuery += " and is_undefined(keySuperiorSection) " ;
				
		} else {
				
			oqlQuery += " and keySuperiorSection = $4";
			}

		query.create(oqlQuery);
			
		query.bind(site.getExecutionCourse().getSigla());
		query.bind(site.getExecutionCourse().getExecutionPeriod().getName());
		query.bind(site.getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear());
	
			
		 
		if (section != null) {
			System.out.println(oqlQuery);
			query.bind(section.getInternalCode());			
		 
		}	
		
		List result = (List) query.execute();		
		lockRead(result);
		
		return result;
	} catch (QueryException queryEx) {
		throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, queryEx);
	}
}

public List readBySite(ISite site) throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select section from " + Section.class.getName();
			oqlQuery +=	" where site.executionCourse.code = $1";
			oqlQuery += " and site.executionCourse.executionPeriod.name = $2";
			oqlQuery += " and site.executionCourse.executionPeriod.executionYear.year = $3";

			query.create(oqlQuery);
			
			query.bind(site.getExecutionCourse().getSigla());
			query.bind(site.getExecutionCourse().getExecutionPeriod().getName());
			query.bind(site.getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear());

			List result = (List) query.execute();
			lockRead(result);
			
			return result;
		} catch (QueryException queryEx) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, queryEx);
		}
	}
	
	public List readAll() throws ExcepcaoPersistencia {
		 try {
			 String oqlQuery = "select section from " + Section.class.getName();
			 query.create(oqlQuery);
			 List result = (List) query.execute();
			 lockRead(result);
			 return result;
		 } catch (QueryException ex) {
			 throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		 }
	 }
	 
	public void lockWrite(ISection section) throws ExcepcaoPersistencia, ExistingPersistentException {

		// If there is nothing to write, simply return.
		if (section == null) { return;}
		
		ISection sectionFromDB = this.readBySiteAndSectionAndName(section.getSite(), section.getSuperiorSection(), section.getName());
		
		// If section is not in database, then write it.
		if (sectionFromDB == null){
			super.lockWrite(section);
		}
		// else If the section is mapped to the database, then write any existing changes.
		else if ((section instanceof Section) && ((Section) sectionFromDB).getInternalCode().equals(
			((Section) section).getInternalCode())) {
										
					super.lockWrite(section);
					
					// else Throw an already existing exception
				} else{
					throw new ExistingPersistentException();
				}
	}

	public void delete(ISection section) throws ExcepcaoPersistencia {
					
		ISection inferiorSection = section;
		SectionOJB inferiorSectionOJB = new SectionOJB();			
				
		List imediatlyInferiorSections = readBySiteAndSection(section.getSite(),  section);
		  
		   Iterator iterator = imediatlyInferiorSections.iterator();
		   while(iterator.hasNext())
		        {
		    	 inferiorSection = (ISection) iterator.next();
	  		     delete(inferiorSection);
		        }
		    
//	  		super.delete(inferiorSection);
	


		try {

			IItem item = null;
			ItemOJB itemOJB = new ItemOJB();
			String oqlQuery = "select all from " + Item.class.getName();
			oqlQuery
			+= " where section.name = $1 and section.site.executionCourse.code = $2 "
			+ "and  section.site.executionCourse.executionPeriod.name = $3  "
			+ "and  section.site.executionCourse.executionPeriod.executionYear.year = $4  ";
			query.create(oqlQuery);
			query.bind(section.getName());
			query.bind(section.getSite().getExecutionCourse().getSigla());
			query.bind(section.getSite().getExecutionCourse().getExecutionPeriod().getName());
			query.bind(section.getSite().getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear());
						
			List result = (List) query.execute();
			lockRead(result);
			
			Iterator iterador = result.iterator();
			while (iterador.hasNext()) {
				item = (IItem) iterador.next();
				itemOJB.delete(item);
			}
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
		
	
		super.delete(section);
		
		
	}


	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + Section.class.getName();
		super.deleteAll(oqlQuery);
	}

	

}
