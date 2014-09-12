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
    
}

