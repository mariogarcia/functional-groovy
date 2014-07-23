package groovyfp.categories

import static EitherApplicative.right
import static EitherApplicative.left

import spock.lang.Specification

class EitherApplicativeSpec extends Specification {
    void 'Applicative: Either applicative implementation'() {
        when:
            def inc = { Integer v -> v + 1 }
        then:
            left(null).fapply(right(1)).value == null
    }
}

