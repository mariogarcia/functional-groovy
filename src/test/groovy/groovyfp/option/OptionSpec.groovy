package groovyfp.option

import spock.lang.Specification

class OptionSpec extends Specification {

    def 'Getting several objects with null/notnull value'() {
        when: 'Getting a given object'
            Option option = obj
        then: 'It should give us some information about itself'
            with(option) {
                hasValue() != isNull
                hasValue() ?
                    getOrElse(defaultValue) != defaultValue :
                    getOrElse(defaultValue) == defaultValue
            }
        where: 'Possible values are'
            obj                 | isNull | defaultValue
            optionalWithNoValue | true   | "something"
            optionalWithValue   | false  | "something"
    }

    Option<String> getOptionalWithNoValue() {
        return new Option<String>()
    }

    Option<String> getOptionalWithValue() {
        return new Option<String>(value: 'hello')
    }

}
