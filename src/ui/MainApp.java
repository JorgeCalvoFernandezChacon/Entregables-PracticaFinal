package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import modelo.*;
import logica.*;
import estructuras.*;
import excepciones.*;
import java.io.IOException;

public class MainApp extends Application {
    private Stage primaryStage;
    private GameContext ctx;
    private GridPane gameGrid;
    private Label lblVidaText, lblAtaque, lblDefensa, lblVelocidad, lblTurnos, lblHabitacion;
    private ProgressBar vidaBar;
    private TextArea logArea;
    private Button btnAtacar, btnRecoger, btnUsar, btnAbrirPuerta, btnCaminoOptimo, btnVolverMenu, btnRendirse, btnGuardar, btnEsperar;
    private ListaEnlazada<Posicion> caminoOptimoPath;
    private boolean mostrarCaminoOptimo = false;
    private boolean haMovido = false;
    private boolean haActuado = false;
    private boolean juegoTerminado = false;
    private Label lblEstadoMovimiento, lblEstadoAccion;
    private VBox inventarioContent;

    private enum Dificultad {
        FACIL(150, 25, 15, 2, 300, 0.6),
        NORMAL(100, 20, 10, 1, 250, 1.0),
        DIFICIL(70, 15, 5, 1, 225, 1.6);

        int vida, ataque, defensa, velocidad, turnos;
        double multEnemigo;

        Dificultad(int vida, int ataque, int defensa, int velocidad, int turnos, double multEnemigo) {
            this.vida = vida;
            this.ataque = ataque;
            this.defensa = defensa;
            this.velocidad = velocidad;
            this.turnos = turnos;
            this.multEnemigo = multEnemigo;
        }
    }

    public static class GameContext {
        public Jugador jugador;
        public Grafo grafo;
        public Habitacion[] habitaciones;
        public Habitacion currentHabitacion;
        public GestorTurnos gestorTurnos;
        public InteraccionService interaccionService;
        public int exitHabitacionId;
        public Dificultad dificultad;
        public Arbol<String> arbolItems;

        public GameContext(Dificultad dif) {
            this.dificultad = dif;
            this.jugador = new Jugador(dif.vida, dif.ataque, dif.defensa, dif.velocidad, 0, 0);
            this.jugador.setHabitacionId(0);

            int numRooms = 5;
            this.grafo = new Grafo(numRooms);
            this.grafo.addEdge(0, 1, true);
            this.grafo.addEdge(1, 2, true);
            this.grafo.addEdge(2, 3, true);
            this.grafo.addEdge(3, 4, true);
            this.exitHabitacionId = 4;

            this.habitaciones = new Habitacion[numRooms];
            for (int i = 0; i < numRooms; i++) {
                int rows = 4 + (int)(Math.random() * 4);
                int cols = 4 + (int)(Math.random() * 4);
                this.habitaciones[i] = new Habitacion(rows, cols);
                populateRoom(this.habitaciones[i], i);
            }

            this.currentHabitacion = habitaciones[this.jugador.getHabitacionId()];
            this.gestorTurnos = new GestorTurnos(dif.turnos, jugador);
            this.interaccionService = new InteraccionService();
            this.arbolItems = new Arbol<>("Items");
            Arbol.Nodo<String> armas = arbolItems.getRoot().addChild("⚔ Armas");
            armas.addChild("️🗡 Espada");
            armas.addChild("🪓 Hacha");
            Arbol.Nodo<String> proteccion = arbolItems.getRoot().addChild("🛡️ Protección");
            proteccion.addChild("🛡️ Escudo");
            proteccion.addChild("👕 Armadura");
            Arbol.Nodo<String> consumibles = arbolItems.getRoot().addChild("🫙 Consumibles");
            consumibles.addChild("🧋 Poción");
            Arbol.Nodo<String> llaves = arbolItems.getRoot().addChild("🔑 Llaves");
            arbolItems.getRoot().addChild("⚠️ Trampas");
        }

        private void populateRoom(Habitacion h, int roomId) {
            int rows = h.getFilas();
            int cols = h.getColumnas();
            boolean[][] ocupado = new boolean[rows][cols];
            ocupado[0][0] = true;

            double mult = dificultad.multEnemigo;
            int numEnemigos = 1 + (int)(Math.random() * 3);
            for (int i = 0; i < numEnemigos; i++) {
                int[] pos = findFreeCell(ocupado, rows, cols);
                if (pos != null) {
                    int vida = (int)((45 + Math.random() * 56) * mult);
                    int ataque = (int)((7 + Math.random() * 14) * mult);
                    int def = (int)(Math.random() * 15 * mult);
                    int vel = 3 + (int)(Math.random() * 5);
                    h.getCelda(pos[0], pos[1]).setEntidad(new Enemigo(vida, ataque, def, pos[0], pos[1], vel));
                    ocupado[pos[0]][pos[1]] = true;
                }
            }

            int numObjetos = 1 + (int)(Math.random() * 3);
            for (int i = 0; i < numObjetos; i++) {
                int[] pos = findFreeCell(ocupado, rows, cols);
                if (pos != null) {
                    int tipo = (int)(Math.random() * 3);
                    Objeto obj;
                    if (tipo == 0) {
                        obj = new Consumible("Poción", "Cura vida", 15 + (int)(Math.random() * 20));
                    } else if (tipo == 1) {
                        Equipable.Slot[] slots = Equipable.Slot.values();
                        Equipable.Slot slot = slots[(int)(Math.random() * slots.length)];
                        String[] prefijos = {"rústico", "básico", "fino", "élfico", "enano", "mágico"};
                        String[] nombresSlot = {"Espada", "Escudo", "Armadura"};
                        int ba = (int)(Math.random() * 10);
                        int bd = (int)(Math.random() * 8);
                        String nombre = prefijos[(int)(Math.random() * prefijos.length)] + " " + nombresSlot[slot.ordinal()];
                        obj = new Equipable(nombre, "Ataque+" + ba + " Defensa+" + bd, ba, bd, slot);
                    } else {
                        obj = new Trampa("Trampa", "Causa daño", 5 + (int)(Math.random() * 15));
                    }
                    h.getCelda(pos[0], pos[1]).setObjeto(obj);
                    ocupado[pos[0]][pos[1]] = true;
                }
            }

            if (roomId < 4) {
                int dr = rows - 1;
                int dc = cols - 1;
                String keyId = (roomId == 0 || roomId == 1) ? null : "door" + roomId;
                h.getCelda(dr, dc).setObjeto(new Puerta("Puerta " + roomId, "Lleva a la siguiente sala", keyId, roomId + 1));
                ocupado[dr][dc] = true;
                if (keyId != null) {
                    int[] kpos = findFreeCell(ocupado, rows, cols);
                    if (kpos != null) {
                        h.getCelda(kpos[0], kpos[1]).setObjeto(new Llave("Llave " + roomId, "Abre la puerta " + roomId, keyId));
                        ocupado[kpos[0]][kpos[1]] = true;
                    }
                }
            } else {
                ocupado[rows - 1][cols - 1] = true;
            }
        }

