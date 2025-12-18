import java.util.Scanner

fun main() {
    conectarBD()
    importarBD("src/main/resources/warframes.json", coleccionWarframes)
    importarBD("src/main/resources/mods.json", coleccionMods)
    importarBD("src/main/resources/squads.json", coleccionSquads)

    mainMenu()

    exportarBD(coleccionWarframes,"src/main/resources/warframes.json")
    exportarBD(coleccionMods,"src/main/resources/mods.json")
    exportarBD(coleccionSquads,"src/main/resources/squads.json")
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
            2. Weapon database menu (in progress)
            3. Mods database menu
            4. Squads database Menu
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

            4 -> {
                squadMenu()
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
    val scanner = Scanner(System. `in`)
    var option: Int

    do {
        println(
            """

            ==== WARFRAME DATABASE MENU ====
            1. Listar Mods
            2. Insertar nueva Mod
            3. Actualizar Mod
            4. Eliminar Mod
            0. Salir
            =================================
            Elige una opción:
            """.trimIndent()
        )

        print("> ")
        option = scanner.nextLine().toIntOrNull() ?: -1

        when (option) {
            1 -> ModsDAO.mostrarMods()
            2 -> ModsDAO.insertarMod()
            3 -> ModsDAO.actualizarCosteCapacidadMod()
            4 -> ModsDAO.eliminarMod()

            0 -> println("Saliendo del menú...")

            else -> println("Opción inválida, intenta de nuevo.")
        }
    } while (option != 0)
}

fun weaponMenu() {

}

fun squadMenu() {
    val scanner = Scanner(System.`in`)
    var option: Int

    do {
        println(
            """

            ==== SQUADS DATABASE MENU ====
            1. Listar squads
            2. Insertar warframe en squad (crea el squad si no existe)
            3. Actualizar squad
            4. Eliminar squad
            5. Consultar squad por ID
            
            2. Consultar Warframes con filtro
            3. Consultar Warframes con filtro (2)
            0. Salir
            =================================
            Elige una opción:
            """.trimIndent()
        )

        print("> ")
        option = scanner.nextLine().toIntOrNull() ?: -1

        when (option) {
            1 -> SquadDAO.mostrarSquads()
            2 -> SquadDAO.insertarSquad()
            3 -> SquadDAO.actualizarSquad()
            4 -> SquadDAO.eliminarSquad()
            5 -> SquadDAO.consultaSquadsConJoin()
            0 -> println("Saliendo del menú...")

            else -> println("Opción inválida, intenta de nuevo.")
        }
    } while (option != 0)
}