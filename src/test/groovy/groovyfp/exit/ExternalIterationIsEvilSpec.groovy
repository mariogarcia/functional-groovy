package groovyfp.exit

import spock.lang.Specification

class ExternalIterationIsEvil extends Specification {

    // tag::external_iteration_1[]
    void 'External iteration. Java like code'() {
        given: 'A collection we want to iterate'
            def expendables = ['Stallone', 'Staham', 'Couture']
        when: 'Collecting the upper case version of the names'
            def expendablesUpperCase = []
            for(String name: expendables) {
                expendablesUpperCase << name.toUpperCase()
            }
        then: 'All names should be in upper case'
          expendablesUpperCase == ['STALLONE', 'STAHAM', 'COUTURE']
    }
    // end::external_iteration_1[]

    // tag::external_iteration_2[]
    void 'External iteration. Groovy style code'() {
        given: 'A collection we want to iterate'
            def expendables = ['Stallone', 'Staham', 'Couture']
        when: 'Collecting the upper case version of the names'
            def expendablesUpperCase = []
            expendables.each { name ->
                expendablesUpperCase << name.toUpperCase()
            }
        then: 'All names should be in upper case'
          expendablesUpperCase == ['STALLONE', 'STAHAM', 'COUTURE']
    }
    // end::external_iteration_2[]

    // tag::external_iteration_3[]
    void 'Internal iteration (I)'() {
        given: 'A collection we want to iterate'
            def expendables = ['Stallone', 'Staham', 'Couture']
            def upperCaseVersion = { String word -> word.toUpperCase() }
        when: 'Collecting the upper case version of the names'
            def upperCaseExpendables = expendables.collect(upperCaseVersion)
        then: 'All names should be in upper case'
          upperCaseExpendables == ['STALLONE', 'STAHAM', 'COUTURE']
    }
    // end::external_iteration_3[]

    // tag::external_iteration_4[]
    void 'Internal iteration (II)'() {
        given: 'A collection we want to iterate'
            def expendables = ['Stallone', 'Staham', 'Couture']
        when: 'Collecting the upper case version of the names'
            def upperCaseVersion = expendables.collect { it.toUpperCase() }
        then: 'All names should be in upper case'
          upperCaseVersion == ['STALLONE', 'STAHAM', 'COUTURE']
    }
    // end::external_iteration_4[]


    void 'External iteration. Filtering logic'() {
        given: 'A collection we want to iterate'
            def expendables = ['Stallone', 'Staham', 'Couture']
        when: 'Collecting the upper case version of the names'
            def expendablesUpperCase = []
            expendables.each { name ->
                if (name.startsWith('S')) {
                    expendablesUpperCase << name.toUpperCase()
                }
            }
        then: 'All names should be in upper case'
          expendablesUpperCase == ['STALLONE', 'STAHAM']
    }

    void 'Internal iteration. Filtering logic'() {
        given: 'A collection we want to iterate'
            def expendables = ['Stallone', 'Staham', 'Couture']
        and: 'Declaring a collector and a filter'
            def upperCaseVersion = { String word -> word.toUpperCase() }
            def startingWithS = { String word -> word.startsWith('S') }
        when: 'Collecting the upper case version of the names'
            def upperCaseExpendables =
                expendables.
                    findAll(startingWithS).
                    collect(upperCaseVersion)
        then: 'All names should be in upper case'
          upperCaseExpendables == ['STALLONE', 'STAHAM']
    }

    // tag::external_iteration_5[]
    void 'external iteration: adding up all numbers from a given collection'() {
        given: 'a collection of numbers'
            def numbers = (0..1000)
            def sum = 0
        when: 'summing all of them'
            numbers.each { nextnumber ->
                sum += nextnumber
            }
        then: 'the expected result should be the expected'
            sum == 500500
    }
    // end::external_iteration_5[]

    // tag::external_iteration_6[]
    void 'External iteration: Adding numbers from 20 to 100 from a given collection'() {
        given: 'a collection of numbers'
            def numbers = (0..1000)
            def sum = 0
        when: 'adding up all of them'
            numbers.each { nextnumber ->
                if (nextnumber > 20 && nextnumber < 100) {
                    sum += nextnumber
                }
            }
        then: 'the expected result should be the expected'
            sum == 4740
    }
    // end::external_iteration_6[]

    // tag::external_iteration_7[]
    Integer sum(
        final Collection<Integer> source, // <1>
        final Closure<Boolean> filter = { it }) { // <2>

        return source.findAll(filter).sum() // <3>
    }
    // end::external_iteration_7[]

