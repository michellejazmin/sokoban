# Sokoban

Trabajo Integrador — Proceso de Desarrollo de Software  
**Grupo:** Los JavaFantásticos

Implementación del clásico juego de lógica Sokoban en Java con Swing.

## Requisitos

- Java 24 o superior
- Apache Maven 3.9+
- IntelliJ IDEA (recomendado)

## Cómo ejecutar

### Desde IntelliJ IDEA

1. Abrir el proyecto: `File → Open` y seleccionar la carpeta raíz.
2. Esperar a que Maven importe las dependencias (JDK 24 configurado).
3. Ejecutar `src/main/java/org/javafantasticos/sokoban/Main.java` con `Run`.

### Desde terminal

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="org.javafantasticos.sokoban.Main"
```

## Controles

| Tecla     | Acción                         |
|-----------|--------------------------------|
| ↑ / W     | Mover arriba                   |
| ↓ / S     | Mover abajo                    |
| ← / A     | Mover izquierda                |
| → / D     | Mover derecha                  |
| Ctrl+Z    | Deshacer últimos 5 movimientos |

**Botones en pantalla:** Deshacer, Reiniciar nivel, Volver al menú.

## Funcionalidades

- Tres tipos de caja: normal, frágil (con resistencia), llave (abre rejas)
- Terreno resbaladizo (aceite): cajas y jugador se deslizan
- Rejas que se abren/cierran según el estado de los cerrojos
- Ítems de piso: moneda (+puntaje), bomba (game over), undo extra
- Deshacer hasta 3 usos (retrocede 5 movimientos cada uno)
- Repetición automática de la partida (replay)
- Efectos de sonido para cada acción
- HUD con movimientos, empujes, nivel actual y usos de undo restantes

## Estructura del proyecto

```
src/main/java/org/javafantasticos/sokoban/
├── Main.java                              — Punto de entrada
│
├── controller/                            — Lógica de control y orquestación
│   ├── GameController.java                — Controlador principal (singleton)
│   ├── GameFlowController.java            — Flujo de inicio, juego y fin de partida
│   ├── GestorNiveles.java                 — Carga y gestión de niveles (singleton)
│   ├── GestorDePartida.java               — Puntaje, undo (Caretaker) y grabación
│   ├── GestorSonido.java                  — Reproducción de efectos de sonido
│   ├── InputController.java               — Gestión de entrada del usuario
│   ├── MovimientoTeclado.java             — Estrategia de teclado (flechas / WASD)
│   ├── Caretaker.java                     — Historial de undo (Memento)
│   ├── Grabacion.java                     — Grabación completa para replay
│   ├── ReproductorPartida.java            — Reproduce la partida grabada
│   └── TxtLevelsExtractor.java            — Lector de niveles desde archivo .txt
│
├── model/                                 — Dominio y reglas del juego
│   ├── ElementoBase.java                  — Clase base abstracta de todo elemento
│   ├── ElementoFactory.java               — Fábrica de elementos del tablero
│   ├── Tablero.java                       — Estado del nivel (grillas, cajas, jugador)
│   ├── TableroFactory.java                — Fábrica que construye el Tablero
│   ├── TableroMemento.java                — Instantánea del estado (Memento)
│   ├── MotorFisico.java                   — Algoritmo de movimiento físico
│   ├── ReglasDelJuego.java                — Reglas de victoria y apertura de rejas
│   │
│   ├── cajas/
│   │   ├── Caja.java                      — Clase base abstracta de caja
│   │   ├── CajaNormal.java                — Caja estándar
│   │   ├── CajaFragil.java                — Caja con resistencia limitada
│   │   └── CajaLlave.java                 — Caja que abre rejas en cerrojos
│   │
│   ├── items/
│   │   ├── ItemPiso.java                  — Clase base abstracta (Template Method)
│   │   ├── ItemMoneda.java                — Otorga bonus de puntaje
│   │   ├── Bomba.java                     — Termina la partida
│   │   └── ItemUndoExtra.java             — Concede un uso extra de undo
│   │
│   ├── muros/
│   │   ├── Pared.java                     — Pared fija
│   │   ├── Reja.java                      — Reja que cambia de estado (Context)
│   │   ├── RejaCerrada.java               — Estado: bloquea el paso
│   │   └── RejaAbierta.java               — Estado: se comporta como suelo
│   │
│   ├── suelo/
│   │   ├── Suelo.java                     — Suelo normal
│   │   ├── Destino.java                   — Casilla de destino para cajas
│   │   ├── Cerrojo.java                   — Casilla que activa rejas
│   │   └── Aceite.java                    — Terreno resbaladizo
│   │
│   ├── player/
│   │   ├── Jugador.java                   — El sokoban
│   │   └── Orientacion.java               — Enum de direcciones (sprites)
│   │
│   └── dto/
│       └── Coordenada.java                — Par (x, y) de posición
│
├── view/                                  — Interfaz gráfica (Swing)
│   ├── Ventana.java                       — Ventana principal (JFrame, singleton)
│   ├── VistaJuego.java                    — Panel contenedor (TableroPanel + HUDPanel)
│   ├── TableroPanel.java                  — Dibuja las grillas del tablero
│   ├── HUDPanel.java                      — HUD con movimientos, empujes, nivel
│   ├── Menu.java                          — Pantalla de menú principal
│   ├── GameOverPanel.java                 — Pantalla de fin de partida
│   ├── VictoriaPanel.java                 — Pantalla de victoria
│   ├── PasoNivelPanel.java                — Pantalla de resumen entre niveles
│   ├── ReplayPanel.java                   — Pantalla de reproducción (replay)
│   ├── BaseOverlayPanel.java              — Clase base para pantallas modales
│   └── UIResources.java                   — Carga de imágenes, fuentes y recursos
│
└── interfaces/                            — Abstracciones para desacoplar capas
    ├── ISuscriptor.java                   — Observer del modelo hacia la vista
    ├── ITableroFisico.java                — Acceso del motor físico al tablero
    ├── IContextoItem.java                 — Contexto limitado para ítems
    ├── IMovimientos.java                  — Estrategia de entrada (Strategy)
    ├── IMoveCallback.java                 — Callback de movimiento
    ├── ILectorNiveles.java                — Abstracción para cargar niveles
    ├── IReproductorSonido.java            — Abstracción del reproductor de audio
    ├── IReproductorVista.java             — Abstracción de la vista de replay
    ├── IHUDDataSource.java                — Fuente de datos para el HUD
    ├── IEstadoReja.java                   — Estado de la reja (State)
    ├── INavegadorPantallas.java           — Navegación entre pantallas
    ├── IVistaDeJuego.java                 — Contrato de la vista de juego
    ├── IVistaHUD.java                     — Contrato del HUD
    ├── IVistaMenu.java                    — Contrato del menú
    ├── IVistaReplay.java                  — Contrato del panel de replay
    ├── IPantallaGameOver.java             — Contrato de game over
    ├── IPantallaVictoria.java             — Contrato de victoria
    └── IPantallaPasoNivel.java            — Contrato de paso de nivel
```

## Informe técnico

Las decisiones de diseño, patrones aplicados (GoF), principios SOLID/GRASP y la declaración de uso de IA se encuentran en:

➡️ [`Documentación Técnica.pdf`](DocumentaciónTécnica.pdf)

## Diagrama UML

Editado en PlantUML y en formato PNG para mayor comodidad:

➡️ [`DiagramaDeClases.png`](DiagramaDeClases.png)
➡️ [`sokoban.plantuml`](sokoban.plantuml)

## Video de demostración (Gameplay)

Video que demuestra las características del juego:

➡️ [`JavaFantásticos – Gameplay Sokoban - YouTube`](https://www.youtube.com/watch?v=pltDtLvrL6M)

## Assets

- **Sonidos:** "Level up, power up, Coin get (13 Sounds)" por wobbleboxx (CC0) — OpenGameArt.org
- **Gráficos:** Sprites e imágenes de dominio público en `src/main/resources/`, obtenidos de la web Kenney.nl

## Créditos

Desarrollado con apoyo de Claude y OpenCode como herramientas de asistencia.
