package groovyfp.closures

import spock.lang.Specification

class ClosuresSpec extends Specification {

    // tag::closures1[]
    def closure = { -> }

    def closureWithArgs = { arg1, arg2 -> // <1>
        println "statements" // <2>
    }

    def closureWithImplicit = {
        println it // <3>
    }

    Closure<?> typedClosure = {-> } // <3>
    // end::closures1[]

    // tag::closures2[]
    void 'Declaring and executing a closure'() {
        given: 'a variable and a closure'
            def x = 1 // <1>
            def c = { ->
                def y = 1 // <2>
                x + y // <3>
            }
        when: 'executing the closure'
            def result = c() // <4>
        then: 'the value should be the expected'
            result == 2
    }
    // end::closures2[]

    // tag::closures3[]
    void 'Using closures with collections'() {
        given: 'a collection of numbers'
            def numbers = [1, 2, 3, 4]
        when: 'collecting the double of each of them'
            def doubledNumbers = numbers.collect { it * 2 } // <1>
        then:
            doubledNumbers == [2, 4, 6, 8]
    }
    // end::closures3[]

    // tag::closures4[]
    void 'Using closures with collections'() {
        given: 'a collection of numbers'
            def numbers = [1, 2, 3, 4]
        when: 'collecting the double of each of them'
            def collector = { it * 2 } // <1>
            def doubledNumbers = numbers.collect(collector) // <2>
        then:
            doubledNumbers == [2, 4, 6, 8]
    }
    // end::closures4[]

    // tag::closures5[]
    Integer calculate1(Integer... numbers) {
        return numbers.sum()
    }

    void 'Composition: Just one calculation. No composition'() {
        given: 'two numbers as input'
            def first = 1
            def second = 2
        when: 'invoking the function'
            def result = calculate1(first, second)
        then: 'we should be getting the sum'
            result == 3
    }
    // end::closures5[]

    // tag::closures6[]
    Double calculate2(Integer... numbers) {
        return numbers
            .collect { it * 2 }
            .collect { it / 10 }
            .sum()
    }

    void 'Composition: Calculation gets bigger. No composition'() {
        given: 'two numbers as input'
            def first = 10
            def second = 20
        when: 'invoking the function'
            def result = calculate2(first, second)
        then: 'the result should be the expected'
            result == 6
    }

    // end::closures6[]

    // tag::closures7[]
    Double calculate3(Integer... numbers) {
        def twoTimes = { it * 2 } // <1>
        def divideByTen = { it / 10 } // <2>
        def composedFn = twoTimes >> divideByTen // <3>

        return numbers
            .collect(composedFn) // <4>
            .sum()
    }

    void 'Composition: Calculation gets bigger. Composing'() {
        given: 'two numbers as input'
            def first = 10
            def second = 20
        when: 'invoking the function'
            def result = calculate3(first, second)
        then: 'the result should be the expected'
            result == 6
    }
    // end::closures7[]

    // tag::closures8[]
    // <1>
    Double calculate4(Integer... numbers) {
        def operation =
            Calculations.divideByTen <<
            Calculations.twoTimes

        return numbers.collect(operation).sum()
    }

    // <2>
    Double applyFunctionAndSum(Closure<Number> fn, Integer... numbers) {
        return numbers.collect(fn).sum()
    }

    void 'Composition: Calculation gets bigger. Composing'() {
        given: 'two numbers as input'
            def first = 10
            def second = 20
        when: 'invoking the function'
            def result1 = calculate4(first, second) // <3>
            def result2 =
                applyFunctionAndSum(Calculations.twoTimes, first, second) // <4>
        then: 'the result should be the expected'
            result1 == 6
            result2 == 60
    }
    // end::closures8[]

}

