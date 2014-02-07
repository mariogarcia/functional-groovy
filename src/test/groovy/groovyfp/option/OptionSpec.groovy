package groovyfp.option

import spock.lang.Specification

class OptionSpec extends Specification {

    def 'Getting several objects with null/notnull value'() {
        when: 'Getting a given object'
            Option option = object2Test
        then: 'It should give us some information about itself'
            isNull == option.hasValue()
        where: 'Possible values are'
            obj | isNull
            a   | true
            b   | false
    }


}
