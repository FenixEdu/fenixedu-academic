package ServidorPersistente;

import java.util.List;

import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.DegreeCurricularPlanState;
import Util.TipoCurso;

public interface IPersistentDegreeCurricularPlan extends IPersistentObject {

	public List readAll() throws ExcepcaoPersistencia;
	public void write(IDegreeCurricularPlan planoCurricular) throws ExcepcaoPersistencia, ExistingPersistentException;
	public void deleteDegreeCurricularPlan(IDegreeCurricularPlan planoCurricular) throws ExcepcaoPersistencia;
	
	public IDegreeCurricularPlan readByNameAndDegree(String name, ICurso degree) throws ExcepcaoPersistencia;
	public List readByDegree(ICurso degree) throws ExcepcaoPersistencia;
	public List readByDegreeAndState(ICurso degree,DegreeCurricularPlanState state) throws ExcepcaoPersistencia;
    public List readByDegreeTypeAndState(TipoCurso degreeType, DegreeCurricularPlanState state) throws ExcepcaoPersistencia;
}