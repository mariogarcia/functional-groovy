package groovyfp.categories

import static Maybe.just
import static Maybe.nothing

import spock.lang.Specification

/**
 *
 */
class MaybeSpec extends Specification {

    void 'Applicative: Maybe applicative implementation'() {
        when: 'using a function to increment a value'
            def inc = { Integer v -> v + 1 } as Function
        then: 'if the value is nothing the function shouldnt be applied'
            nothing().fapply(just(inc)).value == null
        and: 'otherwise if the initial value is correct the function will work'
            just(1).fapply(just(inc)).value == 2
    }
   
}

