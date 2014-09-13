package groovyfp.categories

import static groovyfp.categories.ListMonad.list
import spock.lang.Specification

/**
 *
 */
class ListMonadSpec extends Specification {

    void 'using fmap'() {
        given: 'a list of numbers'
            ListMonad<Integer> fa = list(1,2,3,4)
        when: 'incrementing their value'
            ListMonad<Integer> fb = fa.fmap { it + 1 }
        then: 'result should be the expected'
            fb instanceof ListMonad
            fb.typedRef.value == [2,3,4,5]
    }

    void 'using bind'() {
        given: 'a list of numbers'
            ListMonad<Integer> fa = list(1,2,3,4)
            Function<Integer,ListMonad<Integer>> fn = { Integer i ->
                return list(i + 1)
            }
        when: 'binding with a increment function'
            ListMonad<Integer> result = fa.bind(fn)
        then: 'checking result'
            result.typedRef.value == [2,3,4,5]
    }

    void 'using bind for list comprehensions'() {
        given: 'a list of numbers'
            ListMonad<Integer> fa = list("hi","bye")
        and: 'making bind to look like Haskell bind'
            fa.metaClass.'>>=' = { fn -> delegate.bind(fn) }
        and: 'creating a function containing a list monad'
            def wordAndCount = { String w -> list(w, w.length()) }
        when: 'executing the binding'
            def result = fa.'>>=' wordAndCount
        then: 'we should get the word length and vowels in both words'
            result.typedRef.value == list("hi",2,"bye",3).typedRef.value
    }

    // tag::listmonadvsplaingroovy1[]
    void 'Comparing list monad with plain Groovy (I)'() {
        when: 'collecting number, its double and its half'
            def result1 = (1..3).collect { x -> [x, x * 2, x / 2] }.flatten()
            def result2 = list(1..3).bind { x -> [x, x * 2, x / 2 ] as ListMonad }
        then: 'all results should give the same result'
            result1 == [1,2,0.5,2,4,1,3,6,1.5]
            result1 == result2.typedRef.value
    }
    // end::listmonadvsplaingroovy1[]

        // tag::listmonadvsplaingroovy2[]
    void 'Comparing list monad with plain Groovy (II)'() {
        when: 'collecting number, its double and its half'
            def result1 = // <1>
                (1..3)
                    .findAll { x -> x % 2 == 0 }
                    .collect { x -> [x, x * 2, x / 2] }
                    .flatten()
        and:
            def result2 = // <2>
                list(1..3)
                    .bind { x ->
                        (x % 2 == 0 ? [x] : []) as ListMonad
                    }.bind { y ->
                        [y, y * 2, y / 2 ] as ListMonad
                    }
        then: 'all results should give the same result'
            result1 == [2,4,1]
            result1 == result2.typedRef.value
    }
    // end::listmonadvsplaingroovy2[]

}

