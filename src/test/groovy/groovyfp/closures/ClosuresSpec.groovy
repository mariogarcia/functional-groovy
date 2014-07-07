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
}

