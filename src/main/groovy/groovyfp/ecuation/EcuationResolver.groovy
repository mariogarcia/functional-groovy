package groovyfp.ecuation

/**
 *
 */
class EcuationResolver {

    static final String PATTERN = /_([\d]{0,1})([x-z]{1})/

    Map<String,Number> values
    List<Closure> ecuations = []

    def propertyMissing(String name) {

        def matcher = name =~ PATTERN

        if (!matcher) {
            return super.propertyMissing(name)
        }

        def (multiplier, variableName) = matcher[0][1..2]
        def solution =
            multiplier ?
                values[variableName] * multiplier.toInteger() :
                values[variableName]

        return solution

    }

    def check(Closure... ecuations) {
        this.ecuations.with {
            clear()
            addAll(ecuations)
        }
        return this
    }

    def with(Map values) {
        this.values = values
        this.ecuations*.delegate = this
        return this.ecuations*.doCall()
    }

}


