package Tools;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class MakeCloner
{

    public static boolean DEBUG = true;

    protected static void collectPropertyTypes(String className, HashSet types)
    {
        try
        {
            Class classObj = Class.forName(className);
            PropertyDescriptor[] pDescs = Introspector.getBeanInfo(classObj)
                    .getPropertyDescriptors();
            for (int i = 0; i < pDescs.length; i++)
            {
                Class type = pDescs[i].getPropertyType();
                if (type == null)
                {
                    System.err.println("A property with type 'null': "
                            + className + "." + pDescs[i].getName());
                }
                else
                {
                    types.add(type.getName());
                }
            }
        }
        catch (ClassNotFoundException cnfe)
        {
            System.err.println("Class name not valid: " + className);
        }
        catch (IntrospectionException ie)
        {
            System.err.println("Error on Introspection: " + ie.getMessage());
        }
    }

    protected static void descendDirectory(String topDirName, HashSet types)
    {
        File topDir = new File(topDirName);
        if (!topDir.isDirectory())
        {
            System.err.println("Specified filename is not a directory: "
                    + topDirName);
        }
        else
        {
            descendDirectoryFiles(topDir, "", types);
        }
    }

    protected static void descendDirectoryFiles(File dir, String packagePrefix,
            HashSet types)
    {
        File[] fileList = dir.listFiles();
        for (int i = 0; i < fileList.length; i++)
        {
            File file = fileList[i];
            if (file.isDirectory())
            {
                descendDirectoryFiles(file, packagePrefix + file.getName()
                        + ".", types);
            }
            else
            {
                String fileName = file.getName();
                if (fileName.endsWith(".class"))
                {
                    collectPropertyTypes(packagePrefix
                            + fileName.substring(0, fileName.lastIndexOf(".")),
                            types);
                }
            }
        }
    }

    public static void oldMain(String[] args)
    {
        HashSet types = new HashSet();

        descendDirectory(args[0], types);

        // Dump to stdout the type-names found
        Iterator it = types.iterator();
        while (it.hasNext())
        {
            String typeName = (String) it.next();
            System.out.println(typeName);
        }
    }

    public static void main(String[] args)
    {
        if (args.length != 2)
        {
            System.err.println("Usage: java MakeCloner filename, filename");
            System.exit(1);
        }

        BufferedReader in = null;
        ClonerInfo clonerInfo = null;
        try
        {
            in = new BufferedReader(new FileReader(args[0]));
            clonerInfo = readCloningMap(in);
        }
        catch (FileNotFoundException fnfe)
        {
            System.err.println("Cannot read file " + args[0]);
            System.exit(1);
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException ioe)
                {
                }
            }
        }

        if (clonerInfo != null)
        {
            try
            {
                generateCloner(new PrintWriter(new FileWriter(args[1]), true),
                        clonerInfo);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                System.out.println("Output file not Found!");
            }
        }
    }

    public static ClonerInfo readCloningMap(BufferedReader in)
    {
        HashSet types = new HashSet();
        HashMap clonerMap = new HashMap();
        try
        {
            String line = in.readLine();
            while (line != null)
            {
                boolean generate = !line.startsWith("#");
                if (!generate)
                {
                    line = line.substring(1);
                }
                String[] classes = line.split("\\s+");
                if (classes.length != 2)
                {
                    System.err.println("Ignoring mal-formed line: '" + line
                            + "'");
                }
                else
                {
                    try
                    {
                        ClonerFunction clFn = new ClonerFunction(generate,
                                classes[0], classes[1]);
                        clonerMap.put(clFn.getSourceType(), clFn);
                        clFn.registerTypes(types);
                    }
                    catch (ClassNotFoundException cnfe)
                    {
                        System.err
                                .println("Ignoring line with unknown class: '"
                                        + line + "'");
                    }
                }
                line = in.readLine();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return new ClonerInfo(types, clonerMap);
    }

    public static void generateCloner(PrintWriter out, ClonerInfo clonerInfo)
    {
        out.println("package DataBeans.util;");
        out.println("import org.apache.ojb.broker.VirtualProxy;");

        // write imports
        Iterator it = clonerInfo.getTypes().iterator();
        while (it.hasNext())
        {
            out.print("import ");
            out.print((String) it.next());
            out.println(";");
        }

        out.println("public abstract class NewCloner {");

        out.println("    private void assertClass(Object obj, Class klass) {");
      //  out.println("        if (obj instanceof VirtualProxy) {");
//        out.println("           obj=((VirtualProxy)obj).getRealSubject();");
     //   out.println("        }");
//        out.println("        if (obj.getClass() != klass) {");
//        out.println("           obj=((VirtualProxy)obj).getRealSubject();");
//        out.println("        if (obj.getClass() != klass) {");
//        out
//                .println("            throw new Error(\"Assertion failed: object is not from expected class\");");
//        out.println("        }");
//        out.println("        }");
        out.println("    }");

        HashMap clonerMap = clonerInfo.getMap();
        it = clonerMap.values().iterator();
        while (it.hasNext())
        {
            ClonerFunction clFn = (ClonerFunction) it.next();
            clFn.generateCode(out, clonerMap);
        }

        out.println("");
        out.println("}");
    }

    public static void warning(String msg)
    {
        if (DEBUG)
        {
            System.err.println(msg);
        }
    }

    public static class ClonerInfo
    {
        private HashSet types;

        private HashMap map;

        public ClonerInfo(HashSet types, HashMap map)
        {
            this.types = types;
            this.map = map;
        }

        public HashSet getTypes()
        {
            return types;
        }

        public HashMap getMap()
        {
            return map;
        }
    }

    public static class ClonerFunction
    {
        private boolean generate;

        private String sourcePlainName;

        private String targetPlainName;

        private Class sourceType;

        private Class sourceConcreteType;

        private Class targetType;

        private Class targetConcreteType;

        private String name;

        public ClonerFunction(boolean generate, String sourceClassName,
                String targetClassName) throws ClassNotFoundException
        {
            this.generate = generate;
            this.sourcePlainName = onlyClassName(sourceClassName);
            this.targetPlainName = onlyClassName(targetClassName);
            this.sourceType = Class.forName(sourceClassName);
            this.targetType = Class.forName(targetClassName);
            this.name = "copy" + sourcePlainName + "2" + targetPlainName;

            this.sourceConcreteType = concreteTypeFor(sourceType);
            this.targetConcreteType = concreteTypeFor(targetType);

            // we can only deal with Info -> Dominio or the other way around
            if (!((isDomainType(this.sourceConcreteType) && isInfoType(this.targetConcreteType)) || (isDomainType(this.targetConcreteType) && isInfoType(this.sourceConcreteType))))
            {
                warning("ERROR: we can only deal with Info -> Dominio or the other way around");
                throw new Error(
                        "ERROR: we can only deal with Info -> Dominio or the other way around");
            }
        }

        private boolean isInfoType(Class typeClass)
        {
            return DataBeans.InfoObject.class.isAssignableFrom(typeClass);
        }

        private boolean isDomainType(Class typeClass)
        {
            return Dominio.IDomainObject.class.isAssignableFrom(typeClass);
        }

        public void registerTypes(HashSet types)
        {
            types.add(sourceType.getName());
            types.add(sourceConcreteType.getName());
            types.add(targetType.getName());
            types.add(targetConcreteType.getName());
        }

        private Class concreteTypeFor(Class type) throws ClassNotFoundException
        {
            if (type.isInterface())
            {
                Class concrete = Class.forName(type.getPackage().getName()
                        + "." + onlyClassName(type.getName()).substring(1));
                if (!type.isAssignableFrom(concrete))
                {
                    warning("WARNING: heuristic failed: couldn't find a concrete type for interface "
                            + type);
                    return null;
                }
                return concrete;
            }
            else
            {
                return type;
            }
        }

        private String onlyClassName(String className)
        {
            int pos = className.lastIndexOf('.');
            return (pos == -1) ? className : className.substring(pos + 1);
        }

        public Class getSourceType()
        {
            return sourceType;
        }

        public Class getTargetType()
        {
            return targetType;
        }

        public String getName()
        {
            return name;
        }

        public void generateCode(PrintWriter out, HashMap clonerMap)
        {

            newline(out);
            out.print("public ");
            if (!generate)
            {
                out.print("abstract ");
            }
            out.print(targetPlainName);
            out.print(" ");
            out.print(name);
            out.print("(");
            out.print(sourcePlainName);
            out.print(" source)");
            if (!generate)
            {
                out.print(";");
                return;
            }
            out.print(" {");
            indentMore();
            newline(out);
            out.println("if (source == null) { return null;");
            out.println(" }");
            newline(out);
            // insert assertion
            out.print("assertClass(source, ");
            out.print(sourceConcreteType.getName());
            out.print(".class);");
            newline(out);

            out.print(targetPlainName);
            out.print(" target = new ");
            out.print(onlyClassName(targetConcreteType.getName()));
            out.print("();");
            newline(out);
            out.print("target.setIdInternal(source.getIdInternal());");
            newline(out);

            generateCopyingCode(out, clonerMap);

            out.print("return target;");
            indentLess();
            newline(out);
            out.print("}");
            newline(out);
        }

        private void generateCodeForProperties(PrintWriter out,
                PropertyDescriptor sProp, PropertyDescriptor tProp,
                HashMap clonerMap)
        {

            if (Collection.class.isAssignableFrom(sProp.getPropertyType()))
            {
                comment(out, "!!!!! Found a collection: " + sProp.getName());
                comment(out, "  Ignoring it...");
                return;
            }

            Method readMethod = sProp.getReadMethod();
            Method writeMethod = tProp.getWriteMethod();
            Class sType = sProp.getPropertyType();
            Class tType = tProp.getPropertyType();
            if ((readMethod != null) && (writeMethod != null))
            {
                out.print("target.");
                out.print(writeMethod.getName());
                if (sType != tType)
                {
                    out.print("(");
                    ClonerFunction clFn = (ClonerFunction) clonerMap.get(sType);
                    out.print((clFn == null) ? "missingCopyFunction" : clFn
                            .getName());
                }
                out.print("(source.");
                out.print(readMethod.getName());
                out.print("()");
                if (sType != tType)
                {
                    out.print(")");
                }
                out.print(");");
                newline(out);
            }
            else
            {
                comment(out, "Ignoring copy from " + sProp.getName() + " to "
                        + tProp.getName());
            }
        }

        private ArrayList getPropertiesFor(Class typeClass)
        {
            Class rootClass = null;

            if (isDomainType(typeClass))
            {
                rootClass = Dominio.DomainObject.class;
            }
            else if (isInfoType(typeClass))
            {
                rootClass = DataBeans.InfoObject.class;
            }
            else
            {
                warning("WARNING: couldn't find expected superclass for "
                        + typeClass);
                rootClass = Object.class;
            }

            try
            {
                PropertyDescriptor[] props = Introspector.getBeanInfo(
                        typeClass, rootClass).getPropertyDescriptors();
                return new ArrayList(Arrays.asList(props));
            }
            catch (IntrospectionException ie)
            {
                System.err
                        .println("Error on Introspection: " + ie.getMessage());
                return null;
            }
        }

        private void generateCopyingCode(PrintWriter out, HashMap clonerMap)
        {
            ArrayList sourceDescs = getPropertiesFor(sourceConcreteType);
            ArrayList targetDescs = getPropertiesFor(targetConcreteType);

            // first process the ones with the same name, where "same" means
            // that "person" matches "infoPerson"
            Iterator it = sourceDescs.iterator();
            while (it.hasNext())
            {
                PropertyDescriptor sProp = (PropertyDescriptor) it.next();
                String sName = sProp.getName();
                PropertyDescriptor tProp = getPropertyNamed(targetDescs, sName);
                if (tProp != null)
                {
                    it.remove();
                    targetDescs.remove(tProp);
                    generateCodeForProperties(out, sProp, tProp, clonerMap);
                }
            }

            // now, consider the cases when there is a cloning function between
            // two types
            it = sourceDescs.iterator();
            while (it.hasNext())
            {
                PropertyDescriptor sProp = (PropertyDescriptor) it.next();
                Class sType = sProp.getPropertyType();
                ClonerFunction propClFn = (ClonerFunction) clonerMap.get(sType);
                if (propClFn != null)
                {
                    PropertyDescriptor tProp = getPropertyByType(out,
                            targetDescs, propClFn.getTargetType());
                    if (tProp != null)
                    {
                        comment(out, "HEURISTIC: assuming that "
                                + sProp.getName() + " corresponds to "
                                + tProp.getName());
                        it.remove();
                        targetDescs.remove(tProp);
                        generateCodeForProperties(out, sProp, tProp, clonerMap);
                    }
                }
            }

            // report differences...
            reportMissingProperties(out, sourceDescs,
                    isInfoType(sourceConcreteType));
            reportMissingProperties(out, targetDescs,
                    isInfoType(targetConcreteType));
        }

        private void reportMissingProperties(PrintWriter out, ArrayList props,
                boolean isFromInfo)
        {
            Iterator it = props.iterator();
            while (it.hasNext())
            {
                PropertyDescriptor prop = (PropertyDescriptor) it.next();
                if (isFromInfo)
                {
                    comment(out,
                            "WARNING: a property of an InfoObject is missing in the DomainObject: "
                                    + prop.getName());
                }
                else
                {
                    comment(out,
                            "INFO: domain-property is not in the InfoObject: "
                                    + prop.getName());
                }
            }
        }

        private PropertyDescriptor getPropertyNamed(ArrayList descs, String name)
        {
            for (int i = 0; i < descs.size(); i++)
            {
                PropertyDescriptor prop = (PropertyDescriptor) descs.get(i);
                String pName = prop.getName();
                if (pName.equals(name)) { return prop; }
                if (pName.startsWith("info")
                        && pName.substring(4).equals(capitalize(name))) { return prop; }
                if (name.startsWith("info")
                        && name.substring(4).equals(capitalize(pName))) { return prop; }
            }
            return null;
        }

        private String capitalize(String str)
        {
            if ((str == null) || Character.isUpperCase(str.charAt(0)))
            {
                return str;
            }
            else
            {
                return Character.toUpperCase(str.charAt(0)) + str.substring(1);
            }
        }

        private PropertyDescriptor getPropertyByType(PrintWriter out,
                ArrayList descs, Class type)
        {
            PropertyDescriptor found = null;
            for (int i = 0; i < descs.size(); i++)
            {
                PropertyDescriptor prop = (PropertyDescriptor) descs.get(i);
                if (prop.getPropertyType() == type)
                {
                    if (found != null)
                    {
                        comment(out,
                                "Found more than one property on the target for the source...");
                        comment(out, "    " + found.getName() + " and "
                                + prop.getName());
                        return null;
                    }
                    else
                    {
                        found = prop;
                    }
                }
            }
            return found;
        }

        private int indent = 4;

        private void indentMore()
        {
            indent += 4;
        }

        private void indentLess()
        {
            indent -= 4;
        }

        private void newline(PrintWriter out)
        {
            out.println();
            for (int i = 0; i < indent; i++)
            {
                out.print(" ");
            }
        }

        private void comment(PrintWriter out, String msg)
        {
            out.print("// ");
            out.print(msg);
            newline(out);
        }
    }
}
