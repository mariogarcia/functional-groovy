package groovyfp.oop2fn

import spock.lang.Specification

class TemplatePatternSpec extends Specification {

    def 'Using Function Builder instead of inheritance'() {
        given: 'A price'
            def price = 240
        when: 'Applying vat and discount to calculate'
            def total =
                TemplatePattern.calculateWithVatAndDiscount(
                    price,
                    { t -> t += (t * 0.21) },
                    { t -> t -= (t * 0.10) }
                )
        then: 'We should get the final price'
            total > 261
            total < 262
    }


}
