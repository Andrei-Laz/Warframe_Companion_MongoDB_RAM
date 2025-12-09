import java.util.Scanner

fun main() {
    conectarBD()
    importarBD("src/main/resources/warframes.json", coleccionWarframes)

    mainMenu()

    exportarBD(coleccionWarframes,"src/main/resources/warframes.json")
    desconectarBD()
}

fun mainMenu() {
    val scanner = Scanner(System.`in`)
    var option: Int

    do {
        println(
            """
            ==== WARFRAME COMPANION MENU ====
            1. Warframe database menu
            2. Weapon database menu
            3. Mods database menu
            0. Salir
            =================================
            Elige una opción:
            """.trimIndent()
        )

        print("> ")
        option = scanner.nextLine().toIntOrNull() ?: -1

        when (option) {
            1 -> {
                warframeMenu()
            }

            2 -> {
                weaponMenu()
            }

            3 -> {
                modMenu()
            }

            0 -> println("Saliendo del menú...")

            else -> println("Opción inválida, intenta de nuevo.")
        }

    } while (option != 0)
}

fun warframeMenu() {
    val scanner = Scanner(System.`in`)
    var option: Int

    do {
        println(
            """

            ==== WARFRAME DATABASE MENU ====
            1. Listar Warframes
            2. Consultar Warframes con filtro
            3. Consultar Warframes con filtro (2)
            4. Consultar promedios/máximos warframes
            5. Insertar nuevo Warframe
            6. Actualizar Warframe
            7. Eliminar Warframe
            0. Salir
            =================================
            Elige una opción:
            """.trimIndent()
        )

        print("> ")
        option = scanner.nextLine().toIntOrNull() ?: -1

        when (option) {
            1 -> WarframesDAO.mostrarWarframes()
            2 -> WarframesDAO.consultarWarframesConFiltros()
            3 -> WarframesDAO.proyeccionesWarframes()
            4 -> WarframesDAO.agregacionesWarframes()
            5 -> WarframesDAO.insertarWarframe()
            6 -> WarframesDAO.actualizarVidaWarframe()
            7 -> WarframesDAO.eliminarWarframe()

            0 -> println("Saliendo del menú...")

            else -> println("Opción inválida, intenta de nuevo.")
        }
    } while (option != 0)
}

fun modMenu() {

}

fun weaponMenu() {

}