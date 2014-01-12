package groovyfp.ecuation

import java.util.regex.Matcher
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

        return !values.isEmpty() ? checkSolution(matcher) : calculateSolution(matcher)

    }

    def checkSolution(Matcher matcher) {
        def (multiplier, variableName) = matcher[0][1..2]
        def solution =
            multiplier ?
                values[variableName] * multiplier.toInteger() :
                values[variableName]

        return solution
    }

    def calculateSolution(Matcher matcher) {

    }

    def check(Closure... ecuations) {
        this.ecuations.with {
            clear()
            addAll(ecuations)
        }
        return this
    }

    def resolve(Closure... ecuations) {
        return check(ecuations).with([:])
    }

    def with(Map values) {
        this.values = values
        this.ecuations*.delegate = this
        return this.ecuations*.doCall()
    }

}


