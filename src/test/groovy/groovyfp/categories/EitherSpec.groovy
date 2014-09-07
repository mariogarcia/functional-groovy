package groovyfp.categories

import static Either.right
import static Either.left

import spock.lang.Specification

class EitherSpec extends Specification {
    
    void 'Applicative: Either applicative implementation'() {
        when:
            def inc = { Integer v -> v + 1 }
        then:
            left(null).fapply(right(1)).value == null
    }
    
    // tag::functor2[]
    void 'Either functor implementation'() {
        given: 'a function we want to apply'
            def inc = { Integer v -> v + 1 }
        expect: 'to return the result of applying the function when RIGHT'
            right(1).fmap(inc).value == 2
            right(2).fmap(inc).value == 3
        and: 'to return the same input when LEFT'
            left(null).fmap(inc).value == null
    }
    // end::functor2[]

    // tag::functor3[]
    void 'first law'() {
        when: 'mapping identity function overy every item in a container'
            def identity = { Integer v -> v }
        then: 'it has no effect'
            right(1).fmap(identity).value == 1
    }
    // end::functor3[]

    // tag::functor4[]
    void 'second law'() {
        when: 'Mapping a composition of two functions over every item in a container'
            def inc = { Integer v -> v + 1 }
            def twoTimes = { Integer v -> v * 2 }
            def composition = inc >> twoTimes
        then: 'the same as first mapping one function'
        and: 'then mapping the other'
            right(1)
                .fmap(composition)
                .value == right(right(1).fmap(inc).value)
                            .fmap(twoTimes)
                            .value
    }
    // end::functor4[]
    
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
    
    void 'Either monad when some function returns a left value'() {
        when: 'having a function that could return a left value'
            def div = { Integer v -> 
                return v == 0 ? Either.left(v)  : Either.right(1/v) 
            }
        then: 'if apply a valid value then the function will be applied'
            right(1).bind(div).value == 1
        and: 'otherwise if using 0 I will get a left zero'
            with(left(0).bind(div)) {
                value == 0
            }
    }
}

