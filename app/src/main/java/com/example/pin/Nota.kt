import androidx.room.*

@Dao
interface NotaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarNota(nota: Nota)

    @Query("SELECT * FROM Nota WHERE nombre = :nombre AND descripcion = :descripcion")
    suspend fun buscarNota(nombre: String, descripcion: String): Nota?

    @Transaction
    suspend fun insertarNotaSegura(nota: Nota): Boolean {
        return try {
            insertarNota(nota)
            true // La inserción fue exitosa
        } catch (e: Exception) {
            false // La inserción falló
        }
    }
}

@Entity(tableName = "Nota")
data class Nota(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var nombre: String,
    var descripcion: String
)
