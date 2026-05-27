# PROCESO.md - Seguimiento de Tareas

## Reglas de Proceso
- **Estados de Tarea**:
  - `pendiente`: Tarea no iniciada.
  - `en_proceso`: Tarea siendo ejecutada por el subagente.
  - `completado`: Tarea aprobada por el subagente validador.
  - `error`: Tarea rechazada por el validador o con fallo crítico.
- **Flujo de Trabajo**:
  1. El agente principal marca la tarea como `en_proceso`.
  2. Se asigna un **Subagente Ejecutor** para implementar la funcionalidad.
  3. Se asigna un **Subagente Validador** para revisar que el resultado cumpla con el `PDR.md` y no introduzca regresiones.
  4. **Regla de Oro**: No se puede pasar a la siguiente tarea hasta que el validador marque la actual como `completado`.
  5. En caso de `error`, el ejecutor debe corregir basándose en el feedback del validador.
- **Actualización**: Este archivo se actualiza inmediatamente tras cada cambio de estado o nota de validación.

## Lista de Tareas

| ID | Descripción | Estado | Ejecutor ID | Validador ID | Notas | Última Actualización |
|----|-------------|--------|-------------|--------------|-------|----------------------|
| T1| Fase de Diseño de Interfaz (MainApp.java)(Sec 13) | completado | Exec_T1 | Val_T1 | Aprobado por Val_T1 | 2026-05-06 |
| T2| Implementar interfaz JavaFX: GridPane y manejo de eventos (Sec 11) | completado | Exec_T2 | Val_T2 | Aprobado por Val_T2 | 2026-05-06 |
| T3| Panificación de Persistencia (JSON) (Sec 12) | completado | Exec_T3 | Val_T3 | Aprobado por Val_T3 | 2026-05-06 |
| T4| Implementar persistencia JSON: configuración inicial y estado de partida (Sec 12) | completado | Exec_T4 | Val_T4 | Aprobado por Val_T4 | 2026-05-06 |

## Histórico de Cambios
- **2026-05-06**: Creación de `PROCESO.md` y definición de tareas iniciales.
