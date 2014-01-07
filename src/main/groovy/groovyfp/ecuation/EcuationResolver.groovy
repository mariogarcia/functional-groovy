package groovyfp.ecuation

/**
 *
 */
class EcuationResolver {

    static final String PATTERN = /_([\d]{0,1})([x-z]{1})/

    Map<String,Number> values
    Closure ecuation

    def propertyMissing(String name) {

        def matcher = name =~ PATTERN

        if (!matcher) {
            return super.propertyMissing(name)
        }

        def multiplier = matcher[0][1]
        def variableName = matcher[0][2]
        def solution =
            multiplier ?
                values[variableName] * multiplier.toInteger() :
                values[variableName]

        return solution

    }

    def check(Closure ecuation) {
        this.ecuation = ecuation
        return this
    }

    def with(Map values) {
        this.values = values
        return this.with(ecuation)
    }

}


