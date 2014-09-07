package groovyfp.categories

import static Maybe.just
import static Maybe.nothing
import groovyfp.categories.Maybe.Nothing

import spock.lang.Specification

/**
 *
 */
class MaybeSpec extends Specification {

    void 'Applicative: Maybe applicative implementation'() {
        when: 'combining two closures'
            def inc = { Integer v -> v + 1 } 
            def byTwo = { Integer v -> v * 2 }
        and: 'using the combination as a Function'
            def combination = (inc >> byTwo) as Function
        then: 'if the value is nothing the function shouldnt be applied'
            nothing().fapply(just(combination)).value == null
        and: 'otherwise if the initial value is correct the function will work'
            just(1).fapply(just(combination)).value == 4
    }
   
}

