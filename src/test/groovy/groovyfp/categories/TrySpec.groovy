package groovyfp.categories

import static groovyfp.categories.Fn.List
import static groovyfp.categories.Fn.Try
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
        when: 'calculating average safely'
            def average =
                numbers.findResults { n ->
                    Try(Integer.&parseInt)
                        .fmap { fn -> fn.apply(n) }
                        .with { no ->
                            no.isSuccess() ? no.typedRef.value : null
                        }
                }.with { list ->
                    list.sum().div(list.size())
                }
        then: 'the average should be 12'
            average == 12
    }
    // end::classictrycatchreloaded[]

     // tag::classictrycatchmonadic[]
    def 'classic try catch example MONADIC'() {
        given: 'a list of numbers as strings'
            def numbers = ["1","2a","11","24","4A"]
        when:
            def average =
                List(numbers).bind { n ->
                    Try(Integer.&parseInt)
                        .fmap { fn -> fn.apply(n) }
                        .with { no ->
                            no.isSuccess() ? List(no.typedRef.value) : List()
                        }
                }.typedRef.value.with { list ->
                    list.sum().div(list.size())
                }
        then: 'the average should be 12'
            average == 12
    }
    // end::classictrycatchmonadic[]

    def 'basic execution of a try'() {
        given: 'an action'
            def divideByZero = { 0.div(it) }
            def tryAction = Try(divideByZero)
        when: 'trying to execute it'
            def failure = tryAction.fmap { fn -> fn(0) } // it failed
            def success =
		tryAction
                    .fmap { fn -> fn.apply(1) }
                    .fmap { no -> no + 1 }
        then: 'checking both results'
            failure instanceof Try.Failure
            success instanceof Try.Success
	and: 'success action ends with a given value'
	    success.typedRef.value == 1
    }

    def 'once we have a success we want to make it fail'() {
	given: 'an action'
	    def getWordLength = { String word -> word.length() }
	when: 'we use it wisely'
	    Try successSoFar =
		Try(getWordLength)
                    .fmap { fn -> fn.apply("john") } // ok
                    .fmap { no -> no * 2 }
	    assert successSoFar.isSuccess()
	    assert successSoFar.typedRef.value == 8
	and: 'then screw it'
	    Try failure = successSoFar.fmap { no -> no.div(0) }
	then: 'the try instance will return failure'
	    failure.isFailure()
    }

    def 'making a failure to throw an exception'() {
	given: 'an action'
	    def getWordLength = { String word -> word.length() }
	when: 'we use it wisely'
	    Try successSoFar =
		Try { 0.div(0) } // something wrong :P
		    .fmap { fn -> fn.apply("john") }
	and: 'once we know it ended wrong'
	    assert successSoFar.isFailure()
	and: 'asking the failure to throw an exception'
	    successSoFar.throwException()
	then: 'and only then we will get the exception'
            thrown(ArithmeticException)
    }

}
