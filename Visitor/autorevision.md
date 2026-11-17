# Informe de Auditoría Técnica — Presentación Visitor e Interpreter (Canva + Java 17)

## 1. Verificación de los 12 Puntos de Auditoría (Sección 12)
1. **Estructura (17 bloques en orden estricto, sin extras ni cierres)**:
   - Bloque 1: Portada general con campos visibles (`Autor`, `Curso`, `Institución`, `Fecha`).
   - Bloques 2–9 (Módulo 1 — Visitor): Analogía, Agenda, Objetivos, Definición, Diagrama, Ejemplo (3 diapositivas), Pruebas, Bibliografía.
   - Bloques 10–17 (Módulo 2 — Interpreter): Analogía, Agenda, Objetivos, Definición, Diagrama, Ejemplo (3 diapositivas), Pruebas, Bibliografía.
   - Sin diapositiva de cierre, agradecimientos ni resumen global.
2. **Analogía**: Primera diapositiva de cada módulo, una sola diapositiva, incluye situación en 2 frases, tabla de mapeo de 2 columnas a los participantes formales y línea final con el límite de la analogía.
3. **Agenda**: Lista únicamente las 6 secciones posteriores a la Analogía (`Objetivos`, `Definición`, `Diagrama`, `Ejemplo`, `Pruebas`, `Bibliografía`).
4. **Definición**: Presenta el problema técnico antes de la solución, la intención según la fuente, los participantes formales, el mecanismo de resolución y cuándo aplicarlo o no.
5. **Independencia total entre módulos**:
   - Módulo 1 (`Visitor`): Cartografía urbana (`visitor.domain`: `MapNode`, `ResidentialBuilding`, `RoadSegment`, `MapNodeVisitor`, `MaintenanceCostVisitor`, `GeoJsonExportVisitor`, `CityMap`).
   - Módulo 2 (`Interpreter`): Dosificación clínica en laboratorio (`interpreter.domain` / `interpreter.parser`: `FormulaExpression`, `NumberLiteral`, `ReagentVariable`, `AddOperation`, `SubtractOperation`, `MultiplyOperation`, `DosageContext`, `FormulaTokenizer`, `FormulaParser`).
   - Cero clases, analogías, entidades o datos compartidos.
6. **Términos excluidos en diapositivas**: No aparecen las siglas de arquitectura ni menciones al catálogo en el cuerpo de las diapositivas.
7. **Código Java 17 compilado y verificado**:
   - Compilado con `javac --release 17` y ejecutado sin errores.
   - Cada fragmento tiene 18 líneas o menos por bloque.
   - Sin `TODO`, `...`, nombres genéricos ni comentarios redundantes.
8. **Participantes formales y mapeo**: Coinciden con Gamma et al. (1994) y Refactoring.Guru.
9. **Estilo y redacción**: Prosa técnica seca, sin palabras vetadas, sin emojis, máximo 6 líneas de texto fuera de tablas y código.
10. **Marcas `[VERIFICAR]`**: Incluidas explícitamente para señalar que Refactoring.Guru no posee entrada dedicada a Interpreter.
11. **Marcos pendientes visibles**: Rotulados como `DIAGRAMA PENDIENTE` y `CAPTURA PENDIENTE`.
12. **Pruebas**: Exactamente 2 casos por módulo (camino normal y entrada inválida), ejecutados por `Main.java` y con la salida real incluida en el marco de captura.
