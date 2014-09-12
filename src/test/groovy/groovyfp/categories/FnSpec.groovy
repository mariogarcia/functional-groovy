package groovyfp.categories

import static groovyfp.categories.Fn.bind
import static groovyfp.categories.Fn.fmap
import static groovyfp.categories.Fn.just

import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import spock.lang.Specification

@CompileStatic
class FnSpec extends Specification {

    @CompileStatic(TypeCheckingMode.SKIP)
    void 'Fmap'() {
        given: 'a function (a->b) using functional interface coertion'
            Function<String,Integer> fn =
                { String word -> return word.length() } as Function<String,Integer>
        when: 'applying fmap::(a->b) -> fa -> fb'
            Maybe.Just<Integer> result = fmap(just("hi"), fn)
        then: 'result should be the expected'
            result instanceof Maybe.Just
            result.typedRef.value == 2
    }

    @CompileStatic(TypeCheckingMode.SKIP)
    void 'Binding'() {
        when: 'Building a nested binding expression'
            Maybe.Just<Integer> result =
                bind(just(1)) { Integer x ->
                    bind(just(x + 1)) { Integer y ->
                        just(y + 1)
                    }
                }
        then: 'Result should be 2 more'
            result instanceof Maybe.Just
            result.typedRef.value == 3
    }

    @CompileStatic(TypeCheckingMode.SKIP)
    void 'Using bind with a list monad: looks like comprehensions'() {
        given: 'a list monad'
            ListMonad<Integer> numbers = [1,2,3,4]
        when: 'applying a function to bind'
            ListMonad<Integer> result =
                bind(numbers){ x -> [x, x + 1] as ListMonad }
        then: 'we should get the expected sequence'
            result.typedRef.value == [1,2,2,3,3,4,4,5]
    }

}

