package com.mycompany.aeropuerto;


import java.util.List;

class Actualizar extends Thread {
    private List<PuertaEmbarque> puertaEmbarques;

    public Actualizar(List<PuertaEmbarque> puertaEmbarques) {
        this.puertaEmbarques=puertaEmbarques;
    }

    @Override
    public void run() {
        /*for (int i = 0; i < puertas.length; i++) {
            PuertaEmbarque puerta = aeropuerto_Madrid.getPuertasEmbarque().get(i);

            Avion avion = puerta.getAvionAsignado();
            if (avion != null) {
                puertas[i].setText(avion.getNombre());

            } else {
                puertas[i].setText("");
            }
        }*/
    }
}
