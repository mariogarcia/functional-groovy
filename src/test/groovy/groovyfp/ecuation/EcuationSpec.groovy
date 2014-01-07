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
        given: 'A simple ecuation'
            def ecuation = { _2x + _3x == 25 }
        when: 'Using an ecuation resolver'
            def resolver = new EcuationResolver()
        then: 'We should be able to resolve the problem'
            resolver.resolve(ecuation).with(5)
    }

}


