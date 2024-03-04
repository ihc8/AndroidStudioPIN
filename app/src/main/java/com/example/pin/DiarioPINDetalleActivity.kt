package com.example.pin



import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class DiarioPINDetalleActivity : AppCompatActivity() {
    private val CODIGO_RESPUESTA_ACTIVIDAD = 1

    private lateinit var mTvNombre: TextView
    private lateinit var mTvDescripcion: TextView

    private lateinit var mDb: DataHelper
    private var mId: Long = -1

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)

        mTvNombre = findViewById(R.id.tvNombre)
        mTvDescripcion = findViewById(R.id.tvDescripcion)

        if (intent == null) {
            returnResult("Error: no se ha especificado ninguna nota", RESULT_CANCELED)
            return
        }

        mDb = DataHelper(this)
        mId = intent.getLongExtra(Extra.NOTA_ID, -1)

        if (mId == -1L) {
            returnResult("Error: nota incorrecta", RESULT_CANCELED)
            return
        }

        actualizarInterfaz()

        findViewById<Button>(R.id.btnEditar).setOnClickListener {
            val intent = Intent(this@DiarioPINDetalleActivity, DiarioPINFormularioActivity::class.java)
            intent.putExtra(Extra.NOTA_ID, mId)
            startActivityForResult(intent, CODIGO_RESPUESTA_ACTIVIDAD)
        }

        findViewById<Button>(R.id.btnEliminar).setOnClickListener {
            eliminarNota(mId)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODIGO_RESPUESTA_ACTIVIDAD && resultCode == RESULT_OK) {
            actualizarInterfaz()
            setResult(RESULT_OK)
        }
    }

    private fun actualizarInterfaz() {
        val nota = mDb.selectById(mId)
        if (nota == null)
            returnResult("Error: nota incorrecta", RESULT_CANCELED)
        else {
            mTvNombre.text = nota.nombre
            mTvDescripcion.text = nota.descripcion
        }
    }

    private fun eliminarNota(id: Long) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar")
            .setMessage("¿Desea eliminar esta nota?")
            .setPositiveButton("Aceptar") { _, _ ->
                val result: Int = mDb.deleteById(mId)
                mediaPlayer = MediaPlayer.create(this, R.raw.audio)
                mediaPlayer?.start()
                if (result > 0){

                    returnResult("La nota se ha eliminado", RESULT_OK)

                }else{
                    Toast.makeText(this, "Error al eliminar la nota", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun returnResult(message: String, resultCode: Int) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        setResult(resultCode)
        finish()
    }
}
