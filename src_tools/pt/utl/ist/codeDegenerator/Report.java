package pt.utl.ist.codeDegenerator;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Report {

	private int dirCount = 0;

	private Map<String, Integer> processedFileRegistry = new HashMap<String, Integer>();

	private Map<String, Integer> changedRegistry = new HashMap<String, Integer>();

	public void registerDirAsProccessed(final File file) {
		dirCount++;
	}

	public void registerFileAsProccessed(final File file) {
		register(processedFileRegistry, file);
	}

	public void registerFileAsChanged(final File file) {
		register(changedRegistry, file);
	}

	private void register(final Map<String, Integer> registry, final File file) {
		final String filenameSuffix = extractSuffix(file.getName());
		final int currentCount = registry.containsKey(filenameSuffix) ?
				registry.get(filenameSuffix).intValue() : 0;
		registry.put(filenameSuffix, Integer.valueOf(currentCount + 1));
	}

	private String extractSuffix(final String filename) {
		final int posOfDot = filename.lastIndexOf('.');
		return (posOfDot > 0) ? filename.substring(posOfDot) : "";
	}

	public void writeReport() {
		System.out.println();
		System.out.println("Processed " + dirCount + " directories.");

		int totalProcessed = 0;
		int totalChanged = 0;
		for (final Entry<String, Integer> entry : processedFileRegistry.entrySet()) {
			final String filenameSuffix = entry.getKey();
			final int processed = entry.getValue().intValue();
			final int changed = changedRegistry.containsKey(filenameSuffix) ?
					changedRegistry.get(filenameSuffix).intValue() : 0;
			System.out.println("Processed/Changed " + filenameSuffix + " : " + processed + "/" + changed);

			totalProcessed += processed;
			totalChanged += changed;
		}

		System.out.println("Total Processed " + totalProcessed);
		System.out.println("Total Changed " + totalChanged);
	}

}
