package groovyfp.categories

import static groovyfp.categories.Fn.List
import static groovyfp.categories.Fn.Just
import static groovyfp.categories.Fn.bind
import static groovyfp.categories.Fn.fmap
import static groovyfp.categories.Fn.val
import static groovyfp.categories.Fn.Try
import static groovyfp.categories.Fn.TryOrElse
import static groovyfp.categories.Fn.recover

import spock.lang.Specification

class TrySpec extends Specification {

    // tag::basic1[]
    def 'parsing a non valid number: Try'() {
        when: 'trying to parse a non valid number'
            Function inc = { x -> x + 1 }
            Try result1 = Try { Integer.parseInt("2a")} // <1>
            Try result2 = fmap(result1, inc) // <2>
        then: 'computation failed'
            result1.isFailure()
        and: 'any possible composition will return the same failure'
            result2.isFailure()
    }
    // end::basic1[]

    // tag::exception1[]
    def 'throwing an exception'() {
        when: 'doing something wrong'
            Try.Failure failure = Try { 0.div(0) }
            // <1>
            assert failure.exception instanceof ArithmeticException
        and: 'wants to propagate the exception'
            failure.throwException() // <2>
        then:'the exception will be thrown as usual'
            thrown(ArithmeticException)
    }
    // end::exception1[]

    // tag::tryorelse[]
    def 'reacting with TryElse'() {
        given: 'the functions used in this example'
            def returnZero = { 0 }
            def increment = { Integer x -> x + 1 }
            def parseInt = { String number ->
                return {
                   Integer.parseInt(number)
                }
            }
        when: 'trying to parse any type of value'
            Try result =
                fmap(
                    TryOrElse( // <1>
                        parseInt(next),
                        returnZero
                    ),
                    increment)
        then:'the value should always be greater than 0'
            val(result) >= 1
        where: 'values can be valid or invalid'
            next << ["1","2a","3"]
    }
    // end::tryorelse[]

    // tag::recover[]
    def 'using recover()'() {
        when: 'you cant always get what you want'
            Try something =
                TryOrElse(
                    { 0.div(0) }, // WRONG
                    { new Date() + "1" } // WORST
                )
            Try anything = recover(something, Try { 0 })
        then: 'you can always get what you need :P'
            val(anything) == 0

    }
    // end::recover[]

    def 'classic try catch example'() {
        given: 'a list of numbers as strings'
            def numbers = ["1","2a","11","24","4A"]
        when:
            def average =
                numbers.findResults { n ->
                    // <1>
                    try {
                        return Integer.parseInt(n)
                    } catch (e) {
                        return null // <2>
                    }
                }.with { list ->
                    list.sum().div(list.size())
                }
        then: 'the average should be 12'
            average == 12
    }

    def 'classic try catch example RELOADED'() {
        given: 'a list of numbers as strings'
            def numbers = ["1","2a","11","24","4A"]
            def parse = { item -> return { Integer.parseInt(item) } }
            def ZERO = { 0 }
            def addToList = { x -> x ? List(x) : List() }
            def AVG = { list -> list.sum().div(list.size()) }
        when: 'calculating average safely'
            def average =
                val(fmap(
                    Just(
                        numbers.collectMany { n ->
                            val(bind(recover(Try(parse(n)),Try(ZERO)), addToList))
                        }
                    ),
                    AVG
                ))
        then: 'the average should be 12'
            average == 12
    }

    def 'classic try catch example MONADIC'() {
        given: 'a list of numbers as strings'
            def numbers = ["1","2a","11","24","4A"]
            def ZERO = { 0 }
            def AVG = { list -> list.sum().div(list.size()) }
            def parse = { item -> return { Integer.parseInt(item) } }
            def addToList = { x -> x ? List(x) : List() }
        when: 'trying to get the average'
            def average =
                val(
                    fmap(
                        Just(
                            val(bind(List(numbers)) { n ->
                                bind(recover(Try(parse(n)),Try(ZERO)), addToList)
                            })
                        ),
                        AVG))
        then: 'the average should be 12'
            average == 12
    }

    def 'basic execution of a try'() {
        given: 'an action'
            def koDivision = { 0.div(0) }
            def okDivision = { 1.div(2) }
            def addOne = { x -> x + 1 }
        when: 'trying to execute it'
            def failure = fmap(Try(koDivision), addOne) // it failed
            def success = fmap(Try(okDivision), addOne) // it succeed
        then: 'checking both results'
            failure instanceof Try.Failure
            success instanceof Try.Success
	and: 'success action ends with a given value'
	    val(success) == 1.5
    }

    def 'once we have a success we want to make it fail'() {
        given: 'an action'
            def getWordLength = { String word ->
                return { word.length() }
            }
            def multiplyByTwo = { x -> x * 2 }
            def divByZero = { x -> x.div(0) }
        when: 'we use it wisely'
            Try successSoFar =
                fmap(Try(getWordLength("John")), multiplyByTwo)
        and: 'checking so far so good'
            assert successSoFar.isSuccess()
            assert val(successSoFar) == 8
        and: 'then screw it'
            Try failure = fmap(successSoFar, divByZero)
        then: 'the try instance will return failure'
            failure.isFailure()
    }

    def 'making a failure to throw an exception'() {
        given: 'an action'
            def getWordLength = { String word -> word.length() }
        when: 'we use it wisely'
            Function action = { 0.div(0) }
            Try successSoFar =
                fmap(Try(action)){ undefined ->
                    undefined + 1 // wont be executed
                }
        and: 'once we know it ended wrong'
            assert successSoFar.isFailure()
        and: 'asking the failure to throw an exception'
            successSoFar.throwException()
        then: 'and only then we will get the exception'
            thrown(ArithmeticException)
    }

}
