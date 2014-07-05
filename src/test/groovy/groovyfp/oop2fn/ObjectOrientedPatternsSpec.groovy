package groovyfp.oop2fn

import groovy.stream.Stream
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

    // tag::oop2fn_7[]
    void 'Immutability through Builder'() {
        given: 'an object built by a builder'
            def video =
                VideoBuilder
                    .builder()
                    .name('trip.avi')
                    .type('mp4')
                    .length(100000)
                    .build()
        when: 'trying to mutate the object'
            video.name = 'anothervideo'
        then: 'the instance should not have been mutated'
            video.name == 'trip.avi'
    }
    // end::oop2fn_7[]

    // tag::oop2fn_8[]
    void 'Immutability through @Immutable'() {
        given: 'an immutable instance'
            def video = new ImmutableVideo(
                name: 'video.mp4',
                type: 'mp4',
                length: 12434
            )
        when: 'trying to mutate the object'
            video.name = 'somethingelse.avi'
        then: 'an exception will be thrown'
            thrown(Exception)
    }
    // end::oop2fn_8[]

    // tag::oop2fn_9[]
    void 'Looping two collections at the same time'() {
        given: 'two different collections'
            def collection1 = (10..12)
            def collection2 = (60..62)
        and: 'the collection combining the result'
            def combination = []
        when: 'combining both collections'
            collection1.each { outer ->
                collection2.each { inner ->
                   combination << [outer, inner]
                }
            }
        then: 'values should be the expected'
            combination == [
                [10, 60],
                [10, 61],
                [10, 62],
                [11, 60],
                [11, 61],
                [11, 62],
                [12, 60],
                [12, 61],
                [12, 62]
            ]
    }
    // end::oop2fn_9[]

    // tag::oop2fn_10[]
    void 'Groovy comprehensions using stream-groovy'() {
        given: 'two different collections'
            def collection1 = (10..12)
            def collection2 = (60..62)
        when: 'combining both collections using stream-groovy'
            def combination =
                Stream
                    .from(x: collection1, y: collection2)
                    .map { [x, y] }
                    .collect()
        then: 'the results should be the expected'
            combination == [
                [10, 60],
                [10, 61],
                [10, 62],
                [11, 60],
                [11, 61],
                [11, 62],
                [12, 60],
                [12, 61],
                [12, 62]
            ]
    }
    // end::oop2fn_10[]

    // tag::oop2fn_11[]
    void 'Classical template method implementation'() {
        given: 'One concrete subclass implementation'
            def softCalculation = new FinancialProcessSoft()
        and: 'another different implementation based in the same parent'
            def hardCalculation = new FinancialProcessHard()
        expect: 'both results differ using the same process'
            softCalculation.calculate(100) !=
            hardCalculation.calculate(100)
    }
    // end::oop2fn_11[]

    // tag::oop2fn_12[]
    Closure<Double> calculate = {
            Closure<Double> calculateTaxes,
            Closure<Double> calculateExtras,
            Double amount ->

        return amount + calculateTaxes(amount) + calculateExtras(amount)
    }
    // end::oop2fn_12[]

    // tag::oop2fn_13[]
    Closure<Double> calculateSoftTaxes = { Double amount ->
        return amount * 0.01
    }

    Closure<Double> calculateSoftExtras = { Double amount ->
        return amount * 0.2
    }
    // end::oop2fn_13[]

    // tag::oop2fn_14[]
    void 'Template pattern using functions'() {
        when: 'calculating using predefined functions'
            def result1 =
                calculate(
                    calculateSoftTaxes,
                    calculateSoftExtras,
                    100
                )
        and: 'calculating using anonymous functions'
            def result2 =
                calculate(
                    { amount -> amount * 0.2 },
                    { amount -> amount * 0.10 },
                    100
                )
        then: 'same process...different results...with less code'
            result1 != result2
    }
    // end::oop2fn_14[]

    // tag::oop2fn_15[]
    void 'Composing functions to reuse templates'() {
        when: 'building a preset calculation'
            Closure<Double> customCalculation = { Double amount ->
                calculate(
                    calculateSoftTaxes,
                    calculateSoftExtras,
                    amount
                )
            }
        then: 'you can reuse it along your code'
            customCalculation(100) == 121.0
    }
    // end::oop2fn_15[]

    // tag::oop2fn_16[]

    List<ImmutableVideo> findAllFaultyVideo(List<ImmutableVideo> videoList, Closure<Boolean> validationStrategy) {
        return videoList
            .findAll(validationStrategy) // <1>
            .asImmutable() // <2>
    }

    Closure<Boolean> strategy1 = { ImmutableVideo video -> video.type == 'mp4' } // <3>
    Closure<Boolean> strategy2 = { ImmutableVideo video -> video.length < 700  } // <4>

    // end::oop2fn_16[]

    // tag::oop2fn_17[]
    void 'Changing the validation strategy'() {
        given:'A list of cars'
            List<ImmutableVideo> videoList = [
                [name: 'video1', type: 'avi', length: 1000],
                [name: 'video2', type: 'mp4', length: 1000],
                [name: 'video3', type: 'avi', length: 500] ,
                [name: 'video4', type: 'mp4', length: 1000],
                [name: 'video5', type: 'mp4', length: 50]
            ].asImmutable() as ImmutableVideo[]
        when: 'applying different strategies to the same list'
            def result1 = findAllFaultyVideo(videoList, strategy1)
            def result2 = findAllFaultyVideo(videoList, strategy2)
        then: 'you will have different result size'
            result1.size() == 3
            result2.size() == 2
    }
    // end::oop2fn_17[]
}
