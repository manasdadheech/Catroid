/**
 *  Catroid: An on-device graphical programming language for Android devices
 *  Copyright (C) 2010  Catroid development team
 *  (<http://code.google.com/p/catroid/wiki/Credits>)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package at.ist.tugraz.catroid.test.code;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;
import at.tugraz.ist.catroid.utils.UtilFile;

public class SystemOutTest extends TestCase {

	private StringBuffer errorMessages;
	private boolean errorFound;

	private static final String[] DIRECTORIES = { "../catroidUiTest", "../catroidTest", "../catroid" };

	private void checkFileForSystemOut(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));

		int lineCount = 1;
		String line = null;

		while ((line = reader.readLine()) != null) {
			if (line.contains("System.out")) {
				errorFound = true;
				errorMessages.append(file.getName() + " in line " + lineCount + "\n");
			}
			++lineCount;
		}
	}

	public void testForBlockCharacters() throws IOException {
		errorMessages = new StringBuffer();
		errorFound = false;

		for (String directoryName : DIRECTORIES) {
			File directory = new File(directoryName);
			assertTrue("Couldn't find directory: " + directoryName, directory.exists() && directory.isDirectory());
			assertTrue("Couldn't read directory: " + directoryName, directory.canRead());

			List<File> filesToCheck = UtilFile.getFilesFromDirectoryByExtension(directory, new String[] { ".java", });
			for (File file : filesToCheck) {
				checkFileForSystemOut(file);
			}
		}

		assertFalse("Files with System.out found: \n" + errorMessages.toString(), errorFound);
	}
}
