# PDR.md - Proyecto Juego de Mazmorras JavaFX

## 1. OBJETIVO DEL SISTEMA
Desarrollar un juego de mazmorras por turnos en Java con JavaFX, donde el jugador navega por un grafo de habitaciones (cada una representada como una matriz), interactúa con elementos del entorno y debe alcanzar una salida antes de quedarse sin vida o turnos.

**Restricción crítica:**
- Prohibido usar estructuras de `java.util`.
- Uso obligatorio de estructuras propias (listas, colas, pilas, grafos, árboles).

## 2. ARQUITECTURA DEL SISTEMA
- **Modelo**: Lógica del juego.
- **Vista**: JavaFX.
- **Controlador**: Gestión de eventos.
Separación estricta entre lógica e interfaz.

## 3. MODELO DE DOMINIO

### Jugador
- **Atributos**:
  - Vida [0-100]
  - Ataque [15-60]
  - Defensa [0-50]
  - Velocidad [1-10]
  - Posición (habitación, fila, columna)
  - Inventario (ListaEnlazada)
  - Equipo (slots)
- **Reglas**:
  - Defensa es automática (no consume acción).
  - No puede tener vida negativa.

### Enemigo
- **Atributos**:
  - Vida [45-100]
  - Ataque [7-20]
  - Defensa
  - Posición
- **Comportamiento**:
  - Si adyacente → atacar.
  - Si no → moverse hacia jugador.

### Objeto
- **Tipos**: Equipables, Consumibles, Llaves.
- **Reglas**:
  - Consumibles se destruyen al usar.
  - Objetos no se duplican (instancias únicas).

### Celda
- Solo una entidad por celda.

### Habitación
- Matriz mxn (≤10x10).
- Implementada con estructura propia.

### Grafo
- Nodos: habitaciones.
- Aristas: puertas.

## 4. SISTEMA DE TURNOS
- **Cola de turnos**: Estructura propia.
- **Orden**: 1. Jugador, 2. Enemigos.
- **Contador global de turnos**: `turnosRestantes` (se decrementa cada turno).
- **Derrota**: Si `turnosRestantes == 0`.
- **Opcional**: Contador por habitación con límite de turnos.

## 5. MOVIMIENTO
- BFS sobre matriz.
- Coste 1 por celda, no diagonal.
- **Visualización obligatoria**: Iluminar celdas alcanzables (solo esas son seleccionables).

## 6. COMBATE
- **Fórmula**: `vida = vida - max(0, ataque * (random * 2) - defensa)`.
- Defensa siempre aplicada automáticamente.

## 7. INVENTARIO
- Lista enlazada.
- Tamaño máximo configurable.

## 8. INTERACCIONES
- Recoger, usar, atacar, abrir puertas.

## 9. TRAMPAS
- Se activan al entrar y desaparecen.

## 10. PUERTAS
- Pueden requerir llave.
- Cambio de habitación inmediato.
- Fin de turno.

## 11. INTERFAZ (JavaFX)
- **Componentes**: GridPane, panel jugador, panel acciones, panel log.
- **Botón obligatorio**: "Mostrar camino óptimo" (calcula camino mínimo en grafo, muestra distancia y habitaciones restantes).

## 12. PERSISTENCIA JSON
- **Configuración inicial**: Grafo, habitaciones, objetos, enemigos.
- **Estado de partida**: Jugador, inventario, posiciones, turnos.

## 13. LOG DEL SISTEMA
- **Estructura**: Lista enlazada.
- **Eventos registrados**: Movimientos, ataques, daño, recogida, uso de objetos.
- Se muestra al final de la partida.

## 14. INVARIANTES
- Jugador siempre en celda válida.
- Vida ≥ 0.
- Una entidad por celda.
- Objetos no duplicados.

## 15. EXCEPCIONES
- `MovimientoInvalidoException`
- `AccionInvalidaException`
- `JsonException`

## 16. CASOS DE USO
### Mover jugador
- **Pre**: Celda válida.
- **Post**: Posición actualizada.

## 17. PRUEBAS
- Estructuras de datos.
- Lógica del juego.
- JSON.

## 18. DISEÑO PREVIO
- Bocetos de interfaz.
- Distribución de elementos.

## 19. USO DE IA
- Registro de prompts, resultados y análisis crítico.

## 20. ESTRUCTURAS DE DATOS Y JUSTIFICACIÓN
- **Lista enlazada**: Inventario (inserción eficiente).
- **Cola**: Turnos (FIFO).
- **Grafo**: Mapa.
- **BFS**: Caminos mínimos (O(V+E)).

## 21. CONDICIONES DE FIN
- **Victoria**: Llegar a salida.
- **Derrota**: Vida = 0 o Turnos = 0.

## 22. FLUJO DE TURNO
1. Calcular movimiento.
2. Iluminar celdas.
3. Seleccionar destino.
4. Mover.
5. Acción.
6. Enemigos actúan.
7. Actualizar estado.

## 23. OBJETIVO DEL DOCUMENTO
Permitir a un agente generar: PDR.md, PROCESO.md, UML, código base, sin ambigüedad.