        private int[] findFreeCell(boolean[][] ocupado, int rows, int cols) {
            for (int intento = 0; intento < 50; intento++) {
                int r = (int)(Math.random() * rows);
                int c = (int)(Math.random() * cols);
                if (!ocupado[r][c]) return new int[]{r, c};
            }
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (!ocupado[r][c]) return new int[]{r, c};
                }
            }
            return null;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        mostrarPantallaInicio();
    }

    private void mostrarPantallaInicio() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #1a1a2e;");

        Label title = new Label("⚔ PRÁCTICA FINAL ⚔");
        title.setFont(Font.font("Monospace", FontWeight.BOLD, 32));
        title.setTextFill(Color.GOLD);

        Label subtitle = new Label("Un juego de mazmorras por turnos");
        subtitle.setFont(Font.font("Monospace", 14));
        subtitle.setTextFill(Color.web("#87CEEB"));

        Label diffLabel = new Label("SELECCIONA DIFICULTAD:");
        diffLabel.setFont(Font.font("Monospace", FontWeight.BOLD, 16));
        diffLabel.setTextFill(Color.WHITE);

        HBox btnRow = new HBox(15);
        btnRow.setAlignment(Pos.CENTER);

        Button easyBtn = new Button("FÁCIL");
        easyBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14; -fx-padding: 10 25;");
        Button normalBtn = new Button("NORMAL");
        normalBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-size: 14; -fx-padding: 10 25;");
        Button hardBtn = new Button("DIFÍCIL");
        hardBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 14; -fx-padding: 10 25;");

        Label descLabel = new Label();
        descLabel.setTextFill(Color.LIGHTGRAY);

        easyBtn.setOnAction(e -> descLabel.setText("Fácil: 150❤, 300 turnos, enemigos débiles, 2 movimientos por turno"));
        normalBtn.setOnAction(e -> descLabel.setText("Normal: 100❤, 250 turnos, enemigos equilibrados, 1 movimiento por turno"));
        hardBtn.setOnAction(e -> descLabel.setText("Difícil: 70❤, 225 turnos, enemigos fuertes, 1 movimiento por turno"));

        easyBtn.setOnMouseClicked(e -> iniciarJuego(Dificultad.FACIL));
        normalBtn.setOnMouseClicked(e -> iniciarJuego(Dificultad.NORMAL));
        hardBtn.setOnMouseClicked(e -> iniciarJuego(Dificultad.DIFICIL));

        btnRow.getChildren().addAll(easyBtn, normalBtn, hardBtn);

        Button tutorialBtn = new Button("📖 CONTROLES Y TUTORIAL");
        tutorialBtn.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-size: 14; -fx-padding: 10 25; -fx-border-color: #1abc9c; -fx-border-width: 2;");
        tutorialBtn.setOnAction(e -> mostrarTutorial());

        Button cargarBtn = new Button("📂 CARGAR PARTIDA");
        cargarBtn.setStyle("-fx-background-color: #8e44ad; -fx-text-fill: white; -fx-font-size: 14; -fx-padding: 10 25;");
        cargarBtn.setOnAction(e -> handleCargarPartida());
        if (!PersistenciaService.archivoExiste("partida.json")) {
            cargarBtn.setDisable(true);
            cargarBtn.setText("📂 CARGAR PARTIDA (no hay guardado)");
        }

        root.getChildren().addAll(title, subtitle, diffLabel, btnRow, descLabel, tutorialBtn, cargarBtn);

        Scene scene = new Scene(root, 520, 580);
        primaryStage.setTitle("Dungeon Crawler - JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void mostrarTutorial() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("📖 CONTROLES Y TUTORIAL");
        alert.setHeaderText("Dungeon Crawler - Guía Rápida");
        alert.setContentText(
            "🎯 OBJETIVO\n"
            + "Llegar a la sala 5 (🏆 SALIDA) antes de quedarte sin vida o turnos.\n\n"
            +             "⌨ TECLADO\n"
            + "• W A S D → moverte (una celda por tecla)\n"
            + "• ENTER → saltar turno\n\n"
            + "🖱️ CONTROLES (ratón)\n"
            + "• Click en celda amarilla → moverte\n"
            + "• Botón ⚔ Atacar → golpea a enemigos adyacentes (4 direcciones)\n"
            + "• Botón 📥 Recoger → coge el objeto de tu celda\n"
            + "• Botón 💼 Usar Objeto → consume/equipa del inventario\n"
            + "• Botón 🚪 Abrir Puerta → atraviesa al siguiente cuarto\n"
            + "• Botón 📍 Mostrar Camino Óptimo → resalta la ruta a la puerta\n"
            + "• Botón ⏭ Terminar Turno → finaliza tu turno sin gastar acción\n"
            + "• Botón 🔙 Volver atrás → regresa a la sala anterior\n\n"
            + "🔄 POR TURNO\n"
            + "• En cada turno puedes hacer 1 movimiento Y 1 acción.\n"
            + "• Primero muévete, luego ataca/recoge/usa.\n"
            + "• O puedes solo moverte, solo actuar, o pasar directamente.\n\n"
            + "👾 ENEMIGOS\n"
            + "Al terminar tu turno, todos los enemigos actúan: "
            + "atacan si están cerca o se mueven hacia ti.\n\n"
            + "📦 OBJETOS\n"
            + "• 🧋 Poción → recupera vida al usarla\n"
            + "• 🗡 Equipo → mejora ataque/defensa al equiparlo\n"
            + "• 🔑 Llave → abre puertas con candado\n"
            + "• ⚠ Trampa → se activa al pisarla (causa daño)\n"
            + "• 🚪 Puerta → conduce a otra sala (🔒 = necesita llave)\n\n"
            + "📊 LEYENDA DEL MAPA\n"
            + "🙍‍♂️ azul = Tú  |  👹 rojo = Enemigo\n"
            + "🟡 = Celda alcanzable  |  🔵 = Camino óptimo\n"
            + "🏆 verde = Salida"
        );
        alert.setWidth(500);
        alert.show();
    }

    private void iniciarJuego(Dificultad dif) {
        juegoTerminado = false;
        SistemaLog.getInstance().limpiar();
        if (logArea != null) logArea.clear();
        ctx = new GameContext(dif);
        crearPantallaJuego();
    }

    private void crearPantallaJuego() {
        juegoTerminado = false;
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #2c3e50;");

        gameGrid = new GridPane();
        gameGrid.setAlignment(Pos.CENTER);
        gameGrid.setHgap(5);
        gameGrid.setVgap(5);
        root.setCenter(gameGrid);

        Button btnVolverMenuTop = new Button("🏠 VOLVER AL MENÚ");
        btnVolverMenuTop.setStyle("-fx-background-color: #7f8c8d; -fx-text-fill: white; -fx-font-weight: bold;");
        btnVolverMenuTop.setOnAction(e -> volverAlMenu());
        btnGuardar = new Button("💾 GUARDAR");
        btnGuardar.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        btnGuardar.setOnAction(e -> handleGuardar());
        HBox topBar = new HBox(10, btnGuardar, btnVolverMenuTop);
        topBar.setPadding(new Insets(0, 0, 10, 0));
        root.setTop(topBar);

        VBox rightPanel = new VBox(15);
        rightPanel.setPadding(new Insets(0, 0, 0, 10));
        rightPanel.setMaxWidth(240);

        VBox playerPanel = new VBox(5);
        playerPanel.setStyle("-fx-background-color: #34495e; -fx-border-color: #1abc9c; -fx-border-width: 2; -fx-padding: 10;");
        Label statsTitle = new Label("📊 ESTADÍSTICAS");
        statsTitle.setTextFill(Color.CYAN);
        statsTitle.setFont(Font.font("Monospace", FontWeight.BOLD, 13));

        lblVidaText = new Label("❤ Vida: 100/100");
        lblVidaText.setTextFill(Color.LIGHTGRAY);
        vidaBar = new ProgressBar(1.0);
        vidaBar.setPrefWidth(150);
        vidaBar.setStyle("-fx-accent: #e74c3c;");
        lblAtaque = new Label();
        lblAtaque.setTextFill(Color.LIGHTGRAY);
        lblDefensa = new Label();
        lblDefensa.setTextFill(Color.LIGHTGRAY);
        lblVelocidad = new Label();
        lblVelocidad.setTextFill(Color.LIGHTGRAY);
        lblTurnos = new Label();
        lblTurnos.setTextFill(Color.LIGHTGRAY);
        lblHabitacion = new Label();
        lblHabitacion.setTextFill(Color.LIGHTGRAY);
        lblEstadoMovimiento = new Label("👟 Movimiento: ⬜");
        lblEstadoMovimiento.setTextFill(Color.web("#f1c40f"));
        lblEstadoAccion = new Label("⚔ Acción: ⬜");
        lblEstadoAccion.setTextFill(Color.web("#f1c40f"));
        playerPanel.getChildren().addAll(statsTitle, lblVidaText, vidaBar, lblAtaque, lblDefensa, lblVelocidad, lblTurnos, lblHabitacion, lblEstadoMovimiento, lblEstadoAccion);

        VBox inventarioPanel = new VBox(5);
        inventarioPanel.setStyle("-fx-background-color: #34495e; -fx-border-color: #f39c12; -fx-border-width: 2; -fx-padding: 10;");
        Label invTitle = new Label("🎒 INVENTARIO");
        invTitle.setTextFill(Color.GOLD);
        invTitle.setFont(Font.font("Monospace", FontWeight.BOLD, 13));
        inventarioContent = new VBox(3);
        inventarioContent.setFillWidth(true);
        inventarioPanel.getChildren().addAll(invTitle, inventarioContent);

        VBox actionPanel = new VBox(10);
        actionPanel.setStyle("-fx-background-color: #34495e; -fx-border-color: #e74c3c; -fx-border-width: 2; -fx-padding: 10;");

        Label actionTitle = new Label("🎯 ACCIONES");
        actionTitle.setTextFill(Color.ORANGE);
        actionTitle.setFont(Font.font("Monospace", FontWeight.BOLD, 13));

        btnAtacar = new Button("⚔ Atacar");
        btnAtacar.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        btnRecoger = new Button("📦 Recoger");
        btnRecoger.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        btnUsar = new Button("💼 Usar");
        btnUsar.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
        btnAbrirPuerta = new Button("🚪 Abrir");
        btnAbrirPuerta.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white;");
        btnCaminoOptimo = new Button("📍 Camino");
        btnCaminoOptimo.setStyle("-fx-background-color: #9b59b6; -fx-text-fill: white;");
        btnRendirse = new Button("🏳 Rendirse");
        btnRendirse.setStyle("-fx-background-color: #7f8c8d; -fx-text-fill: white;");
        btnEsperar = new Button("⏭ Terminar");
        btnEsperar.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-font-weight: bold;");
        btnVolverMenu = new Button("🏠 Menú");
        btnVolverMenu.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white;");
        btnVolverMenu.setVisible(false);

        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(8);
        buttonGrid.setVgap(8);

        buttonGrid.add(btnAtacar, 0, 0);
        buttonGrid.add(btnRecoger, 1, 0);
        buttonGrid.add(btnUsar, 0, 1);
        buttonGrid.add(btnAbrirPuerta, 1, 1);
        buttonGrid.add(btnCaminoOptimo, 0, 2);
        buttonGrid.add(btnRendirse, 1, 2);
        buttonGrid.add(btnEsperar, 0, 3);
        GridPane.setColumnSpan(btnEsperar, 2);

        for (var btn : new Button[]{btnAtacar, btnRecoger, btnUsar, btnAbrirPuerta, btnCaminoOptimo, btnRendirse}) {
            btn.setPrefWidth(95);
            btn.setStyle(btn.getStyle() + "-fx-font-family: 'Monospace'; -fx-font-size: 11px;");
        }

        for (var btn : new Button[]{btnEsperar}) {
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setStyle(btn.getStyle() + "-fx-font-family: 'Monospace'; -fx-font-size: 11px;");
        }

        actionPanel.getChildren().addAll(actionTitle, buttonGrid);

        rightPanel.getChildren().addAll(playerPanel, inventarioPanel, actionPanel);
        root.setRight(rightPanel);

        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefHeight(150);
        logArea.setStyle("-fx-control-inner-background: #1a1a2e; -fx-text-fill: #00ff00;");
        root.setBottom(logArea);

        btnAtacar.setOnAction(e -> handleAtacar());
        btnRecoger.setOnAction(e -> handleRecoger());
        btnUsar.setOnAction(e -> handleUsar());
        btnAbrirPuerta.setOnAction(e -> handleAbrirPuerta());
        btnCaminoOptimo.setOnAction(e -> handleCaminoOptimo());
        btnVolverMenu.setOnAction(e -> volverAlMenu());
        btnRendirse.setOnAction(e -> handleRendirse());
        btnEsperar.setOnAction(e -> finalizarTurnoJugador());

        iniciarTurno();
        updateUI();

        Scene scene = new Scene(root, 950, 850);
        scene.setOnKeyPressed(this::handleTeclado);
        primaryStage.setTitle("Dungeon Crawler - Sala " + ctx.jugador.getHabitacionId());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateUI() {
        ctx.currentHabitacion = ctx.habitaciones[ctx.jugador.getHabitacionId()];

        double vidaRatio = (double) ctx.jugador.getVida() / ctx.jugador.getVidaMaxima();
        lblVidaText.setText("❤ Vida: " + ctx.jugador.getVida() + "/" + ctx.jugador.getVidaMaxima());
        vidaBar.setProgress(Math.max(0, vidaRatio));
        if (vidaRatio > 0.5) {
            vidaBar.setStyle("-fx-accent: #2ecc71;");
        } else if (vidaRatio > 0.25) {
            vidaBar.setStyle("-fx-accent: #f39c12;");
        } else {
            vidaBar.setStyle("-fx-accent: #e74c3c;");
        }
        lblAtaque.setText("⚔ Ataque: " + ctx.jugador.getAtaqueTotal());
        lblDefensa.setText("🛡 Defensa: " + ctx.jugador.getDefensaTotal());
        lblVelocidad.setText("👟 Velocidad: " + ctx.jugador.getVelocidad());
        lblTurnos.setText("⏳ Turnos: " + ctx.gestorTurnos.getTurnosRestantes());
        lblHabitacion.setText("🏠 Habitación: " + ctx.jugador.getHabitacionId());

        gameGrid.getChildren().clear();
        int rows = ctx.currentHabitacion.getFilas();
        int cols = ctx.currentHabitacion.getColumnas();

        ListaEnlazada<Posicion> alcanzables = MovimientoService.obtenerCeldasAlcanzables(ctx.jugador, ctx.currentHabitacion);

        boolean isExitRoom = ctx.jugador.getHabitacionId() == ctx.exitHabitacionId;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                StackPane cell = new StackPane();
                cell.setPrefSize(45, 45);
                cell.setStyle("-fx-border-color: #555; -fx-border-width: 1;");

                Celda logicCell = ctx.currentHabitacion.getCelda(r, c);

                boolean isReachable = false;
                if (!haMovido) {
                    for (int i = 0; i < alcanzables.size(); i++) {
                        Posicion p = alcanzables.get(i);
                        if (p.fila == r && p.columna == c) {
                            isReachable = true;
                            break;
                        }
                    }
                }

                boolean isOptimoPath = false;
                if (mostrarCaminoOptimo && caminoOptimoPath != null) {
                    for (int i = 0; i < caminoOptimoPath.size(); i++) {
                        Posicion p = caminoOptimoPath.get(i);
                        if (p.fila == r && p.columna == c) {
                            isOptimoPath = true;
                            break;
                        }
                    }
                }

                if (r == ctx.jugador.getFila() && c == ctx.jugador.getColumna()) {
                    cell.setStyle("-fx-background-color: #1a5276; -fx-border-color: #FFD700; -fx-border-width: 2;");
                    Label label = new Label("🙍‍♂️");
                    label.setFont(Font.font("Monospace", FontWeight.BOLD, 18));
                    label.setTextFill(Color.web("#85c1e9"));
                    cell.getChildren().add(label);
                    if (logicCell.hasObjeto()) {
                        Objeto obj = logicCell.getObjeto();
                        String subText;
                        Color subColor;
                        if (obj instanceof Puerta) {
                            subText = "🚪";
                            subColor = Color.web("#4caf50");
                        } else if (obj instanceof Consumible) {
                            subText = "🫙";
                            subColor = Color.web("#f1c40f");
                        } else if (obj instanceof Equipable) {
                            subText = "🗡";
                            subColor = Color.web("#f1c40f");
                        } else if (obj instanceof Llave) {
                            subText = "🔑️";
                            subColor = Color.web("#f1c40f");
                        } else if (obj instanceof Trampa) {
                            subText = "⚠";
                            subColor = Color.web("#f1c40f");
                        } else {
                            subText = "📦";
                            subColor = Color.web("#f1c40f");
                        }
                        Label sub = new Label(subText);
                        sub.setFont(Font.font("Monospace", FontWeight.BOLD, 10));
                        sub.setTextFill(subColor);
                        StackPane.setAlignment(sub, Pos.BOTTOM_RIGHT);
                        StackPane.setMargin(sub, new Insets(0, 2, 2, 0));
                        cell.getChildren().add(sub);
                        if (obj instanceof Puerta && ((Puerta) obj).getRequiredKeyId() != null) {
                            Label lock = new Label("🔒");
                            lock.setFont(Font.font("Monospace", FontWeight.BOLD, 8));
                            StackPane.setAlignment(lock, Pos.BOTTOM_LEFT);
                            StackPane.setMargin(lock, new Insets(0, 0, 2, 2));
                            cell.getChildren().add(lock);
                        }
                    }
                } else if (isOptimoPath) {
                    cell.setStyle("-fx-background-color: #3498db; -fx-border-color: #2980b9; -fx-border-width: 2;");
                    String dirArrow = "";
                    int pr = ctx.jugador.getFila();
                    int pc = ctx.jugador.getColumna();
                    if (caminoOptimoPath != null && caminoOptimoPath.size() > 0) {
                        for (int i = 0; i < caminoOptimoPath.size(); i++) {
                            Posicion p = caminoOptimoPath.get(i);
                            if (p.fila == r && p.columna == c) {
                                if (i + 1 < caminoOptimoPath.size()) {
                                    Posicion next = caminoOptimoPath.get(i + 1);
                                    if (next.fila < r) dirArrow = "↑";
                                    else if (next.fila > r) dirArrow = "↓";
                                    else if (next.columna < c) dirArrow = "←";
                                    else if (next.columna > c) dirArrow = "→";
                                }
                                break;
                            }
                        }
                    }
                    Label arrow = new Label(dirArrow);
                    arrow.setFont(Font.font("Monospace", FontWeight.BOLD, 18));
                    arrow.setTextFill(Color.WHITE);
                    cell.getChildren().add(arrow);
                } else if (isReachable && !juegoTerminado) {
                    cell.setStyle("-fx-background-color: #f1c40f; -fx-border-color: #f39c12; -fx-border-width: 2;");
                    if (logicCell.hasEntidad() && logicCell.getEntidad() instanceof Enemigo) {
                        Enemigo e = (Enemigo) logicCell.getEntidad();
                        Label label = new Label("👹");
                        label.setFont(Font.font("Monospace", FontWeight.BOLD, 16));
                        label.setTextFill(Color.web("#2c3e50"));
                        cell.getChildren().add(label);
                        Label hp = new Label(String.valueOf(e.getVida()));
                        hp.setFont(Font.font("Monospace", FontWeight.BOLD, 9));
                        hp.setTextFill(Color.web("#2c3e50"));
                        StackPane.setAlignment(hp, Pos.BOTTOM_RIGHT);
                        StackPane.setMargin(hp, new Insets(0, 1, 1, 0));
                        cell.getChildren().add(hp);
                        if (logicCell.hasObjeto()) {
                            Objeto obj = logicCell.getObjeto();
                            String objEmoji = "📦";
                            if (obj instanceof Consumible) objEmoji = "🫙";
                            else if (obj instanceof Equipable) objEmoji = "🗡";
                            else if (obj instanceof Llave) objEmoji = "🗝️";
                            else if (obj instanceof Trampa) objEmoji = "⚠";
                            Label objLabel = new Label(objEmoji);
                            objLabel.setFont(Font.font("Monospace", FontWeight.BOLD, 9));
                            objLabel.setTextFill(Color.web("#2c3e50"));
                            StackPane.setAlignment(objLabel, Pos.BOTTOM_LEFT);
                            StackPane.setMargin(objLabel, new Insets(0, 0, 1, 1));
                            cell.getChildren().add(objLabel);
                        }
                    } else if (logicCell.hasObjeto()) {
                        Objeto obj = logicCell.getObjeto();
                        if (obj instanceof Puerta) {
                            Puerta p = (Puerta) obj;
                            String text = "🚪";
                            Color color = (p.getRequiredKeyId() != null) ? Color.web("#004d00") : Color.web("#1b5e20");
                            Label label = new Label(text);
                            label.setFont(Font.font("Monospace", FontWeight.BOLD, 16));
                            label.setTextFill(color);
                            cell.getChildren().add(label);
                            if (p.getRequiredKeyId() != null) {
                                Label lock = new Label("🔒");
                                lock.setFont(Font.font("Monospace", FontWeight.BOLD, 10));
                                StackPane.setAlignment(lock, Pos.BOTTOM_RIGHT);
                                StackPane.setMargin(lock, new Insets(0, 1, 1, 0));
                                cell.getChildren().add(lock);
                            }
                        } else {
                            String emoji = "📦";
                            if (obj instanceof Consumible) emoji = "🫙";
                            else if (obj instanceof Equipable) emoji = "🗡";
                            else if (obj instanceof Llave) emoji = "🗝️";
                            else if (obj instanceof Trampa) emoji = "⚠";
                            Label label = new Label(emoji);
                            label.setFont(Font.font("Monospace", FontWeight.BOLD, 16));
                            label.setTextFill(Color.web("#2c3e50"));
                            cell.getChildren().add(label);
                        }
                    }
                } else if (logicCell.hasEntidad()) {
                    cell.setStyle("-fx-background-color: #641e16; -fx-border-color: #000000; -fx-border-width: 2;");
                    Label label = new Label("👹");
                    label.setFont(Font.font("Monospace", FontWeight.BOLD, 16));
                    label.setTextFill(Color.web("#e74c3c"));
                    cell.getChildren().add(label);
                    Enemigo e = (Enemigo) logicCell.getEntidad();
                    Label hp = new Label(String.valueOf(e.getVida()));
                    hp.setFont(Font.font("Monospace", FontWeight.BOLD, 9));
                    hp.setTextFill(Color.web("#ff6b6b"));
                    StackPane.setAlignment(hp, Pos.BOTTOM_RIGHT);
                    StackPane.setMargin(hp, new Insets(0, 1, 1, 0));
                    cell.getChildren().add(hp);
                    if (logicCell.hasObjeto()) {
                        Objeto obj = logicCell.getObjeto();
                        String objEmoji = "📦";
                        if (obj instanceof Consumible) objEmoji = "🫙";
                        else if (obj instanceof Equipable) objEmoji = "🗡";
                        else if (obj instanceof Llave) objEmoji = "🗝️";
                        else if (obj instanceof Trampa) objEmoji = "⚠";
                        Label objLabel = new Label(objEmoji);
                        objLabel.setFont(Font.font("Monospace", FontWeight.BOLD, 9));
                        objLabel.setTextFill(Color.web("#ff6b6b"));
                        StackPane.setAlignment(objLabel, Pos.BOTTOM_LEFT);
                        StackPane.setMargin(objLabel, new Insets(0, 0, 1, 1));
                        cell.getChildren().add(objLabel);
                    }
                } else if (isExitRoom && r == rows - 1 && c == cols - 1) {
                    cell.setStyle("-fx-background-color: #1a6e1a; -fx-border-color: #00ff00; -fx-border-width: 2;");
                    Label label = new Label("🏆");
                    label.setFont(Font.font("Monospace", FontWeight.BOLD, 20));
                    label.setTextFill(Color.web("#00ff00"));
                    cell.getChildren().add(label);
                    Label sub = new Label("SALIDA");
                    sub.setFont(Font.font("Monospace", FontWeight.BOLD, 7));
                    sub.setTextFill(Color.LIGHTGREEN);
                    StackPane.setAlignment(sub, Pos.BOTTOM_CENTER);
                    StackPane.setMargin(sub, new Insets(0, 0, 2, 0));
                    cell.getChildren().add(sub);
                } else if (logicCell.hasObjeto()) {
                    Objeto obj = logicCell.getObjeto();
                    if (obj instanceof Puerta) {
                        Puerta p = (Puerta) obj;
                        boolean necesitaLlave = p.getRequiredKeyId() != null && !p.isAbierta();
                        cell.setStyle(necesitaLlave
                            ? "-fx-background-color: #1b5e20; -fx-border-color: #4caf50; -fx-border-width: 2;"
                            : "-fx-background-color: #2e7d32; -fx-border-color: #a5d6a7; -fx-border-width: 2;");
                        Label label = new Label("🚪");
                        label.setFont(Font.font("Monospace", FontWeight.BOLD, 16));
                        label.setTextFill(necesitaLlave ? Color.web("#4caf50") : Color.web("#a5d6a7"));
                        cell.getChildren().add(label);
                        if (necesitaLlave) {
                            Label lock = new Label("🔒");
                            lock.setFont(Font.font("Monospace", FontWeight.BOLD, 10));
                            StackPane.setAlignment(lock, Pos.BOTTOM_RIGHT);
                            StackPane.setMargin(lock, new Insets(0, 1, 1, 0));
                            cell.getChildren().add(lock);
                        }
                    } else {
                        cell.setStyle("-fx-background-color: #7d6608; -fx-border-color: #f1c40f; -fx-border-width: 1;");
                        String emoji = "📦";
                        if (obj instanceof Consumible) emoji = "🫙";
                        else if (obj instanceof Equipable) emoji = "🗡";
                        else if (obj instanceof Llave) emoji = "🗝️";
                        else if (obj instanceof Trampa) emoji = "⚠";
                        Label label = new Label(emoji);
                        label.setFont(Font.font("Monospace", FontWeight.BOLD, 16));
                        label.setTextFill(Color.web("#f1c40f"));
                        cell.getChildren().add(label);
                    }
                } else {
                    cell.setStyle("-fx-background-color: #2c3e50; -fx-border-color: #555; -fx-border-width: 1;");
                }

                if (isReachable && ctx.jugador.isVivo() && !ctx.gestorTurnos.isDerrotaPorTurnos() && !haMovido && !juegoTerminado) {
                    final int finalR = r;
                    final int finalC = c;
                    cell.setOnMouseClicked(e -> {
                        mostrarCaminoOptimo = false;
                        try {
                            MovimientoService.moverJugador(ctx.jugador, ctx.currentHabitacion, new Posicion(finalR, finalC));
                            log("🙍‍♂️ Te has movido a (" + finalR + ", " + finalC + ")");
                            registrarMovimiento();
                        } catch (MovimientoInvalidoException ex) {
                            log("Error: " + ex.getMessage());
                            updateUI();
                        }
                    });
                }

                gameGrid.add(cell, c, r);
            }
        }
        actualizarInventario();
    }

    private void actualizarInventario() {
        inventarioContent.getChildren().clear();

        Equipable[] slots = ctx.jugador.getSlots();
        String[] slotEmojis = {"🗡", "🛡️", "👕"};
        String[] slotNombres = {"Mano", "Escudo", "Armadura"};
        for (int i = 0; i < slots.length; i++) {
            String texto;
            if (slots[i] != null) {
                texto = slotEmojis[i] + " [" + slotNombres[i] + "] " + slots[i].getNombre()
                    + " (ATK+" + slots[i].getBonoAtaque() + " DEF+" + slots[i].getBonoDefensa() + ")";
            } else {
                texto = "⬜ [" + slotNombres[i] + "] — vacío —";
            }
            Label slotLabel = new Label(texto);
            slotLabel.setTextFill(Color.web("#f1c40f"));
            slotLabel.setFont(Font.font("Monospace", 10));
            inventarioContent.getChildren().add(slotLabel);
        }

        Llave llave = ctx.jugador.getLlaveEquipada();
        String llaveTexto = llave != null ? "🗝️ [Llave] " + llave.getNombre() : "⬜ [Llave] — ninguna —";
        Label llaveLabel = new Label(llaveTexto);
        llaveLabel.setTextFill(Color.web("#f1c40f"));
        llaveLabel.setFont(Font.font("Monospace", 10));
        inventarioContent.getChildren().add(llaveLabel);

        Label sep = new Label("── 🎒 Inventario ──");
        sep.setTextFill(Color.LIGHTGRAY);
        sep.setFont(Font.font("Monospace", 11));
        sep.setStyle("-fx-padding: 5 0 0 0;");
        inventarioContent.getChildren().add(sep);

        Inventario inv = ctx.jugador.getInventario();
        if (inv.getTamaño() == 0) {
            Label empty = new Label("(vacío)");
            empty.setTextFill(Color.GRAY);
            empty.setFont(Font.font("Monospace", 11));
            inventarioContent.getChildren().add(empty);
        } else {
            for (int i = 0; i < inv.getTamaño(); i++) {
                Objeto obj = inv.obtenerObjeto(i);
                if (obj instanceof Equipable) continue;
                String emoji = "📦";
                if (obj instanceof Consumible) emoji = "🫙";
                else if (obj instanceof Llave) emoji = "🗝️";
                else if (obj instanceof Trampa) emoji = "⚠";
                Label itemLabel = new Label(emoji + " " + obj.getNombre());
                itemLabel.setTextFill(Color.LIGHTGRAY);
                itemLabel.setFont(Font.font("Monospace", 11));
                inventarioContent.getChildren().add(itemLabel);
            }
        }
    }

    private void log(String msg) {
        logArea.appendText(msg + "\n");
    }

    private void handleAtacar() {
        int fila = ctx.jugador.getFila();
        int columna = ctx.jugador.getColumna();

        int[][] direcciones = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] dir : direcciones) {
            int nf = fila + dir[0];
            int nc = columna + dir[1];

            if (nf >= 0 && nf < ctx.currentHabitacion.getFilas()
                    && nc >= 0 && nc < ctx.currentHabitacion.getColumnas()) {

                Celda celda = ctx.currentHabitacion.getCelda(nf, nc);
                if (celda.hasEntidad() && celda.getEntidad() instanceof Enemigo) {
                    Enemigo enemigo = (Enemigo) celda.getEntidad();
                    try {
                        int danio = ctx.interaccionService.atacarEnemigo(ctx.jugador, enemigo);
                        if (danio > 0) {
                            log("⚔ Atacaste al 👹: " + danio + " de daño.");
                        } else {
                            log("⚔ Atacaste al 👹, pero no lograste hacer daño (defensa muy alta).");
                        }
                        if (enemigo.isMuerto()) {
                            celda.setEntidad(null);
                            log("💀 ¡El 👹 ha muerto!");
                        }
                        registrarAccion();
                    } catch (AccionInvalidaException ex) {
                        log("Error: " + ex.getMessage());
                        updateUI();
                    }
                    return;
                }
            }
        }

        log("❌ No hay 👹 adyacentes.");
        updateUI();
    }

    private void handleRecoger() {
        try {
            Celda current = ctx.currentHabitacion.getCelda(ctx.jugador.getFila(), ctx.jugador.getColumna());
            Objeto obj = current.getObjeto();
            if (obj == null) {
                log("❌ No hay ningún objeto aquí.");
                updateUI();
                return;
            }
            String nombre = obj.getNombre();
            boolean esEquipable = obj instanceof Equipable;
            String emoji = "📦";
            if (obj instanceof Consumible) emoji = "🫙";
            else if (esEquipable) emoji = "🗡️";
            else if (obj instanceof Llave) emoji = "🗝️";
            else if (obj instanceof Trampa) emoji = "⚠";
            ctx.interaccionService.recogerObjeto(ctx.jugador, current);
            if (esEquipable) {
                log("🗡 Equipaste " + emoji + " " + nombre);
            } else {
                log("📥 Recogiste " + emoji + " " + nombre);
            }
            registrarAccion();
        } catch (AccionInvalidaException ex) {
            log("Error: " + ex.getMessage());
            updateUI();
        }
    }

    private void handleUsar() {
        Inventario inventario = ctx.jugador.getInventario();
        if (inventario == null || inventario.getTamaño() == 0) {
            log("El inventario está vacío.");
            return;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>();
        dialog.setTitle("Inventario");
        dialog.setHeaderText("Selecciona un objeto");

        for (int i = 0; i < inventario.getTamaño(); i++) {
            dialog.getItems().add(inventario.obtenerObjeto(i).getNombre());
        }

        dialog.showAndWait().ifPresent(nombre -> {
            for (int i = 0; i < inventario.getTamaño(); i++) {
                Objeto obj = inventario.obtenerObjeto(i);
                if (obj.getNombre().equals(nombre)) {
                    try {
                        if (obj instanceof Equipable) {
                            Equipable old = ctx.jugador.equipar((Equipable) obj);
                            log("🗡 Has equipado: " + obj.getNombre());
                            if (old != null) {
                                Celda celdaActual = ctx.currentHabitacion.getCelda(ctx.jugador.getFila(), ctx.jugador.getColumna());
                                celdaActual.setObjeto(old);
                                log("🗡 Dejaste caer: " + old.getNombre());
                            }
                        } else if (obj instanceof Llave) {
                            Celda currentCell = ctx.currentHabitacion.getCelda(ctx.jugador.getFila(), ctx.jugador.getColumna());
                            if (currentCell.hasObjeto() && currentCell.getObjeto() instanceof Puerta) {
                                Puerta puerta = (Puerta) currentCell.getObjeto();
                                if (puerta.getRequiredKeyId() == null) {
                                    log("🗝️ Esta puerta no necesita llave, solo ábrela con 'Abrir Puerta'.");
                                } else if (((Llave) obj).getPuertaId().equals(puerta.getRequiredKeyId())) {
                                    puerta.unlock();
                                    inventario.eliminarObjeto(obj);
                                    log("🗝️ Usaste " + obj.getNombre() + " en la puerta. ¡El candado se rompió!");
                                } else {
                                    log("❌ Esta llave no es para esta puerta.");
                                }
                            } else {
                                log("❌ Debes estar en la casilla de una puerta 🔒 para usar la llave.");
                            }
                            updateUI();
                            return;  // no consume turno
                        } else if (obj instanceof Consumible) {
                            ((Consumible) obj).usar(ctx.jugador);
                            inventario.eliminarObjeto(obj);
                            log("🫙 Has usado: " + obj.getNombre()
                                    + " (+ " + ((Consumible) obj).getRecuperacionVida() + " ❤ vida)");
                        }
                        registrarAccion();
                    } catch (Exception ex) {
                        log("Error: " + ex.getMessage());
                        updateUI();
                    }
                    break;
                }
            }
        });
    }

    private void handleAbrirPuerta() {
        try {
            Celda current = ctx.currentHabitacion.getCelda(ctx.jugador.getFila(), ctx.jugador.getColumna());
            Objeto obj = current.getObjeto();
            if (obj instanceof Puerta) {
                Puerta puerta = (Puerta) obj;
                int destId = puerta.getHabitacionDestinoId();
                if (destId == ctx.exitHabitacionId) {
                    juegoTerminado = true;
                    mostrarAlerta("🎉 VICTORIA", "¡Has llegado a la salida! Has escapado de la mazmorra.");
                    mostrarLogFinal();
                    log("🎉 ¡HAS LLEGADO A LA SALIDA! ¡VICTORIA! 🎉");
                    deshabilitarBotones();
                    updateUI();
                    return;
                }
                Habitacion nuevaHabitacion = ctx.habitaciones[destId];
                ctx.interaccionService.abrirPuerta(ctx.jugador, puerta, nuevaHabitacion, current);
                ctx.currentHabitacion = nuevaHabitacion;
                    log("🚪 Puerta abierta. Has entrado a la 🏠 " + destId + ".");
                haMovido = true;
                haActuado = true;
                finalizarTurnoJugador();
            } else {
                log("❌ No hay ninguna 🚪 aquí.");
                updateUI();
            }
        } catch (AccionInvalidaException ex) {
            log("Error: " + ex.getMessage());
            updateUI();
        }
    }

    private void deshabilitarBotones() {
        btnAtacar.setDisable(true);
        btnRecoger.setDisable(true);
        btnUsar.setDisable(true);
        btnAbrirPuerta.setDisable(true);
        btnRendirse.setDisable(true);
        btnGuardar.setDisable(true);
        btnEsperar.setDisable(true);
        btnVolverMenu.setVisible(true);
    }

    private void handleRendirse() {
        juegoTerminado = true;
        mostrarAlerta("🏳 RENDICIÓN", "Te has rendido. Vuelve al menú para intentarlo de nuevo.");
        mostrarLogFinal();
        log("🏳 Te has rendido.");
        deshabilitarBotones();
        updateUI();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarLogFinal() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("📜 Historial completo");
        alert.setHeaderText("Registro de toda la partida:");

        StringBuilder sb = new StringBuilder();
        ListaEnlazada<String> logCompleto = SistemaLog.getInstance().obtenerLogCompleto();
        for (int i = 0; i < logCompleto.size(); i++) {
            sb.append(logCompleto.get(i)).append("\n");
        }
        if (sb.length() == 0) sb.append("(sin eventos)");

        TextArea textArea = new TextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setPrefSize(500, 400);
        textArea.setStyle("-fx-control-inner-background: #1a1a2e; -fx-text-fill: #00ff00; -fx-font-family: Monospace;");

        alert.getDialogPane().setContent(textArea);
        alert.setWidth(550);
        alert.setHeight(500);
        alert.showAndWait();
    }

    private void iniciarTurno() {
        haMovido = false;
        haActuado = false;
        mostrarCaminoOptimo = false;
        actualizarEstadoTurno();
    }

    private void actualizarEstadoTurno() {
        if (this.juegoTerminado) return;

        lblEstadoMovimiento.setText(haMovido ? "👟 Movimiento: ✅" : "👟 Movimiento: ⬜");
        lblEstadoAccion.setText(haActuado ? "⚔ Acción: ✅" : "⚔ Acción: ⬜");

        btnAtacar.setDisable(haActuado);
        btnRecoger.setDisable(haActuado);
        btnUsar.setDisable(haActuado);
        btnAbrirPuerta.setDisable(haActuado);
        btnEsperar.setDisable(false);
        btnCaminoOptimo.setDisable(false);
        btnGuardar.setDisable(false);
        btnRendirse.setDisable(false);
    }

    private void registrarMovimiento() {
        haMovido = true;
        actualizarEstadoTurno();
        if (haActuado) {
            finalizarTurnoJugador();
        } else {
            updateUI();
        }
    }

    private String mostrarArbolItems() {
        if (ctx == null || ctx.arbolItems == null || ctx.arbolItems.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        Arbol.Nodo<String> root = ctx.arbolItems.getRoot();
        for (int i = 0; i < root.getChildren().size(); i++) {
            Arbol.Nodo<String> child = root.getChildren().get(i);
            sb.append(child.getData());
            int subCount = child.getChildren().size();
            if (subCount > 0) {
                sb.append("(");
                for (int j = 0; j < subCount; j++) {
                    sb.append(child.getChildren().get(j).getData());
                    if (j < subCount - 1) sb.append(",");
                }
                sb.append(")");
            }
            sb.append(" ");
        }
        return sb.toString();
    }

    private void registrarAccion() {
        haActuado = true;
        actualizarEstadoTurno();
        if (haMovido) {
            finalizarTurnoJugador();
        } else {
            updateUI();
        }
    }

    private void finalizarTurnoJugador() {
        mostrarCaminoOptimo = false;
        ctx.gestorTurnos.finalizarTurno();
        ListaEnlazada<String> msgs = ctx.gestorTurnos.procesarEnemigos(ctx.currentHabitacion);
        for (int i = 0; i < msgs.size(); i++) {
            log(msgs.get(i));
        }
        if (ctx.jugador.getVida() <= 0) {
            juegoTerminado = true;
            mostrarAlerta("💀 DERROTA", "¡Has muerto! Vuelve al menú para intentarlo de nuevo.");
            mostrarLogFinal();
            log("💀 ¡Has muerto! GAME OVER 💀");
            deshabilitarBotones();
        } else if (ctx.gestorTurnos.isDerrotaPorTurnos()) {
            juegoTerminado = true;
            mostrarAlerta("⏰ DERROTA", "¡Te has quedado sin turnos! Vuelve al menú para intentarlo de nuevo.");
            mostrarLogFinal();
            log("⏰ ¡Te has quedado sin turnos! GAME OVER ⏰");
            deshabilitarBotones();
        }
        iniciarTurno();
        updateUI();
    }

    private void handleTeclado(KeyEvent e) {
        if (ctx == null || juegoTerminado || ctx.jugador.getVida() <= 0 || ctx.gestorTurnos.isDerrotaPorTurnos())
            return;

        int f = ctx.jugador.getFila();
        int c = ctx.jugador.getColumna();
        int nf = f, nc = c;

        switch (e.getCode()) {
            case W -> nf--;
            case A -> nc--;
            case S -> nf++;
            case D -> nc++;
            case ENTER -> { btnEsperar.fire(); return; }
            default -> { return; }
        }

        if (!haMovido && nf >= 0 && nf < ctx.currentHabitacion.getFilas()
            && nc >= 0 && nc < ctx.currentHabitacion.getColumnas()) {
            try {
                MovimientoService.moverJugador(ctx.jugador, ctx.currentHabitacion, new Posicion(nf, nc));
                log("🙍‍♂️ Te has movido a (" + nf + ", " + nc + ")");
                registrarMovimiento();
            } catch (MovimientoInvalidoException ignored) {
            }
        }
    }

    private void handleGuardar() {
        try {
            PersistenciaService.guardarPartida("partida.json",
                ctx.jugador, ctx.gestorTurnos, ctx.habitaciones, ctx.grafo,
                ctx.exitHabitacionId, ctx.dificultad.name());
            log("💾 Partida guardada en partida.json");
        } catch (IOException e) {
            log("❌ Error al guardar: " + e.getMessage());
        }
    }

    private void handleCargarPartida() {
        try {
            PersistenciaService.DatosPartida datos = PersistenciaService.cargarPartida("partida.json");
            Dificultad dif = Dificultad.valueOf(datos.dificultad);
            ctx = new GameContext(dif);
            ctx.exitHabitacionId = datos.exitHabitacionId;
            ctx.jugador = datos.jugador;
            ctx.habitaciones = datos.habitaciones;
            ctx.currentHabitacion = ctx.habitaciones[ctx.jugador.getHabitacionId()];
            ctx.jugador.setHabitacion(ctx.currentHabitacion);
            ctx.grafo = datos.grafo;
            ctx.gestorTurnos = new GestorTurnos(datos.turnosRestantes, ctx.jugador);
            crearPantallaJuego();
            log("📂 Partida cargada correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("❌ Error", "No se pudo cargar la partida: " + e.getMessage());
        }
    }

    private void volverAlMenu() {
        mostrarPantallaInicio();
    }

    private void handleCaminoOptimo() {
        ListaEnlazada<Integer> path = BFS.findShortestPath(ctx.grafo, ctx.jugador.getHabitacionId(), ctx.exitHabitacionId);
        if (path == null || path.size() < 2) {
            if (ctx.jugador.getHabitacionId() == ctx.exitHabitacionId) {
                log("📍 ¡Ya estás en la habitación de salida!");
            } else {
                log("No se encontró camino al objetivo.");
            }
            return;
        }

        int distance = path.size() - 1;
        log("📍 Camino óptimo: " + distance + " habitaciones restantes.");

        int nextRoom = path.get(1);
        Puerta targetDoor = null;
        int doorR = -1, doorC = -1;
        Habitacion h = ctx.currentHabitacion;

        for (int r = 0; r < h.getFilas(); r++) {
            for (int c = 0; c < h.getColumnas(); c++) {
                Celda celda = h.getCelda(r, c);
                if (celda.hasObjeto() && celda.getObjeto() instanceof Puerta) {
                    Puerta p = (Puerta) celda.getObjeto();
                    if (p.getHabitacionDestinoId() == nextRoom) {
                        targetDoor = p;
                        doorR = r;
                        doorC = c;
                        break;
                    }
                }
            }
            if (targetDoor != null) break;
        }

        if (targetDoor == null) {
            log("No hay puerta visible hacia la siguiente habitación en esta sala.");
            return;
        }

        int[][] grid = new int[h.getFilas()][h.getColumnas()];
        for (int r = 0; r < h.getFilas(); r++) {
            for (int c = 0; c < h.getColumnas(); c++) {
                Celda celda = h.getCelda(r, c);
                if (celda.hasEntidad() && celda.getEntidad() != ctx.jugador) {
                    grid[r][c] = 1;
                } else {
                    grid[r][c] = 0;
                }
            }
        }
        grid[doorR][doorC] = 0;

        ListaEnlazada<Integer> gridPath = BFS.findShortestPathGrid(
            grid,
            ctx.jugador.getFila(), ctx.jugador.getColumna(),
            doorR, doorC
        );

        if (gridPath == null) {
            log("No se encontró camino en el grid hacia la puerta.");
            return;
        }

        caminoOptimoPath = new ListaEnlazada<>();
        int colsGrid = h.getColumnas();
        for (int i = 0; i < gridPath.size(); i++) {
            int node = gridPath.get(i);
            int pr = node / colsGrid;
            int pc = node % colsGrid;
            caminoOptimoPath.add(new Posicion(pr, pc));
        }
        mostrarCaminoOptimo = true;

        log("📍 Se han resaltado " + (caminoOptimoPath.size() - 1) + " celdas en el camino hacia la puerta.");
        updateUI();
    }
}
