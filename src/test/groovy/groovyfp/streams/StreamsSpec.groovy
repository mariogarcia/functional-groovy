package groovyfp.streams

import spock.lang.Specification

class StreamsSpec extends Specification {

    // tag::streams_1[]
    void 'Multiply even numbers 10 times'() {
        given: 'A collection of numbers'
            def numbers = (0..10)
        when: 'Filtering and transforming numbers'
            def result =
                numbers
                    .findAll { it % 2 == 0} // <1>
                    .collect { it * 10 } // <2>
                    .collect { it + 1 } // <3>
        then: 'We should get the expected result'
            result == [1, 21, 41, 61, 81, 101]
    }
    // end::streams_1[]

    // tag::streams_composedclosures[]
    void 'Multiply even numbers 10 times'() {
        given: 'A collection of numbers'
            def numbers = (0..10)
            def multiplyTenTimes = { x -> x * 10}
            def byEvenNumber = { x -> x % 2 == 0 }
            def plusOne = { x -> x + 1 }
        when: 'Filtering and transforming numbers'
            def result =
                numbers
                    .findAll(byEvenNumber) // <1>
                    .collect(multiplyTenTimes >> plusOne) // <2>
        then: 'We should get the expected result'
            result == [1, 21, 41, 61, 81, 101]
    }
    // end::streams_composedclosures[]

    // tag::streams_2[]
    void 'Multiply even numbers 10 times lazily'() {
        given: 'A collection of numbers'
            def emptyList = []
            def numbers = (0..10)
            def multiplyTenTimes = { x -> x * 10}
            def byEvenNumber = { x -> x % 2 == 0 }
            def plusOne = { x -> x + 1 }
        when: 'Filtering and transforming numbers'
            def result =
                numbers.inject(emptyList) { acc, val ->
                    if (val % 2 == 0) {
                        acc << plusOne(multiplyTenTimes(val))
                    }
                    acc
                }
        then: 'We should get the expected result'
            result == [1, 21, 41, 61, 81, 101]
    }
    // end::streams_2[]

    // tag::streams_3[]
    void 'Multiply even numbers 10 times lazily (nicer way)'() {
        given: 'A collection of numbers'
            def numbers = (0..10)
            def multiplyTenTimes = { x -> x * 10}
            def byEvenNumber = { x -> x % 2 == 0 }
            def plusOne = { x -> x + 1 }
        when: 'Filtering and transforming numbers'
            def result =
                Stream
                    .from(numbers)
                    .filter(byEvenNumber)
                    .map(multiplyTenTimes >> plusOne)
                    .take(2)
                    .collect()
        then: 'We should get the expected result'
            result == [1, 21]
    }
    // end::streams_3[]

}
