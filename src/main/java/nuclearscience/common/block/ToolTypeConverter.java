package nuclearscience.common.block;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import nuclearscience.References;

public class ToolTypeConverter {

	private static String BLOCK_DIRECTORY = "src/main/resources/assets/nuclearscience/blockstates";
	private static String DATA_LOC = "src/main/resources/data/minecraft/tags/blocks/minable/blocknames.txt";

	public static void main(String[] args) {

		File blockDir = new File(BLOCK_DIRECTORY);
		FileWriter blockWriter;
		try {
			blockWriter = new FileWriter(new File(DATA_LOC));
			String json = ".json";

			for (int i = 0; i < blockDir.listFiles().length; i++) {
				File file = blockDir.listFiles()[i];
				String name = file.getName();
				name = name.substring(0, name.indexOf(json));
				name = "\"" + References.ID + ":" + name + "\"";
				if (!(i + 1 == blockDir.listFiles().length)) {
					name = name + ",";
				}
				System.out.println(name);
				blockWriter.write(name);
				blockWriter.write("\n");
			}
			blockWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
