/*
 * JavaTipoAlunoL2SqlTipoAlunoLFieldConversion.java
 *
 * Created on 21 de Novembro de 2002, 15:57
 */

package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.TipoAlunoL;

public class JavaTipoAlunoL2SqlTipoAlunoLFieldConversion implements FieldConversion {
    
    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source)
    {
        if (source instanceof TipoAlunoL)
        {
            TipoAlunoL s = (TipoAlunoL) source;
            return s.getTipoAlunoL();
        }
        else {
            return source;
        }
    }    

   /*
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source)
    {
        if (source instanceof Integer)
        {
            Integer src = (Integer) source;
            return new TipoAlunoL(src);
        }
        else
        {
            return source;
        }
    }    
}
