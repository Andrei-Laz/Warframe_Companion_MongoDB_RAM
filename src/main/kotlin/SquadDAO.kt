import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections
import org.bson.Document
import utility.iteradorNumerosValidosInteger
import utility.iteradorNumerosValidosIntegerPositivos
import java.util.Scanner

data class Squad(
    val squadID: Int? = null,
    val nombre: String,
    val warframeID: Int,
    val cantidad: Int
)

object SquadDAO {

    fun mostrarSquads() {
        val cursor = coleccionSquads.find().iterator()
        cursor.use {
            while (it.hasNext()) {
                val doc = it.next()

                val squadId = doc.getInteger("squad_id")
                val squadNombre = doc.getString("nombre")
                val warframeId = doc.getInteger("warframe_id")
                val squadCantidad = doc.getInteger("cantidad")

                println("Squad [${squadId}]" +
                        "\n\tNombre: $squadNombre" +
                        "\n\tWarframe ID: $warframeId" +
                        "\n\tCantidad: $squadCantidad")
            }
        }
    }

    fun insertarSquad() {
        val scanner = Scanner(System.`in`)

        println("ID del Squad: ")
        val id_squad = iteradorNumerosValidosIntegerPositivos("El ID del squad")
        println("Nombre del Squad: ")
        val nombre = scanner.nextLine()
        println("ID del Warframe: ")
        val warframe_id = iteradorNumerosValidosIntegerPositivos("El ID del warframe")
        println("Cantidad: ")
        val cantidad = iteradorNumerosValidosIntegerPositivos("La cantidad")

        // Verificar si existe el warframe
        val warframeExistente = coleccionWarframes.find(Filters.eq("warframe_id", warframe_id)).firstOrNull()
        if (warframeExistente == null) {
            println("Error: No existe un warframe con ID = $warframe_id")
            return
        }

        val doc = Document("squad_id", id_squad)
            .append("nombre", nombre)
            .append("warframe_id", warframe_id)
            .append("cantidad", cantidad)

        coleccionSquads.insertOne(doc)
        println("Squad insertado con ID: ${doc.getInteger("squad_id")}")
    }

    fun actualizarSquad() {
        val id_squad = iteradorNumerosValidosInteger("El ID del squad")
        val squad = coleccionSquads.find(Filters.eq("squad_id", id_squad)).firstOrNull()

        if (squad == null) {
            println("No se encontró ningún squad con ID = \"$id_squad\".")
            return
        }

        println("Squad encontrado: ${squad.getString("nombre")} (Warframe ID: ${squad.getInteger("warframe_id")}, Cantidad: ${squad.getInteger("cantidad")})")
        println("\n¿Qué deseas actualizar?")
        println("1. Nombre")
        println("2. Warframe ID")
        println("3. Cantidad")
        println("4. Todo")
        print("Selecciona una opción: ")

        val scanner = Scanner(System.`in`)
        val opcion = iteradorNumerosValidosInteger("La opción")

        val updateDoc = Document()

        when (opcion) {
            1 -> {
                print("Nuevo nombre: ")
                val nuevoNombre = scanner.nextLine()
                updateDoc.put("nombre", nuevoNombre)
            }
            2 -> {
                print("Nuevo Warframe ID: ")
                val nuevoWarframeId = iteradorNumerosValidosIntegerPositivos("El ID del warframe")
                // Verificar si existe el warframe
                val warframeExistente = coleccionWarframes.find(Filters.eq("warframe_id", nuevoWarframeId)).firstOrNull()
                if (warframeExistente == null) {
                    println("Error: No existe un warframe con ID = $nuevoWarframeId")
                    return
                }
                updateDoc.put("warframe_id", nuevoWarframeId)
            }
            3 -> {
                print("Nueva cantidad: ")
                val nuevaCantidad = iteradorNumerosValidosIntegerPositivos("La cantidad")
                updateDoc.put("cantidad", nuevaCantidad)
            }
            4 -> {
                print("Nuevo nombre: ")
                val nuevoNombre = scanner.nextLine()
                print("Nuevo Warframe ID: ")
                val nuevoWarframeId = iteradorNumerosValidosIntegerPositivos("El ID del warframe")
                print("Nueva cantidad: ")
                val nuevaCantidad = iteradorNumerosValidosIntegerPositivos("La cantidad")

                // Verificar si existe el warframe
                val warframeExistente = coleccionWarframes.find(Filters.eq("warframe_id", nuevoWarframeId)).firstOrNull()
                if (warframeExistente == null) {
                    println("Error: No existe un warframe con ID = $nuevoWarframeId")
                    return
                }

                updateDoc.put("nombre", nuevoNombre)
                updateDoc.put("warframe_id", nuevoWarframeId)
                updateDoc.put("cantidad", nuevaCantidad)
            }
        }

        val result = coleccionSquads.updateOne(
            Filters.eq("squad_id", id_squad),
            Document("\$set", updateDoc)
        )

        if (result.modifiedCount > 0)
            println("Squad actualizado correctamente (${result.modifiedCount} documento modificado).")
        else
            println("No se modificó ningún documento.")
    }

