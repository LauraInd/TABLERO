package com.example.tablero;

import java.util.Random;
import java.util.Scanner;

import com.diogonunes.jcolor.Attribute;
import static com.diogonunes.jcolor.Ansi.colorize;

public class JuegoDeTablero {

    private static final int TAMANIO = 6;
    private static final char LIBRE = 'L';
    private static final char SALIDA = 'S';
    private static final char JUGADOR1 = 'J';
    private static final char JUGADOR2 = 'K';
    private static final char ENEMIGO = 'E';
    private static int vidasJugador1 = 3;
    private static int vidasJugador2 = 3;

    public static void main(String[] args) {
        char[][] tableroJugador1 = new char[TAMANIO][TAMANIO];
        char[][] tableroJugador2 = new char[TAMANIO][TAMANIO];
        inicializarTablero(tableroJugador1);
        inicializarTablero(tableroJugador2);
        colocarJugadorYSalida(tableroJugador1, JUGADOR1);
        colocarJugadorYSalida(tableroJugador2, JUGADOR2);
        colocarEnemigos(tableroJugador1);
        colocarEnemigos(tableroJugador2);

        Scanner scanner = new Scanner(System.in);
        boolean juegoTerminado = false;
        boolean turnoJugador1 = true;

        while (!juegoTerminado) {
            if (turnoJugador1) {
                System.out.println(colorize("Turno del Jugador 1 (Vidas restantes: " + vidasJugador1 + ")", Attribute.RED_TEXT()));
                imprimirTableroSinEnemigos(tableroJugador1, JUGADOR1);
                System.out.print(colorize("Ingresa el movimiento (ej. 2A): ", Attribute.YELLOW_TEXT()));
                String movimiento = scanner.next();
                int casillas = Character.getNumericValue(movimiento.charAt(0));
                char direccion = movimiento.charAt(1);
                moverJugador(tableroJugador1, JUGADOR1, casillas, direccion);
                juegoTerminado = comprobarEstado(tableroJugador1, JUGADOR1);
                turnoJugador1 = false;
            } else {
                System.out.println(colorize("Turno del Jugador 2 (Vidas restantes: " + vidasJugador2 + ")", Attribute.BLUE_TEXT()));
                imprimirTableroSinEnemigos(tableroJugador2, JUGADOR2);
                System.out.print(colorize("Ingresa el movimiento (ej. 2A): ", Attribute.YELLOW_TEXT()));
                String movimiento = scanner.next();
                int casillas = Character.getNumericValue(movimiento.charAt(0));
                char direccion = movimiento.charAt(1);
                moverJugador(tableroJugador2, JUGADOR2, casillas, direccion);
                juegoTerminado = comprobarEstado(tableroJugador2, JUGADOR2);
                turnoJugador1 = true;
            }

            if (vidasJugador1 == 0 || vidasJugador2 == 0) {
                juegoTerminado = true;
                System.out.println(colorize("¡El juego ha terminado porque un jugador ha perdido todas sus vidas!", Attribute.BRIGHT_RED_TEXT()));
            }
        }

        scanner.close();
    }

    private static void inicializarTablero(char[][] tablero) {
        for (int i = 0; i < TAMANIO; i++) {
            for (int j = 0; j < TAMANIO; j++) {
                tablero[i][j] = LIBRE;
            }
        }
    }

    private static void colocarJugadorYSalida(char[][] tablero, char jugador) {
        Random random = new Random();
        int filaJugador = random.nextInt(TAMANIO);
        int columnaJugador = random.nextInt(TAMANIO);
        int filaSalida = random.nextInt(TAMANIO);
        int columnaSalida = random.nextInt(TAMANIO);

        while (filaJugador == filaSalida && columnaJugador == columnaSalida) {
            filaSalida = random.nextInt(TAMANIO);
            columnaSalida = random.nextInt(TAMANIO);
        }

        tablero[filaJugador][columnaJugador] = jugador;
        tablero[filaSalida][columnaSalida] = SALIDA;
    }

    private static void colocarEnemigos(char[][] tablero) {
        Random random = new Random();
        int enemigosColocados = 0;

        while (enemigosColocados < 8) {
            int fila = random.nextInt(TAMANIO);
            int columna = random.nextInt(TAMANIO);

            if (tablero[fila][columna] == LIBRE) {
                tablero[fila][columna] = ENEMIGO;
                enemigosColocados++;
            }
        }
    }

    private static void imprimirTableroSinEnemigos(char[][] tablero, char jugador) {
        for (int i = 0; i < TAMANIO; i++) {
            for (int j = 0; j < TAMANIO; j++) {
                char celda = tablero[i][j];
                switch (celda) {
                    case LIBRE:
                        System.out.print(colorize("L ", Attribute.BRIGHT_WHITE_TEXT()));
                        break;
                    case SALIDA:
                        System.out.print(colorize("S ", Attribute.GREEN_TEXT()));
                        break;
                    case JUGADOR1:
                        System.out.print(colorize("J ", Attribute.RED_TEXT()));
                        break;
                    case JUGADOR2:
                        System.out.print(colorize("K ", Attribute.BLUE_TEXT()));
                        break;
                    default:
                        System.out.print(colorize("L ", Attribute.BRIGHT_WHITE_TEXT()));
                        break;
                }
            }
            System.out.println();
        }
    }

    private static void moverJugador(char[][] tablero, char jugador, int casillas, char direccion) {
        int filaActual = -1;
        int columnaActual = -1;

        // Encontrar la posición actual del jugador
        for (int i = 0; i < TAMANIO; i++) {
            for (int j = 0; j < TAMANIO; j++) {
                if (tablero[i][j] == jugador) {
                    filaActual = i;
                    columnaActual = j;
                    break;
                }
            }
        }

        int nuevaFila = filaActual;
        int nuevaColumna = columnaActual;

        switch (direccion) {
            case 'W':
                nuevaFila = (filaActual - casillas + TAMANIO) % TAMANIO;
                break;
            case 'S':
                nuevaFila = (filaActual + casillas) % TAMANIO;
                break;
            case 'A':
                nuevaColumna = (columnaActual - casillas + TAMANIO) % TAMANIO;
                break;
            case 'D':
                nuevaColumna = (columnaActual + casillas) % TAMANIO;
                break;
        }

        // Manejar colisión con enemigos y actualizar la posición del jugador
        if (tablero[nuevaFila][nuevaColumna] == ENEMIGO) {
            System.out.println(colorize("¡Te encontraste con un enemigo! Pierdes una vida.", Attribute.BRIGHT_RED_TEXT()));
            if (jugador == JUGADOR1) {
                vidasJugador1--;
            } else {
                vidasJugador2--;
            }
        }

        tablero[filaActual][columnaActual] = LIBRE;
        tablero[nuevaFila][nuevaColumna] = jugador;
    }

    private static boolean comprobarEstado(char[][] tablero, char jugador) {
        for (int i = 0; i < TAMANIO; i++) {
            for (int j = 0; j < TAMANIO; j++) {
                if (tablero[i][j] == SALIDA && tablero[i][j] == jugador) {
                    System.out.println(colorize("¡Jugador " + jugador + " ha ganado!", Attribute.BRIGHT_GREEN_TEXT()));
                    return true;
                }
            }
        }
        return false;
    }
}


