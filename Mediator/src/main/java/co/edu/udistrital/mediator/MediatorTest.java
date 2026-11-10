// Archivo: MediatorTest.java | Paquete: co.edu.udistrital.mediator
package co.edu.udistrital.mediator;

import co.edu.udistrital.mediator.controller.Client;
import co.edu.udistrital.mediator.controller.ViewController;
import co.edu.udistrital.mediator.model.DoctorPool;
import co.edu.udistrital.mediator.model.EPS;
import co.edu.udistrital.mediator.model.ITurnPersistence;
import co.edu.udistrital.mediator.model.SerializationPersistence;
import co.edu.udistrital.mediator.model.TurnMachine;
import co.edu.udistrital.mediator.model.TurnType;
import co.edu.udistrital.mediator.view.ConsoleIn;
import co.edu.udistrital.mediator.view.ConsoleOut;
import co.edu.udistrital.mediator.view.IIn;
import co.edu.udistrital.mediator.view.IOut;
import java.io.File;

/**
 * Clase ejecutable principal (Composition Root) que ensambla la arquitectura MVC del patrón Mediator
 * y concatena las pruebas deterministas (Happy Paths), concurrencia por prioridad, serialización y casos límite.
 */
public class MediatorTest {

    private static final String SERIALIZATION_FILE = "eps_turns_history.ser";

    public static void main(String[] args) {
        // Limpieza previa del archivo de serialización para garantizar una corrida limpia y reproducible
        File previousFile = new File(SERIALIZATION_FILE);
        if (previousFile.exists()) {
            previousFile.delete();
        }

        // 1. Instanciación de abstracciones de Vista (I/O)
        IIn in = new ConsoleIn();
        IOut out = new ConsoleOut();

        // 2. Instanciación de Colegas, Servicio de Serialización y Mediador (EPS)
        TurnMachine turnMachine = new TurnMachine();
        DoctorPool doctorPool = new DoctorPool(400L);
        ViewController viewController = new ViewController(in, out);
        ITurnPersistence persistence = new SerializationPersistence(SERIALIZATION_FILE);

        EPS epsMediator = new EPS(turnMachine, doctorPool, viewController, persistence);

        // 3. Ensamblaje del Cliente Orquestador (Caja Negra)
        Client client = new Client(turnMachine, doctorPool, epsMediator, in, out);

        // =========================================================================
        // FASE 1: PRUEBAS ALFA / DETERMINISTAS (HAPPY PATHS Y PRIORIDAD CON HILOS)
        // =========================================================================
        client.printHeader("FASE 1: REGISTRO DE DOCTORES Y EMISIÓN/CONSUMO DE TURNOS POR PRIORIDAD");

        // Se registran 2 médicos en el pool (atención de 400 ms por turno en hilos secundarios)
        client.registerDoctor("DOC-101", "Carlos Méndez", "Medicina Interna y Urgencias");
        client.registerDoctor("DOC-102", "Laura Gómez", "Medicina General y Familiar");

        // Los primeros 2 turnos ocupan inmediatamente a los 2 doctores disponibles
        client.requestTurn(TurnType.GENERAL, "Juan Pérez (General 1)");
        client.requestTurn(TurnType.SPECIALIST, "María Rodríguez (Especialista 1)");

        // Con ambos doctores ocupados en sus hilos, emitimos 4 turnos en orden inverso de prioridad
        // para demostrar cómo la PriorityQueue del Mediador EPS reordena la atención:
        client.requestTurn(TurnType.GENERAL, "Pedro Ramírez (En cola - Prioridad 4)");
        client.requestTurn(TurnType.SPECIALIST, "Ana Martínez (En cola - Prioridad 3)");
        client.requestTurn(TurnType.PREFERENTIAL, "Rosa Gómez - Adulto Mayor (En cola - Prioridad 2)");
        client.requestTurn(TurnType.URGENCY, "Luis Torres - Trauma Severo (En cola - Prioridad 1)");

        // Esperamos a que el pool de doctores consuma asíncronamente todos los turnos encolados
        // Orden esperado de asignación de la cola: 1° Luis (U), 2° Rosa (P), 3° Ana (E), 4° Pedro (G)
        client.waitForAsyncConsumption(1500L);

        // =========================================================================
        // FASE 2: VERIFICACIÓN DE PERSISTENCIA CON SERIALIZACIÓN DE OBJETOS
        // =========================================================================
        client.printHeader("FASE 2: PERSISTENCIA Y DESERIALIZACIÓN DE TURNOS ATENDIDOS");
        client.showSerializedHistory();

        // =========================================================================
        // FASE 3: PRUEBAS DE VALIDACIÓN DE INVARIANTES Y CASOS LÍMITE (EDGE CASES)
        // =========================================================================
        client.printHeader("FASE 3: PRUEBAS DE VALIDACIÓN, PROTECCIÓN DE INVARIANTES Y CASOS LÍMITE");

        // Caso límite 1: Paciente con nombre vacío o en blanco
        client.requestTurn(TurnType.URGENCY, "   ");

        // Caso límite 2: Paciente nulo
        client.requestTurn(TurnType.PREFERENTIAL, null);

        // Caso límite 3: Tipo de turno nulo
        client.requestTurn(null, "Paciente Sin Tipo");

        // Caso límite 4: Registro de doctor con ID vacío
        client.registerDoctor("", "Doctor Inválido", "Cardiología");

        // Caso límite 5: Registro de doctor con especialidad nula
        client.registerDoctor("DOC-999", "Roberto Díaz", null);

        // Cierre ordenado de hilos consumidores y recursos de entrada
        client.shutdown();
    }
}