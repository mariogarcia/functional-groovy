package groovyfp.categories

import static groovyfp.categories.Fn.List
import static groovyfp.categories.Fn.Just
import static groovyfp.categories.Fn.bind
import static groovyfp.categories.Fn.fmap
import static groovyfp.categories.Fn.val
import static groovyfp.categories.Fn.Try
import static groovyfp.categories.Fn.recover

import spock.lang.Specification

class TrySpec extends Specification {

    // tag::classictrycatch[]
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
    // end::classictrycatch[]

     // tag::classictrycatchreloaded[]
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
    // end::classictrycatchreloaded[]

     // tag::classictrycatchmonadic[]
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
    // end::classictrycatchmonadic[]
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
