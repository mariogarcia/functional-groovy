package groovyfp.ecuation

/**
 *
 */
class EcuationResolver {

    static final String PATTERN = /_(\d)x/

    Number solution = 1
    Closure ecuation

    def propertyMissing(String name) {

        def matcher = name =~ PATTERN
        def result = matcher ? matcher[0][1].toInteger() : super.propertyMissing(name)

        return result * solution

    }

    def resolve(Closure ecuation) {
        this.ecuation = ecuation
        return this
    }

    def with(Number solution) {
        this.solution = solution
        return this.with(ecuation)
    }

}


