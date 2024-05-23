/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aeropuerto;

import java.util.concurrent.ThreadLocalRandom;
/**
 *
 * @author gonzalo
 */
public class PuertaEmbarque {
    private boolean disponible = true;
    private Avion avionAsignado;
    private int idPuertaEmbarque;
    boolean pausado = false;

    public PuertaEmbarque(int idPuertaEmbarque) {
        this.idPuertaEmbarque = idPuertaEmbarque;
    }
    
    public synchronized boolean estaDisponible() {
        return disponible;
    }

    public synchronized void embarcarPasajeros(Aeropuerto aeropuerto) throws InterruptedException {
        System.out.println("Puerta " + idPuertaEmbarque + " ocupada");
        int intentos = 0;
        while (intentos < 3) {
            aeropuerto.pausaSiEsNecesario();
            if (avionAsignado == null) {
                break;
            }
            int pasajerosDisponibles = aeropuerto.getPasajerosDisponibles();
            int capacidadAvion = avionAsignado.getCapacidad();
            int pasajerosAEmbarcar = Math.min(capacidadAvion, pasajerosDisponibles);
        
            if (pasajerosAEmbarcar > 0) {
                // Comprobar si hay suficientes pasajeros disponibles antes de reducir
                if (pasajerosAEmbarcar <= pasajerosDisponibles) {
                    aeropuerto.reducirPasajeros(pasajerosAEmbarcar);
                    avionAsignado.aumentarPasajeros(pasajerosAEmbarcar);
                    Registro.logEvent("["+ aeropuerto.getNombre()+ "]" + " Avion " + avionAsignado.Id() + " embarcando " + pasajerosAEmbarcar + " pasajeros");
                } else {
                    aeropuerto.reducirPasajeros(pasajerosDisponibles);
                    avionAsignado.aumentarPasajeros(pasajerosDisponibles);
                    Registro.logEvent("["+ aeropuerto.getNombre()+ "]" + " Avion " + avionAsignado.Id() + " embarcando " + pasajerosDisponibles + " pasajeros");
                }
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3001)); // Tiempo de transferencia de pasajeros
            }
            if (avionAsignado.getPasajeros() == capacidadAvion || pasajerosDisponibles == 0) {
                break;
            }
            Registro.logEvent("["+ aeropuerto.getNombre()+ "]" + " Avion " + avionAsignado.Id() + " esperando a que lleguen más pasajeros");
            intentos++;
            Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 5001)); // Tiempo de espera antes de admitir más pasajeros
        }
        System.out.println("Puerta " + idPuertaEmbarque + " liberada");
        notifyAll();
        
    }
    

    public synchronized void desembarcarAvion(Aeropuerto aeropuerto) {
        try {
        if (avionAsignado!=null) {
            while (avionAsignado.getPasajeros() > 0) {
                aeropuerto.pausaSiEsNecesario();
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3001)); // Tiempo de transferencia de pasajeros
                aeropuerto.aumentarPasajeros(avionAsignado.getPasajeros());
                Registro.logEvent("["+ aeropuerto.getNombre()+ "]" + " Avion " + avionAsignado.Id() + " desembarcando "+ avionAsignado.getPasajeros() +" pasajeros");
                avionAsignado.reducirPasajeros(avionAsignado.getPasajeros());
                
                
            }
            notifyAll();
        }
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    }
    public void liberarPuerta(PuertaEmbarque puerta) {
        disponible = true;
    }

    public int getIdPuertaEmbarque() {
        return idPuertaEmbarque;
    }

    public void setIdPuertaEmbarque(int idPuertaEmbarque) {
        this.idPuertaEmbarque = idPuertaEmbarque;
    }

    public synchronized Avion getAvionAsignado() {
        return avionAsignado;
    }
    public void setAvionAsignado(Avion avionAsignado) {
        this.avionAsignado = avionAsignado;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
    
}
