import com.mongodb.client.model.Filters
import org.bson.Document
import utility.iteradorNumerosValidosInteger
import utility.iteradorNumerosValidosIntegerPositivos
import java.util.Scanner

data class Mod(
    val modID: Int? = null,
    val name: String,
    val capacityCost: Int,
    val polarity: String,
    val rarity: String,
    val description: String
)

object ModsDAO {
    fun mostrarMods() {
        val cursor = coleccionMods.find().iterator()
        cursor.use {
            while (it.hasNext()) {
                val doc = it.next()

                val mod_id = doc.getInteger("mod_id")
                val modName = doc.getString("name")
                val modCapacityCost = doc.getInteger("capacity_cost")
                val modPolarity = doc.getString("polarity")
                val modRarity = doc.getString("rarity")
                val modDescription = doc.getString("description")

                println("Mod [${mod_id}]" +
                        "\n\tName: $modName" +
                        "\n\tCapacity Cost: $modCapacityCost" +
                        "\n\tPolarity: $modPolarity" +
                        "\n\tRarity: $modRarity" +
                        "\n\tDescription: $modDescription"
                )
            }
        }
    }

    fun insertarMod() {
        val scanner = Scanner(System. `in`)

        println("ID de la Mod: ")
        val id_mod = iteradorNumerosValidosIntegerPositivos("El ID")
        println("Nombre de la Mod: ")
        val nombre = scanner.nextLine()
        println("Coste de capacidad de la Mod: ")
        val costeCapacidad = iteradorNumerosValidosInteger("El coste de capacidad")
        println("Polaridad de la Mod: ")
        val polaridad = scanner.nextLine()
        println("Raridad de la Mod: ")
        val raridad = scanner.nextLine()
        println("Descripcion de la Mod: ")
        val descripcion = scanner.nextLine()

        val doc = Document("mod_id", id_mod)
            .append("name", nombre)
            .append("capacity_cost", costeCapacidad)
            .append("polarity", polaridad)
            .append("rarity", raridad)
            .append("description", descripcion)

        coleccionMods.insertOne(doc)
        println("Mod insertado con ID: ${doc.getObjectId("_id")}")
    }

    fun actualizarCosteCapacidadMod() {
        var id_mod = iteradorNumerosValidosInteger("El ID")
        var mod = coleccionMods.find(Filters.eq("mod_id", id_mod)).firstOrNull()

        if (mod == null) {
            println("No se encontró ninguna Mod con ID = \"$id_mod\".")
        }
        else {
            println("Mod encontrado: ${mod.getString("name")} (Coste de capacidad: ${mod.get("capacity_cost")})")

            println("El nuevo coste de capacidad será: ")
            var costeCapacidad = iteradorNumerosValidosInteger("Elo coste de capacidad")

            // Actualizar el documento
            val result = coleccionMods.updateOne(
                Filters.eq("mod_id", id_mod),
                Document("\$set", Document("capacity_cost", costeCapacidad))
            )

            if (result.modifiedCount > 0)
                println("Coste de capacidad actualizado correctamente (${result.modifiedCount} documento modificado).")
            else
                println("No se modificó ningún documento (El coste de capacidad quizá ya era el mismo).")
        }
    }

    fun eliminarMod() {

        println("ID de la Mod a eliminar: ")
        var idMod = iteradorNumerosValidosInteger("El ID")

        val result = coleccionMods.deleteOne(Filters.eq("mod_id", idMod))
        if (result.deletedCount > 0)
            println("Mod eliminada correctamente.")
        else
            println("No se encontró niguna Mod con ese ID.")
    }
}