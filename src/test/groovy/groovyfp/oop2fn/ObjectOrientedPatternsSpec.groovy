package groovyfp.oop2fn

import spock.lang.Specification

class ObjectOrientedPatternsSpec extends Specification {

    // tag::oop2fn_1[]
    void 'Functional Interface: the old fashioned way'() {
        given: 'a list of numbers'
            def numbers = (0..10)
        when: 'filtering using the class wrapping the filtering behavior'
            def evenNumbers = filterNumbersByFilter(numbers, new EvenNumbersFilter())
        then: 'We should get the expected result'
            evenNumbers == [0, 2, 4, 6, 8, 10]
    }
    // end::oop2fn_1[]

    // tag::oop2fn_2[]
    List<Integer> filterNumbersByFilter(List<Integer> numbers, Filter<Integer> filter) {
        List<Integer> resultList = new ArrayList()

        for (Integer number : numbers) {
            if (filter.accept(number)) {
                resultList.add(number)
            }
        }

        return resultList
    }
    // end::oop2fn_2[]

    // tag::oop2fn_3[]
    void 'Functional Interface: closures coercion'() {
        given: 'a list of numbers'
            def numbers = (0..10)
        when: 'filtering using the class wrapping the filtering behavior'
            def filter = { it % 2 == 0 } as Filter // <1>
            def evenNumbers = filterNumbersByFilter(numbers, filter) // <2>
        then: 'We should get the expected result'
            evenNumbers == [0, 2, 4, 6, 8, 10]
    }
    // end::oop2fn_3[]



}
