package groovyfp.categories

import spock.lang.Specification
/**
 *
 */
class FunctorSpec extends Specification {
	
    // tag::functorspec1[]
    void 'applying a given function to a functor'() {
        given: 'a function'
            Function<Integer,Integer> plus_3 = { Integer v -> v + 3 } 
        and: 'a functor implementation. This time Maybe.Just implements functor'
            Functor<Integer> boxOfFive = Maybe.just(5)
        when: 'applying the function to functor to get another functor'
            Functor<Integer> result = boxOfFive.fmap(plus_3)
        then: 'the result should be the expected'
            result.typedRef.value == 8
    }
    // end::functorspec1[]
}

