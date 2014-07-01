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

    // tag::oop2fn_4[]
    void 'State: using state wrong'() {
        given: 'an object used in a closure'
            def state = new State(discount:50) // <1>
            def closure = { price ->
                price * (state.discount / 100) // <2>
            }
        when: 'calculating the price discount'
            def result1 = closure(100)
        and: 'changing state value'
            state = new State(discount:20) // <3>
            def result2 = closure(100)
        then: 'the second result is different'
            result1 != result2
    }
    // end::oop2fn_4[]

    // tag::oop2fn_5[]
    void 'State: using state wrong'() {
        given: 'an object used in a closure'
            def state = new State(discount:50) // <1>
        and: 'reducing the closure avaiable scope when is created'
            def closure = { State st -> // <2>
                return { price ->
                    price * (st.discount / 100)
                }
            }
        when: 'calculating the price discount'
            def closureWithImmutableState  = closure(state)
            def result1 = closureWithImmutableState(100)
        and: 'changing state value'
            state = new State(discount:20) // <3>
            def result2 = closureWithImmutableState(100)
        then: 'the second result is different'
            result1 == result2
    }
    // end::oop2fn_5[]

    // tag::oop2fn_6[]
    void 'Command: passing behavior to another class'() {
        given: 'a given purchase has a certain price'
            def purchaseEntry = new PurchaseEntry(price: 200) // <1>
        when: 'two different purchase processes'
            def result = purchaseEntry.applyPurchaseProcess(process) // <2>
        then: 'the price is the expected depending on the process'
            result == expectedPrice
        where: 'possible processes are'
            process                           | expectedPrice
            { price -> price }                | 200 // <3>
            { price -> price += price * 0.3 } | 260 // <4>
    }
    // end::oop2fn_6[]

}
