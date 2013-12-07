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

/**
 * This service does some calculations over a dataset of NBA games
 * using some functional programming techniques such as
 *
 * folding (reduce), recursion...
 *
 * @author Mario Garcia
 */
class NbaGameService extends CsvReaderAware {

    /**
     * This method return the first 10 teams by winning games
     *
     * FP concept: FOLD
     *
     * @param year
     * @return Name of the first 10 teams
     */
    List<String> getTop10TeamsByWinningGamesAndYear(Integer year) {

        def aggregator = [:]
        def collector = { acc, line ->
            def key = line.with { visitorPoints > homePoints ? visitor : home }
            def val = acc.get(key, 0)
            acc[key] = val + 1
            acc
        }
        def desc = { a, b -> b.value <=> a.value }

        return csv.inject(aggregator, collector).entrySet().sort(desc).key.take(10)

    }

    /**
     * This method sums all points of all games during a given
     * year
     *
     * FP concept: recursion
     *
     * @param year the year to sum all points from
     * @return summing up all points marked a given year
     */
    Integer sumAllPointsByYear(Integer year) {

        def sumLines

        sumLines = { accumulator, lines ->

            return !lines.hasNext() ?
                accumulator :
                sumLines.trampoline(
                    accumulator + lines.next()?.with { visitorPoints.toInteger() + homePoints.toInteger() } ?: 0,
                    lines
                )

        }.trampoline()

        return sumLines(0, csv)

    }

}
