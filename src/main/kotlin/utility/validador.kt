package utility
import java.util.Scanner

fun iteradorNumerosValidosInteger(descripcionNumero: String): Int {
    //método usado para pedir numeros enteros válidos
    //recibe un string con el nombre del numero pedido
    val scanner = Scanner(System. `in`)
    var numeroValido: Int? = null
    while (numeroValido == null) {
        val entrada = scanner.nextLine()
        numeroValido = entrada.toIntOrNull()
        if (numeroValido == null) {
            println("$descripcionNumero debe ser un número!!: ")
        }
    }

    return numeroValido
}

fun iteradorNumerosValidosDouble(descripcionNumero: String): Double {
    //método usado para pedir numeros decimales válidos
    //recibe un string con el nombre del numero pedido
    val scanner = Scanner(System. `in`)
    var numeroValido: Double? = null
    while (numeroValido == null) {
        val entrada = scanner.nextLine()
        numeroValido = entrada.toDoubleOrNull()
        if (numeroValido == null) {
            println("$descripcionNumero debe ser un número!!: ")
        }
    }
    return numeroValido
}

fun iteradorNumerosValidosPositivos(descripcionNumero: String, num: Int): Boolean {

    if (num <= 0) {
        println("$descripcionNumero tiene que ser positivo")
        return false
    }
    else {
        return true
    }
}