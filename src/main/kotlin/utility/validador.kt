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

fun iteradorNumerosValidosIntegerPositivos(descripcionNumero: String): Int {
    val scanner = Scanner(System. `in`)
    var numValido: Int? = null
    while (numValido == null) {
        val entrada = scanner.nextLine()
        numValido = entrada.toIntOrNull()
        if (numValido == null) {
            println("$descripcionNumero debe ser un número!!: ")
        }
        var numValidoPositivo = numValido
        while (numValidoPositivo != null) {
            if (numValidoPositivo <= 0) {
                println("$descripcionNumero debe ser un número positivo!!: ")
                numValidoPositivo = null
                numValido = null
            }
            else {
                numValidoPositivo = null
            }
        }
    }

    return numValido
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

fun iteradorNumerosValidosDoublePositivos(descripcionNumero: String): Double {
    val scanner = Scanner(System. `in`)
    var numValido: Double? = null
    while (numValido == null) {
        val entrada = scanner.nextLine()
        numValido = entrada.toDoubleOrNull()
        if (numValido == null) {
            println("$descripcionNumero debe ser un número!!: ")
        }
        var numValidoPositivo = numValido
        while (numValidoPositivo != null) {
            if (numValidoPositivo <= 0) {
                println("$descripcionNumero debe ser un número positivo!!: ")
                numValidoPositivo = null
                numValido = null
            }
            else {
                numValidoPositivo = null
            }
        }
    }

    return numValido
}