    fun eliminarSquad() {
        println("ID del squad a eliminar: ")
        val id_squad = iteradorNumerosValidosInteger("El ID")

        val result = coleccionSquads.deleteOne(Filters.eq("squad_id", id_squad))
        if (result.deletedCount > 0)
            println("Squad eliminado correctamente.")
        else
            println("No se encontró ningún squad con ese ID.")
    }

    fun consultaSquadsConJoin() {
        println("Introduce el ID del squad a consultar: ")
        val idSquad = iteradorNumerosValidosIntegerPositivos("El ID del squad")

        val lookupPipeline = listOf(
            Document("\$match", Document("squad_id", idSquad)),
            Document("\$lookup", Document()
                .append("from", "warframes")
                .append("localField", "warframe_id")
                .append("foreignField", "warframe_id")
                .append("as", "warframe_info")
            ),
            Document("\$unwind",  "\$warframe_info"),
            Document("\$project", Document()
                .append("squad_id", 1)
                .append("nombre", 1)
                .append("cantidad", 1)
                .append("warframe_name", "\$warframe_info.name")
                .append("warframe_health", "\$warframe_info.health")
                .append("warframe_armor", "\$warframe_info.armor")
                .append("warframe_energy", "\$warframe_info.energy")
                .append("warframe_sprint_speed", "\$warframe_info.sprint_speed")
                .append("warframe_passive", "\$warframe_info.passive")
            )
        )

        val resultados = coleccionSquads.aggregate(lookupPipeline).toList()

        if (resultados.isEmpty()) {
            println("╔════════════════════════════════════════════════════════╗")
            println("║               ⚠️  NO SE ENCONTRÓ EL SQUAD            ║")
            println("╚════════════════════════════════════════════════════════╝")
            println("No existe ningún squad con ID = $idSquad")
            println()
        } else {
            resultados.forEach{ resultado ->

            }
            val squadDoc = resultados.first()
            imprimirSquadDetalladoConWarframe(squadDoc)
        }
    }

    private fun imprimirSquadDetalladoConWarframe(doc: Document) {
        val squadId = doc.getInteger("squad_id")
        val squadNombre = doc.getString("nombre")
        val cantidad = doc.getInteger("cantidad")
        val warframeNombre = doc.getString("warframe_name")
        val warframeHealth = doc.getInteger("warframe_health")
        val warframeArmor = doc.getInteger("warframe_armor")
        val warframeEnergy = doc.getInteger("warframe_energy")
        val warframeSprintSpeed = doc.getDouble("warframe_sprint_speed")
        val warframePassive = doc.getString("warframe_passive")

        // Calcular estadísticas
        val saludTotal = cantidad * warframeHealth
        val armaduraTotal = cantidad * warframeArmor
        val energiaTotal = cantidad * warframeEnergy

        println("╔════════════════════════════════════════════════════════════════════════════════╗")
        println("║                            📋 INFORMACIÓN COMPLETA DEL SQUAD                   ║")
        println("╠════════════════════════════════════════════════════════════════════════════════╣")
        println("║                                                                                ║")
        println("║  🎮 SQUAD: ${squadNombre.padEnd(67)}║")
        println("║  🔢 ID: ${squadId.toString().padEnd(71)}║")
        println("║                                                                                ║")
        println("║  ══════════════════════ 📊 COMPOSICIÓN ═════════════════════════              ║")
        println("║                                                                                ║")
        println("║  👥 Cantidad de Warframes: ${cantidad.toString().padEnd(54)}║")
        println("║                                                                                ║")
        println("║  ══════════════════════ ⚔️  WARFRAME ASIGNADO ═══════════════════════════      ║")
        println("║                                                                                ║")
        println("║  🏷️  Nombre: ${warframeNombre.padEnd(65)}║")
        println("║  ❤️  Salud por unidad: ${warframeHealth.toString().padEnd(56)}║")
        println("║  🛡️  Armadura por unidad: ${warframeArmor.toString().padEnd(54)}║")
        println("║  ⚡ Energía por unidad: ${warframeEnergy.toString().padEnd(56)}║")
        println("║  🏃 Velocidad Sprint: ${warframeSprintSpeed.toString().padEnd(58)}║")
        println("║  ✨ Pasiva: ${if (warframePassive.length > 60) "${warframePassive.substring(0, 57)}..."
        else warframePassive.padEnd(66)}║")
        println("║                                                                                ║")
        println("║  ══════════════════════ 📈 ESTADÍSTICAS DEL SQUAD ═══════════════════════      ║")
        println("║                                                                                ║")
        println("║  ❤️  Salud total del squad: ${saludTotal.toString().padEnd(54)}║")
        println("║  🛡️  Armadura total del squad: ${armaduraTotal.toString().padEnd(52)}║")
        println("║  ⚡ Energía total del squad: ${energiaTotal.toString().padEnd(54)}║")
        println("║                                                                                ║")

        // Evaluación del squad
        when {
            cantidad >= 10 -> {
                println("║  ⭐ EVALUACIÓN: SQUAD DE ASALTO (Gran capacidad ofensiva)                ║")
                println("║  💡 Recomendación: Ideal para misiones de defensa y exterminio          ║")
            }
            cantidad >= 5 -> {
                println("║  ⭐ EVALUACIÓN: SQUAD ESTÁNDAR (Balanceado)                              ║")
                println("║  💡 Recomendación: Versátil para cualquier tipo de misión               ║")
            }
            cantidad >= 2 -> {
                println("║  ⭐ EVALUACIÓN: SQUAD DE ÉLITE (Precisión y movilidad)                   ║")
                println("║  💡 Recomendación: Perfecto para misiones stealth y captura             ║")
            }
            else -> {
                println("║  ⭐ EVALUACIÓN: ESCUADRÓN ESPECIAL (Operaciones únicas)                  ║")
                println("║  💡 Recomendación: Misiones de infiltración y sabotaje                  ║")
            }
        }

        println("║                                                                                ║")
        println("╚════════════════════════════════════════════════════════════════════════════════╝")
        println()
    }




