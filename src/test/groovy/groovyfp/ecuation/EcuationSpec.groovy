package groovyfp.ecuation

import spock.lang.Specification

/**
 *
 */
class EcuationSpec extends Specification {

    def 'Resolving some ecuations'() {
        expect: 'That the ecuation result is true'
            ecuation.curry(solution_value)
        where: 'The ecuation cases are'
                        ecuation                | solution_value
            { x -> (2 * x) + (3 * x) == 25 }    |       5
    }

    def 'Using ecuation resolver'() {
        when: 'Using an ecuation resolver'
            def resolver = new EcuationResolver()
        then: 'We should be able to check the result'
            resolver.check(ecuation).with(values)
        where: 'Possible type of ecuations could be'
                    ecuation                |      values
            { it -> _2x + _3x == 25 }       |      [x:5]
            { it -> _2x + _x == 9 }         |      [x:3]
            { it -> _2x + _3y + 1 == 10 }   |      [x:3, y:1]
    }



}


