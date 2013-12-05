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
package groovyfp.ac

import groovyfp.csv.CsvReaderAware

class NbaGameService extends CsvReaderAware {

    /**
     * This method return the first 10 teams by winning games
     *
     * @param year
     * @return Name of the first 10 teams
     */
    List<String> getTop10TeamsByWinningGamesAndYear(Integer year) {

        def aggregator = [:]
        def collector = { acc, line ->
            def key = line.visitorPoints > line.homePoints ? line.visitor : line.home
            def val = acc.get(key, 0)
            acc[key] = val + 1
            acc
        }
        def desc = { a, b -> b.value <=> a.value }

        return csv.inject(aggregator, collector).entrySet().sort(desc).key.take(10)

    }

}
