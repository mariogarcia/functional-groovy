package groovyfp.oop2fn

class FinancialProcessSoft extends AbstractFinancialProcess {

    Double calculateTaxes(Double amount) {
        return amount * 0.01
    }

    Double calculateExtras(Double amount) {
        return amount * 0.2
    }

}

