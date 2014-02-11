package groovyfp.oop2fn

/**
 * This class tries to avoid the use of class hierarchy when trying to apply the template pattern.
 * Instead of having the abstract methods implemented in children classes we use function
 * composition
 */
final class TemplatePattern {

    /**
     * This method calculates the total price using a closure for calculate the vat and another one
     * to calculate the discount.
     *
     * @param price
     * @param vatOperation
     * @param discountOperation
     */
    static Double calculateWithVatAndDiscount(
        Double price,
        Closure<Double> vatOperation,
        Closure<Double> discountOperation) {
            return (vatOperation << discountOperation << price)
    }

    /**
     * Another point of view. This time the vat and dto values are calculated within the function
     * based on the percentages passed as parameters
     *
     * @param price
     * @param vatValue
     * @param dtpValue
     */
    static Double calculateWithVatAndDiscount(Double price, Double vatValue, Double dtoValue) {
        def bind = { v, fn -> fn(v) }
        def vat = { v, vat -> bind(v, { a -> a + (a * vat )} )}.rcurry(vatValue)
        def dto = { v, dto -> bind(v, { a -> a - (a * dto )} )}.rcurry(dtoValue)

        return calculateWithVatAndDiscount(price, vat, dto)
    }

}
