package pt.utl.ist.struts;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;

public class StrutsMappingGenerator {

    public static void main(String[] args) throws IOException {
	
	File file = new File(args[1]);
	file.createNewFile();
	
	FileWriter writer = new FileWriter(file);
	
	writeHeader(writer);
	
	writer.append("struts.files=");
	String[] fileNames = getFileNames(args[0]);
	
	int i = fileNames.length;
	for (String name : fileNames) {
	    writer.append(name);
	    i--;
	    if ( i > 0 ) {
		writer.append(",");
	    }
	    
	}
	
	writer.close();
    }

    private static void writeHeader(FileWriter writer) throws IOException {
	writer.append("# This is a generated file, please do not modify it\n# Properties are used to load automatically struts mapping configuration\n");
    }

    private static String[] getFileNames(String directoryName) {
	File directory = new File(directoryName);

	return directory.list(new FilenameFilter() {
	    public boolean accept(File dir, String name) {
		return name.matches("struts-.*\\.xml") && !name.endsWith("-default.xml");
	    }
	});
    }
}
