/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package groovyfp.csv

import static com.xlson.groovycsv.CsvParser.parseCsv

import java.nio.file.Path
import java.nio.file.Paths

/**
 * This class serves as convention for reading csv files
 *
 * @author Mario Garcia
 */
class CsvReaderAware {

    /**
     * This method tries to read a file with the same name as the actual
     * Java node
     *
     * @return Iterable object with every line
     */
    def getCsv() {

        return getCsvFrom(
            Paths.get(getClass().getResource("${this.getClass().simpleName}.csv").toURI())
        )

    }

    def getCsvFrom(Path path) {

        return parseCsv(
            new BufferedReader(
                new FileReader(path.toFile().absolutePath)
            )
        )

    }

}
