/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aeropuerto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author gonzalo
 */
public class AreaEstacionamiento {
    private List<Avion> avionesEnEspera;

    public AreaEstacionamiento() {
        avionesEnEspera = new ArrayList<>();
    }

    public void realizarComprobaciones(Avion avion) throws InterruptedException {//metodo que realiza comprobaciones de seguridad
        Registro.logEvent(" [ "+ avion.getAeropuertoOrigen().getNombre()+ " ] " +"Realizando comprobaciones de seguridad en el avión " + avion.Id());
        int tiempoDeComprobacion = ThreadLocalRandom.current().nextInt(1000, 5001); // Tiempo aleatorio entre 1 y 5 segundos
        Thread.sleep(tiempoDeComprobacion);
        avionesEnEspera.add(avion);
    }

    public synchronized PuertaEmbarque esperarPuertaDisponible(Avion avion) throws InterruptedException {//metodo que espera a que haya una puerta disponible
        avionesEnEspera.add(avion); // el avión se añade a la lista de aviones en espera
        System.out.println("Aviones en espera: " + avion.Id());
        List<PuertaEmbarque> puertasEmbarque = avion.getAeropuertoOrigen().getPuertasEmbarque();// Obtiene las puertas de embarque del aeropuerto de origen
        PuertaEmbarque puerta = null;
        Registro.logEvent(" [ "+ avion.getAeropuertoOrigen().getNombre()+ " ] " +"Esperando puerta disponible en area de estacionamiento " + avion.Id());
        while (puerta == null) { // Mientras no haya puertas disponibles
            for (PuertaEmbarque pe : puertasEmbarque) {
                if (pe.estaDisponible()) {
                    puerta = pe;
                    Registro.logEvent(" [ "+ avion.getAeropuertoOrigen().getNombre()+ " ] " +"Puerta " + puerta.getIdPuertaEmbarque() + " asignada al avión " + avion.Id());
                    break;
                } else {
                    wait(1000);// Espera 1 segundo antes de volver a comprobar si hay puertas disponibles
                }
            }
        }
        liberarAvion();
        return puerta;
    }

    public synchronized void liberarAvion() {//metodo que libera un avion
        avionesEnEspera.remove(0); // Remueve el avión de la lista de aviones en espera
        notify(); // Notifica a un avión en espera que hay una puerta disponible

    }

    public synchronized Avion obtenerSiguienteAvion() {
        if (!avionesEnEspera.isEmpty()) {
            return avionesEnEspera.remove(0); // Obtiene y remueve el siguiente avión en espera
        }
        return null;
    }

    public List<Avion> getAvionesEnEspera() {//metodo que devuelve los aviones en espera
        return avionesEnEspera;
    }

}
