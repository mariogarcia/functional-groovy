package groovyfp.filter

import spock.lang.Specification

class ColFnSpec extends Specification {

    def 'Filtering a given list of names'() {
        given: 'A list of names'
            List<String> names = ['john', 'joseph', 'mario', 'peter', 'gina', 'ana']
        when: 'Filtering the collection'
            List<String> otherNames =
                ColFn.from(names).filter{ it.startsWith("j") }.result
        then: 'Names is neither the same nor equals otherNames'
            names != otherNames
            !names.equals(otherNames)
        and: 'It should have only two elements'
            otherNames.size() == 2
            otherNames == ['john', 'joseph']
    }

}
