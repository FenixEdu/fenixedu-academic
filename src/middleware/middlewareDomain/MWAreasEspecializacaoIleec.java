/*
 * Created on 5/Dez/2003
 *
 */
package middleware.middlewareDomain;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class MWAreasEspecializacaoIleec
{

    private Integer idAreaEspecializacao;
    private Integer maxCreditos;
    private String nome;

    public Integer getIdAreaEspecializacao()
    {
        return this.idAreaEspecializacao;
    }
    public void setIdAreaEspecializacao(Integer areaId)
    {
        this.idAreaEspecializacao = areaId;
    }


    public Integer getMaxCreditos()
    {
        return this.maxCreditos;
    }
    
    public void setMaxCreditos(Integer maxCreditos)
    {
        this.maxCreditos = maxCreditos;
    }

    public String getNome()
    {
        return this.nome;
    }
    public void setNome(String param)
    {
        this.nome = param;
    }

    public String toString()
    {
        return " [idAreaEspecializacao] "
            + idAreaEspecializacao
            + " [max_creditos] "
            + maxCreditos
            + " [nome] "
            + nome;
    }
    
}
