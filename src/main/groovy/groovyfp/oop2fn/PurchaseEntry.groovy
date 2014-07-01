package groovyfp.oop2fn

class PurchaseEntry {

    BigDecimal price

    BigDecimal applyPurchaseProcess(PurchaseProcess process) {
        return process.calculate(price)
    }

}
