package ServidorPersistente;

import java.util.List;

import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import Util.DegreeCurricularPlanState;
import Util.TipoCurso;

public interface IPersistentDegreeCurricularPlan extends IPersistentObject {

    public List readAll() throws ExcepcaoPersistencia;

    public void deleteDegreeCurricularPlan(IDegreeCurricularPlan planoCurricular)
            throws ExcepcaoPersistencia;

    public IDegreeCurricularPlan readByNameAndDegree(String name, ICurso degree)
            throws ExcepcaoPersistencia;

    public List readByDegree(ICurso degree) throws ExcepcaoPersistencia;

    public List readByDegreeAndState(ICurso degree, DegreeCurricularPlanState state)
            throws ExcepcaoPersistencia;

    public List readByDegreeTypeAndState(TipoCurso degreeType, DegreeCurricularPlanState state)
            throws ExcepcaoPersistencia;
}