package pt.ist.fenixframework.plugins.remote.servlet.request;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Modifier;

import javax.servlet.http.HttpServletResponse;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.pstm.DomainClassInfo;
import dml.DomainClass;
import dml.DomainModel;

public class RemoteHostRequest extends RemoteRequest {

    @Override
    protected int getStatus() {
        return HttpServletResponse.SC_OK;
    }

    @Override
    protected void writeResponseBody(Writer writer) throws IOException {
        writer.write("<domainClassInfos>");
        final DomainModel domainModel = FenixFramework.getDomainModel();
        for (final DomainClass domainClass : domainModel.getDomainClasses()) {
            final String className = domainClass.getFullName();
            try {
                final Class clazz = Class.forName(className);
                if (!Modifier.isAbstract(clazz.getModifiers())) {
                    final int mapClassToId = DomainClassInfo.mapClassToId(clazz);

                    writer.write("<domainClassInfo>");
                    writeTag(writer, "domainClassId", Integer.toString(mapClassToId));
                    writeTag(writer, "domainClassName", clazz.getName());
                    writer.write("</domainClassInfo>");
                }
            } catch (final ClassNotFoundException e) {
                throw new Error(e);
            }
        }
        writer.write("</domainClassInfos>");
    }

}
