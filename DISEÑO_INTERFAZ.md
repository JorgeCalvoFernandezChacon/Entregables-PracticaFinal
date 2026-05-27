  # Diseño de Interfaz - Juego de Mazmorras

Este documento detalla la distribución de elementos y la lógica de visualización de la interfaz de usuario desarrollada en JavaFX, basada en los requerimientos del PDR.md.

## 1. Distribución de la Pantalla (Layout General)

La interfaz se organizará utilizando un `BorderPane` como contenedor principal para asegurar una distribución clara y responsiva.

### A. Área Central (Centro) - Tablero de Juego (`GridPane`)
- **Componente**: `GridPane`.
- **Función**: Representación visual de la habitación actual (matriz mxn).
- **Elementos**: Cada celda de la matriz será un `StackPane` o `Button` que contiene:
    - Fondo de color según el tipo de terreno o estado.
    - Icono/Imagen representando la entidad presente (Jugador, Enemigo, Objeto, Puerta, Salida).
- **Interacción**: Las celdas iluminadas serán clicables para ejecutar el movimiento.

### B. Panel del Jugador (Derecha - Superior)
- **Componente**: `VBox` con etiquetas (`Label`) y barras de progreso (`ProgressBar`).
- **Información Visualizada**:
    - **Vida**: Barra de progreso (0-100) y texto numérico.
    - **Ataque**: Valor numérico.
    - **Defensa**: Valor numérico.
    - **Velocidad**: Valor numérico.
    - **Habitación Actual**: Nombre o coordenadas del nodo en el grafo.
    - **Turnos Restantes**: Contador global visible.

### C. Panel de Acciones (Derecha - Inferior)
- **Componente**: `FlowPane` o `GridPane` de botones.
- **Botones de Interacción**:
    - `Atacar`: Ejecuta combate si hay un enemigo adyacente.
    - `Recoger`: Recoge un objeto en la celda actual.
    - `Usar Objeto`: Abre un submenú del inventario para consumir/equipar.
    - `Abrir Puerta`: Interactúa con puertas en la celda.
- **Botón Especial (Obligatorio)**:
    - `Mostrar Camino Óptimo`: Calcula el camino más corto hacia la salida usando BFS sobre el grafo de habitaciones. Al activarse, muestra en el Log la distancia y la cantidad de habitaciones restantes.

### D. Panel del Log (Inferior)
- **Componente**: `TextArea` o `ListView` con scroll automático.
- **Función**: Registro en tiempo real de los eventos del juego.
- **Contenido**:
    - "Te has movido a (x, y)".
    - "Has atacado al Enemigo X y causado 15 de daño".
    - "Has recogido una Llave de Plata".
    - "Trampa activada: pierdes 10 de vida".

---

## 2. Visualización de Celdas y Movimiento

La interfaz debe reflejar dinámicamente las posibilidades de movimiento del jugador basadas en su atributo de `Velocidad`.

### A. Celdas Alcanzables (Iluminación)
- **Lógica**: Al iniciar el turno del jugador, se ejecuta un BFS sobre la matriz de la habitación actual limitado por la `Velocidad` del jugador.
- **Efecto Visual**: Las celdas que resulten alcanzables cambiarán su color de fondo a un tono brillante (ej. Amarillo Claro o Verde Neón) para indicar que son seleccionables.
- **Restricción**: Solo las celdas iluminadas responderán al evento de clic.

### B. Camino Óptimo
- **Visualización en Matriz**: Si el camino óptimo pasa por la habitación actual, las celdas que conducen a la puerta de salida se resaltarán con un color distinto (ej. Azul Brillante o Dorado).
- **Indicadores**: Se pueden añadir pequeñas flechas direccionales dentro de las celdas para guiar al jugador hacia la salida de la habitación.

---

## 3. Diseño de Elementos Visuales (Estética)

| Elemento | Representación Sugerida | Color de Fondo/Borde |
| :--- | :--- | :--- |
| **Jugador** | Icono de Guerrero / Círculo Azul | Borde Dorado |
| **Enemigo** | Icono de Monstruo / Círculo Rojo | Borde Negro |
| **Objeto** | Icono de Cofre o Item / Círculo Amarillo | Sin borde |
| **Puerta** | Icono de Puerta / Rectángulo Marrón | Borde Gris |
| **Salida** | Icono de Portal / Estrella Verde | Brillo Animado |
| **Celda Vacía** | Color Neutro (Gris Oscuro) | Borde Tenue |
| **Celda Alcanzable**| Color Resaltado (Amarillo) | Borde Blanco |
