package ServidorPersistente;

import java.util.List;

import Dominio.IDegree;
import Dominio.IDegreeCurricularPlan;
import Util.DegreeCurricularPlanState;
import Util.TipoCurso;

public interface IPersistentDegreeCurricularPlan extends IPersistentObject {

    public List readAll() throws ExcepcaoPersistencia;

    public void deleteDegreeCurricularPlan(IDegreeCurricularPlan planoCurricular)
            throws ExcepcaoPersistencia;

    public IDegreeCurricularPlan readByNameAndDegree(String name, IDegree degree)
            throws ExcepcaoPersistencia;

    public List readByDegree(IDegree degree) throws ExcepcaoPersistencia;

    public List readByDegreeAndState(IDegree degree, DegreeCurricularPlanState state)
            throws ExcepcaoPersistencia;

    public List readByDegreeTypeAndState(TipoCurso degreeType, DegreeCurricularPlanState state)
            throws ExcepcaoPersistencia;
}