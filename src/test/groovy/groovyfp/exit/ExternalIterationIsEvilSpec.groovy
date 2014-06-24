package groovyfp.exit

import spock.lang.Specification

class ExternalIterationIsEvil extends Specification {

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

    void 'Internal iteration'() {
        given: 'A collection we want to iterate'
            def expendables = ['Stallone', 'Staham', 'Couture']
            def upperCaseVersion = { String word -> word.toUpperCase() }
        when: 'Collecting the upper case version of the names'
            def upperCaseExpendables = expendables.collect(upperCaseVersion)
        then: 'All names should be in upper case'
          upperCaseExpendables == ['STALLONE', 'STAHAM', 'COUTURE']
    }

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

}
