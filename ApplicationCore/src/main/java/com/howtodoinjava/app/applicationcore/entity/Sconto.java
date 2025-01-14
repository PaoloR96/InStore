package com.howtodoinjava.app.applicationcore.entity;

public class Sconto {
            //definizione variabili
            protected ClientePremium clientePremium;
            protected Carrello carrello;
            protected Float percentualeSconto;
            //costruttore
            public Sconto(ClientePremium clientePremium, Carrello carrello, Float percentualeSconto) {
                this.clientePremium = clientePremium;
                this.carrello = carrello;
                this.percentualeSconto = percentualeSconto;
            }
            //Get&Set
            public ClientePremium getClientePremium() {
                return clientePremium;
            }

            public void setClientePremium(ClientePremium clientePremium) {
                this.clientePremium = clientePremium;
            }

            public Carrello getCarrello() {
                return carrello;
            }

            public void setCarrello(Carrello carrello) {
                this.carrello = carrello;
            }

            public Float getPercentualeSconto() {
                return percentualeSconto;
            }

            public void setPercentualeSconto(Float percentualeSconto) {
                this.percentualeSconto = percentualeSconto;
            }
}
