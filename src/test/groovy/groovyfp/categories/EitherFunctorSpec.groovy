package groovyfp.categories

// tag::functor1[]
import static EitherFunctor.right
import static EitherFunctor.left
// end::functor1[]

import spock.lang.Specification

class EitherFunctorSpec extends Specification {

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
    void 'mapping identity function overy every item in a container has no effect'() {
        given: 'identity'
            def identity = { Integer v -> v }
        expect: 'applied to functor it has no effect'
            right(1).fmap(identity).value == 1
    }
    // end::functor3[]

}
