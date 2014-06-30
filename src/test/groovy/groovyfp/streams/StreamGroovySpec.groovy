package groovyfp.streams

import spock.lang.Specification
import groovy.stream.Stream

class StreamGroovySpec extends Specification {

    // tag::streams_4[]
    void 'Getting double values from a given collection'() {
        when:
            def result = Stream.
                from(0..5).
                map { n -> n * 10 }.collect()
        then:
            result == [0, 10, 20, 30, 40, 50]

    }
    // end::streams_4[]




}
