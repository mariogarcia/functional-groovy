package functional.iteration

import spock.lang.Specification

class ExternalIterationIsEvil extends Specification {

    void 'Dont do this'() {
        given: 'A collection we want to iterate'
            def expendables = ['Stallone', 'Staham', 'Couture']
        when: 'Collecting the upper case version of the names'
            def expendablesUpperCase = []
            for(String name: expendables) {
                expendables << name.toUpperCase()
            }
        then: 'All names should be in upper case'
          expendables == ['STALLONE', 'STAHAM', 'COUTURE']
    }

    void 'This is not better either'() {
        given: 'A collection we want to iterate'
            def expendables = ['Stallone', 'Staham', 'Couture']
        when: 'Collecting the upper case version of the names'
            def expendablesUpperCase = []
            expendables.each { name ->
                expendablesUpperCase << name.toUpperCase()
            }
        then: 'All names should be in upper case'
          expendables == ['STALLONE', 'STAHAM', 'COUTURE']
    }

    void 'Do this'() {
        given: 'A collection we want to iterate'
            def expendables = ['Stallone', 'Staham', 'Couture']
        when: 'Collecting the upper case version of the names'
            def expendablesUpperCase = expendables.collect { name ->
                name.toUpperCase()
            }
        then: 'All names should be in upper case'
          expendables == ['STALLONE', 'STAHAM', 'COUTURE']
    }

}
