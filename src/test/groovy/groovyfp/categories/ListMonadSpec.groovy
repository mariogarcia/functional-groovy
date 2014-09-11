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
    
}

