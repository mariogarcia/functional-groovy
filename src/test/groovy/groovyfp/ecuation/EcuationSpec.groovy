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

    def 'Using ecuation resolver to check ecuation solutions'() {
        when: 'Using an ecuation resolver'
            def resolver = new EcuationResolver()
            def results = resolver.check(ecuation).with(values)
        then: 'We should be able to check the result'
            results.every { partial -> partial == true}
        where: 'Possible type of ecuations could be'
                    ecuation                |      values
            { it -> _2x + _3x == 25 }       |      [x:5]
            { it -> _2x + _x == 9 }         |      [x:3]
            { it -> _2x + _3y + 1 == 10 }   |      [x:3, y:1]
    }

    def 'Using ecuation resolver to check ecuation system solutions'() {
        given: 'An ecuation resolver'
            def resolver = new EcuationResolver()
        and: 'Two ecuations building a system'
            def one = { it -> _2x + _3y == 25 }
            def two = { it -> _x + _y == 10 }
        when: 'Resolving all ecuations'
            def results = resolver.check(one, two).with(x: 5, y: 5)
        then: 'We should be able to check the result'
            results.every { partial -> partial == true}
    }

    def 'Resolving a simple ecuation'() {
        given: 'A simple ecuation'
            def resolver = new EcuationResolver()
            def simple = { it -> _2x + _3x == 25 }
        expect: 'To resolve the variable'
            resolver.resolve(simple) == [x: 5]
    }

}


