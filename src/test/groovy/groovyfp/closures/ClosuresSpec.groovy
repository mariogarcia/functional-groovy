package groovyfp.closures

import groovy.transform.Memoized
import spock.lang.Specification
import spock.lang.Unroll

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
    void 'Using closures with collections (I)'() {
        given: 'a collection of numbers'
            def numbers = [1, 2, 3, 4]
        when: 'collecting the double of each of them'
            def doubledNumbers = numbers.collect { it * 2 } // <1>
        then:
            doubledNumbers == [2, 4, 6, 8]
    }
    // end::closures3[]

    // tag::closures4[]
    void 'Using closures with collections (II)'() {
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

    void 'Composition: Calculation gets bigger. Composing (I)'() {
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

    void 'Composition: Calculation gets bigger. Composing (II)'() {
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

    // tag::closures9[]
    void 'Composing for filtering: no composition'() {
        given: 'a huge list of numbers'
            def numbers = (1..1000)
        when: 'applying several filters...wrong'
            def result =
                numbers
                    .findAll { it % 2 == 0 }
                    .findAll { it > 100 }
                    .sum()
        then: 'we should get the expected result'
            result == 247950
    }
    // end::closures9[]

    // tag::closures10[]
    Closure<Boolean> combineFiltersVerbose(final Closure<Boolean>... filters) {
        Closure<Boolean> allFiltersCombined = { Integer number, Closure<Boolean>... allFilters -> // <1>
            allFilters*.call(number).every()
        }

        return allFiltersCombined.rcurry(filters) // <2>
    }

    void 'Combining filters with rcurry'() {
        given: 'a huge list of numbers'
            def numbers = (1..1000)
        and: 'composing filtering'
            def even = { it % 2 == 0 }
            def greaterThanHundred = { it > 100 }
            def byCriteria = combineFiltersVerbose(even, greaterThanHundred) // <3>
        when: 'applying several filters'
            def result = numbers.findAll(byCriteria).sum() // <4>
        then: 'we should get the expected result'
            result == 247950
    }
    // end::closures10[]

    // tag::closures11[]
    Closure<Boolean> combineFilters(final Closure<Boolean>... filters) {
        return { Integer number -> // <1>
            filters*.call(number).every()
        }
    }

    void 'Combining filters with closure combination'() {
        given: 'a huge list of numbers'
            def numbers = (1..1000)
        and: 'composing filtering'
            def even = { it % 2 == 0 }
            def greaterThanHundred = { it > 100 }
            def byCriteria = combineFilters(even, greaterThanHundred) // <2>
        when: 'applying several filters'
            def result = numbers.findAll(byCriteria).sum() // <3>
        then: 'we should get the expected result'
            result == 247950
    }
    // end::closures11[]

    // tag::closures12[]
    void 'Trampoline: Adding up numbers recursively'() {
        given: 'a closure prepared to call itself recursively'
            def sumRecursively // <1>
            sumRecursively = { List<Integer> numbers, aggregator = 0 ->
                if (!numbers) {
                    return aggregator
                } else {
                    sumRecursively.trampoline( // <2>
                            numbers.tail(),
                            aggregator + numbers.head())
                }
            }
        when: 'usisng the transformed version of closure'
            sumRecursively = sumRecursively.trampoline() // <3>
            def result = sumRecursively(1..10000)
        then: 'we should get the result without the stackoverflow exception'
            result == 50005000
    }
    // end::closures12[]

    // tag::closures_mutualrecursion[]
    void 'Trampoline: Mutual recursion'() {
        given: 'two functions'
            def even, odd // <1>
        and: 'describing how the mutually call each other recursively'
            even = { Integer x -> x == 0 ? true : odd.trampoline(x - 1) }.trampoline() // <2>
            odd = { Integer x -> x == 0 ? false : even.trampoline(x - 1) }.trampoline() // <3>
        expect: 'to get right answers'
            [100, 101, 102, 103].collect(even) == [true, false, true, false] // <4>
    }
    // end::closures_mutualrecursion[]


    // tag::closures13[]
    @groovy.transform.TailRecursive
    Integer recursiveSum(List<Integer> numbers, Integer aggregator = 0) {
        if (!numbers) {
            return aggregator
        } else {
            return recursiveSum(
                numbers.tail(),
                numbers.head() + aggregator
            )
        }
    }

    void 'TailRecursive: recursive sum'() {
        when: 'using the transformed method'
            def result = recursiveSum(1..10000)
        then: 'we should get the result without the stackoverflow exception'
            result == 50005000
    }
    // end::closures13[]

    // tag::closures14[]
    void 'curry(...): partial application'() {
        given: 'a function taking a certain number of args'
            def fn1 = { a, b, c -> a + b + c } // <1>
        when: 'producting another functions'
            def fn2 = fn1.curry(1) // <2>
            def fn3 = fn1.curry(1, 2, 3) // <3>
        then: 'different applications'
            fn2(4, 5) == 10
            fn3() == 6
    }
    // end::closures14[]

    // tag::closures15[]
    void 'rcurry(...): partial application'() {
        given: 'a source function'
            def fn1 = { String name, Integer age, String city ->
                return "$name is $age and is from $city"
            }
        when: 'producing new funtions'
            def fn2 = fn1.rcurry('Madrid')
            def fn3 = fn1.rcurry(22, 'Barcelona')
        then: 'we could use them in several different ways'
            fn2('John', 37) == 'John is 37 and is from Madrid'
            fn3('Ronnie') == 'Ronnie is 22 and is from Barcelona'
    }
    // end::closures15[]

    // tag::closures16[]
    void 'ncurry(...): partial application'() {
        given: 'a source function'
            def fn1 = { String name, Integer age, String city ->
                return "$name is $age and is from $city"
            }
        when: 'producing a new function'
            def fn2 = fn1.ncurry(1,22) // <1>
        then: 'we should get the expected result'
            fn2('John', 'Madrid') == 'John is 22 and is from Madrid'
    }
    // end::closures16[]

    // tag::closures17[]
    void 'Using memoize for a given method'() {
        given: 'a invocation to get a user'
            def result = findUserDataById(1)
        and: 'waiting to check timestamp later'
            Thread.sleep(500)
        when: 'asking again for the same user'
            result = findUserDataById(1)
        then: 'if cache is expected timestamp should match'
            result.timestamp == old(result.timestamp)
    }
    // end::closures17[]

    // tag::closures18[]
    @groovy.transform.Memoized
       def findUserDataById(Integer userId) {
            println "Getting user [$userId] data"

            return [id: userId, name: "john doe $userId", timestamp: System.currentTimeMillis()]
       }
    // end::closures18[]

    // tag::closures19[]
       void 'Using a memoized closure'() {
            given: 'a list of words'
                def words = [\
                    'car','peter','maggie',
                    'ronnie','book','peter',
                    'road','car','ronnie'
                ]
            and: 'building the memoized closure'
                def md5fromWord = { String word ->
                    println "Word: $word" // <1>
                    java.security.MessageDigest
                        .getInstance("MD5")
                        .digest(word.getBytes("UTF-8"))
                        .encodeHex()
                        .toString()
                }.memoize() // <2>
            when: 'collecting their md5 hashes'
                def md5Hashes = words.collect(md5fromWord) // <3>
            then: 'checking figures'
                md5Hashes.size() == 9
                md5Hashes.unique().size() == 6
            // Word: car
            // Word: peter
            // Word: maggie
            // Word: ronnie
            // Word: book
            // Word: road
       }
    // end::closures19[]

    // tag::closures20[]
    class ClosureBehavior {

        // <3>
        def a = 1
        def b = 0
        def c = 0

        def sum() {
            // <2>
            def b = 2

            Closure<Integer> cl = {
                // <1>
                c = 3
                a + b + c
            }

            return cl()
        }

    }
    // end::closures20[]

    // tag::closures21[]
    void 'default closure resolution'() {
        given: 'an instance of ClosureBehavior'
            def instance = new ClosureBehavior()
        expect: 'result to be two'
            instance.sum() == 6
    }
    // end::closures21[]

}

