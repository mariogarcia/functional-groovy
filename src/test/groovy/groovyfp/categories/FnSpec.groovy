package groovyfp.categories

import static groovyfp.categories.Fn.bind
import static groovyfp.categories.Fn.Just

import groovy.transform.CompileStatic
import spock.lang.Specification

@CompileStatic
class FnSpec extends Specification {
	
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

