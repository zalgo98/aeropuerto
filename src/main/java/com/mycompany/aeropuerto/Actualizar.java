package com.mycompany.aeropuerto;


class Actualizar extends Thread {
    private Control control;

    public Actualizar(Control control) {
        this.control = control;
    }

    @Override
    public void run() {
        while (true) {
            control.mostrar();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }
    }
}
