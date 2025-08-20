import com.google.gson.JsonSyntaxException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Principal {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static void main(String[] args) {
        try (Scanner lectura = new Scanner(System.in)) {
            
            ConsultaConversion consulta = new ConsultaConversion();
            Calculos calculos = new Calculos(consulta);
            GeneradorDeArchivos generador = new GeneradorDeArchivos();
            List<String> respuestas = new ArrayList<>();
            
            int opcionElegida = 0;

            while (opcionElegida != MenuOpciones.SALIR.getOpcion()) {
                mostrarMenu();
                try {
                    opcionElegida = Integer.parseInt(lectura.nextLine());
                    procesarOpcion(opcionElegida, calculos, respuestas);
                } catch (NumberFormatException | InputMismatchException e) {
                    System.out.println("⚠ Error: Ingrese un valor numérico válido.");
                } catch (JsonSyntaxException | NullPointerException e) {
                    System.out.println("⚠ Error: Ingrese solo códigos de moneda válidos.");
                }
            }

            generador.guardarJson(respuestas);
            System.out.println("Programa finalizado. Historial guardado.");
        }
    }

    private static void mostrarMenu() {
        String menu = """
                \n***************************************************
                *** Bienvenido al Conversor de Monedas ***
                
                1) Peso Argentino → Dólar Estadounidense
                2) Peso Argentino → Euro
                3) Peso Argentino → Libra Esterlina
                4) Dólar Estadounidense → Peso Argentino
                5) Euro → Peso Argentino
                6) Libra Esterlina → Peso Argentino
                7) Otra opción de conversión
                8) Salir
                ***************************************************
                """;
        System.out.println(menu);
    }

    private static void procesarOpcion(int opcion, Calculos calculos, List<String> respuestas) {
        String fecha = LocalDateTime.now().format(DATE_FORMATTER);

        switch (opcion) {
            case 1 -> registrarConversion(calculos, respuestas, "ARS", "USD", fecha);
            case 2 -> registrarConversion(calculos, respuestas, "ARS", "EUR", fecha);
            case 3 -> registrarConversion(calculos, respuestas, "ARS", "GBP", fecha);
            case 4 -> registrarConversion(calculos, respuestas, "USD", "ARS", fecha);
            case 5 -> registrarConversion(calculos, respuestas, "EUR", "ARS", fecha);
            case 6 -> registrarConversion(calculos, respuestas, "GBP", "ARS", fecha);
            case 7 -> {
                calculos.almacenarValoresPersonalizados();
                respuestas.add(fecha + " - " + calculos.obtenerMensajeRespuesta());
            }
            case 8 -> System.out.println("👋 Saliendo del programa...");
            default -> System.out.println("⚠ Ingrese una opción válida.");
        }
    }

    private static void registrarConversion(Calculos calculos, List<String> respuestas, String origen, String destino, String fecha) {
        calculos.almacenarValores(origen, destino);
        respuestas.add(fecha + " - " + calculos.obtenerMensajeRespuesta());
    }
}

// Enum para mejorar legibilidad del menú
enum MenuOpciones {
    ARS_USD(1), ARS_EUR(2), ARS_GBP(3),
    USD_ARS(4), EUR_ARS(5), GBP_ARS(6),
    PERSONALIZADO(7), SALIR(8);

    private final int opcion;
    MenuOpciones(int opcion) { this.opcion = opcion; }
    public int getOpcion() { return opcion; }
}
