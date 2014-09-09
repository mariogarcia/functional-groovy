package groovyfp.categories

import static groovyfp.categories.Fn.bind
import static groovyfp.categories.Fn.fmap
import static groovyfp.categories.Fn.Just

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
            Maybe.Just<Integer> result = fmap(Just("hi"), fn)
        then: 'result should be the expected'
            result instanceof Maybe.Just
            result.value == 2
    }

    void 'Binding'() {
        when: 'Building a nested binding expression'
            Maybe.Just<Integer> result = 
                bind(1) { Integer x ->
                    bind(x + 1) { Integer y ->
                        Just(y + 1)
                    }
                }
        then: 'Result should be 2 more'
            result instanceof Maybe.Just
            result.value == 3
    }

}

