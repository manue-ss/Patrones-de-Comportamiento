// Archivo: ViewController.java | Paquete: co.edu.udistrital.mediator.controller
package co.edu.udistrital.mediator.controller;

import co.edu.udistrital.mediator.model.Colleague;
import co.edu.udistrital.mediator.model.DoctorDTO;
import co.edu.udistrital.mediator.model.EPS;
import co.edu.udistrital.mediator.model.TurnDTO;
import co.edu.udistrital.mediator.view.IIn;
import co.edu.udistrital.mediator.view.IOut;

/**
 * Componente Colega que actúa como puente entre el Mediador (EPS) y las abstracciones de la Vista (IIn / IOut),
 * preservando el aislamiento estricto del patrón MVC sin recurrir a patrones externos como Observer.
 */
public class ViewController extends Colleague {

    private final IIn in;
    private final IOut out;

    /**
     * Construye el controlador de vista inyectando las abstracciones de Entrada/Salida.
     *
     * @param in  Abstracción de lectura de datos.
     * @param out Abstracción de escritura de datos.
     */
    public ViewController(IIn in, IOut out) {
        super();
        if (in == null || out == null) {
            throw new IllegalArgumentException("Las dependencias de vista (IIn, IOut) no pueden ser nulas.");
        }
        this.in = in;
        this.out = out;
    }

    @Override
    public void receive(String event, Object context) {
        if (EPS.EVENT_SHOW_CREATED.equals(event) && context instanceof TurnDTO) {
            TurnDTO turn = (TurnDTO) context;
            out.showMessage("  [EMISIÓN] Turno generado y encolado -> " + turn.toString());
        } else if (EPS.EVENT_SHOW_ASSIGNED.equals(event) && context instanceof TurnDTO) {
            TurnDTO turn = (TurnDTO) context;
            out.showMessage("  [ASIGNACIÓN ASÍNCRONA] Turno consumido -> " + turn.toString());
        } else if (EPS.EVENT_SHOW_DOCTOR_READY.equals(event) && context instanceof DoctorDTO) {
            DoctorDTO doctor = (DoctorDTO) context;
            out.showMessage("  [POOL MÉDICO] Médico registrado en servicio -> " + doctor.toString());
        }
    }

    public IIn getIn() {
        return in;
    }

    public IOut getOut() {
        return out;
    }
}