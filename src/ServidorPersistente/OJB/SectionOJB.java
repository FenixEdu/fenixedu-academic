/*
 * SeccaoOJB.java
 *
 * Created on 23 de Agosto de 2002, 16:58
 */

package ServidorPersistente.OJB;

/**
 *
 * @author  ars
 */

import java.util.List;

import org.odmg.QueryException;

import Dominio.ISection;
import Dominio.ISite;
import Dominio.Section;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentSection;

public class SectionOJB extends ObjectFenixOJB implements IPersistentSection {

	/** Creates a new instance of SeccaoOJB */
	public SectionOJB() {
	}

	public ISection readBySiteAndSectionAndName(
		ISite site,
		ISection superiorSection,
		String name)
		throws ExcepcaoPersistencia {
		try {
			ISection resultSection = null;
			Section section = (Section) superiorSection;
			String oqlQuery = "select section from " + Section.class.getName();
			oqlQuery += " where name = $1 ";
			if (section == null) {
				
				oqlQuery += " and site.executionCourse.nome = $2";
				oqlQuery += " and site.executionCourse.executionPeriod.name = $3";
				oqlQuery += " and site.executionCourse.executionPeriod.executionYear.year = $4";
			} else {
				
			
				oqlQuery += " and superiorSection.name = $2";
				oqlQuery += " and superiorSection.site.executionCourse.nome = $3";
				oqlQuery += " and superiorSection.site.executionCourse.executionPeriod.name = $4";
				oqlQuery += " and superiorSection.site.executionCourse.executionPeriod.executionYear.year = $5";}

			query.create(oqlQuery);
			query.bind(name);

			if (section == null) {
				query.bind(site.getExecutionCourse().getNome());
				query.bind(
					site.getExecutionCourse().getExecutionPeriod().getName());
				query.bind(
					site
						.getExecutionCourse()
						.getExecutionPeriod()
						.getExecutionYear()
						.getYear());
			} else {
				query.bind(section.getName());
				query.bind(section.getSite().getExecutionCourse().getNome());
								query.bind(section.getSite().getExecutionCourse().getExecutionPeriod().getName());
								query.bind(section.getSite()
										.getExecutionCourse()
										.getExecutionPeriod()
										.getExecutionYear()
										.getYear());
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

	public void lockWrite(ISection section) throws ExcepcaoPersistencia {
		super.lockWrite(section);
	}

	public void delete(ISection section) throws ExcepcaoPersistencia {
		super.delete(section);
	}

	public void deleteAll() throws ExcepcaoPersistencia {
		String oqlQuery = "select all from " + Section.class.getName();
		super.deleteAll(oqlQuery);
	}

}