    // tag::external_iteration_8[]
    void 'Internal iteration: Adding up with reusability'() {
        given: 'A collection of numbers'
            def numbers = (0..1000)
        when: 'adding up'
            def all = sum(numbers)
            def from20To100 = sum(numbers) {
                it > 20 && it < 100
            }
        then: 'Results should be the expected'
            all == 500500
            from20To100 == 4740
    }
    // end::external_iteration_8[]

    // tag::external_iteration_9[]
    void 'Internal iteration: Reusability limited ? (I)'() {
        given: 'A collection of numbers'
            def numbers1 = [100, 20, 2, 54, 33, 14]
            def numbers2 = [100, 20, 2, 54, 33, 14]
            def numbers3 = [100, 24, 6, 48, 22, 40]
        when: 'adding up all numbers of first collection'
            def numbers1Sum = sum(numbers1)
        and: 'adding up numbers > 20 and < 100 for the remaining collections'
            def numbers2Sum = sum(numbers2) {
                it > 20 && it < 100
            }
            def numbers3Sum = sum(numbers3) {
                it > 20 && it < 100
            }
        then: 'Results should be the expected'
            numbers1Sum == 223
            numbers2Sum == 87
            numbers3Sum == 134
    }
    // end::external_iteration_9[]

    // tag::external_iteration_10[]
    void 'Internal iteration: Reusability limited ? (II)'() {
        given: 'A collection of numbers'
            def numbers1 = [100, 20, 2, 54, 33, 14]
            def numbers2 = [100, 20, 2, 54, 33, 14]
            def numbers3 = [100, 24, 6, 48, 22, 40]
        and: 'A fixed type of filter'
            def greaterThan20LessThan100 = { it > 20 && it < 100} // <1>
        when: 'adding up all numbers of first collection'
            def numbers1Sum = sum(numbers1)
        and: 'adding up numbers > 20 and < 100 for the remaining collections'
            def numbers2Sum = sum(numbers2, greaterThan20LessThan100) // <2>
            def numbers3Sum = sum(numbers3, greaterThan20LessThan100)
        then: 'Results should be the expected'
            numbers1Sum == 223
            numbers2Sum == 87
            numbers3Sum == 134
    }
    // end::external_iteration_10[]

    // tag::external_iteration_11[]
    void 'Internal iteration: Reusability limited ?'() {
        given: 'A collection of numbers'
            def numbers1 = [100, 20, 2, 54, 33, 14]
            def numbers2 = [100, 20, 2, 54, 33, 14]
            def numbers3 = [100, 24, 6, 48, 22, 40]
        and: 'A fixed type of behavior'
            def customSum = this.&sum.rcurry { it > 20 && it < 100} // <1>
        when: 'adding up all numbers of first collection'
            def numbers1Sum = sum(numbers1)
        and: 'adding up numbers > 20 and < 100 for the remaining collections'
            def numbers2Sum = customSum(numbers2) // <2>
            def numbers3Sum = customSum(numbers3)
        then: 'Results should be the expected'
            numbers1Sum == 223
            numbers2Sum == 87
            numbers3Sum == 134
    }
    // end::external_iteration_11[]


    // tag::composition_1[]
    void 'Specifications change. Looping Twice'() {
        given: 'a list of words'
            def words = [' animal', ' person', 'something else ']
        and: 'a couple of transformations'
            def trim = { it.trim() }
            def toUpperCase = { it.toUpperCase() }
        when: 'applying transformations one after another'
            def result =
                words
                    .collect(trim) // <1>
                    .collect(toUpperCase) // <2>
        then: 'we should get a cleaned list of words'
            result == ['ANIMAL', 'PERSON', 'SOMETHING ELSE']
    }

    // end::composition_1[]

    // tag::composition_2[]
    void 'Specifications change. Composition'() {
        given: 'a list of words'
            def words = [' animal', ' person', 'something else ']
        and: 'a couple of transformations'
            def trim = { it.trim() }
            def toUpperCase = { it.toUpperCase() }
            def trimAndUpperCase = toUpperCase << trim // <1>
        when: 'applying transformations one after another'
            def result = words.collect(trimAndUpperCase)
        then: 'we should get a cleaned list of words'
            result == ['ANIMAL', 'PERSON', 'SOMETHING ELSE']
    }
    // end::composition_2[]

}
