package groovyfp.categories

import static Either.right
import static Either.left

import spock.lang.Specification

class EitherSpec extends Specification {
    // tag::eithermonad1[]
    void 'Either monad'() {
        when: 'having a fmap function'
            def inc = { Integer v -> v + 1 }
        and: 'the monad bind function'
            def mfn = { x -> Either.right(inc(x)) }
        then: 'when applying to a right the monad will progress'
            right(1).bind(mfn).value == 2
        and: 'when applying to a left it wont go any further'
            left(1).bind(mfn).value == 1
    }
    // end::eithermonad1[]
}

