package middleware.middlewareDomain;

public class MWTiposPrecendenciaIleec
{

    private String descricao;
    private Integer tipoPrecedencia;

    public String getDescricao()
    {
        return this.descricao;
    }
    public void setDescricao(String param)
    {
        this.descricao = param;
    }

    public Integer getTipoPrecedencia()
    {
        return this.tipoPrecedencia;
    }
    public void setTipoPrecedencia(Integer param)
    {
        this.tipoPrecedencia = param;
    }

    public String toString()
    {
        return " [descricao] " + descricao + " [tipo_precedencia] " + tipoPrecedencia;
    }

    public boolean equals(Object obj)
    {
        boolean resultado = false;
        if (obj instanceof MWTiposPrecendenciaIleec)
        {
            MWTiposPrecendenciaIleec mwtpi = (MWTiposPrecendenciaIleec) obj;
            resultado =
                getTipoPrecedencia().equals(mwtpi.getTipoPrecedencia())
                    && getDescricao().equals(mwtpi.getDescricao());
            }
        return resultado;
    }

}
