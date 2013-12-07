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

import static org.junit.Assert.assertThat
import static org.hamcrest.CoreMatchers.is

import spock.lang.Specification

/**
 * This specs test all actions over the Nba games csv file
 *
 * @author Mario Garcia
 */
class NbaGameServiceSpec extends Specification {

    def 'folding: Getting the top 10 teams by winning games'() {
        given: 'The NBA service'
            def nbaService = new NbaGameService()
            def top10 = [
                'Brooklyn Nets', 'Charlotte Bobcats',
                'Minnesota Timberwolves', 'Utah Jazz',
                'Chicago Bulls', 'Phoenix Suns',
                'Dallas Mavericks', 'Miami Heat',
                'Cleveland Cavaliers', 'Milwaukee Bucks'
            ]
        when: 'Getting the top 10 teams'
            def resultList = nbaService.getTop10TeamsByWinningGamesAndYear(2012)
        then: 'The top ten are the expected'
            resultList.every { it in top10 }
    }

    def 'recursion: Summing up recursively all marked points during a given year'() {
        when: 'Calculating all points during a given year'
            def nbaService = new NbaGameService()
            def total = nbaService.sumAllPointsByYear(2012)
        then: 'We should get the total marked that year'
            assertThat total, is(241223)
    }

}
