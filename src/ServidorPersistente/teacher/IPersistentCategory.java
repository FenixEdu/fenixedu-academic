/*
 * Created on 11/Nov/2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package ServidorPersistente.teacher;

import java.util.List;

import Dominio.teacher.ICategory;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public interface IPersistentCategory extends IPersistentObject {

	public List readAll() throws ExcepcaoPersistencia;
	public ICategory readCategoryByCode(String code) throws ExcepcaoPersistencia;
}
