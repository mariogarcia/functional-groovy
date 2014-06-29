package groovyfp.streams

import spock.lang.Specification

class StreamsSpec extends Specification {

    // tag::streams_1[]
    void 'Multiply even numbers 10 times'() {
        given: 'A collection of numbers'
            def numbers = (0..10)
        when: 'Filtering and transforming numbers'
            def result =
                numbers.
                    findAll { it % 2 == 0}. // <1>
                    collect { it * 10 } // <2>
        then: 'We should get the expected result'
            result == [0, 20, 40, 60, 80, 100]
    }
    // end::streams_1[]

    // tag::streams_2[]
    void 'Multiply even numbers 10 times lazily'() {
        given: 'A collection of numbers'
            def numbers = (0..10)
        when: 'Filtering and transforming numbers'
            def result =
                numbers.inject([]){ acc, val ->
                    if (val % 2 == 0) {
                        acc << (val * 10)
                    }
                    acc
                }
        then: 'We should get the expected result'
            result == [0, 20, 40, 60, 80, 100]
    }
    // end::streams_2[]

    // tag::streams_3[]
    void 'Multiply even numbers 10 times lazily (nicer way)'() {
        given: 'A collection of numbers'
            def numbers = (0..10)
        when: 'Filtering and transforming numbers'
            def result =
                Stream.
                    from(numbers).
                    filter { it % 2 == 0 }.
                    map { it * 10 }.collect()
        then: 'We should get the expected result'
            result == [0, 20, 40, 60, 80, 100]
    }
    // end::streams_3[]

}
