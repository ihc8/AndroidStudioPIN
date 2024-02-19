package com.example.pin



import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class DiarioPINFormularioActivity : AppCompatActivity() {

    private lateinit var mDb: DataHelper
    private var mId: Long = -1

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario)

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etDescripcion = findViewById<EditText>(R.id.etDescripcion)

        mDb = DataHelper(this)

        // Comprobamos si se usa el formulario para crear una nueva nota o para modificarla.
        if (intent != null) {
            mId = intent.getLongExtra(Extra.NOTA_ID, -1)
            if (mId != -1L) {
                mDb.selectById(mId)?.let {
                    etNombre.setText(it.nombre)
                    etDescripcion.setText(it.descripcion)
                }
            }
        }

        // Listener Guardar
        findViewById<Button>(R.id.btnGuardar).setOnClickListener {
            val nombre = etNombre.text.toString()
            val descripcion = etDescripcion.text.toString()

            if (nombre.isEmpty())
                showToast("El nombre no puede estar vacío")
            else if (descripcion.isEmpty())
                showToast("La descripción no puede estar vacía")
            else {
                if (mId == -1L) {
                    mDb.insert(Nota(-1, nombre, descripcion))
                    showToast("La nota se ha añadido")
                } else {
                    mDb.update(Nota(mId, nombre, descripcion))
                    showToast("La nota se ha actualizado")
                }
                setResult(RESULT_OK)
                finish()
            }
        }

        // Listener Cancelar
        findViewById<Button>(R.id.btnCancelar).setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