    //Maybe leave in
    fun consultarSquadsConFiltros() {
        println("***** Squads con más de 2 unidades")
        coleccionSquads.find(Filters.gt("cantidad", 2)).forEach { println(it.toJson()) }

        println("\n***** Squads con nombre específico (ejemplo: 'Alpha')")
        coleccionSquads.find(Filters.eq("nombre", "Alpha")).forEach { println(it.toJson()) }

        println("\n***** Squad con ID específico")
        print("Introduce el ID del squad: ")
        val idBuscar = iteradorNumerosValidosInteger("El ID")
        coleccionSquads.find(Filters.eq("squad_id", idBuscar)).forEach { println(it.toJson()) }

        println("\n***** Squads ordenados por cantidad (descendente)")
        coleccionSquads.find().sort(Document("cantidad", -1)).forEach { println(it.toJson()) }
    }

    fun proyeccionesSquads() {
        println("***** Solo nombres de los Squads")
        coleccionSquads.find()
            .projection(Projections.include("nombre"))
            .forEach { println(it.toJson()) }

        println("\n***** Nombres y cantidades de los Squads")
        coleccionSquads.find()
            .projection(Projections.include("nombre", "cantidad"))
            .forEach { println(it.toJson()) }

        println("\n***** Todos los campos excepto warframe_id")
        coleccionSquads.find()
            .projection(Projections.exclude("warframe_id"))
            .forEach { println(it.toJson()) }
    }

    fun agregacionesSquads() {
        // 1) Total de unidades en todos los squads
        println("***** Total de unidades en todos los squads")
        val totalUnidadesPipeline = listOf(
            Document("\$group", Document("_id", null)
                .append("totalUnidades", Document("\$sum", "\$cantidad")))
        )
        coleccionSquads.aggregate(totalUnidadesPipeline).forEach { println(it.toJson()) }

        // 2) Squad con mayor cantidad
        println("\n***** Squad con mayor cantidad de unidades")
        val maxCantidadPipeline = listOf(
            Document("\$sort", Document("cantidad", -1)),
            Document("\$limit", 1)
        )
        coleccionSquads.aggregate(maxCantidadPipeline).forEach { println(it.toJson()) }

        // 3) Cantidad promedio de unidades por squad
        println("\n***** Cantidad promedio de unidades por squad")
        val avgPipeline = listOf(
            Document("\$group", Document("_id", null)
                .append("promedioUnidades", Document("\$avg", "\$cantidad")))
        )
        coleccionSquads.aggregate(avgPipeline).forEach { println(it.toJson()) }

        // 4) Agrupación por warframe_id (cuántos squads tienen cada warframe)
        println("\n***** Squads agrupados por Warframe ID")
        val groupByWarframePipeline = listOf(
            Document("\$group", Document("_id", "\$warframe_id")
                .append("totalSquads", Document("\$sum", 1))
                .append("totalUnidades", Document("\$sum", "\$cantidad")))
        )
        coleccionSquads.aggregate(groupByWarframePipeline).forEach { println(it.toJson()) }
    }
}