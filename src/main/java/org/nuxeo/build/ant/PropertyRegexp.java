/*
 * (C) Copyright 2006-2014 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Nuxeo - initial API and implementation
 *
 */

package org.nuxeo.build.ant;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * TODO NXBT-258
 */
public class PropertyRegexp extends Task {

    String property;

    String pattern;

    String input;

    int select = 0;

    public void setProperty(String property) {
        this.property = property;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * @param input file path
     */
    public void setInput(String input) {
        this.input = input;
    }

    public void setSelect(int select) {
        this.select = select;
    }

    @Override
    public void execute() {
        try {
            String inputString = readFileAsString(input);
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(inputString);
            String findString = "";
            if (m.find()) {
                findString = m.group(select);
            }
            getProject().setProperty(property, findString);
        } catch (IOException e) {
            AntClient.getInstance().log("Couldn't read " + input, e,
                    Project.MSG_ERR);
        }
    }

    private static String readFileAsString(String filePath) throws IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            fileData.append(buf, 0, numRead);
        }
        reader.close();
        return fileData.toString();
    }

}
